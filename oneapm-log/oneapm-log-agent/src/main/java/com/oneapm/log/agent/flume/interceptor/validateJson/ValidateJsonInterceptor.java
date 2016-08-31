package com.oneapm.log.agent.flume.interceptor.validateJson;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.Iterator;
import java.util.List;

import static org.apache.flume.source.MultiportSyslogTCPSource.logger;

/**
 * PcakgeName is com.oneapm.log.agent.flume.interceptor.ValidateJson.
 * Created by will on 16-8-15.
 */
public class ValidateJsonInterceptor implements Interceptor {

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
        try {
            JSONObject.parseObject(eventBodyStr);
            return event;
        } catch (Exception e) {
            logger.debug("this event is not  json ,please check it ,the content is :" + eventBodyStr + " ,the error message is : " + e.getMessage());
            return null;
        }

    }

    @Override
    public List<Event> intercept(List<Event> events) {
        for (Iterator<Event> iterator = events.iterator(); iterator.hasNext(); ) {

            Event next = iterator.next();
            Event current = intercept(next);

            if (current == null) {
                iterator.remove();
            }
        }
        return events;
    }

    @Override
    public void close() {

    }

    public static class ValidateJsonInterceptorBuilder implements Builder {

        @Override
        public Interceptor build() {
            return new ValidateJsonInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
