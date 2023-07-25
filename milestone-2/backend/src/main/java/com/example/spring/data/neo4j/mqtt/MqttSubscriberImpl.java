package com.example.spring.data.neo4j.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


@Component(value = "subscriber")
public class MqttSubscriberImpl extends MqttConfig implements MqttCallback{
  
  @Autowired
  ProductControl productControl;
  
  //private static final String fota_fetch_record = "fota_fetch_record";
  private String brokerUrl = null;
  final private String colon = ":";
  final private String clientId = UUID.randomUUID().toString();
  
  private MqttClient mqttClient = null;
  private MqttConnectOptions connectionOptions = null;
  private MemoryPersistence persistence = null;
  
  private static final Logger logger = LoggerFactory.getLogger(MqttSubscriberImpl.class);
  
  public MqttSubscriberImpl() {
    logger.info("I am MqttSub impl");
    this.config();
  }
  
  @Override
  public void connectionLost(Throwable cause) {
    logger.info("Connection Lost" + cause);
    this.config();
  }
  
  @Override
  protected void config(String broker, Integer port, Boolean ssl, Boolean withUserNamePass) {
    logger.info("Inside Parameter Config");
    String protocal = this.TCP;
    
    this.brokerUrl = protocal + this.broker + colon + port;
    this.persistence = new MemoryPersistence();
    this.connectionOptions = new MqttConnectOptions();
    
    try {
      this.mqttClient = new MqttClient(brokerUrl, clientId, persistence);
      this.connectionOptions.setCleanSession(true);
      this.connectionOptions.setPassword(this.password.toCharArray());
      this.connectionOptions.setUserName(this.userName);
      this.mqttClient.connect(this.connectionOptions);
      this.mqttClient.setCallback(this);
    } catch (Exception e) {
      System.out.println("Unable to connect");
    }
  }
  
  @Override
  protected void config() {
    logger.info("Inside Config with parameter");
    this.brokerUrl = this.TCP + this.broker + colon + this.port;
    this.persistence = new MemoryPersistence();
    this.connectionOptions = new MqttConnectOptions();
    try {
      this.mqttClient = new MqttClient(brokerUrl, clientId, persistence);
      this.connectionOptions.setCleanSession(true);
      this.connectionOptions.setPassword(this.password.toCharArray());
      this.connectionOptions.setUserName(this.userName);
      this.mqttClient.connect(this.connectionOptions);
      this.mqttClient.setCallback(this);
    } catch (MqttException me) {
      System.out.println("not conneted to mqtt");
    }
  }
  
  public void subscribeMessage(String topic) {
    try {
      
      this.mqttClient.subscribe(topic, this.qos);
    } catch (MqttException me) {
      System.out.println("Not able to Read Topic  "+ topic);
      // me.printStackTrace();
    }
  }
  
  
  public void disconnect() {
    try {
      this.mqttClient.disconnect();
    } catch (MqttException me) {
      logger.error("ERROR", me);
    }
  }
  
  public void publishMessage(String topic, String message) {
    try {
      MqttMessage mqttMessage = new MqttMessage(message.getBytes());
      mqttMessage.setQos(this.qos);
      mqttMessage.setRetained(false);
      this.mqttClient.publish(topic, mqttMessage);
    } catch (MqttException me) {
      logger.error("ERROR", me);
    }
  }
  
  @Override
  public void messageArrived(String mqttTopic, MqttMessage message) throws Exception {
    String rawPayload = new String(message.getPayload());
    System.out.println(rawPayload);

    // send some acknowledgement
    this.publishMessage("SpringBootAck", "ACK");
    try{
      JSONObject payloadObject = new JSONObject(rawPayload);
      System.out.print(payloadObject.toString());
      String operation = payloadObject.getString("op");
      // handle operations
      if (operation.equals("create")) {
        productControl.create(payloadObject.getJSONObject("data"));
      } else if (operation.equals("xml")) {
        productControl.fromXML(payloadObject.getString("data"));
      } else if (operation.equals("update")) {
        productControl.update(payloadObject.getString("id"), payloadObject.getJSONObject("data"));
      }
    } catch(Exception e) {
      System.out.println("Errors while processing payload");
      e.printStackTrace();
    }
  }
  
  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    
  }
}