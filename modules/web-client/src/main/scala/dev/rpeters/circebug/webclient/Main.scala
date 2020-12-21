package dev.rpeters.circebug.webclient

import dev.rpeters.circebug.LoginResponse

object Main extends App {
  val loginResponse = LoginResponse.LoginFailed("test-username")
  val json = LoginResponse.loginResponseCodec(loginResponse).noSpaces
  println(json)
  val result = io.circe.parser.decode[LoginResponse](json)
  println(result)
}
