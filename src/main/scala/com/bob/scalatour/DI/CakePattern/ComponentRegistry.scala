package com.bob.scalatour.DI.CakePattern

import com.bob.scalatour.DI._

object ComponentRegistry extends UserServiceComponent with UserRepositoryComponent {

  override val userRepository: UserRepository = new MockUserRepository

  override val userService: UserService = new MockUserService(userRepository)

}