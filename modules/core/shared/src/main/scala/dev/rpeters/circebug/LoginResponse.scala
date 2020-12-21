package dev.rpeters.circebug

import cats.implicits._
import io.circe._
import cats.kernel.Eq

sealed trait LoginResponse extends Product with Serializable

object LoginResponse {
  final case class LoginFailed(username: String) extends LoginResponse
  final case class LoginSucceeded(username: String) extends LoginResponse

  implicit val eq: Eq[LoginResponse] = Eq.fromUniversalEquals

  implicit val loginFailedCodec: Codec[LoginFailed] = ErrorResponse
    .errorPayloadCodec[LoginFailed](
      "LoginFailed",
      Codec.forProduct1("username")(LoginFailed.apply)(_.username)
    )
  implicit val loginSucceededCodec: Codec[LoginSucceeded] =
    Codec.forProduct1("username")(LoginSucceeded.apply)(_.username)
  implicit val loginResponseCodec: Codec[LoginResponse] = Codec.from(
    (Decoder[LoginFailed].widen).or(Decoder[LoginSucceeded].widen),
    Encoder.instance {
      case f: LoginFailed    => loginFailedCodec(f)
      case s: LoginSucceeded => loginSucceededCodec(s)
    }
  )
}
