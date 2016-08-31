package com.oneapm.log.agent.flume.interceptor.jvm.hotspot;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.interceptor.Constants;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import com.oneapm.log.common.utils.InterceptorEventUtil;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JvmInterceptor implements Interceptor,Constants{

  private static final Logger logger = LoggerFactory.getLogger(JvmInterceptor.class);


  public static final String APPNAME = "appName";

  private Map<String,String> map = new HashMap<>();

  @Override
  public void initialize() {
    // TODO Auto-generated method stub

  }

  public JvmInterceptor(String appName) {
    map.put(APPNAME,appName);
  }

  @Override
  public Event intercept(Event event) {
    String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
    try {
      if(!eventBodyStr.contains("GC")){
         return null;
      }
      logger.debug("the input string is : "+eventBodyStr);
      Parser parser = LogParserFactory.getInstance().getParserByClass(this.getClass(),map);
      String parserResult = parser.parser(eventBodyStr);

      if(parserResult==null){
        return null;
      }
      event.setBody(parserResult.getBytes(Charsets.UTF_8));
      return event;
    } catch (Exception e) {
      logger.error("error happened in parser the content",e);
    }
    return null;
  }

  @Override
  public List<Event> intercept(List<Event> events) {
    List<Event> eventList = new ArrayList<>();
    for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {

      Event next = iterator.next();
      Event current = intercept(next);

      if (current == null) {
        iterator.remove();
      }else{
        eventList.addAll(InterceptorEventUtil.eventTransformer(current));
      }
    }
    return eventList;
  }

  @Override
  public void close() {

  }

  public static class JvmInterceptorBuilder implements Interceptor.Builder{

    private String appName;

    @Override
    public void configure(Context context) {
      this.appName = context.getString(APPNAME);
      logger.debug("the APPNAME is : "+appName);
    }

    @Override
    public Interceptor build() {
      return new JvmInterceptor(appName);
    }

  }

}