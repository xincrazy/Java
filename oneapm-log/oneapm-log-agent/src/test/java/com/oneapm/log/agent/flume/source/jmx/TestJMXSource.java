/**
 * Project Name:oneapm-log-agent
 * File Name:TestJMXSource.java
 * Package Name:com.oneapm.log.agent.flume.source.jmx
 * Date: 
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.log.agent.flume.source.jmx;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Lists;
import org.apache.flume.Channel;
import org.apache.flume.ChannelSelector;
import org.apache.flume.Context;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.channel.MemoryChannel;
import org.apache.flume.channel.ReplicatingChannelSelector;
import org.apache.flume.conf.Configurables;
import org.apache.flume.source.AbstractSource;
import org.apache.flume.source.ExecSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.management.ObjectName;

/**
 * ClassName:TestJMXSource <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 * @see
 */
public class TestJMXSource {
    private AbstractSource source;
    private Channel channel = new MemoryChannel();

    private Context context = new Context();

    private ChannelSelector rcs = new ReplicatingChannelSelector();

    @Before
    public void setUp() {
        context.put("keep-alive", "1");
        context.put("capacity", "1001");
        context.put("transactionCapacity", "1001");
        Configurables.configure(channel, context);
        rcs.setChannels(Lists.newArrayList(channel));

        source = new ExecSource();
        source.setChannelProcessor(new ChannelProcessor(rcs));
    }

    @After
    public void tearDown() {
        source.stop();

        // Remove the MBean registered for Monitoring
        ObjectName objName = null;
        try {
            objName = new ObjectName("org.apache.flume.source"
                    + ":type=" + source.getName());

            ManagementFactory.getPlatformMBeanServer().unregisterMBean(objName);
        } catch (Exception ex) {
            System.out.println("Failed to unregister the monitored counter: "
                    + objName + ex.getMessage());
        }
    }
    
   /* *//**
     * testLocal01: <br/>
     *
     * @author hadoop
     * @since JDK 1.8
     *//*
    @Test
    public void testLocal01() {
        // given
        Context context = new Context();
        Map<String, String> params = new HashMap<String, String>();
        params.put("host",
                   "10.128.7.97");
        params.put("rmi.host",
                   "10.128.7.97");
        params.put("rmi.port",
                   "7199");
        context.putAll(params);
        // when
        JMXSource source = new JMXSource();
        source.configure(context);
        // then
        source.start();
    }

    *//**
     * testLocal02: <br/>
     *
     * @author hadoop
     * @since JDK 1.8
     *//*
    @Test
    public void testLocal02() {
        // given
        Context context = new Context();
        Map<String, String> params = new HashMap<String, String>();
        params.put("host",
                   "localhost");
        params.put("rmi.host",
                   "localhost");
        params.put("rmi.port",
                   "9999");
        params.put("serializer.type",
                   "com.oneapm.log.agent.flume.source.jmx.SampleJMXSerializer");
        context.putAll(params);
        // when
        JMXSource source = new JMXSource();
        source.configure(context);
        // then
        source.start();
    }
    */

}
