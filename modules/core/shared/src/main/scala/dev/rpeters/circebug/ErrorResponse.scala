package dev.rpeters.circebug

import io.circe.Codec
import io.circe.Decoder
import io.circe.Encoder

object ErrorResponse {
  def errorSingletonCodec[A](a: A, errorString: String) = {
    val decoder: Decoder[A] =
      Decoder.forProduct1[String, String]("error")(identity).emap { s =>
        if (s != errorString)
          Left(s"Wrong error type, got $s, expected $errorString")
        else Right(a)
      }
    val encoder: Encoder[A] = Encoder.forProduct1("error")(_ => errorString)
    Codec.from(decoder, encoder)
  }
  def errorPayloadCodec[A](
      errorString: String,
      codec: Codec[A]
  ): Codec[A] = {
    val decoder = Decoder
      .forProduct2[(String, A), String, A]("error", "payload")(_ -> _)(
        Decoder.decodeString,
        codec
      )
      .emap { case (s, a) =>
        if (s != errorString)
          Left(s"Wrong error type, got $s, expected $errorString")
        else Right(a)
      }
    val encoder = Encoder.forProduct2[A, String, A]("error", "payload")(a =>
      errorString -> a
    )(
      Encoder.encodeString,
      codec
    )
    Codec.from(decoder, encoder)
  }
}
