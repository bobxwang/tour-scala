package com.bob.scalatour.twitters

import java.net.{InetSocketAddress, SocketAddress}
import java.util.concurrent.{ExecutorService, Executors}

import com.twitter.finagle._
import com.twitter.finagle.client.{StackClient, StdStackClient, Transporter}
import com.twitter.finagle.dispatch.{SerialClientDispatcher, SerialServerDispatcher}
import com.twitter.finagle.filter.MaskCancelFilter
import com.twitter.finagle.netty3.{Netty3Listener, Netty3Transporter}
import com.twitter.finagle.server.{Listener, StackServer, StdStackServer}
import com.twitter.finagle.service.{RetryExceptionsFilter, RetryPolicy, TimeoutFilter}
import com.twitter.finagle.transport.Transport
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.{Await, Future}
import org.jboss.netty.channel._
import org.jboss.netty.handler.codec.frame.{DelimiterBasedFrameDecoder, Delimiters}
import org.jboss.netty.handler.codec.string.{StringDecoder, StringEncoder}
import org.jboss.netty.util.CharsetUtil

/**
  * Created by bob on 16/5/25.
  */
object FinagelExtend {

  implicit val service = new Service[String, String] {
    override def apply(request: String): Future[String] = {
      Future.value(request + " come on " + "\n")
    }
  }

  /**
    * it just an echo service
    *
    * @param args
    */
  def main(args: Array[String]) {

    new EchoService("127.0.0.1", 8887).start
    new EchoService("127.0.0.1", 8888).start
    new EchoService("127.0.0.1", 8889).start

    // after the service is start, below client can run
    val addr = new java.net.InetSocketAddress("localhost", 8888)
    val transporter = Netty3Transporter[String, String](StringClientPipeline, addr, StackClient.defaultParams)
    val bridge = transporter.apply().map(tr => new SerialClientDispatcher[String, String](tr))

    val client = new Service[String, String] {
      override def apply(request: String): Future[String] = bridge flatMap (svc => svc(request).ensure(svc.close()))
    }

    import Filters._
    val newClient = retry andThen timeout andThen maskCancel andThen client
    println(Await.result(newClient("hello Fuck u self"))) // it will print hello Fuck u self come on

    val dest = Resolver.eval("127.0.0.1:8887,127.0.0.1:8888,127.0.0.1:8889")

    val b = Echo.newService(dest, "echo")
    (1 to 20).foreach(x => {
      println(Await.result(b.apply(s"${x} times to invoke new service")))
    })

    val a = Echo.newClient(dest, "echo").toService
    (1 to 20).foreach(x => {
      val bbb = a.apply(s"${x} times to invoke new client")
      println(Await.result(bbb))
    })

  }

  /**
    * it just a echo service,after it start,then we can echo "hello" | nc host port to test
    *
    * @param host
    * @param port
    */
  class EchoService(host: String, port: Int) {

    def start(implicit service: Service[String, String]): Unit = {
      //      val serveTransport = (t: Transport[String, String]) => new SerialServerDispatcher(t, service)
      val address = new InetSocketAddress(host, port)
      val listener = Netty3Listener[String, String](StringServerPipeline, StackServer.defaultParams)
      val threadPool: ExecutorService = Executors.newFixedThreadPool(5)
      threadPool.execute(new Runnable {
        override def run(): Unit = listener.listen(address) {

          // this is more primitive, it closes each connection after one read and write
          //      transprt => {
          //        transprt.read().flatMap(transprt.write(_)).ensure(transprt.close)
          //      }

          // we can using dispatcher with more sophisticated behavior,besides it we can add filter in service
          //          serveTransport(_)

          // below line just to test the client loadbalance
          transport => {
            new SerialServerDispatcher[String, String](transport, new Service[String, String] {
              override def apply(request: String): Future[String] = {
                Future.value(s"req is ${request} and port is ${address.getPort}")
              }
            })
          }
        }
      })
      // for we using thread, so below code can comment it
      //      Await.result(Future.never)
    }
  }

  object StringServerPipeline extends ChannelPipelineFactory {
    def getPipeline = {
      val pipeline = Channels.pipeline()
      pipeline.addLast("line", new DelimiterBasedFrameDecoder(100, Delimiters.lineDelimiter: _*))
      pipeline.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8))
      pipeline.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8))
      pipeline
    }
  }

  object StringClientPipeline extends ChannelPipelineFactory {

    class DelimEncoder(delim: Char) extends SimpleChannelHandler {
      override def writeRequested(ctx: ChannelHandlerContext, evt: MessageEvent) = {
        val newMessage = evt.getMessage match {
          case m: String => m + delim
          case m => m
        }
        Channels.write(ctx, evt.getFuture, newMessage, evt.getRemoteAddress)
      }
    }

    override def getPipeline: ChannelPipeline = {
      val pipeline = Channels.pipeline()
      pipeline.addLast("stringEncode", new StringEncoder(CharsetUtil.UTF_8))
      pipeline.addLast("stringDecode", new StringDecoder(CharsetUtil.UTF_8))
      pipeline.addLast("line", new DelimEncoder('\n'))
      pipeline
    }
  }

  object Filters {
    //#filters
    val retry = new RetryExceptionsFilter[String, String](
      retryPolicy = RetryPolicy.tries(3),
      timer = DefaultTimer.twitter
    )

    import com.twitter.conversions.time._

    val timeout = new TimeoutFilter[String, String](
      timeout = 3.seconds,
      timer = DefaultTimer.twitter
    )

    val maskCancel = new MaskCancelFilter[String, String]
    //#filters
  }

  object Echo extends Client[String, String] with Server[String, String] {

    //#client
    case class Client(stack: Stack[ServiceFactory[String, String]] = StackClient.newStack,
                      params: Stack.Params = StackClient.defaultParams) extends StdStackClient[String, String, Client] {
      protected type In = String
      protected type Out = String

      protected override def copy1(stack: Stack[ServiceFactory[String, String]], params: Stack.Params): Client = copy(stack, params)

      override protected def newTransporter(addr: SocketAddress): Transporter[String, String] = Netty3Transporter(StringClientPipeline, addr, params)

      protected def newDispatcher(transport: Transport[String, String]): Service[String, String] = new SerialClientDispatcher(transport)
    }

    //#client

    //#server
    case class Server(stack: Stack[ServiceFactory[String, String]] = StackServer.newStack,
                      params: Stack.Params = StackServer.defaultParams) extends StdStackServer[String, String, Server] {
      protected type In = String
      protected type Out = String

      override protected def copy1(stack: Stack[ServiceFactory[String, String]], params: Stack.Params): Server = copy(stack, params)

      protected def newListener(): Listener[String, String] = Netty3Listener(StringServerPipeline, params)

      protected def newDispatcher(transport: Transport[String, String], service: Service[String, String]) = new SerialServerDispatcher(transport, service)
    }

    //#server

    val client = Client()
    val server = Server()

    override def newService(dest: Name, label: String): Service[String, String] = client.newService(dest, label)

    override def newClient(dest: Name, label: String): ServiceFactory[String, String] = client.newClient(dest, label)

    override def serve(addr: SocketAddress, service: ServiceFactory[String, String]): ListeningServer = server.serve(addr, service)
  }

}