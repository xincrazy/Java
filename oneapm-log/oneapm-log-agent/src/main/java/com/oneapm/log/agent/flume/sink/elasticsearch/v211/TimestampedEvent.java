/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.oneapm.log.agent.flume.sink.elasticsearch.v211;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import lombok.Getter;

import org.apache.commons.lang.StringUtils;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.event.SimpleEvent;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentParser.Token;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.oneapm.log.common.constants.OverallConstants;

/**
 * {@link org.apache.flume.Event} implementation that has a timestamp. The timestamp is taken from
 * (in order of precedence):
 * <ol>
 * <li>The "timestamp" header of the base event, if present</li>
 * <li>The "@timestamp" header of the base event, if present</li>
 * <li>The current time in millis, otherwise</li>
 * </ol>
 */
@Getter
final class TimestampedEvent extends SimpleEvent {
    
    public static final Logger       log    = LoggerFactory.getLogger(TimestampedEvent.class);
    
    public static final ObjectMapper mapper = new ObjectMapper();
    
    private final long               timestamp;
    
    private String                   indexName;
    
    private String                   indexType;
    
    private Event                    document;
    
    /**
     * Creates a new instance of TimestampedEvent.
     * 
     * @param base
     */
    TimestampedEvent(Event base) {
        // get body
        setBody(base.getBody());
        Map<String, String> headers = Maps.newHashMap(base.getHeaders());
        // get timestamp from header
        String timestampString = headers.get("timestamp");
        if (StringUtils.isBlank(timestampString)) {
            timestampString = headers.get("@timestamp");
        }
        // get timestamp from body, parse body as JSON
        if (StringUtils.isBlank(timestampString)) {
            timestampString = parseBody(base.getBody(),
                                        "timestamp");
        }
        if (StringUtils.isBlank(timestampString)) {
            this.timestamp = DateTimeUtils.currentTimeMillis();
            headers.put("timestamp",
                        String.valueOf(timestamp));
        } else {
            this.timestamp = Long.valueOf(timestampString);
        }
        // indexname
        String name = headers.get("indexName");
        name = StringUtils.isEmpty(name)
                                        ? headers.get("@indexName")
                                        : name;
        this.indexName = StringUtils.isEmpty(name)
                                                  ? parseBody(base.getBody(),
                                                              "indexName")
                                                  : name;
        // indextype
        String type = headers.get("indexType");
        type = StringUtils.isEmpty(type)
                                        ? headers.get("@indexType")
                                        : type;
        this.indexType = StringUtils.isEmpty(type)
                                                  ? parseBody(base.getBody(),
                                                              "indexType")
                                                  : type;
        // document
        String realDocument = parseBody(base.getBody(),
                                        "document");
        this.document = StringUtils.isEmpty(realDocument)
                                                         ? null
                                                         : EventBuilder.withBody(realDocument,
                                                                                 Charset.forName(OverallConstants.DEFAULT_CHARSET_NAME));
        // get header
        setHeaders(headers);
    }
    
    /**
     * parseBody: parse timestamp and indexname and indextype <br/>
     * 
     * @author hadoop
     * @param body
     * @return
     * @since JDK 1.8
     */
    private String parseBody(byte[] body,
                             String key) {
        //
        if (StringUtils.isBlank(key)) {
            log.error("target key is blank {}",
                      key);
            return null;
        }
        // parse body for timestamp and @timestamp and @indexname and @indextype
        try {
            // parse the json byte[] body as JSON string
            XContentParser parser = XContentFactory.xContent(XContentFactory.xContentType(body))
                                                   .createParser(body);
            // find timestamp and @timestamp and @indexname and @indextype
            while (true) {
                // get next token
                Token t = parser.nextToken();
                // over the json string
                if (t == null) {
                    break;
                }
                // FIELD_NAME
                if (t.compareTo(Token.FIELD_NAME) == 0 &&
                    key.equalsIgnoreCase(parser.text())) {
                    // goto value token
                    Token value = parser.nextToken();
                    if (value != null) {
                        // number format
                        if (value.compareTo(Token.VALUE_NUMBER) == 0) {
                            return String.valueOf(parser.longValue());
                        }
                        // string format
                        if (value.compareTo(Token.VALUE_STRING) == 0) {
                            return parser.text();
                        }
                        // start object
                        if (value.compareTo(Token.START_OBJECT) == 0) {
                            JsonNode node = mapper.readTree(body);
                            JsonNode d = node.findValue(key);
                            return d.toString();
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.error("{}",
                      e);
        }
        return null;
    }
    
}
