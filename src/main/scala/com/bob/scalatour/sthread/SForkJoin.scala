package com.bob.scalatour.sthread

import scala.concurrent.forkjoin.{ForkJoinPool, RecursiveTask}


/**
 * fork: 把任务分成更小的任务和使用这个框架执行
 * join: 一个任务等待它创建的任务的结束
 *
 * Work-stealing算法: 类似Hadoop的推测执行，一个先完成所有任务的线程会尝试着窃取其它线程中没有完成的任务来执行(任务队列尾部窃取)，好处是重用了并发多线程的优点，减少线程间竞争
 *
 * 任务只能用fork及join操作来作为同步机制
 * 不适合IO操作
 * 抛出的异常需要特定的额外代码处理
 *
 * ForkJoinPool: 实现了Executor接口及work-stealing算法，
 * ForkJoinTask: 在ForkJoinPool中执行的任务基类，提供在任务中执行fork和join操作机制，有两个子类
 * RecursiveAction: 执行没结果的任务
 * RecursiveTask: 执行有结果的任务
 */
object SForkJoin {

  class CountTask(start: Int, end: Int) extends RecursiveTask[Int] {

    val MAX = 1000

    override def compute(): Int = {
      val conCompute = (end - start) <= MAX
      conCompute match {
        case true => {
          (start to end).sum
        }
        case false => {
          val middle = (start + end) / 2
          val task1 = new CountTask(start, middle)
          val task2 = new CountTask(middle + 1, end)
          task1.fork()
          task2.fork()
          task1.join() + task2.join()
        }
      }
    }
  }

  def main(args: Array[String]) {

    val forkJoinPool = new ForkJoinPool()
    val task = new CountTask(1, 10000)
    val rs = forkJoinPool.submit(task)
    println(rs.get())
    if (task.isCompletedAbnormally) {
      println(task.getException)
    }
    forkJoinPool.shutdown()
  }
}
