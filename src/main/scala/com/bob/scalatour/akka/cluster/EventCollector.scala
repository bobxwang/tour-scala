package com.bob.scalatour.akka.cluster

import akka.actor.Terminated
import akka.cluster.ClusterEvent._

class EventCollector extends ClusterRoledWorker {

  @volatile var recordCounter: Int = 0

  override def receive: Receive = {
    case MemberUp(member) => println(s"Member is Up: ${member.address}")
    case UnreachableMember(member) => println(s"Member detected as Unreachable: ${member}")
    case MemberRemoved(member, previousStatus) => println(s"Member is removed: ${member.address} after ${previousStatus}")
    case Registration => {
      context.watch(sender)
      workers = workers :+ sender
      println(s"Interceptor registered: ${sender}, Registered interceptors: ${workers.size}")
    }
    case Terminated(interceptingActorRef) => workers = workers.filterNot(_ == interceptingActorRef)
    case RawNginxRecord(sourceHost, line) => {
      val eventCode = "eventcode=(\\d+)".r.findFirstIn(line).getOrElse("")
      println(s"Raw message: eventCode=${eventCode}, sourceHost=${sourceHost}, line=${line}")
      recordCounter += 1
      if (workers.size > 0) {
        // 模拟Roudrobin方式，将日志记录消息发送给下游一组interceptor中的一个
        val interceptorIndex = (if (recordCounter < 0) 0 else recordCounter) % workers.size
        workers(interceptorIndex) ! NginxRecord(sourceHost, eventCode, line)
        println("Details: interceptorIndex=" + interceptorIndex + ", interceptors=" + workers.size)
      }
    }
    case _: MemberEvent =>
  }

}