/**
 * Project Name:oneapm-log-agent
 * File Name:JMXSource.java
 * Package Name:com.oneapm.log.agent.flume.source.jmx
 * Date: 
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.log.agent.flume.source.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.apache.flume.ChannelException;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.oneapm.log.common.constants.OverallConstants;

import lombok.Getter;

/**
 * ClassName:JMXSource <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 * @see
 */
public class JMXSource extends AbstractSource implements Configurable, EventDrivenSource {
    
    public static final Logger                     log       = LoggerFactory.getLogger(JMXSource.class);
    
    private static final Validator                 validator = Validation.buildDefaultValidatorFactory()
                                                                         .getValidator();
    
    @NotNull
    private Context                                context;
    
    @NotNull
    @NotBlank
    @NotEmpty
    private String                                 host;
    
    private Integer                                port;
    
    @NotNull
    @NotBlank
    @NotEmpty
    private String                                 rmiHost;
    
    @NotNull
    @Range(min = 1,
           max = 65535)
    private Integer                                rmiPort;
    
    @NotNull
    @Range(min = 10)
    private Long                                   interval;
    
    @NotNull
    @Range(min = 1,
           max = 1000)
    private Integer                                batch;
    
    @NotNull
    @Range(min = 1)
    private Long                                   minInterval;
    
    @NotNull
    @Valid
    private AbstractJMXSerializer                  serializer;
    
    @NotNull
    private ScheduledExecutorService               scheduler = Executors.newSingleThreadScheduledExecutor();
    
    @NotNull
    private JMXServiceURL                          url       = null;
    
    @NotNull
    private JMXConnector                           connector = null;
    
    @NotNull
    private volatile MBeanServerConnection         conn      = null;
    
    @NotNull
    private final List<Event>                      events    = Collections.synchronizedList(new ArrayList<Event>());
    
    @Getter
    private ChannelProcessor                       channel   = null;
    
    @NotNull
    private final BiConsumer<List<Event>, Integer> process   = (eventList,
                                                                batchSize) -> {
                                                                 // for multi threads
                                                                 synchronized (eventList) {
                                                                     // split into groups of
                                                                     // sub-list
                                                                     List<List<Event>> batchLists = Lists.partition(eventList,
                                                                                                                    batchSize);
                                                                     // push each sub-list
                                                                     batchLists.forEach(oneBatch -> {
                                                                         try {
                                                                             // push batch of events
                                                                             // to channel
                                                                             getChannel().processEventBatch(oneBatch);
                                                                             log.info("send {} events.",
                                                                                      oneBatch.size());
                                                                         } catch (ChannelException e) {
                                                                             log.error("send events failed because of {}",
                                                                                       e);
                                                                         }
                                                                     });
                                                                     // clear the whole list
                                                                     eventList.clear();
                                                                 }
                                                             };
    
    @NotNull
    private Runnable                               task      = () -> {
                                                                 // make sure the schedulser is not
                                                                 // shutdown
                                                                 if (scheduler.isShutdown()) {
                                                                     return;
                                                                 }
                                                                 // get, parse, push
                                                                 try {
                                                                     // serialize to a JSON string
                                                                	 
                                                                     List<String> metrics = serializer.pullMetrics(conn,
                                                                                                                   context);
                                                                     // construct Event for flume
                                                                     metrics.forEach(eachMetric -> events.add(EventBuilder.withBody(eachMetric,
                                                                                                                                    Charset.forName(OverallConstants.DEFAULT_CHARSET_NAME))));
                                                                     // timeout or full batch
                                                                     if (interval.longValue() > minInterval.longValue() ||
                                                                         (interval.longValue() <= minInterval.longValue() && events.size() >= batch.intValue())) {
                                                                         // split events into group
                                                                         // of batch sub-list and
                                                                         // push to channel
                                                                         process.accept(events,
                                                                                        batch);
                                                                     }
                                                                 } catch (IOException e) {
                                                                     log.error("get metrics failed: {}",
                                                                               e);
                                                                 }
                                                             };
    
    /**
     * @see org.apache.flume.conf.Configurable#configure(org.apache.flume.Context)
     */
    @Override
    public void configure(Context context) {
        this.context = context;
        // config
        host = context.getString(JMXSourceConstants.HOST,
                                 JMXSourceConstants.DEFAULT_HOST);
        port = context.getInteger(JMXSourceConstants.PORT);
        rmiHost = context.getString(JMXSourceConstants.RMI_HOST,
                                    JMXSourceConstants.DEFAULT_RMI_HOST);
        rmiPort = context.getInteger(JMXSourceConstants.RMI_PORT);
        interval = context.getLong(JMXSourceConstants.INTERVAL,
                                   JMXSourceConstants.DEFAULT_INTERVAL);
        batch = context.getInteger(JMXSourceConstants.BATCH,
                                   JMXSourceConstants.DEFAULT_BATCH);
        minInterval = context.getLong(JMXSourceConstants.INTERVAL_MIN,
                                      JMXSourceConstants.DEFAULT_INTERVAL_MIN);
        try {
            Class<?> serializerType = Class.forName(context.getString(JMXSourceConstants.SERIALIZER_TYPE,
                                                                      JMXSourceConstants.DEFAULT_SERIALIZER_TYPE));
            if (!AbstractJMXSerializer.class.isAssignableFrom(serializerType))
                throw new RuntimeException("!AbstractJMXSerializer.class.isAssignableFrom(serializerType)");
            serializer = (AbstractJMXSerializer) serializerType.newInstance();
            serializer.build();
        } catch (Exception e) {
            serializer = null;
            log.error("load class failed {}",
                      e);
        }
        // init
        try {
            if (url == null)
                url = new JMXServiceURL("service:jmx:rmi://" +
                                        host +
                                        (port == null
                                                     ? ""
                                                     : ":" +
                                                       port.intValue()) +
                                        "/jndi/rmi://" +
                                        rmiHost +
                                        ":" +
                                        rmiPort.intValue() +
                                        "/jmxrmi");
            log.info("jmx service url is: {}",
                     url.toString());
        } catch (MalformedURLException e) {
            url = null;
            log.error("create jmxserviceurl failed: {}",
                      e);
        }
        try {
            if (connector == null)
                connector = JMXConnectorFactory.connect(url);
        } catch (IOException e) {
            connector = null;
            log.error("create connector failed: {}",
                      e);
        }
        try {
            if (conn == null)
                conn = connector.getMBeanServerConnection();
        } catch (IOException e) {
            conn = null;
            log.error("create connection failed: {}",
                      e);
        }
        // validate
        Set<ConstraintViolation<JMXSource>> constraints = validator.validate(this);
        constraints.forEach(cv -> {
            log.error("validation error: {}-{}",
                      cv.getPropertyPath(),
                      cv.getMessage());
        });
        if (constraints.size() > 0) {
            System.exit(-1);
        }
    }
    
    /**
     * @see org.apache.flume.source.AbstractSource#start()
     */
    @Override
    public synchronized void start() {
        //
        channel = getChannelProcessor();
        //
        scheduler.scheduleAtFixedRate(task,
                                      0L,
                                      interval.longValue(),
                                      TimeUnit.MILLISECONDS);
        super.start();
    }
    
    /**
     * @see org.apache.flume.source.AbstractSource#stop()
     */
    @Override
    public synchronized void stop() {
        scheduler.shutdown();
        while (!scheduler.isTerminated()) {
            try {
                scheduler.awaitTermination(1,
                                           TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("await scheduler terminate error: {}",
                          e);
                break;
            }
        }
        super.stop();
    }
    
}
