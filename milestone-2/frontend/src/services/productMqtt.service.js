import mqttApi from "../mqtt-common"

class ProductMqttService {
  create(data, deliveryCallback) {
    mqttApi.publishObjectOperation(data, "create",deliveryCallback);
  }

  update(id, data, deliveryCallback) {
    mqttApi.publishObjectOperationWithId(data, id, "update", deliveryCallback);
  }

  delete(id, deliveryCallback) {
    mqttApi.publishOperationWithId(id, "delete", deliveryCallback);
  }
  
  deleteAll(deliveryCallback) {
    mqttApi.publishOperationWithId("null", "deleteAll", deliveryCallback);
  }

  uploadXML(data, deliveryCallback) {
    mqttApi.publishObjectOperation(data, "xml", deliveryCallback);
  }
}

export default new ProductMqttService();