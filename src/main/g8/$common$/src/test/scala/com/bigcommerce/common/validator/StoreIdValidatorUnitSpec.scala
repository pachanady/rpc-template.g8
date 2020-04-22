package com.bigcommerce.common.validator

import org.scalatest.{FunSpec, Matchers}

class StoreIdValidatorUnitSpec extends FunSpec with Matchers {

  val validator = new StoreIdValidator {}

  describe ("validStoreIdRule") {
    it ("should return true if the store id is greater than zero") {
      val actual = validator.validStoreIdRule(847576785L)
      actual should be ((true, validator.invalidStoreId))
    }

    it ("should return false if the store id is zero") {
      val actual = validator.validStoreIdRule(0L)
      actual should be ((false, validator.invalidStoreId))
    }

    it ("should return false if the store id is less than zero") {
      val actual = validator.validStoreIdRule(-7474580L)
      actual should be ((false, validator.invalidStoreId))
    }
  }
}
