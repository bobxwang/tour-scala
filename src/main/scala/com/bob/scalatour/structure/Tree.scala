package com.bob.scalatour.structure

sealed trait STree[+A]

case class Leaf[A](value: A) extends STree[A]

case class Branch[A](left: STree[A], right: STree[A]) extends STree[A]
