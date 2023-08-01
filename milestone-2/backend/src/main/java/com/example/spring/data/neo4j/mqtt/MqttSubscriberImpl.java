package com.example.spring.data.neo4j.mqtt;

import jakarta.annotation.PostConstruct;
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

  private String brokerUrl = null;
  private static final String COLON = ":";
  private final String clientId = UUID.randomUUID().toString();
  
  private MqttClient mqttClient = null;
  private MqttConnectOptions connectionOptions = null;
  private MemoryPersistence persistence = null;
  
  private static final Logger logger = LoggerFactory.getLogger(MqttSubscriberImpl.class);
  
  public MqttSubscriberImpl() {
    logger.info("I am MqttSub impl");

  }

  @PostConstruct
  public void brokerValInit() {
    this.config();
  }

  @Override
  public void connectionLost(Throwable cause) {
    String message = String.format("Connection Lost %1$s", cause);
    logger.info(message);
    this.config();
  }
  
  @Override
  protected void config(String broker, Integer port, Boolean ssl, Boolean withUserNamePass) {
    logger.info("Inside Parameter Config");
    String protocal = MqttConfig.TCP;
    
    this.brokerUrl = protocal + this.broker + COLON + port;
    this.persistence = new MemoryPersistence();
    this.connectionOptions = new MqttConnectOptions();
    
    try {
      this.mqttClient = new MqttClient(brokerUrl, clientId, persistence);
      this.connectionOptions.setCleanSession(true);
      this.connectionOptions.setPassword(MqttConfig.PASSWORD.toCharArray());
      this.connectionOptions.setUserName(MqttConfig.USERNAME);
      this.mqttClient.connect(this.connectionOptions);
      this.mqttClient.setCallback(this);
    } catch (Exception e) {
      logger.info("Unable to connect");
    }
  }
  
  @Override
  protected void config() {
    logger.info("Inside Config with parameter");
    this.brokerUrl = MqttConfig.TCP + this.broker + COLON + this.port;
    this.persistence = new MemoryPersistence();
    this.connectionOptions = new MqttConnectOptions();
    try {
      this.mqttClient = new MqttClient(brokerUrl, clientId, persistence);
      this.connectionOptions.setCleanSession(true);
      this.connectionOptions.setPassword(MqttConfig.PASSWORD.toCharArray());
      this.connectionOptions.setUserName(MqttConfig.USERNAME);
      this.mqttClient.connect(this.connectionOptions);
      this.mqttClient.setCallback(this);
    } catch (MqttException me) {
      logger.info("not conneted to mqtt");
    }
  }
  
  public void subscribeMessage(String topic) {
    try {
      
      this.mqttClient.subscribe(topic, MqttConfig.QOS);
    } catch (MqttException me) {
      if(me.getReasonCode()==MqttException.REASON_CODE_CLIENT_NOT_CONNECTED) {
        logger.info("MQTT: Trying to reconnect");
        config();
      } else {
        logger.info("MQTT: client not connected unknown reason");
      }
    }
  }

  public void publishMessage(String topic, String message) {
    try {
      MqttMessage mqttMessage = new MqttMessage(message.getBytes());
      mqttMessage.setQos(MqttConfig.QOS);
      mqttMessage.setRetained(false);
      this.mqttClient.publish(topic, mqttMessage);
    } catch (MqttException me) {
      logger.error("ERROR", me);
    }
  }
  
  @Override
  public void messageArrived(String mqttTopic, MqttMessage message) throws Exception {
    String rawPayload = new String(message.getPayload());
    logger.info(rawPayload);

    // send some acknowledgement
    this.publishMessage("SpringBootAck", "ACK");
    try{
      JSONObject payloadObject = new JSONObject(rawPayload);
      String operation = payloadObject.getString("op");
      // handle operations
      if (operation.equals("create")) {
        productControl.create(payloadObject.getJSONObject("data"));
      } else if (operation.equals("xml")) {
        productControl.fromXML(payloadObject.getString("data"));
      } else if (operation.equals("update")) {
        productControl.update(payloadObject.getString("id"), payloadObject.getJSONObject("data"));
      } else if (operation.equals("delete")) {
        productControl.deleteProduct(payloadObject.getString("id"));
      } else if (operation.equals("deleteAll")) {
        productControl.deleteAll();
      }
    } catch(Exception e) {
      logger.error("Errors while processing payload");
      e.printStackTrace();
    }
  }
  
  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    // essential ovrride
  }
}