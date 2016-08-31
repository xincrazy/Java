#!/bin/sh
#定义需要监控的topic，用空格隔开
topics=(systemstate);
while true;
#时间戳timestamp
 do tm=`date +%s%3N`;
 for topic in $topics;
  do kafka=`/opt/kafka-0.8.2.2/bin/kafka-consumer-offset-checker.sh --group flume --topic ${topic} --zookeeper localhost:2181|tail -n +2|awk '{print ",group:" $1 ",topic:" $2 ",partition:" $3 ",cluster_type:kafka_cluster,offset:"  $4 ",logsize:" $5 ",lag:" $6}'`;
  echo "kafka/checker timestamp:"$tm$kafka;
  done;
  #每隔1s执行一次
sleep 1;
done

