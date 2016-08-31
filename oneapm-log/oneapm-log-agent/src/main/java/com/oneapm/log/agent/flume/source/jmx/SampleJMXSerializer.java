/**
 * Project Name:oneapm-log-agent
 * File Name:SampleJMXSerializer.java
 * Package Name:com.oneapm.log.agent.flume.source.jmx
 * Date: 
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.log.agent.flume.source.jmx;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.flume.Context;

/**
 * ClassName:SampleJMXSerializer <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 * @see
 */
public class SampleJMXSerializer extends AbstractJMXSerializer {
    
    /**
     * @see com.oneapm.log.agent.flume.source.jmx.AbstractJMXSerializer#build()
     */
    @Override
    public void build() {
        try {
            addKeyValue(new ObjectName("com.oneapm.jmx.common:type=MonitorImpl"),
                        "Content");
            addKeyValue(new ObjectName("com.oneapm.jmx.common:type=MonitorImpl"),
                        "P.x",
                        "px");
            addKeyValue(new ObjectName("com.oneapm.jmx.common:type=MonitorImpl"),
                        "P.y",
                        "py");
            addKeyValue(new ObjectName("com.oneapm.jmx.common:type=Node"),
                        "Os.mem.totalBytes",
                        "totalBytes");
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
        return Arrays.asList(jsonFormatter.apply(metrics));
    }
    
}
