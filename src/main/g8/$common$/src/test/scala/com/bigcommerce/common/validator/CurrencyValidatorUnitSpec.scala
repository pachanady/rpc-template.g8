package com.bigcommerce.common.validator

import com.bigcommerce.common.financial.{CurrencyCode}
import org.scalatest.{FunSpec, Matchers}

class CurrencyValidatorUnitSpec extends FunSpec with Matchers {

  val validator = new CurrencyValidator {}

  describe ("validCurrencyRule") {
    it ("should return true if the currency is valid") {
      val actual = validator.validCurrencyRule(CurrencyCode.USD)
      actual should be ((true, validator.invalidCurrency))
    }

    it ("should return false if the currency is not valid (is NONE)") {
      val actual = validator.validCurrencyRule(CurrencyCode.NONE)
      actual should be ((false, validator.invalidCurrency))
    }
  }
}
