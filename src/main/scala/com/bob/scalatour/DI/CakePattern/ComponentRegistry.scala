package com.bob.scalatour.DI.CakePattern

import com.bob.scalatour.DI._

object ComponentRegistry extends UserServiceComponent with UserRepositoryComponent {

  private val tempUserRepository = new MockUserRepository

  private val tempUserService = new MockUserService
  tempUserService.userRepository = tempUserRepository

  override val userRepository: UserRepository = tempUserRepository

  override val userService: UserService = tempUserService

}