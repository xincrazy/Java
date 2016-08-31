/**
 * Project Name:oneapm-log-agent
 * File Name:TestElasticSearchSink.java
 * Package Name:com.oneapm.log.agent.flume.sink.elasticsearch.v211
 * Date: 
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.log.agent.flume.sink.elasticsearch.v211;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import lombok.Getter;
import lombok.Setter;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * ClassName:TestElasticSearchSink <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 * @see
 */
public class TestElasticSearchSink {
    
/*    public static final Logger       log    = LoggerFactory.getLogger(TestElasticSearchSink.class);
    
    public static final ObjectMapper mapper = new ObjectMapper();
    
    *//**
     * ClassName: BoxedEvent <br/>
     * Function: <br/>
     * date: <br/>
     *
     * @author hadoop
     * @version TestElasticSearchSink
     * @since JDK 1.8
     *//*
    @Getter
    @Setter
    public static class BoxedEvent {
        
        private String     indexName = "box_index";
        
        private String     indexType = "box_type";
        
        private InnerEvent document  = new InnerEvent();
        
        @Getter
        @Setter
        public static class InnerEvent {
            
            private Long   timestamp = System.currentTimeMillis();
            
            private String field     = "inner";
            
        }
        
    }
    
    *//**
     * testIndexNameType01: <br/>
     * 
     * @author hadoop
     * @throws Exception
     * @throws JsonProcessingException
     * @since JDK 1.8
     *//*
    @Test
    public void testIndexNameType01() throws JsonProcessingException, Exception {
        // given
        Context ctx = new Context();
        ctx.put(ElasticSearchSinkConstants.HOSTNAMES,
                "127.0.0.1");
        ctx.put(ElasticSearchSinkConstants.INDEX_NAME,
                "sink-1");
        ctx.put(ElasticSearchSinkConstants.INDEX_TYPE,
                "logs");
        ctx.put(ElasticSearchSinkConstants.CLUSTER_NAME,
                "test_1");
        ctx.put(ElasticSearchSinkConstants.CLIENT_TYPE,
                "transport");
        ctx.put(ElasticSearchSinkConstants.SERIALIZER,
                "com.oneapm.log.agent.flume.sink.elasticsearch.v211.serializer.ElasticSearchSourceSerializer");
        ctx.put(ElasticSearchSinkConstants.INDEX_NAME_BUILDER,
                "com.oneapm.log.agent.flume.sink.elasticsearch.v211.TimeBasedIndexNameBuilder");
        // when
        ElasticSearchSink sink = new ElasticSearchSink();
        sink.configure(ctx);
        // then
        try {
            sink.start();
        } catch (Exception e) {
            log.error("{}",
                      e);
        }
        BoxedEvent event = new BoxedEvent();
        sink.getClient()
            .addEvent(EventBuilder.withBody(mapper.writeValueAsString(event),
                                            Charset.forName("UTF-8")),
                      sink.getIndexNameBuilder(),
                      sink.getIndexType(),
                      sink.getTTLMs());
        sink.getClient()
            .execute();
    }
    
    *//**
     * testIndexNameType02: <br/>
     * 
     * @author hadoop
     * @throws JsonProcessingException
     * @throws Exception
     * @since JDK 1.8
     *//*
    @Test
    public void testIndexNameType02() throws JsonProcessingException, Exception {
        // given
        Context ctx = new Context();
        ctx.put(ElasticSearchSinkConstants.HOSTNAMES,
                "127.0.0.1");
        ctx.put(ElasticSearchSinkConstants.INDEX_NAME,
                "sink-1");
        ctx.put(ElasticSearchSinkConstants.INDEX_TYPE,
                "logs");
        ctx.put(ElasticSearchSinkConstants.CLUSTER_NAME,
                "test_1");
        ctx.put(ElasticSearchSinkConstants.CLIENT_TYPE,
                "transport");
        ctx.put(ElasticSearchSinkConstants.SERIALIZER,
                "com.oneapm.log.agent.flume.sink.elasticsearch.v211.serializer.ElasticSearchSourceSerializer");
        ctx.put(ElasticSearchSinkConstants.INDEX_NAME_BUILDER,
                "com.oneapm.log.agent.flume.sink.elasticsearch.v211.TimeBasedIndexNameBuilder");
        // when
        ElasticSearchSink sink = new ElasticSearchSink();
        sink.configure(ctx);
        // then
        try {
            sink.start();
        } catch (Exception e) {
            log.error("{}",
                      e);
        }
        InnerEvent event = new InnerEvent();
        sink.getClient()
            .addEvent(EventBuilder.withBody(mapper.writeValueAsString(event),
                                            Charset.forName("UTF-8")),
                      sink.getIndexNameBuilder(),
                      sink.getIndexType(),
                      sink.getTTLMs());
        sink.getClient()
            .execute();
    }
    
    *//**
     * test02: <br/>
     * 
     * @author hadoop
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     * @since JDK 1.8
     *//*
    @Test
    public void test02() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        BoxedEvent event = new BoxedEvent();
        System.out.println(mapper.writeValueAsString(event));
        Event e = EventBuilder.withBody(mapper.writeValueAsString(event),
                                        Charset.forName("UTF-8"));
        TimestampedEvent parser = new TimestampedEvent(e);
        Method m = TimestampedEvent.class.getDeclaredMethod("parseBody",
                                                            byte[].class,
                                                            String.class);
        m.setAccessible(true);
        m.invoke(parser,
                 e.getBody(),
                 "document");
        JsonNode node = mapper.readTree(e.getBody());
        JsonNode d = node.findValue("document");
        System.out.println(d.toString());
    }*/

}
