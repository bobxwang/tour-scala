package com.bob.scalatour.DI.CakePattern

import com.bob.scalatour.DI.UserRepository

trait UserRepositoryComponent {

  val userRepository: UserRepository
}