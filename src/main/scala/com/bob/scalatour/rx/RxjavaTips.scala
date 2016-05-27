package com.bob.scalatour.rx

import java.lang.Long
import java.util.concurrent.TimeUnit

import org.joda.time.DateTime
import rx.functions.Action1
import rx.{Observer, Subscriber, Observable}
import rx.Observable.OnSubscribe
import scala.collection.JavaConverters._

object RxjavaTips {

  def main(args: Array[String]) {

    // subject is an magic object,it can be an Observable, it also can be an Observer
    // it has four son :
    //    PublishSubject --> 一个基础子类
    //    BehaviorSubject --> 向订阅者发送截至订阅前最新的一个数据对象(或初始值),然后正常发送订阅后的数据流
    //    ReplaySubject --> 缓存它所订阅的所有数据，向任意一个订阅它的观察者重发
    //    AsyncSubject --> 只会发布最后一个数据给已经订阅的每一个观察者

    // Schedulers
    //    .io --> 专用于I/O操作，不是Rx的默认方法
    //    .computation --> 计算工作默认的调度器，与I/O无关，是buffer,debounce,delay,interval,sample,skip默认
    //    .immediate --> 立即在当前线程执行工作，是timeout,timeInterval及timestamp默认
    //    .newThread
    //    .trampoline --> 当我们想在当前线程执行一个任务，但不是立即，可以用此将其入队，是repeat,retry方法默认调度器

    basicRx()
  }

  def basicRx(): Unit = {
    // using creat to generate an observable
    Observable.create(new OnSubscribe[Object] {
      override def call(t: Subscriber[_ >: Object]): Unit = {
        (1 to 10).foreach(i => t.onNext(new Integer(i)))
        t.onCompleted()
      }
    }).subscribe(new Observer[Object] {
      override def onCompleted(): Unit = println("completed")

      override def onError(throwable: Throwable): Unit = println(throwable.getMessage)

      override def onNext(t: Object): Unit = println(s"item now is: ${t}")
    })

    // using list to generate an observable
    val items = (10 to 20).toList
    Observable.from(items.asJava).subscribe(new Observer[Int] {
      override def onCompleted(): Unit = println("completed")

      override def onError(throwable: Throwable): Unit = println(throwable.getMessage)

      override def onNext(t: Int): Unit = println(s"item now is: ${t}")
    })

    Observable.range(20, 30)

    Observable.interval(3, TimeUnit.SECONDS).subscribe(new Action1[Long] {
      // 每隔三秒钟，call方法执行
      override def call(t: Long): Unit = println(s"now time is ${DateTime.now} and t value is ${t}")
    })

    Observable.just("hello fuck, 51", "come again", "again and again")
      // the same data will send two times using repeat method
      .repeat(2).subscribe(new Action1[String] {
      override def call(t: String): Unit = println(s"item now is: ${t}")
    })

    /**
     * we can using Observable.empty, never, throw to constructor an Observable
     * defer -> 创建后不立即执行，有观察者订阅时才发射
     * timer -> 一段时间后再发射
     * sample(30,TimeUnit.SECONDS) -> 指定的时间间隔里发身最近一次数据
     * timeout() -> 限时，在指定时间间隔 Observable 不发射值的话，就会触发 onError()函数
     * debounce() -> 过滤发射速率过快的数据，即：在一个时间间隔过去之后，仍然没有发射的话，则发射最后的那个
     *
     * concatMap解决了flatMap的交叉问题
     */

  }
}
