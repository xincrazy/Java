package com.oneapm.log.agent.flume.interceptor.transform;

import java.util.Iterator;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;

public class TransformInterceptor implements Interceptor {

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Event intercept(Event event) {
		String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
		Parser parser = null;
		parser = LogParserFactory.getInstance().getParser();
		if (parser != null) {
			String parserResult = parser.parser(eventBodyStr);
			if (parserResult != null)
				event.setBody(parserResult.getBytes(Charsets.UTF_8));
			else
				event.setBody("parserResult is null！".getBytes(Charsets.UTF_8));
		} else
			event.setBody("No parser is found!".getBytes(Charsets.UTF_8));
		return event;
	}

	@Override
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

	public static class TransformInterceptorBuilder implements Interceptor.Builder {

		@Override
		public void configure(Context content) {

		}

		@Override
		public Interceptor build() {

			return new TransformInterceptor();
		}

	}


}