/**
 * Project Name:oneapm-log-agent
 * File Name:TestElasticsearchSource.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

import com.oneapm.log.agent.flume.source.elasticsearch.v211.util.ElasticsearchSourceConstants;

/**
 * ClassName:TestElasticsearchSource <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
public class TestElasticsearchSource {
    
   /* *//**
     * testLocalES01: <br/>
     * 
     * @author hadoop
     * @throws EventDeliveryException
     * @since JDK 1.7
     *//*
    @Test
    public void testLocalES01() throws EventDeliveryException {
        // context
        Context context = new Context();
        Map<String, String> params = new HashMap<String, String>();
        params.put("cluster.name",
                   "test_1");
        params.put("host.name",
                   "127.0.0.1");
        params.put("port",
                   "9300");
        params.put("batch.size",
                   "100");
        params.put("duration",
                   "600000");
        params.put("buffer.size",
                   "10000");
        context.putAll(params);
        // source
        ElasticsearchSource source = new ElasticsearchSource();
        source.configure(context);
        // call
        source.start();
        source.process();
    }
    
    *//**
     * test002: <br/>
     * 
     * @author hadoop
     * @since JDK 1.7
     *//*
    @Test
    public void test002() {
        Settings settings = Settings.settingsBuilder()
                                    .put("cluster.name",
                                         "test_1")
                                    .build();
        TransportClient client = TransportClient.builder()
                                                .settings(settings)
                                                .build()
                                                .addTransportAddresses(Arrays.asList("127.0.0.1".split(","))
                                                                             .stream()
                                                                             .map(a -> {
                                                                                 try {
                                                                                     return new InetSocketTransportAddress(InetAddress.getByName(a.split(":").length < 1
                                                                                                                                                                        ? ElasticsearchSourceConstants.DEFAULT_HOST_NAME
                                                                                                                                                                        : a.split(":")[0].trim()),
                                                                                                                           a.split(":").length < 2
                                                                                                                                                  ? 9300
                                                                                                                                                  : Integer.valueOf(a.split(":")[1].trim())
                                                                                                                                                           .intValue());
                                                                                 } catch (Exception e) {
                                                                                     return null;
                                                                                 }
                                                                             })
                                                                             .filter(t -> t != null)
                                                                             .toArray(InetSocketTransportAddress[]::new));
        NodesStatsResponse ni = client.admin()
                                      .cluster()
                                      .prepareNodesStats()
                                      .all()
                                      .get();
        System.out.println(ni);
    }
    
    @Getter
    public enum Test003 {
        A("aaa"),
        B("bbb"),
        C("ccc");
        private String x;
        
        private Test003(String x) {
            this.x = x;
        }
    }
    
    *//**
     * test003: <br/>
     * 
     * @author hadoop
     * @since JDK 1.7
     *//*
    @Test
    public void test003() {
        System.out.println(Test003.A.toString());
        System.out.println(Test003.B);
        for (Test003 t : Test003.values()) {
            System.out.println(t.toString());
            System.out.println(t);
            System.out.println(t.getX());
        }
    }
    
    *//**
     * test004: <br/>
     * 
     * @author hadoop
     * @throws EventDeliveryException
     * @since JDK 1.8
     *//*
    @Test
    public void test004() throws EventDeliveryException {
        // context
        Context context = new Context();
        Map<String, String> params = new HashMap<String, String>();
        params.put("cluster.name",
                   "test_1");
        params.put("host.name",
                   "127.0.0.1");
        params.put("port",
                   "9300");
        params.put("batch.size",
                   "100");
        params.put("duration",
                   "600000");
        params.put("buffer.size",
                   "10000");
        context.putAll(params);
        // source
        ElasticsearchSource source = new ElasticsearchSource();
        source.configure(context);
        // call
        source.start();
        source.process();
    }
    */
}
