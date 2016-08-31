package com.oneapm.log.agent.flume.parser.commonlog;

import com.oneapm.log.agent.flume.parser.akka.AkkaLoggingParser;
import com.oneapm.log.agent.flume.parser.common.CommonLoggingParser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by root on 16-8-25.
 */
public class TestCommonLoggingParser {
    CommonLoggingParser commonLoggingParser =new CommonLoggingParser();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void testGetparser(){
        CommonLoggingParser parser = commonLoggingParser.getParser();
        Assert.assertNotNull(parser);
    }
    @Test
    public void testParser(){
        String content= "test";
        String result = commonLoggingParser.parser(content);
        String str = "test";
        Assert.assertNotNull(result);
        Assert.assertEquals(str, result);
    }
}
