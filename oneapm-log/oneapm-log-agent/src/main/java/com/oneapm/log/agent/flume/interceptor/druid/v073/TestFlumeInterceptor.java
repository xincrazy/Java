package com.oneapm.log.agent.flume.interceptor.druid.v073;

import java.util.Iterator;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import com.google.common.base.Charsets;

public class TestFlumeInterceptor implements Interceptor {

	private TestFlumeInterceptor(Context ctx) {
	}

	public void initialize() {

	}

	public Event intercept(Event event) {
		addPreposition(event);
		return event;
	}

	private void addPreposition(Event event) {
		String newContent = "Modified Event: " + new String(event.getBody(), Charsets.UTF_8);
		System.out.println("Event processed by interceptor TestFlumeInterceptor: " + newContent);
		// event.setBody(newContent.getBytes(Charsets.UTF_8));
		newContent = "K1:v1, k2:v2";
		newContent = "{\"componentName\":\"druid\", \"type\":\"metrics\", \"timestamp\":\"2016-05-04T01:42:29.592Z\\\", \"metric\":\"events/processed\",\"value\":1036943,\"user2\":\"druid_metric_1day\"}";
		event.setBody(newContent.getBytes(Charsets.UTF_8));
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

	public void close() {

	}

	public static class CounterInterceptorBuilder implements Interceptor.Builder {

		private Context ctx;

		public Interceptor build() {
			return new TestFlumeInterceptor(ctx);
		}

		public void configure(Context context) {
			this.ctx = context;
		}
	}
}