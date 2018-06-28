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

package com.example.client.app;

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

import com.example.client.StaticConfig;
import com.example.client.impl.ChannelUtil;
import com.example.client.impl.UserFileSystem;
 
public class CaptureConsent {

  private static int sleepTime;

  public static void main(String[] args)
      throws CryptoException, InvalidArgumentException, TransactionException, IOException, ProposalException,
      InterruptedException, ExecutionException, TimeoutException, IllegalAccessException, InstantiationException,
      ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

    String channelName = StaticConfig.CHANNEL_NAME;
    String chainCode = "consentcc";
    String ops = "consent";
    String org = "maple";
    String peerName = "peer0." + org + ".example.com";
    String[] params = new String[] { "CL12345" };
 

    User user = new UserFileSystem("Admin", "fund" + ".example.com");
    TransactionEvent event = new CaptureConsent().invoke(ops, params, org, peerName, channelName, chainCode,
        user);
    if (event != null) {
      // event.getTransactionID().
    }
    System.out.println("DONE ->>>>>>>>>>>>>>>");
  }
 

  public TransactionEvent invoke(String operation, String[] params, String org, String peerName, String channelName,
      String chainCode, User user) throws CryptoException, InvalidArgumentException, TransactionException, IOException,
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
      System.out.println("Cannot endorse failure");
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
 
}
