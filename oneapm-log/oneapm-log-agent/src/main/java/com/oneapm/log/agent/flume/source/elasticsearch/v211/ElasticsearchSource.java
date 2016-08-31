/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oneapm.log.agent.flume.source.elasticsearch.v211.jmx.ElasticsearchSourceCounter;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.util.ElasticsearchSourceConstants;

/**
 * ClassName: ElasticsearchSource <br/>
 * Function: <br/>
 * date: <br/>
 *
 * @author hadoop
 * @version
 * @since JDK 1.7
 */
public class ElasticsearchSource extends AbstractSource implements Configurable, PollableSource {
    
    private static final Logger                            log       = LoggerFactory.getLogger(ElasticsearchSource.class);
    
    private Context                                        context;
    
    private Optional<ElasticsearchSourceCounter>           counter   = Optional.empty();
    
    private Optional<ElasticsearchSourceEventBodyIterator> iterator  = Optional.empty();
    
    private final List<Event>                              eventList = new ArrayList<Event>();
    
    /**
     * @see org.apache.flume.PollableSource#process()
     */
    @Override
    public Status process() throws EventDeliveryException {
        // size
        int batchSize = (context.getInteger(ElasticsearchSourceConstants.BATCH_SIZE) != null && context.getInteger(ElasticsearchSourceConstants.BATCH_SIZE)
                                                                                                       .intValue() > 0)
                                                                                                                       ? context.getInteger(ElasticsearchSourceConstants.BATCH_SIZE)
                                                                                                                                .intValue()
                                                                                                                       : ElasticsearchSourceConstants.DEFAULT_BATCH_SIZE;
        // validate
        if (iterator.isPresent() &&
            iterator.get()
                    .hasNext()) {
            // remain
            counter.ifPresent(mx -> mx.setQueueSize(iterator.get()
                                                            .getRemaining()));
            // clear
            eventList.clear();
            // batch
            int count = 0;
            while (count < batchSize) {
                // has next
                if (iterator.get()
                            .hasNext()) {
                    count++;
                    eventList.add(EventBuilder.withBody(iterator.get()
                                                                .next()
                                                                .toBytes()));
                } else {
                    getChannelProcessor().processEventBatch(eventList);
                    return Status.BACKOFF;
                }
            }
            // send a batch
            getChannelProcessor().processEventBatch(eventList);
            if (iterator.get()
                        .hasNext()) {
                return Status.READY;
            } else {
                return Status.BACKOFF;
            }
        } else {
            return Status.BACKOFF;
        }
    }
    
    /**
     * @see org.apache.flume.conf.Configurable#configure(org.apache.flume.Context)
     */
    @Override
    public void configure(Context context) {
        // backup
        this.context = context;
        // build iterator
        if (!iterator.isPresent()) {
            iterator = ElasticsearchSourceEventBodyIterator.build(context);
        } else {
            iterator.get()
                    .configure(context);
        }
        // build elasticsearch mxbean for jmx
        if (!counter.isPresent()) {
            counter = Optional.ofNullable(new ElasticsearchSourceCounter(getName()));
        }
    }
    
    /**
     * @see org.apache.flume.source.AbstractSource#start()
     */
    @Override
    public synchronized void start() {
        iterator.ifPresent(i -> i.start());
        counter.ifPresent(c -> c.start());
        super.start();
    }
    
    /**
     * @see org.apache.flume.source.AbstractSource#stop()
     */
    @Override
    public synchronized void stop() {
        iterator.ifPresent(i -> i.close());
        counter.ifPresent(c -> c.stop());
        super.stop();
    }
}
