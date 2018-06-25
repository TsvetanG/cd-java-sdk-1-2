package com.example.client;

public class StaticConfig {
  /**
   * Setup your specific host, channel name , cahin code id, etc.
   */
  public static final String HOST = "ec2-13-58-59-246.us-east-2.compute.amazonaws.com";
  public static final String GRPC_HOST = "grpc://" + HOST;
  public static final String ORDERER = "fundorderer.funds.com:" + GRPC_HOST + ":7050";
  public static final String CHANNEL_NAME = "transfer";
  public static final String CHAIN_CODE_ID = "transfercc";
  public static final String CHAIN_CODE_NODEJS_ID = "nodecccc";

}
