#!/bin/bash

# expose frontend
kubectl port-forward inventory 8081:8081 &

# expose read api
kubectl port-forward inventory 8080:8080 &

# expose mqtt port
kubectl port-forward inventory 9002:9002 &

wait