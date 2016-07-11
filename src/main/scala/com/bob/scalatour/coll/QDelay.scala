package com.bob.scalatour.coll

import java.util.concurrent.{DelayQueue, ConcurrentHashMap, TimeUnit, Delayed}

import scala.io.StdIn
import scala.util.Random

case class DelayedItem[T](val t: T, val liveTime: Long) extends Delayed {

  val removeTime = TimeUnit.NANOSECONDS.convert(liveTime, TimeUnit.NANOSECONDS) + System.nanoTime()

  override def getDelay(unit: TimeUnit): Long = unit.convert(removeTime - System.nanoTime(), unit)

  override def hashCode(): Int = t.hashCode()

  override def equals(obj: scala.Any): Boolean = {
    if (obj.isInstanceOf[DelayedItem[T]]) (obj.hashCode() == hashCode()) else false
  }

  override def compareTo(o: Delayed): Int = {
    o match {
      case null => 1
      case DelayedItem(st, sliveTime) => {
        if (liveTime > sliveTime) 1
        else {
          if (liveTime == sliveTime) 0 else -1
        }
      }
      case _ => {
        val diff = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS)
        if (diff > 0) 1
        else {
          if (diff == 0) 0 else -1
        }
      }
    }
  }
}

class Cache[K, V] {

  private val map = new ConcurrentHashMap[K, V]()

  private val queue = new DelayQueue[DelayedItem[K]]()

  val t = new Thread() {
    override def run(): Unit = {
      while (true) {
        val delayedItem: DelayedItem[K] = queue.poll()
        if (delayedItem != null) {
          map.remove(delayedItem.t)
          println(s"${System.nanoTime()} remove ${delayedItem.t} from cache")
        }
      }
      try {
        Thread.sleep(100)
      } catch {
        case e: Exception =>
      }
    }
  }
  t.setDaemon(true)
  t.start()

  def put(k: K, v: V, liveTime: Long) = {
    val v2 = map.put(k, v)
    val tmpItem: DelayedItem[K] = new DelayedItem[K](k, liveTime)
    if (v2 != null) {
      queue.remove(tmpItem)
    }
    queue.put(tmpItem)
  }
}

object QDelay {

  def main(args: Array[String]) {

    val random = new Random()
    val cacheNumber = 10
    var liveTime = 0
    val cache = new Cache[String, Int]()
    (0 to cacheNumber).foreach(x => {
      liveTime = random.nextInt(3000)
      println(s"${x} ${liveTime}")
      cache.put(x + "", x, random.nextInt(liveTime))
      if (random.nextInt(cacheNumber) > 7) {
        liveTime = random.nextInt(3000)
        println(s"${x} ${liveTime}")
        cache.put(x + "", x, random.nextInt(liveTime))
      }
    })
    StdIn.readInt()
    println()
  }
}