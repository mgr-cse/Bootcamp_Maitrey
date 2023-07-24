package com.example.spring.data.neo4j.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "MessageListener")
public class MqttMessageListener implements Runnable {
  @Autowired
  MqttSubscriberImpl subscriber;

  @Override
  public void run() {
    while (true) {
      subscriber.subscribeMessage("SpringBootRequest");
    }

  }
}