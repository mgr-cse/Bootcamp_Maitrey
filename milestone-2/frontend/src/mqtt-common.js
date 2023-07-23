const mqtt = require('mqtt')

const url = 'mqtt://localhost:1883/';

const options = {
  clean: true,
  connectTimeout: 4,
  clientId: 'client1',
}

const mqttclient = mqtt.connect(url, options);
console.log('hello')
mqttclient.on('connect', function (packet) {
  //mqttclient.subscribe('SpringBootAck');
  mqttclient.publish("SpringBootRequest", "Hello", {qos: 2});
  setTimeout(function() {
    mqttclient.end()
  }, 3000)
});

console.log('ack start');
mqttclient.on("message", function(topic, message) {
  console.log(message.toString());
  mqttclient.end()
})

