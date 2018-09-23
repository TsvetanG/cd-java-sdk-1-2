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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import com.example.client.StaticConfig;
import com.example.client.impl.ChannelUtil;
import com.example.client.impl.UserFileSystem;
 
public class QueryFile {

  public static void main(String[] args) throws CryptoException, InvalidArgumentException, TransactionException,
      IOException, ProposalException, InterruptedException, ExecutionException, TimeoutException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
 
    String channelName =  StaticConfig.CHANNEL_NAME;
    
    String org = "maple"; //Change this to the next organization to perform the same operation
    String chainCode = "publiccc";
    String peerName = "peer0." + org + ".example.com";
    String[] params = new String[] { "CL12345" ,"123" }; 

    User user = new UserFileSystem("Admin", org + ".example.com");
    String result =  new QueryFile().query(params, org , peerName, channelName, chainCode, user);

  }
 

  public String query(String[] params, String org, String peerName, String channelName, String chainCode, User user)
  throws CryptoException, InvalidArgumentException, TransactionException, IOException, InterruptedException,
  ExecutionException, TimeoutException, ProposalException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

    ChannelUtil util = new ChannelUtil();
    HFClient client = HFClient.createNewInstance();
    client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
    client.setUserContext(user);
    Channel channel = util.reconstructChannel(org, channelName, peerName, client);

    ChaincodeID chaincodeID;

    chaincodeID = ChaincodeID.newBuilder().setName(chainCode).build();
    QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
    queryByChaincodeRequest.setArgs(params);
    queryByChaincodeRequest.setFcn("query"); 
    queryByChaincodeRequest.setChaincodeID(chaincodeID);


    Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest, channel.getPeers());
    for (ProposalResponse proposalResponse : queryProposals) {
      if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
       
      } else {
        String payload = ""; //proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
        System.out.println("Result > " + payload);
        return payload;
        
      }
    }
    return "";
  }
 
}
