package com.bob.scalatour.structure

sealed trait STree[+A]

case class Leaf[A](value: A) extends STree[A]

case class Branch[A](left: STree[A], right: STree[A]) extends STree[A]

object STree {

  def size[A](t: STree[A]): Int = t match {
    case Leaf(_) => 1
    case Branch(l, r) => 1 + size(l) + size(r)
  }

  def maximum(t: STree[Int]): Int = t match {
    case Leaf(n) => n
    case Branch(l, r) => maximum(l).max(maximum(r))
  }

  def depth[A](t: STree[A]): Int = t match {
    case Leaf(_) => 0
    case Branch(l, r) => 1 + (depth(l).max(depth(r)))
  }

  def map[A, B](t: STree[A])(f: A => B): STree[B] = t match {
    case Leaf(a) => Leaf(f(a))
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))
  }

  def flod[A, B](t: STree[A])(f: A => B)(g: (B, B) => B): B = t match {
    case Leaf(a) => f(a)
    case Branch(l, r) => g(flod(l)(f)(g), flod(r)(f)(g))
  }

  def sizeViaFold[A](t: STree[A]): Int = flod(t)(a => 1)(1 + _ + _)

  def maxViaFold[A](t: STree[Int]): Int = flod(t)(a => a)(_.max(_))

  def depthViaFold[A](t: STree[A]): Int = flod(t)(a => 0)((d1, d2) => 1 + (d1.max(d2)))

  def mapVirFold[A, B](t: STree[A])(f: A => B): STree[B] = flod(t)(a => Leaf(f(a)): STree[B])(Branch(_, _))

}