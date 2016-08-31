package com.oneapm.log.agent.flume.parser.system.linux;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tang on 16-7-29.
 */
public class ProcessAliveCheckerParserTest {
    private String processName="init";
    private String processKeyword="/sbin/init";
    @Test
    public void parser() throws Exception {
        assertNotNull(ProcessAliveCheckerParser.getParser(processName,processKeyword).parser(""));
    }

    @Test
    public void getParser() throws Exception {
        assertSame(ProcessAliveCheckerParser.class,ProcessAliveCheckerParser.getParser(processName,processKeyword).getClass());
    }

}