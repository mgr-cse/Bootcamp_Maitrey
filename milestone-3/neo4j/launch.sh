#!/bin/bash

docker run \
--name neo4j \
-p7474:7474 -p7687:7687 \
--env NEO4J_AUTH=neo4j/8bitguy123 \
neo4j:latest