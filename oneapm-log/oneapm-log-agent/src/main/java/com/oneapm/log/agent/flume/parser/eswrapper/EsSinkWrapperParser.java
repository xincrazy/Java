package com.oneapm.log.agent.flume.parser.eswrapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.oneapm.log.agent.flume.parser.Parser;

public class EsSinkWrapperParser implements Parser {
  
  Logger logger = Logger.getLogger(this.getClass());
  
  public static Map<Integer, EsSinkWrapperParser> parserInstancePool =
      new HashMap<Integer, EsSinkWrapperParser>();
  
  private String indexType;
      
  private String indexName;
  
  public EsSinkWrapperParser(String indexName,String indexType) {
    // TODO Auto-generated constructor stub
    this.indexName = indexName;
    this.indexType = indexType;
  }
  
  @Override
  public String parser(String content) {
    logger.info("the input content is :"+content);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{");
    stringBuilder.append("\"indexName\":"+"\""+indexName+"\""+",");
    stringBuilder.append("\"indexType\":"+"\""+indexType+"\""+",");
    stringBuilder.append("\"document\":"+content);
    stringBuilder.append("}");
    
    return stringBuilder.toString();
  }

  public static EsSinkWrapperParser getParser(String indexName,String indexType) {
    
    int hashCode = indexName.hashCode()^indexType.hashCode();

    if(parserInstancePool.get(hashCode)!=null){
      return parserInstancePool.get(hashCode);
    }else {
      EsSinkWrapperParser parser = new EsSinkWrapperParser(indexName,indexType);
      parserInstancePool.put(hashCode, parser);
      return parser;
    }
   
  }

}
