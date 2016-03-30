package com.bob.scalatour.DI

import com.bob.scalatour.DI.CakePattern.ComponentRegistry

object DIMain {

  def main(args: Array[String]) {
    val userRepository = new MockUserRepository()
    val service = new MockUserService(userRepository)
    service.create(User("user", 23))

    ComponentRegistry.userService.delete(User("user", 12))
  }
}