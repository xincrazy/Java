package com.oneapm.log.agent.flume.source.http;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.http.HTTPSourceHandler;

import com.oneapm.log.common.constants.OverallConstants;

/**
 * A simple http handler which just keep all the original data from http source
 * without any format validation and data modification.
 * 
 * @author Liuqy
 *
 */
public class SimpleHttpHandler implements HTTPSourceHandler, OverallConstants {

	public SimpleHttpHandler() {
	}

	@Override
	public List<Event> getEvents(HttpServletRequest request) throws Exception {
		BufferedReader reader = request.getReader();
		List<Event> eventList = new ArrayList<Event>();

		String currentLine;
		while ((currentLine = reader.readLine()) != null) {

			Event e = new SimpleEvent();
			e.setBody(currentLine.getBytes(DEFAULT_CHARSET_NAME));

			eventList.add(e);
		}

		return getSimpleEvents(eventList);
	}

	@Override
	public void configure(Context context) {
	}

	private List<Event> getSimpleEvents(List<Event> events) {
		List<Event> newEvents = new ArrayList<Event>(events.size());
		for (Event e : events) {
			newEvents.add(EventBuilder.withBody(e.getBody(), e.getHeaders()));
		}
		return newEvents;
	}
}
