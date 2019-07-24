package com.github.alekslitvinenk.domain

import com.github.alekslitvinenk.common.TestData._
import com.github.alekslitvinenk.domain.ProtocolFormat._
import org.scalatest.{Matchers, WordSpec}

class ProtocolFormatSpec extends WordSpec with Matchers {

  "ProtocolFormat" should {
    "convert line of data to TitleBasics object" in {
      val (str, converted) = stringToTitleBasics

      titleBasicsDecoder(str) should be(converted)
    }

    "convert line of data to PrimaryTitleIndex object" in {
      val (str, converted) = stringToPrimaryTitleIndex

      primaryTitleIndexDecoder(str) should be(converted)
    }

    "convert line of data to TitlePrincipals object" in {
      val (str, converted) = stringToTitlePrincipals

      titlePrincipalsDecoder(str) should be(converted)
    }

    "convert line of data to TitleRatings object" in {
      val (str, converted) = stringToTitleRatings

      titleRatingsDecoder(str) should be(converted)
    }

    "convert line of data to NameBasics object" in {
      val (str, converted) = stringToNameBasics

      nameBasicsDecoder(str) should be(converted)
    }

    "convert line of data to PrimaryNameIndex object" in {
      val (str, converted) = stringToPrimaryNameIndex

      primaryNameIndexDecoder(str) should be(converted)
    }
  }
}
