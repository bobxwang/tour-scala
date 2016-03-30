package com.bob.scalatour.DI

trait UserService {

  def create(user: User)

  def find(name: String)

  def update(user: User)

  def delete(user: User)
}

class MockUserService(val userRepository: UserRepository) extends UserService {

  def create(user: User) = userRepository.create(user)

  def find(name: String) = userRepository.find(name)

  def update(user: User) = userRepository.update(user)

  def delete(user: User) = userRepository.delete(user)
}