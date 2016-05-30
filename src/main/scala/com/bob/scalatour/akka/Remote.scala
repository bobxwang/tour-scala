package com.bob.scalatour.akka

import akka.actor.{Props, ActorSystem, Actor}
import com.typesafe.config.ConfigFactory

object Remote extends App {

  val configf = ConfigFactory.load()
  val config = configf.getConfig("RemoteSys")
  val system = ActorSystem("RemoteApp", config)
  val remoteActor = system.actorOf(Props[RemoteActor], name = "remoteActor")
  println(remoteActor.path)
}

class RemoteActor extends Actor {
  override def receive: Receive = {
    case message: String => {
      sender.tell(message + " got sth", this.self)
    }
    case _ => {}
  }
}
