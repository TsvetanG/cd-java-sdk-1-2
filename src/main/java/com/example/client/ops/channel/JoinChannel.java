
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

package com.example.client.ops.channel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import com.example.client.StaticConfig;
import com.example.client.impl.ChannelUtil;
import com.example.client.impl.UserFileSystem;

public class JoinChannel {

  public static void main(String[] args) throws CryptoException, InvalidArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, TransactionException, IOException, ProposalException {
 
    /**
     * For the correct ports check docker-compose-base.yaml
     */
    String channelName = StaticConfig.CHANNEL_NAME;
    String org = "fund"; 
    String portClient = "9051";// for fundinc 9051
    String portEventHub = "9053"; // for fuindinc 9053
    
    String peer = "peer0." + org + ".example.com:" + StaticConfig.GRPC_HOST + ":" + portClient;
    String eventHub = "peer0." + org + ".example.com:" + StaticConfig.GRPC_HOST + ":" + portEventHub;
    JoinChannel join = new JoinChannel();
    User user = new UserFileSystem("Admin", org + ".example.com"); 
    join.join(channelName, peer, eventHub, org, user);

  }

  protected void join(String channelName, String peerPath ,String eventHub, String org, User user) throws CryptoException, InvalidArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, TransactionException, IOException, ProposalException {
    ChannelUtil util = new ChannelUtil();
    HFClient client = HFClient.createNewInstance();
    client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
    client.setUserContext(user);
    
    Channel channel = util.reconstructChannel(org, channelName, client, true);
    Peer peer = util.createPeer(client, peerPath);
    channel = channel.joinPeer(peer);
    
    util.updateChannelProps( channelName , org, peerPath, eventHub);
    System.out.println("DONE>>>>>>>");
    
  }


}