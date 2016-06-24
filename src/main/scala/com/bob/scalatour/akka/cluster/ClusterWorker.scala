package com.bob.scalatour.akka.cluster

import akka.actor.{ActorPath, ActorRef, Actor}
import akka.cluster.{Member, Cluster}
import akka.cluster.ClusterEvent.{MemberEvent, UnreachableMember, MemberUp, InitialStateAsEvents}

object Registration extends Serializable

trait EventMessage extends Serializable

case class RawNginxRecord(sourceHost: String, line: String) extends EventMessage

case class NginxRecord(sourceHost: String, eventCode: String, line: String) extends EventMessage

case class FilteredRecord(sourceHost: String, eventCode: String, line: String, logDate: String, realIp: String) extends EventMessage

abstract class ClusterRoledWorker extends Actor {

  // 创建一个Cluster实例
  val cluster = Cluster(context.system)
  // 缓存下游注册过来的子系统
  var workers = IndexedSeq.empty[ActorRef]

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()
    // 订阅集群事件
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberUp], classOf[UnreachableMember], classOf[MemberEvent])
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    cluster.unsubscribe(self)
    super.postStop()
  }

  def register(member: Member, createPath: (Member) => ActorPath): Unit = {
    val actorPath = createPath(member)
    println("Actor path: " + actorPath)
    val actorSelection = context.actorSelection(actorPath)
    actorSelection ! Registration
  }

}

/**
 * AKKA集群的启动首先要启动一个叫做种子节点(SeedNode)的节点，只有种子节点启动成功，其它节点才能选择任意一个加入集群
 * FirstSeedNodeProcess
 * JoinSeedNodeProcess
 */