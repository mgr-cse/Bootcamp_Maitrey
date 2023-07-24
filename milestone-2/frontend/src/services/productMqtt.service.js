import mqttApi from "../mqtt-common"

class ProductMqttService {
  create(data, deliveryCallback) {
    mqttApi.publishObjectOperation(data, "create",deliveryCallback);
    
  }

  uploadXML(data, deliveryCallback) {
    mqttApi.publishObjectOperation(data, "xml", deliveryCallback);
  }
}

export default new ProductMqttService();