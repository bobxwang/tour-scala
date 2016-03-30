package com.bob.scalatour.DI

trait UserRepository {
  def create(user: User)

  def find(name: String)

  def update(user: User)

  def delete(user: User)
}

class MockUserRepository extends UserRepository {
  def create(user: User) = println("creating user: " + user)

  def find(name: String) = println("finding user: " + name)

  def update(user: User) = println("udating user: " + user)

  def delete(user: User) = println("deleting user: " + user)
}