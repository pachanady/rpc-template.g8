package com.bigcommerce.common.validator

import com.bigcommerce.common.financial.CurrencyCode
import com.bigcommerce.common.errors.FieldError
import org.scalatest.{FunSpec, Matchers}

class PriceValidatorUnitSpec extends FunSpec with Matchers {

  val validator = new PriceValidator {}
  val priceCannotBeNegative: FieldError = FieldError("price", "The price must be at least zero.")
  val priceTooLarge: FieldError = FieldError("price", "Price entered is greater than maximum allowed.")
  val priceTooManyDecimals: FieldError = FieldError("price", "Price entered has more than the four (4) allowed decimals.")

  describe ("pricePositiveRule") {
    it ("should return true if the price is a positive number") {
      val actual = validator.pricePositiveRule(Some(BigDecimal(345.98)), priceCannotBeNegative)
      actual should be ((true, priceCannotBeNegative))
    }

    it ("should return true if the price is None") {
      val actual = validator.pricePositiveRule(None, priceCannotBeNegative)
      actual should be ((true, priceCannotBeNegative))
    }

    it ("should return false if the price is a negative number") {
      val actual = validator.pricePositiveRule(Some(BigDecimal(-1847.87)), priceCannotBeNegative)
      actual should be ((false, priceCannotBeNegative))
    }
  }

  describe ("validPriceLengthRule") {
    it ("should return true if the price is None") {
      val actual = validator.validPriceLengthRule(None, priceTooLarge, priceTooManyDecimals)
      actual should be (true, priceTooLarge)
    }

    it ("should return true if the price is a reasonable size") {
      val actual = validator.validPriceLengthRule(Some(BigDecimal(3765858.98)), priceTooLarge, priceTooManyDecimals)
      actual should be (true, priceTooLarge)
    }

    it ("should return false if the price is too many digits") {
      val actual = validator.validPriceLengthRule(Some(BigDecimal(3787353647478465858.98)), priceTooLarge, priceTooManyDecimals)
      actual should be (false, priceTooLarge)
    }

    it ("should return false if the decimal has too many digits") {
      val actual = validator.validPriceLengthRule(Some(BigDecimal(123.45678)), priceTooLarge, priceTooManyDecimals)
      actual should be (false, priceTooManyDecimals)
    }
  }
}
