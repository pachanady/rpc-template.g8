package com.bigcommerce.common.validator

import com.bigcommerce.common.errors.FieldError

trait StoreIdValidator {
  protected[common] val invalidStoreId = FieldError("store_id", "A store ID must be greater than 0.")

  protected[common] def validStoreIdRule(storeId: Long): (Boolean, FieldError) =
    (storeId > 0, invalidStoreId)
}
