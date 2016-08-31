###1. Overview
OneAPM Log project provides features on log collection, storage, visualization and analysis. 

###2. Execution Command
##2.1 oneapm-log-agent commands  
As current project bases on Apach Flume which helps transmit data, you can launch agent basing on flume commands.
bin/flume-ng agent --conf /root/apache-flume-1.6.0-bin-liuqy/localConf --conf-file /root/apache-flume-1.6.0-bin-liuqy/localConf/logfile.properties --name a1
