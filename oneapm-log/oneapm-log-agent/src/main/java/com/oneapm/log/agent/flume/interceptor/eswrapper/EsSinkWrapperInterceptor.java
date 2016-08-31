package com.oneapm.log.agent.flume.interceptor.eswrapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;

public class EsSinkWrapperInterceptor implements Interceptor {
  
  Logger logger = Logger.getLogger(this.getClass());

  private Map<String, String> map = new HashMap<String, String>();

  public EsSinkWrapperInterceptor(String indexName, String indexType) {
    // TODO Auto-generated constructor stub
   map.put("indexName", indexName);
   map.put("indexType", indexType);
  }

  @Override
  public void initialize() {
      logger.info("this instance Object's value is "+map.toString());
  }

  @Override
  public Event intercept(Event event) {
    String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
    Parser parser = null;
    try {
      parser = LogParserFactory.getInstance().getParserByClass(this.getClass(),map);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      logger.error(e.getLocalizedMessage());
    }
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

  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

  public static class EsSinkWrapperBuilder implements
      Interceptor.Builder {
    Logger logger = Logger.getLogger(this.getClass());
    
    private String indexName;

    private String indexType;
    @Override
    public void configure(Context context) {
      this.indexName = context.getString("indexName");
      this.indexType = context.getString("indexType");
      logger.info("has been entered the jsonItemAddInterceptorBuilder,pay more attention on it");
    }

    @Override
    public Interceptor build() {
      return new EsSinkWrapperInterceptor(indexName,indexType);
    }

  }

}
