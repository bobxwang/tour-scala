package com.bob.scalatour.scalazsss

import scalaz.effect.IO._

object IOMonad extends App {
  val action = for {
    _ <- putStrLn("hello, world")
  } yield ()

  action.unsafePerformIO()

  val whoRyou = for {
    _ <- putStrLn("who r u?")
    name <- readLn
    _ <- putStrLn(s"hello ${name}")
  } yield ()

  whoRyou.unsafePerformIO()
}