/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.util;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.flume.Context;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchSourceUtil {
    
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchSourceUtil.class);
    
    /**
     * getElasticProperties: <br/>
     * 
     * @author hadoop
     * @param context
     * @return
     * @since JDK 1.7
     */
    public static Properties getElasticProperties(Context context) {
        log.info("context={}",
                 context.toString());
        Properties props = generateDefaultElasticProps();
        setElasticProps(context,
                        props);
        return props;
    }
    
    /**
     * getTransClient: <br/>
     * 
     * @author hadoop
     * @param context
     * @return
     * @since JDK 1.7
     */
    public static TransportClient getTransClient(Context context) {
        try {
            return TransportClient.builder()
                                  .settings(Settings.settingsBuilder()
                                                    .put(ElasticsearchSourceConstants.CLUSTER_NAME,
                                                         context.getString(ElasticsearchSourceConstants.CLUSTER_NAME,
                                                                           ElasticsearchSourceConstants.DEFAULT_CLUSTER_NAME))
                                                    .build())
                                  .build()
                                  .addTransportAddresses(Arrays.asList(context.getString(ElasticsearchSourceConstants.HOST_NAME,
                                                                                         ElasticsearchSourceConstants.DEFAULT_HOST_NAME)
                                                                              .split(","))
                                                               .stream()
                                                               .map(a -> {
                                                                   try {
                                                                       return new InetSocketTransportAddress(InetAddress.getByName(a.split(":").length < 1
                                                                                                                                                          ? ElasticsearchSourceConstants.DEFAULT_HOST_NAME
                                                                                                                                                          : a.split(":")[0].trim()),
                                                                                                             a.split(":").length < 2
                                                                                                                                    ? context.getInteger(ElasticsearchSourceConstants.PORT,
                                                                                                                                                         ElasticsearchSourceConstants.DEFAULT_PORT)
                                                                                                                                             .intValue()
                                                                                                                                    : Integer.valueOf(a.split(":")[1].trim())
                                                                                                                                             .intValue());
                                                                   } catch (Exception e) {
                                                                       return null;
                                                                   }
                                                               })
                                                               .filter(t -> t != null)
                                                               .toArray(InetSocketTransportAddress[]::new));
        } catch (Exception e) {
            log.error("Buildding transport client failed: {}",
                      e);
            return null;
        }
    }
    
    /**
     * getAdminClient: <br/>
     * 
     * @author hadoop
     * @param context
     * @return
     * @since JDK 1.7
     */
    public static ClusterAdminClient getAdminClient(Context context) {
        //
        TransportClient client = getTransClient(context);
        //
        return client == null
                             ? null
                             : client.admin()
                                     .cluster();
    }
    
    /**
     * getIndicesClient: <br/>
     * 
     * @author hadoop
     * @param context
     * @return
     * @since JDK 1.8
     */
    public static IndicesAdminClient getIndicesClient(Context context) {
        //
        TransportClient client = getTransClient(context);
        //
        return client == null
                             ? null
                             : client.admin()
                                     .indices();
    }
    
    /**
     * Generate elasticsearch properties object with some defaults
     * 
     * @return
     */
    private static Properties generateDefaultElasticProps() {
        Properties props = new Properties();
        // TODO
        return props;
    }
    
    /**
     * Add all configuration parameters starting with "elasticsearch" to consumer properties
     */
    private static void setElasticProps(Context context,
                                        Properties elasticProps) {
        
        Map<String, String> elasticProperties = context.getSubProperties(ElasticsearchSourceConstants.PROPERTY_PREFIX);
        
        for (Map.Entry<String, String> prop : elasticProperties.entrySet()) {
            
            elasticProperties.put(prop.getKey(),
                                  prop.getValue());
            log.info("Reading a Elasticsearch Property: key: " +
                     prop.getKey() +
                     ", value: " +
                     prop.getValue());
        }
    }
    
}
