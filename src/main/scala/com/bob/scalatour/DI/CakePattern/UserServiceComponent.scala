package com.bob.scalatour.DI.CakePattern

import com.bob.scalatour.DI.UserService

trait UserServiceComponent {

  // 这里使用self-type annotation，即声明UserServiceComponent需要UserRepositoryComponent
  // 如果需要依赖多个，可使用下面格式，this: Foo with Bar with Baz =>
  this: UserRepositoryComponent =>

  val userService: UserService
}