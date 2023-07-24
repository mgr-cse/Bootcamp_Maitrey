//create mqtt send api

import Paho from "paho-mqtt"
class MqttApi {
  constructor() {
    this.client = new Paho.Client("localhost", Number(9001), "/", "clientId");
    this.onConnect = this.onConnect.bind();
    this.client.connect({
      onSuccess: this.onConnect
    })

  } 
  onConnect() {
    console.log("MQTT client connected!")
  }

  publish(payload, deliveryCallback) {
      this.client.onMessageDelivered = deliveryCallback;
      let message = new Paho.Message(payload);
      message.destinationName = "SpringBootRequest";
      message.qos = 2;
      this.client.send(message);
  }

  publishObjectOperation(obj, operation ,deliveryCallback) {
    let payloadObject = {
      op: operation,
      data: obj
    };
    let payload = JSON.stringify(payloadObject);
    console.log(payload)
    this.publish(payload, deliveryCallback);
    
  }

  disconnect (){
    this.client.disconnect();
  }
}

// Create a client instance: Broker, Port, Websocket Path, Client ID

var mqttApi = new MqttApi();
export default mqttApi;