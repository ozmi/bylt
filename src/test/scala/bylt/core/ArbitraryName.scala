package bylt.core

import org.scalacheck.Gen

/**
  * Created by attila on 4/14/2016.
  */
object ArbitraryName {

    lazy val lowerAlphaStr : Gen [String] =
        Gen.nonEmptyListOf (Gen.alphaLowerChar).map(_.mkString)

    // Names

    lazy val nameGen : Gen [Name] =
        for (parts <- Gen.nonEmptyListOf (lowerAlphaStr)) yield Name (parts.toVector)

    lazy val qnameGen : Gen [QName] =
        for {
            path <- Gen.listOf (nameGen)
            name <- nameGen
        } yield QName (path.toVector, name)

}
