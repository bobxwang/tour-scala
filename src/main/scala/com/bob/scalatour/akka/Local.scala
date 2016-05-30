package com.bob.scalatour.akka

import akka.actor.{Props, ActorSystem, Actor}
import akka.pattern.ask
import com.typesafe.config.ConfigFactory
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.util.Timeout

object Local extends App {

  val config = ConfigFactory.load().getConfig("LocalSys")
  val system = ActorSystem("LocalApp", config)
  val clientActor = system.actorOf(Props[LocalActor])
  clientActor ! "Hello, baby"
  Thread.sleep(4000)
  system.terminate()
}

class LocalActor extends Actor {

  val remoteActor = context.actorSelection("akka.tcp://RemoteApp@127.0.0.1:2552/user/remoteActor")
  implicit val timeout = Timeout(5.seconds)

  override def receive: Receive = {
    case message: String => {
      val future = (remoteActor ? message).mapTo[String]
      val result = Await.result(future, timeout.duration)
      println(s"message received from server: ${result}")
    }
  }
}