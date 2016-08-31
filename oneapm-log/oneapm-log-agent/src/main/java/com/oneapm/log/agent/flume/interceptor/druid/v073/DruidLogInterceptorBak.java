package com.oneapm.log.agent.flume.interceptor.druid.v073;

import java.util.Iterator;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import com.google.common.base.Charsets;

public class DruidLogInterceptorBak implements Interceptor {

	public static final String DRUID_METRIC_TYPE = "druidMetricType";
	public static final String DEFAULT_DRUID_METRIC_TYPE = "events/thrownAway";

	private String druidMetricType;

	public DruidLogInterceptorBak(String druidMetricType) {
		this.druidMetricType = druidMetricType;
	}

	public void close() {
		// NO-OPERATION

	}

	public void initialize() {
		// NO-OPERATION

	}

	public Event intercept(Event event) {
		// Sample input data:
		// 2016-05-11 09:14:36,641 INFO c.m.e.c.LoggingEmitter
		// [MonitorScheduler-0] Event
		// [{"feed":"metrics","timestamp":"2016-05-11T09:14:36.640Z","service":"realtime","host":"10.165.113.43:8084","metric":"events/thrownAway","value":8889,"user2":"druid_metric_1day"}]

		String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
		System.out
				.println("Event processed by interceptor DruidLogInterceptor: "
						+ eventBodyStr);

		if (eventBodyStr.contains(druidMetricType))
			eventBodyStr = "The input line contains the keyword : "
					+ druidMetricType;
		else
			eventBodyStr = "The input line does NOT contain the keyword : "
					+ druidMetricType;

		event.setBody(eventBodyStr.getBytes(Charsets.UTF_8));

		return event;
	}

	public List<Event> intercept(List<Event> events) {
		for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {

			Event next = iterator.next();
			intercept(next);

			if (next == null) {
				iterator.remove();
			}
		}
		return events;
	}

	public static class DruidLogInterceptorBuilder implements
			Interceptor.Builder {

		private String druidMetricType;

		public void configure(Context context) {
			druidMetricType = context.getString(DRUID_METRIC_TYPE,
					DEFAULT_DRUID_METRIC_TYPE);

		}

		public Interceptor build() {
			return new DruidLogInterceptorBak(druidMetricType);
		}

	}

}
