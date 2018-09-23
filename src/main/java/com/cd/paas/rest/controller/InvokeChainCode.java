package com.cd.paas.rest.controller;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
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
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cd.paas.rest.dto.InvokeParams;
import com.example.client.impl.ChannelUtil;
import com.example.client.impl.UserFileSystem;

@RestController
@CrossOrigin(origins = "*")
public class InvokeChainCode {

  @CrossOrigin
  @PostMapping("/invoke")
  public ResponseEntity<String> invokeMethod(@RequestBody InvokeParams params, HttpServletRequest request)
      throws Exception {
    String ops = params.getOperation();
    String channelName = params.getChannelID();
    String chainCode = params.getChaincodeID();
    
    System.out.println("Invoking !!!!!" + new File("/tmp/crypto/crypto-config").exists());
    System.out.println("Invoking !!!!!" + new File("/tmp/crypto").exists());


    String[] parameters = params.getParams();

    TransactionEvent event = invoke(ops, parameters, channelName, chainCode, request);
    if (event != null) {
      return ResponseEntity.ok("" + event.getTransactionID());
    } 

    return ResponseEntity.ok("");
  }

  public TransactionEvent invoke(String operation, String[] params, String channelName, String chainCode,
      HttpServletRequest request) throws Exception {
    String peerName = getPeer();
    String org = getOrg();
    User user = getUser(request);

    return invoke(operation, params, org, peerName, channelName, chainCode, user);
  }
  
  public String getPeer() {
    return "peer0.maple.fund.com";
  }

  protected String getOrg() {
    return "maple";
  }

  public User getUser(HttpServletRequest request) throws Exception {
    return new UserFileSystem("Admin", "maple.com");
  }

  public TransactionEvent invoke(String operation, String[] params, String org, String peerName, String channelName,
      String chainCode, User user) throws CryptoException, InvalidArgumentException, TransactionException, IOException,
      InterruptedException, ExecutionException, TimeoutException, ProposalException, IllegalAccessException,
      InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, NetworkConfigurationException {

    ChannelUtil util = new ChannelUtil();
    HFClient client = HFClient.createNewInstance();
    client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
    client.setUserContext(user);
    util.reconstructChannel(channelName, client);
//    Channel channel = util.reconstructChannel(org, channelName, peerName, client);

//    ChaincodeID chaincodeID;
//
//    chaincodeID = ChaincodeID.newBuilder().setName(chainCode).build();
//
//    TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
//    transactionProposalRequest.setChaincodeID(chaincodeID);
//    transactionProposalRequest.setFcn(operation);
//    transactionProposalRequest.setArgs(params);
//
//    Map<String, byte[]> tm2 = new HashMap<>();
//    tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
//    tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
//    tm2.put("result", ":)".getBytes(UTF_8)); /// This should be returned see chaincode.
//    transactionProposalRequest.setTransientMap(tm2);
//
//    Collection<ProposalResponse> successful = new LinkedList<>();
//    Collection<ProposalResponse> failed = new LinkedList<>();
//
//    Collection<ProposalResponse> propResponse = channel.sendTransactionProposal(transactionProposalRequest,
//        channel.getPeers());
//    for (ProposalResponse response : propResponse) {
//      if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
//        successful.add(response);
//      } else {
//        failed.add(response);
//      }
//    }
//
//    if (failed.size() > 0) {
//      ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
//      return null;
//    }
//
//    ProposalResponse resp = propResponse.iterator().next();
//    byte[] x = resp.getChaincodeActionResponsePayload(); // This is the data returned by the chaincode.
//    String resultAsString = null;
//    if (x != null) {
//      resultAsString = new String(x, "UTF-8");
//    }
//
//    TxReadWriteSetInfo readWriteSetInfo = resp.getChaincodeActionResponseReadWriteSetInfo();
//
//    ChaincodeID cid = resp.getChaincodeID();
//
//    ////////////////////////////
//    // Send Transaction Transaction to orderer
//    return channel.sendTransaction(successful).get(10000, TimeUnit.SECONDS);
    return null;
  }

  @ModelAttribute
  public void setVaryResponseHeader(HttpServletResponse response) {
    response.setHeader("Access-Control-Allow-Origin", "*");
  }

}
