import mqttclient from "../mqtt-common"

class ProductMqttService {
  create(data) {
    if (!mqttclient.connected) {
      alert('mqtt client not connected, please wait');
      return;
    }
    mqttclient.publish("SpringBootReceive", "create!");
  }
}

export default new ProductMqttService();