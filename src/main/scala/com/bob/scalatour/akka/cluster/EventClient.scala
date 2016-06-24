package com.bob.scalatour.akka.cluster

import akka.actor.{Props, ActorSystem}

/**
 * Created by bob on 16/6/21.
 */
object EventClient extends App {

  val system = ActorSystem("client")

  val clientActorRef = system.actorOf(Props[EventClientActor], name = "clientActor")

  println(s"Client actor started: ${clientActorRef}")
}