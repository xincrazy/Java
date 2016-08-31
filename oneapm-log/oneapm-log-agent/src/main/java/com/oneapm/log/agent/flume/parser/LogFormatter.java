package com.oneapm.log.agent.flume.parser;


public enum LogFormatter {
  
  DRUID(new String[]{ "timestamp", "hostname", "servicename","servicetype","servicesubtype","servicesubtype1",
          "metricname", "metricdata", "metricname1", "metricdata1", "metricname2", "metricdata2"}),
  SYSTEM(new String[]{"timestamp", "hostname", "maxDiskUsage", "memUsage", "cpuUsage"}),
  KAFKA(new String[]{ "timestamp","group", "topic","cluster_type","partition","offset", "logsize", "lag" }),
  TRANSFORM(new String []{ "key", "eventCategory", "metrics", "ttl", "timestamp" }),
  AI(new String[]{"timestamp","metricdata_ai_consumer"}),
  JVM(new String[]{"timestamp","hostname","appName","pid","fullGCTime","beforGcMem","afterGcMem"}),
  PROCESS(new String[]{"timestamp","hostname","processName","keyword","pid","alive"});



  private String[] context;

  public String[] getMetricContext() {
    return this.context;
  }

  LogFormatter(String[] context) {
    this.context = context;
  }
}