#!/bin/bash

# expose frontend
kubectl --namespace=inventory-space port-forward inventory 8081:8081 &

# expose read api
kubectl  --namespace=inventory-space port-forward inventory 8080:8080 &

# expose mqtt port
kubectl  --namespace=inventory-space port-forward inventory 9002:9002 &

wait