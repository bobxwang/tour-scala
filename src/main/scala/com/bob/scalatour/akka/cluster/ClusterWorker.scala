package com.bob.scalatour.akka.cluster

import akka.actor.{ActorPath, ActorRef, Actor}
import akka.cluster.{Member, Cluster}
import akka.cluster.ClusterEvent.{MemberEvent, UnreachableMember, MemberUp, InitialStateAsEvents}

object Registration extends Serializable

trait EventMessage extends Serializable

case class RawNginxRecord(sourceHost: String, line: String) extends EventMessage

case class NginxRecord(sourceHost: String, eventCode: String, line: String) extends EventMessage

case class FilteredRecord(sourceHost: String, eventCode: String, line: String, logDate: String, realIp: String) extends EventMessage

abstract class ClusterWorker extends Actor {

  val cluster = Cluster(context.system)

  var workers = IndexedSeq.empty[ActorRef]

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()
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