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

package com.example.client.ops.cc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//import org.hyperledger.fabric.protos.peer.Query.ChaincodeInfo;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import com.example.client.StaticConfig;
import com.example.client.impl.ChannelUtil;
import com.example.client.impl.UserFileSystem;

public class InstallPrvChaincode {

//  public static void main(String[] args) throws CryptoException, InvalidArgumentException, IllegalAccessException,
//      InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
//      TransactionException, IOException, ProposalException, ChaincodeEndorsementPolicyParseException {
//
//
//    String channelName = StaticConfig.CHANNEL_NAME;
//    String org = "fund";
////  String chaincodeName = "consentcc";
//  String chaincodeName = "privatecc";
//// String chaincodeName = "publiccc";
//
//    
//    String path = "../cd-node-" + chaincodeName;
//    int version = 1;
//    String peerName = null; // "peer0." + org + ".example.com";
//    InstallPrvChaincode install = new InstallPrvChaincode();
//    User user = new UserFileSystem("Admin", org + ".example.com");
//    install.install(path, org, peerName, channelName, chaincodeName + org, version, user);
////    install.install(path, org, peerName, channelName, chaincodeName , version, user);
//
//  }
//
//  protected void install(String path, String org, String peerName, String channelName, String chaincodeName,
//      int version, User user) throws CryptoException, InvalidArgumentException, IllegalAccessException,
//      InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
//      TransactionException, IOException, ProposalException, ChaincodeEndorsementPolicyParseException {
//    HFClient client = HFClient.createNewInstance();
//    client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
//    client.setUserContext(user);
//    ChannelUtil util = new ChannelUtil();
//    Peer peer = null;
//
//    Channel channel = util.reconstructChannel(org, channelName, client);
//
//    Collection<Peer> peersChannel = channel.getPeers();
//    Collection<Peer> peers = new ArrayList<Peer>();
//    int peerCCVersion = -1;
//    int installVersion = version;
//
//    for (Iterator<Peer> iterator = peersChannel.iterator(); iterator.hasNext();) {
//      peer = iterator.next();
//
//      if (peerName != null && !peer.getName().equals(peerName))
//        continue;
//
//      if (version == 0) {
//
//        List<ChaincodeInfo> installedCC = client.queryInstalledChaincodes(peer);
//
//        for (Iterator<ChaincodeInfo> iter = installedCC.iterator(); iter.hasNext();) {
//          ChaincodeInfo chaincodeInfo = iter.next();
//          String name = chaincodeInfo.getName();
//          if (!chaincodeName.equals(name))
//            continue;
//          // Make sure the chaincode version is integer
//          peerCCVersion = Integer.parseInt(chaincodeInfo.getVersion());
//        }
//        if (peerCCVersion > installVersion)
//          installVersion = peerCCVersion;
//
//      }
//
//      peers.add(peer);
//    }
//
//    if (peers.isEmpty()) {
//      return;
//    }
//    if (version == 0) {
//      ++installVersion; // add 1 to the version
//    }
//    //
//    ChaincodeID chaincodeID;
//    Collection<ProposalResponse> responses;
//    Collection<ProposalResponse> successful = new LinkedList<>();
//    Collection<ProposalResponse> failed = new LinkedList<>();
//
//    chaincodeID = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(String.valueOf(installVersion)).build();
//
//    InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
//    installProposalRequest.setChaincodeID(chaincodeID);
//
//    installProposalRequest.setChaincodeSourceLocation(new File(path));
//
//    installProposalRequest.setChaincodeVersion(String.valueOf(installVersion));
//    installProposalRequest.setChaincodeLanguage(Type.NODE);
//    installProposalRequest.setChaincodePath(null);
//    responses = client.sendInstallProposal(installProposalRequest, peers);
//
//    for (ProposalResponse response : responses) {
//      if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
//        successful.add(response);
//      } else {
//        failed.add(response);
//      }
//    }
//
//    if (failed.size() > 0) {
//      ProposalResponse first = failed.iterator().next();
//    }
//    System.out.println("DONE =>>>>>>>>>>>>>>>>>>>>>>>>>>");
//
//  }

}