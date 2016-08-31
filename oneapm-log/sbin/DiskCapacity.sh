#!/bin/bash
KAFKA_LOG_HOME=/home
while true;
do
df -hlP ${KAFKA_LOG_HOME}|awk NR==2|awk '{print $5}'|awk '{gsub(/%/,"");print}';
sleep 30;
done;