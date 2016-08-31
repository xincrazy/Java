/**
 * Project Name:oneapm-log-common
 * File Name:GrokBean.java
 * Package Name:com.oneapm.log.common.grok
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.common.grok;

import java.io.IOException;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.Match;
import oi.thekraken.grok.api.exception.GrokException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ClassName:GrokBean <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
@Getter
@Setter
public class GrokBean {
    
    public static final Logger       log    = LoggerFactory.getLogger(GrokBean.class);
    
    public static final ObjectMapper mapper = new ObjectMapper();
    
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                         false);
    }
    
    private Grok                     grok;
    
    private Match                    mch;
    
    private State                    state  = State.NOTEXIST;
    
    public GrokBean() {
        grok = new Grok();
        state = State.CONSTRUCTED;
    }
    
    public GrokBean(String grokPatternPath) {
        try {
            grok = Grok.create(grokPatternPath);
            state = State.CONSTRUCTED;
        } catch (GrokException e) {
            e.printStackTrace();
            grok = null;
        }
    }
    
    public GrokBean(String grokPatternPath,
                    String grokExpression) {
        try {
            grok = Grok.create(grokPatternPath,
                               grokExpression);
            state = State.COMPILED;
        } catch (Throwable e) {
            e.printStackTrace();
            grok = null;
        }
    }
    
    /**
     * compilePattern: <br/>
     * 
     * @author hadoop
     * @param pattern
     * @return
     * @since JDK 1.7
     */
    public GrokBean compilePattern(String pattern) {
        //
        if (StringUtils.isBlank(pattern)) {
            log.error("Empty pattern: {0}",
                      pattern);
            return this;
        }
        if (this.state.after(State.COMPILED)) {
            log.warn("Current Grok is compiled previous, now will re-constructed another Grok instance!");
            grok = new Grok();
            this.state = State.CONSTRUCTED;
        }
        //
        try {
            grok.compile(pattern);
            this.state = State.COMPILED;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public GrokBean matchLog(String line) {
        //
        if (StringUtils.isBlank(line)) {
            log.error("Empty log: {0}",
                      line);
            return this;
        }
        if (this.state.beforeThan(State.COMPILED)) {
            log.error("Has not been compiled, compile a pattern first!");
            return this;
        }
        //
        mch = grok.match(line);
        mch.captures();
        this.state = State.MATCHED;
        return this;
    }
    
    public Optional<String> toJson(Boolean pretty) {
        //
        if (this.state.beforeThan(State.MATCHED)) {
            log.error("Has not been matched, match a line of log first!");
            return Optional.empty();
        }
        //
        return Optional.ofNullable(mch.toJson(pretty));
    }
    
    public <T> T getObject(Class<T> type) {
        Optional<String> json = toJson(false);
        try {
            T value = mapper.readValue(json.orElse("{}"),
                                       type);
            return value;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Optional<String> getBySubname(String subName) {
        //
        if (this.state.beforeThan(State.MATCHED)) {
            log.error("Has not been matched, match a line of log first!");
            return Optional.empty();
        }
        //
        return Optional.ofNullable((String) mch.toMap()
                                               .getOrDefault(subName,
                                                             ""));
    }
    
    private enum State {
        NOTEXIST(0),
        CONSTRUCTED(1),
        COMPILED(2),
        MATCHED(3);
        private Integer state;
        
        private State(Integer state) {
            this.state = state;
        }
        
        public boolean before(State s) {
            return this.state.intValue() < s.state.intValue() ||
                   this.state.intValue() == s.state.intValue();
        }
        
        public boolean beforeThan(State s) {
            return this.state.intValue() < s.state.intValue();
        }
        
        public boolean after(State s) {
            return this.state.intValue() > s.state.intValue() ||
                   this.state.intValue() == s.state.intValue();
        }
        
        public boolean afterThan(State s) {
            return this.state.intValue() > s.state.intValue();
        }
        
        public boolean equal(State s) {
            return this.state.intValue() == s.state.intValue();
        }
    }
    
}
