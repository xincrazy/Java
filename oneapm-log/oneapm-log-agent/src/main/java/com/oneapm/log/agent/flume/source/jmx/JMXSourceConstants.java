/**
 * Project Name:oneapm-log-agent
 * File Name:JMXSourceConstants.java
 * Package Name:com.oneapm.log.agent.flume.source.jmx
 * Date: 
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.log.agent.flume.source.jmx;

/**
 * ClassName:JMXSourceConstants <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 * @see
 */
public class JMXSourceConstants {
    
    public static final String  HOST                    = "host";
    public static final String  PORT                    = "port";
    public static final String  RMI_HOST                = "rmi.host";
    public static final String  RMI_PORT                = "rmi.port";
    public static final String  INTERVAL                = "interval";
    public static final String  BATCH                   = "batch";
    public static final String  INTERVAL_MIN            = "interval.min";
    public static final String  SERIALIZER_TYPE         = "serializer.type";
    public static final String  SERVICE_PREFIX          = "service.";
    
    public static final String  DEFAULT_HOST            = "localhost";
    public static final String  DEFAULT_RMI_HOST        = "localhost";
    public static final Long    DEFAULT_INTERVAL        = 60000L;
    public static final Integer DEFAULT_BATCH           = 10;
    public static final Long    DEFAULT_INTERVAL_MIN    = 100L;
    public static final String  DEFAULT_SERIALIZER_TYPE = "com.oneapm.log.agent.flume.source.jmx.CassandraJMXSerializer";
    
}
