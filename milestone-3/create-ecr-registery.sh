#!/bin/bash

kubectl create secret docker-registry regcred \
--docker-server=094412661274.dkr.ecr.ap-southeast-2.amazonaws.com \
--docker-username=AWS \
--docker-password=$(aws ecr get-login-password --region ap-southeast-2) \
--namespace=inventory-space