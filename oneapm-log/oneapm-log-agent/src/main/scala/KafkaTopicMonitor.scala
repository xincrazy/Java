package com.oneapm.log.agent.flume.parser.kafka.v08.topic
import java.util

import joptsimple._
import org.I0Itec.zkclient.ZkClient
import kafka.utils.{Json, Logging, ZKStringSerializer, ZkUtils}
import kafka.consumer.SimpleConsumer
import kafka.api.{OffsetRequest, PartitionOffsetRequestInfo}
import kafka.common.{BrokerNotAvailableException, TopicAndPartition}

import scala.collection._
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._


class KafkaTopicMonitor extends Logging {
  private val consumerMap: mutable.Map[Int, Option[SimpleConsumer]] = mutable.Map()
  private var infoArray = new ArrayBuffer[String]()

  private def getConsumer(zkClient: ZkClient, bid: Int): Option[SimpleConsumer] = {
    try {
      ZkUtils.readDataMaybeNull(zkClient, ZkUtils.BrokerIdsPath + "/" + bid)._1 match {
        case Some(brokerInfoString) =>
          Json.parseFull(brokerInfoString) match {
            case Some(m) =>
              val brokerInfo = m.asInstanceOf[Map[String, Any]]
              val host = brokerInfo.get("host").get.asInstanceOf[String]
              val port = brokerInfo.get("port").get.asInstanceOf[Int]
              Some(new SimpleConsumer(host, port, 10000, 100000, "ConsumerOffsetChecker"))
            case None =>
              throw new BrokerNotAvailableException("Broker id %d does not exist".format(bid))
          }
        case None =>
          throw new BrokerNotAvailableException("Broker id %d does not exist".format(bid))
      }
    } catch {
      case t: Throwable =>
        error("Could not parse broker info", t)
        None
    }
  }

  private def processPartition(zkClient: ZkClient,
                               group: String, topic: String, pid: Int) {
    val offset = ZkUtils.readData(zkClient, "/consumers/%s/offsets/%s/%s".
      format(group, topic, pid))._1.toLong
    val owner = ZkUtils.readDataMaybeNull(zkClient, "/consumers/%s/owners/%s/%s".
      format(group, topic, pid))._1

    ZkUtils.getLeaderForPartition(zkClient, topic, pid) match {
      case Some(bid) =>
        val consumerOpt = consumerMap.getOrElseUpdate(bid, getConsumer(zkClient, bid))
        consumerOpt match {
          case Some(consumer) =>
            val topicAndPartition = TopicAndPartition(topic, pid)
            val request =
              OffsetRequest(immutable.Map(topicAndPartition -> PartitionOffsetRequestInfo(OffsetRequest.LatestTime, 1)))
            val logSize = consumer.getOffsetsBefore(request).partitionErrorAndOffsets(topicAndPartition).offsets.head

            val lag = logSize - offset
            var singlelog:String = "pid:"+pid+",group:"+group+",topic:"+topic+",offset:"+offset+",logsize:"+logSize+",lag:"+lag
            infoArray += singlelog
          case None => // ignore
        }
      case None =>
        error("No broker for partition %s - %s".format(topic, pid))
    }
  }

  private def processTopic(zkClient: ZkClient, group: String, topic: String) {
    val pidMap = ZkUtils.getPartitionsForTopics(zkClient, Seq(topic))
    pidMap.get(topic) match {
      case Some(pids) =>
        pids.sorted.foreach {
          pid => processPartition(zkClient, group, topic, pid)
        }
      case None => // ignore
    }
  }

  private def printBrokerInfo() {
    println("BROKER INFO")
    for ((bid, consumerOpt) <- consumerMap)
      consumerOpt match {
        case Some(consumer) =>
          println("%s -> %s:%d".format(bid, consumer.host, consumer.port))
        case None => // ignore
      }
  }

  def main(args: Array[String]):util.List[String]={
    val parser = new OptionParser()

    val zkConnectOpt = parser.accepts("zkconnect", "ZooKeeper connect string.").
      withRequiredArg().defaultsTo("localhost:2181").ofType(classOf[String]);
    val topicsOpt = parser.accepts("topic",
      "Comma-separated list of consumer topics (all topics if absent).").
      withRequiredArg().ofType(classOf[String])
    val groupOpt = parser.accepts("group", "Consumer group.").
      withRequiredArg().ofType(classOf[String])
    parser.accepts("broker-info", "Print broker info")
    parser.accepts("help", "Print this message.")

    val options = parser.parse(args : _*)

    if (options.has("help")) {
      parser.printHelpOn(System.out)
      System.exit(0)
    }

    for (opt <- List(groupOpt))
      if (!options.has(opt)) {
        System.err.println("Missing required argument: %s".format(opt))
        parser.printHelpOn(System.err)
        System.exit(1)
      }

    val zkConnect = options.valueOf(zkConnectOpt)
    val group = options.valueOf(groupOpt)
    val topics = if (options.has(topicsOpt)) Some(options.valueOf(topicsOpt))
    else None


    var zkClient: ZkClient = null
    try {
      zkClient = new ZkClient(zkConnect, 30000, 30000, ZKStringSerializer)

      val topicList = topics match {
        case Some(x) => x.split(",").view.toList
        case None => ZkUtils.getChildren(
          zkClient, "/consumers/%s/offsets".format(group)).toList
      }

      debug("zkConnect = %s; topics = %s; group = %s".format(
        zkConnect, topicList.toString(), group))

      //println("%-15s %-30s %-3s %-15s %-15s %-15s %s".format("Group", "Topic", "Pid", "Offset", "logSize", "Lag", "Owner"))
      topicList.sorted.foreach {
        topic => processTopic(zkClient, group, topic)
      }

      if (options.has("broker-info"))
        printBrokerInfo();

      for ((_, consumerOpt) <- consumerMap)
        consumerOpt match {
          case Some(consumer) => consumer.close()
          case None => // ignore
        }
    }
    finally {
      for (consumerOpt <- consumerMap.values) {
        consumerOpt match {
          case Some(consumer) => consumer.close()
          case None => // ignore
        }
      }
      if (zkClient != null)
        zkClient.close()
    }
    return infoArray.toList.asJava
  }
}
