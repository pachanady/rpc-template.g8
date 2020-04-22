package com.bigcommerce.common.validator

import com.bigcommerce.common.errors.FieldError

trait PriceValidator {
  val moneyUnitPrecision = 16
  val moneyNanoPrecision = 4

  protected[common] def pricePositiveRule(price: Option[BigDecimal], errorToReturn: FieldError): (Boolean, FieldError) = {
    price match {
      case Some(p) if p < 0 => (false, errorToReturn)
      case _ => (true, errorToReturn)
    }
  }

  protected[common] def validPriceLengthRule(price: Option[BigDecimal], unitsError: FieldError, nanosError: FieldError): (Boolean, FieldError) = {
    price match {
      case Some(p) if p.scale > moneyNanoPrecision => (false, nanosError)
      case Some(p) if (p.precision - p.scale) > moneyUnitPrecision => (false, unitsError)
      case _ => (true, unitsError)
    }
  }
}
