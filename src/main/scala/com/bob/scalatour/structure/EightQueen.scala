package com.bob.scalatour.structure

/**
 * 八皇后
 */
object EightQueen {

  /*
  解决思路：首先应该注意需要把皇后放在每一行中，因此可以一行行连续地摆放皇后，并在每次检查新摆放的皇后不会被吃前的给叫吃，在搜索过程中，有可能会遇到要摆放在第K行的皇后
  将所有位置处于1到K-1行的皇后叫吃的局面，在这情况下，需要放弃这部分搜索以继续另外一种第1到K-1行皇后的配置局面
   */

  /**
   * 扩展到N*N平方的棋盘上放N个皇后，使得没有皇后处于可被叫吃的位置(皇后可以吃吃处于同列，同行或相同对角线上的另一个子)
   * @param n
   * @return
   */
  def queens(n: Int): List[List[(Int, Int)]] = {

    def placeQueens(k: Int): List[List[(Int, Int)]] = {
      if (k == 0) {
        List(List())
      } else {
        for {
          queens <- placeQueens(k - 1)
          column <- 1 to n
          queen = (k, column)
          if (isSafe(queen, queens))
        } yield queen :: queens
      }
    }

    placeQueens(n)
  }

  /**
   * 如果queen没有被queens中的任何一个叫吃的话，queen就是安全的
   * @param queen
   * @param queens
   * @return
   */
  def isSafe(queen: (Int, Int), queens: List[(Int, Int)]) = {
    queens.forall(q => !inCheck(queen, q))
  }

  /**
   * 表示两个皇后q1,q2没有互相叫吃
   * @param q1
   * @param q2
   * @return
   */
  def inCheck(q1: (Int, Int), q2: (Int, Int)) = {
    q1._1 == q2._1 || // 同一行
      q1._2 == q2._2 || // 同一列
      (q1._1 - q2._1).abs == (q1._2 - q2._2).abs // 对角线
  }

}