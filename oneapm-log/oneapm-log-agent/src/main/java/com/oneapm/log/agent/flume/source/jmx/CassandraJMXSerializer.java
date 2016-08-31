/**
 * Project Name:oneapm-log-agent
 * File Name:CassandraJMXSerializer.java
 * Package Name:com.oneapm.log.agent.flume.source.jmx
 * Date: 
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.log.agent.flume.source.jmx;

import java.util.*;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.flume.Context;

/**
 * ClassName:CassandraJMXSerializer <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 * @see
 */
public class CassandraJMXSerializer extends AbstractJMXSerializer {
    
    /**
     * @see com.oneapm.log.agent.flume.source.jmx.AbstractJMXSerializer#build()
     */
    @Override
    public void build() {
        try {
            addKeyValue(new ObjectName("org.apache.cassandra.metrics:type=ClientRequest,scope=Read,name=Latency"),
                        "MeanRate");
            addKeyValue(new ObjectName("org.apache.cassandra.metrics:type=ClientRequest,scope=Read,name=Latency"),
                        "Count");
            addKeyValue(new ObjectName("org.apache.cassandra.metrics:type=ClientRequest,scope=Read,name=Latency"),
                        "Max");
            addKeyValue(new ObjectName("org.apache.cassandra.metrics:type=ClientRequest,scope=Read,name=Latency"),
                        "Min");
            addKeyValue(new ObjectName("org.apache.cassandra.metrics:type=ClientRequest,scope=Read,name=Latency"),
                        "StdDev");
        } catch (MalformedObjectNameException e) {
            log.error("{}",
                      e);
        }
    }
    
    /**
     * @see com.oneapm.log.agent.flume.source.jmx.AbstractJMXSerializer#formateMetrics(java.util.Map,
     *      org.apache.flume.Context)
     */
    @Override
    protected List<String> formateMetrics(Map<String, Object> metrics,
                                          Context ctx) {
        final List<String> result = new ArrayList<>();
        metrics.forEach((key,
                         value) -> {
            // construct map
            Map<String, Object> metric = new HashMap<String, Object>();
            metric.put("timestamp",
                       System.currentTimeMillis());
            metric.put("servicename",
                       ctx.getSubProperties(JMXSourceConstants.SERVICE_PREFIX)
                          .getOrDefault("name",
                                        "node-1"));
            metric.put("servicetype",
                       "cassandra");
            metric.put("metricname",
                       key);
            metric.put("metricdata",
                       value);
            // add
            result.add(jsonFormatter.apply(metric));
        });
        return result;
    }
}
