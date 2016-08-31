/**
 * Project Name:flume-ng-elasticsearch-sink
 * File Name:ElasticSearchSourceSerializer.java
 * Package Name:org.apache.flume.sink.elasticsearch.serializer
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.sink.elasticsearch.v211.serializer;

import java.io.IOException;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.conf.ComponentConfiguration;
import org.elasticsearch.common.io.BytesStream;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;

import com.oneapm.log.agent.flume.sink.elasticsearch.v211.ElasticSearchEventSerializer;

/**
 * ClassName:ElasticSearchSourceSerializer <br/>
 * Function: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
public class ElasticSearchSourceSerializer implements ElasticSearchEventSerializer {
    
    @Override
    public void configure(Context context) {
        // NO-OP...
    }
    
    @Override
    public void configure(ComponentConfiguration conf) {
        // NO-OP...
    }
    
    /**
     * @see org.apache.flume.sink.elasticsearch.ElasticSearchEventSerializer#getContentBuilder(org.apache.flume.Event)
     */
    @Override
    public BytesStream getContentBuilder(Event event) throws IOException {
        BytesStreamOutput bos = new BytesStreamOutput();
        bos.writeBytes(event.getBody());
        XContentBuilder builder = new XContentBuilder(JsonXContent.jsonXContent,
                                                      bos);
        return builder;
    }
    
}
