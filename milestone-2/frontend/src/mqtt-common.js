//create mqtt send api

import Paho from "paho-mqtt"
class MqttApi {
  constructor() {
    this.client = new Paho.Client("localhost", Number(9001), "/", "clientId");
    this.onConnect = this.onConnect.bind(this);
    this.prepare = this.prepare.bind(this);
    this.client.connect({
      onSuccess: this.onConnect
    })

  }

  prepare() {
      if(!this.client.isConnected()) {
      this.client.connect({
        onSuccess: this.onConnect
      });
    }
  }
  onConnect() {
    console.log("MQTT client connected!");
    this.client.subscribe("SpringBootAck")
  }

  publish(payload, deliveryCallback) {
      this.prepare()
      this.client.onMessageDelivered = deliveryCallback;
      let message = new Paho.Message(payload);
      message.destinationName = "SpringBootRequest";
      message.qos = 2;
      this.client.send(message);
  }

  publishObjectOperation(obj, operation ,deliveryCallback) {
    this.prepare();
    let payloadObject = {
      op: operation,
      data: obj
    };
    let payload = JSON.stringify(payloadObject);
    console.log(payload)
    this.publish(payload, deliveryCallback);
    
  }

  publishObjectOperationWithId(obj, objId, operation, deliveryCallback) {
    this.prepare()
    let payloadObject = {
      op: operation,
      id: objId,
      data: obj
    };
    let payload = JSON.stringify(payloadObject);
    console.log(payload);
    this.publish(payload, deliveryCallback);
  }

  disconnect (){
    this.client.disconnect();
  }

  publishOperationWithId(objId, operation, deliveryCallback) {
    this.prepare();
    let payloadObject = {
      op: operation,
      id: objId
    };
    let payload = JSON.stringify(payloadObject);
    console.log(payload);
    this.publish(payload, deliveryCallback); 
  }
}

// Create a client instance: Broker, Port, Websocket Path, Client ID

var mqttApi = new MqttApi();
export default mqttApi;