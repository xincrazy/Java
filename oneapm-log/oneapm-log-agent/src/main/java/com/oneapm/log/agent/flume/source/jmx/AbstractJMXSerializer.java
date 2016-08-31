/**
 * Project Name:oneapm-log-agent
 * File Name:AbstractJMXSerializer.java
 * Package Name:com.oneapm.log.agent.flume.source.jmx
 * Date: 
 * Copyright (c) 2016, All Rights Reserved.
 *
 */

package com.oneapm.log.agent.flume.source.jmx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import org.apache.flume.Context;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ClassName:AbstractJMXSerializer <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 * @see
 */
public abstract class AbstractJMXSerializer {
    
    public static final Logger                      log           = LoggerFactory.getLogger(AbstractJMXSerializer.class);
    
    private static final ObjectMapper               mapper        = new ObjectMapper();
    
    protected Function<Map<String, Object>, String> jsonFormatter = objMap -> {
                                                                      String result = "{}";
                                                                      try {
                                                                          result = mapper.writeValueAsString(objMap);
                                                                      } catch (JsonProcessingException e) {
                                                                          log.error("format Map to JSON string failed because of {}",
                                                                                    e);
                                                                      }
                                                                      return result;
                                                                  };
    
    @NotNull
    @Size(min = 1)
    private List<ObjectElement>                     elements      = new ArrayList<AbstractJMXSerializer.ObjectElement>();
    
    /**
     * ClassName: ObjectElement <br/>
     * Function: <br/>
     * date: <br/>
     *
     * @author hadoop
     * @version AbstractJMXSerializer
     * @since JDK 1.8
     */
    @Getter
    @Setter
    public static class ObjectElement {
        private ObjectName name;
        private String     key;
        private String     alias;
        
        public ObjectElement(ObjectName name,
                             String key,
                             String alias) {
            this.name = name;
            this.key = key;
            this.alias = alias;
        }
        
    }
    
    /**
     * pullMetrics: <br/>
     * 
     * @author hadoop
     * @param conn
     * @param ctx
     * @return
     * @throws IOException
     * @since JDK 1.8
     */
    public List<String> pullMetrics(MBeanServerConnection conn,
                                    Context ctx) throws IOException {
        //
        Map<String, Object> metrics = new ConcurrentHashMap<String, Object>();
        //
        Set<ObjectName> names = conn.queryNames(ObjectName.WILDCARD,
                                                null);
        elements.forEach(oe -> {
            if (names.contains(oe.getName())) {
                try {
                    MBeanInfo info = conn.getMBeanInfo(oe.getName());
                    Arrays.asList(info.getAttributes())
                          .forEach(attr -> {
                              try {
                                  Object value = conn.getAttribute(oe.getName(),
                                                                   attr.getName());
                                  parseAttribute(oe,
                                                 attr.getName(),
                                                 metrics,
                                                 value);
                              } catch (Exception e) {
                                  log.error("{}",
                                            e);
                              }
                          });
                } catch (Exception e) {
                    log.error("{}",
                              e);
                }
            }
        });
        // formate metrics map
        return formateMetrics(metrics,
                              ctx);
    }
    
    /**
     * parseAttribute: <br/>
     * 
     * @author hadoop
     * @param oe
     * @param path
     * @param metrics
     * @param o
     * @since JDK 1.8
     */
    private void parseAttribute(ObjectElement oe,
                                String path,
                                Map<String, Object> metrics,
                                Object o) {
        if (oe.getKey()
              .equalsIgnoreCase(path) &&
            !(o instanceof CompositeData)) {
            metrics.put(oe.getAlias(),
                        o);
            return;
        }
        if (oe.getKey()
              .equalsIgnoreCase(path) &&
            o instanceof CompositeData) {
            return;
        }
        if (!oe.getKey()
               .equalsIgnoreCase(path) &&
            !(o instanceof CompositeData)) {
            return;
        }
        if (!oe.getKey()
               .startsWith(path)) {
            return;
        }
        String[] s = oe.getKey()
                       .substring(path.length() + 1)
                       .split("\\.");
        if (s.length < 1) {
            return;
        }
        CompositeData cd = (CompositeData) o;
        if (!cd.containsKey(s[0])) {
            return;
        }
        // recursive
        parseAttribute(oe,
                       path +
                               "." +
                               s[0],
                       metrics,
                       cd.get(s[0]));
    }
    
    /**
     * formateMetrics: <br/>
     * 
     * @author hadoop
     * @param metrics
     * @param ctx
     * @return
     * @since JDK 1.8
     */
    protected abstract List<String> formateMetrics(Map<String, Object> metrics,
                                                   Context ctx);
    
    /**
     * addKeyValue: <br/>
     * 
     * @author hadoop
     * @param name
     * @param key
     * @since JDK 1.8
     */
    protected void addKeyValue(ObjectName name,
                               String key) {
        addKeyValue(name,
                    key,
                    key);
    }
    
    /**
     * addKeyValue: <br/>
     * 
     * @author hadoop
     * @param name
     * @param key
     * @param alias
     * @since JDK 1.8
     */
    protected void addKeyValue(ObjectName name,
                               String key,
                               String alias) {
        elements.add(new ObjectElement(name,
                                       key,
                                       alias));
    }
    
    /**
     * build: <br/>
     * 
     * @author hadoop
     * @since JDK 1.8
     */
    public abstract void build();
    
}
