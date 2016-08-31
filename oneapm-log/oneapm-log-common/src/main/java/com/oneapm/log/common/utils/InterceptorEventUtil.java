package com.oneapm.log.common.utils;

import com.google.common.base.Charsets;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class 用来处理 flume {@link Event}.
 */
public class InterceptorEventUtil {
    private static final String[] druidQuery = {"broker-query", "realtime-query", "historical-query"};
    /**
     * 将已经给出的Event转化为多个Event如果eventbody含有多条指标数据。
     * 调用方法： 在List<Event> interceptro(events)方法中， 将Event interceptor(event)函数返回的单个event传入本方法，
     * 之后将本方法返回的所有Events作为 List<Event> interceptro(events)的返回值。
     *
     * @param event
     * @return
     */
    public static List<Event> eventTransformer(Event event) {
        List<Event> result = new ArrayList<>();
        String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
        if (StringUtil.containStr(eventBodyStr, druidQuery) != null) {
            result.add(event);
            return result;
        }
        eventBodyStr = eventBodyStr.replace("{","").replace("}","");
        String[] eventDataArray = eventBodyStr.split(",|:");
        int indexOfMetricName = 0;
        for (int i = 0; i < eventDataArray.length; i++) {
            if (eventDataArray[i].contains("metricname")) {
                indexOfMetricName = i;
                break;
            }
        }
        String newEventBody = "{";
        String newEventBodyCommonPart = "";
        for (int i = 0; i < eventDataArray.length; i+=2) {
            if (i < indexOfMetricName) {
                newEventBody = newEventBody + formatMetricNameOrData(eventDataArray[i]) + ":" + eventDataArray[i+1] + ",";
            } else if (i >= indexOfMetricName) {
                if (i == indexOfMetricName) {
                    newEventBodyCommonPart = newEventBody;
                }
                newEventBody = newEventBodyCommonPart + formatMetricNameOrData(eventDataArray[i]) + ":" + eventDataArray[i+1]
                        + "," + formatMetricNameOrData(eventDataArray[i+2]) + ":" + eventDataArray[i+3] + "}";
                i+=2;
                Event newEvent = EventBuilder.withBody(newEventBody, Charsets.UTF_8, event.getHeaders());
                result.add(newEvent);
            }
        }
        return result;
    }

    private static String formatMetricNameOrData(String eventData) {
        if (eventData.contains("metricname")) {
            return "\"metricname\"";
        } else if (eventData.contains("metricdata")) {
            return "\"metricdata\"";
        } else {
            return eventData;
        }
    }
}
