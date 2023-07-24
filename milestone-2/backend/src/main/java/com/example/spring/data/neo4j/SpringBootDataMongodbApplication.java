package com.example.spring.data.neo4j;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.context.ApplicationContext;

import org.json.JSONObject;

import com.example.spring.data.neo4j.model.Product;
import com.example.spring.data.neo4j.mqtt.ProductControl;


@SpringBootApplication
public class SpringBootDataMongodbApplication {
	@Autowired
	private ApplicationContext context;
  
  @Autowired
  private ProductControl productControl;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataMongodbApplication.class, args);
	}

	// mqtt inbound channel config
	@Bean
  public MessageChannel mqttInputChannel() {
    return new DirectChannel();
  }

	@Bean
  public MessageProducer inbound() {
    MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("tcp://localhost:1883", "SpringBoot", "SpringBootRequest");
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(2);
    adapter.setOutputChannel(mqttInputChannel());
    return adapter;
  }

	@Bean
  @ServiceActivator(inputChannel = "mqttInputChannel")
  public MessageHandler handler() {
    return new MessageHandler() {
      @Override
      public void handleMessage(Message<?> message) throws MessagingException {
          System.out.println(message.getPayload());
          try{
            JSONObject payloadObject = new JSONObject(message.getPayload().toString());
            System.out.print(payloadObject.toString());
            String operation = payloadObject.getString("op");
            // handle operations
            if (operation.equals("create")) {
              productControl.create(payloadObject.getJSONObject("data"));
            } else if (operation.equals("xml")) {
              productControl.fromXML(payloadObject.getString("data"));
            }
          } catch(Exception e) {
            System.out.println("Errors while processing payload");
            e.printStackTrace();
          }
					//MyGateway gateway = context.getBean(MyGateway.class);
					//gateway.sendToMqtt("ACK");
      }
    };
  }

	// mqtt outbound channel config
	 @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setConnectionTimeout(4);
        try {
          options.setKeepAliveInterval(4);
        } catch(Exception e) {
          e.printStackTrace(System.out);
        }
        options.setAutomaticReconnect(true);
        options.setServerURIs(new String[] { "tcp://localhost:1883"});
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                       new MqttPahoMessageHandler("SpringBoot", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("SpringBootAck");
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MyGateway {

        void sendToMqtt(String data);

    }
}
