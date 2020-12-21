package dev.rpeters.circebug

import org.scalatest.funspec.AnyFunSpec
import org.typelevel.discipline.scalatest.FunSpecDiscipline
import org.scalatest.prop.Configuration
import io.circe.testing.golden.GoldenCodecTests
import dev.rpeters.circebug.LoginResponse
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import io.circe.testing.instances._

class LoginResponseCodecSpec
    extends AnyFunSpec
    with FunSpecDiscipline
    with Configuration {

  implicit val arbSignUpResponse: Arbitrary[LoginResponse] = Arbitrary {
    Gen.oneOf[LoginResponse](
      LoginResponse.LoginSucceeded("username"),
      LoginResponse.LoginFailed("username")
    )
  }

  checkAll("SignUpResponseCodec", GoldenCodecTests[LoginResponse].goldenCodec)
}
