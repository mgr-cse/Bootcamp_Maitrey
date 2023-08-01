package com.example.spring.data.neo4j.mqtt;

import org.springframework.beans.factory.annotation.Value;

public abstract class MqttConfig {

  @Value("${mqtt.broker.url}")
  protected String broker = "localhost";
  protected static final int QOS = 2;
  protected static Boolean hasSSL = false; /*By default SSL is disabled */
  protected Integer port = 1883; /* Default port */
  protected static final String USERNAME = "";
  protected static final String PASSWORD = "";
  protected static final String TCP = "tcp://";
  protected static final String SSL = "ssl://";

  /**
   * Custom Configuration
   * 
   * @param broker
   * @param port
   * @param ssl
   * @param withUserNamePass
   */
  protected abstract void config(String broker, Integer port, Boolean ssl, Boolean withUserNamePass);

  /**
   * Default Configuration
   */
  protected abstract void config();

}