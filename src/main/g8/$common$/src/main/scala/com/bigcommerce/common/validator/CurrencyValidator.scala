package com.bigcommerce.common.validator

import com.bigcommerce.common.financial.CurrencyCode
import com.bigcommerce.common.errors.FieldError

trait CurrencyValidator {
  protected[common] val invalidCurrency = FieldError("currency", "The currency must be set.")

  protected[common] def validCurrencyRule(currency: CurrencyCode): (Boolean, FieldError) =
    (!currency.isNone, invalidCurrency)
}
