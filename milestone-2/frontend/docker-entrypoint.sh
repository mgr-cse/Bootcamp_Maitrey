#!/bin/bash

cd /app

REST_HOST=$1
MQTT_HOST=$2

sed -i "s/^const MQTT_HOST=.*$/const MQTT_HOST='${MQTT_HOST}';/g" src/constants.js
sed -i "s/^const REST_HOST=.*$/const REST_HOST='${REST_HOST}';/g" src/constants.js

npm start
