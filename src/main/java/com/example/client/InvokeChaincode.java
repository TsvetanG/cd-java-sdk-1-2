/**
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 *  DO NOT USE IN PROJECTS , NOT for use in production
 */


package com.example.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletResponse;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.client.impl.ChannelUtil;
import com.example.client.impl.UserFileSystem;

@RestController
@CrossOrigin(origins = "*")
public class InvokeChaincode {

  private static int sleepTime;

  public static void main(String[] args)
      throws CryptoException, InvalidArgumentException, TransactionException, IOException, ProposalException,
      InterruptedException, ExecutionException, TimeoutException, IllegalAccessException, InstantiationException,
      ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
    
    String channelName = StaticConfig.CHANNEL_NAME;
    String chainCode = StaticConfig.CHAIN_CODE_ID;
    String ops = "transfer";
    String org = "maple";
    String peerName = "peer0." + org + ".funds.com";
    String[] params = new String[] { "Alice", "Bob", "20" };
    
    if (args != null && args.length != 0) {
      params = args;
      sleepTime = Integer.parseInt(args[0]);
      sleepTime = sleepTime*1000;
    }

    User user = new UserFileSystem("Admin", org + ".funds.com");
    TransactionEvent event = new InvokeChaincode().invoke(ops, params, org,peerName, channelName, chainCode, user);
    if (event != null) {
      // event.getTransactionID().
    }
    System.out.println("DONE ->>>>>>>>>>>>>>>");
  }

  @CrossOrigin
  @RequestMapping(value = "/invokechaincode", method = RequestMethod.POST)
  @ResponseBody
  public String invokeMethod(@RequestBody String[] chaincodeParameters)
          throws CryptoException, InvalidArgumentException, TransactionException, IOException,
          InterruptedException, ExecutionException, TimeoutException, ProposalException, IllegalAccessException,
          InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException
  {

      String ops = "transfer";
      String org = "maple";
      String channelName = StaticConfig.CHANNEL_NAME;
      String chainCode =StaticConfig.CHAIN_CODE_ID;

      String[] params = chaincodeParameters;
      String peerName = "peer0.maple.funds.com";
      User user = new UserFileSystem("Admin", "maple.funds.com");

      TransactionEvent event = new InvokeChaincode().invoke(ops, params, org, peerName, channelName,
              chainCode, user);
      if (event != null) {
          // event.getTransactionID().
      }

      return "Success!";
  }

  public TransactionEvent invoke(String operation, String[] params, String org, String peerName, String channelName, String chainCode,
      User user) throws CryptoException, InvalidArgumentException, TransactionException, IOException,
      InterruptedException, ExecutionException, TimeoutException, ProposalException, IllegalAccessException,
      InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

    ChannelUtil util = new ChannelUtil();
    HFClient client = HFClient.createNewInstance();
    client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
    client.setUserContext(user);
    Channel channel = util.reconstructChannel(org, channelName, peerName, client);

    ChaincodeID chaincodeID; 
    
    chaincodeID = ChaincodeID.newBuilder().setName(chainCode).build();

    TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
    transactionProposalRequest.setChaincodeID(chaincodeID);
    transactionProposalRequest.setFcn(operation);
    transactionProposalRequest.setArgs(params);

    Map<String, byte[]> tm2 = new HashMap<>();
    tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
    tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
    tm2.put("result", ":)".getBytes(UTF_8)); /// This should be returned see chaincode.
    transactionProposalRequest.setTransientMap(tm2);

    Collection<ProposalResponse> successful = new LinkedList<>();
    Collection<ProposalResponse> failed = new LinkedList<>();

    Collection<ProposalResponse> propResponse = channel.sendTransactionProposal(transactionProposalRequest,
        channel.getPeers());
    for (ProposalResponse response : propResponse) {
      if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
        successful.add(response);
      } else {
        failed.add(response);
      }
    }

    if (failed.size() > 0) {
      ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
      return null;
    }

    ProposalResponse resp = propResponse.iterator().next();
    byte[] x = resp.getChaincodeActionResponsePayload(); // This is the data returned by the chaincode.
    String resultAsString = null;
    if (x != null) {
      resultAsString = new String(x, "UTF-8");
    }

    TxReadWriteSetInfo readWriteSetInfo = resp.getChaincodeActionResponseReadWriteSetInfo();

    ChaincodeID cid = resp.getChaincodeID();

    Thread.currentThread().sleep(sleepTime);
 
    ////////////////////////////
    // Send Transaction Transaction to orderer
    return channel.sendTransaction(successful).get(10000, TimeUnit.SECONDS);
  }

    @ModelAttribute
    public void setVaryResponseHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
    }
}
