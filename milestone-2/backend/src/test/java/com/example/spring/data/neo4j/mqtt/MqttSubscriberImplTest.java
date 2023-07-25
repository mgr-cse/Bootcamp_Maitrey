package com.example.spring.data.neo4j.mqtt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.spring.data.neo4j.model.Product;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MqttSubscriberImplTest  {
    @InjectMocks
    MqttSubscriberImpl mqttSubscriberImpl;

    @Mock
    private ProductControl productControl;

    @Mock
    private MqttClient mqttClient;

    @Test
    void connectionLost() {
        mqttSubscriberImpl.connectionLost(new Throwable("error"));
    }

    @Test
    void config() {
        mqttSubscriberImpl.config("localhost", 1883, false, false);
    }

    @Test
    public  void publishMessage() {
        mqttSubscriberImpl.publishMessage("test", "test");
        try {
            doThrow(new MqttException(0)).when(mqttClient).publish(Mockito.eq("test1"), Mockito.any());
            mqttSubscriberImpl.publishMessage("test1", "test1");

        } catch (MqttException me) {
            assertThat(0).isEqualTo(1);
        }
    }

    @Test
    public  void messageArrived() {
        String message1 = "{\"op\": \"create\",\"data\":\"data\"}";
        String message2 = "{\"op\": \"xml\",\"data\":\"data\"}";
        String message3 = "{\"op\": \"update\",\"data\":\"data\",\"id\":\"id\"}";
        String message4 = "{\"op\": \"delete\",\"id\":\"id\"}";
        String message5 = "{\"op\": \"deleteAll\"}";

        try {
            MqttMessage mqttMessage = new MqttMessage(message1.getBytes());
            mqttSubscriberImpl.messageArrived("test", mqttMessage);
            mqttMessage = new MqttMessage(message2.getBytes());
            mqttSubscriberImpl.messageArrived("test", mqttMessage);
            mqttMessage = new MqttMessage(message3.getBytes());
            mqttSubscriberImpl.messageArrived("test", mqttMessage);
            mqttMessage = new MqttMessage(message4.getBytes());
            mqttSubscriberImpl.messageArrived("test", mqttMessage);
            mqttMessage = new MqttMessage(message5.getBytes());
            mqttSubscriberImpl.messageArrived("test", mqttMessage);


        } catch (Exception e) {

        }

    }


}