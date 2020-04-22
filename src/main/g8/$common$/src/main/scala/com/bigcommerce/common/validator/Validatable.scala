package com.bigcommerce.common.validator

import com.bigcommerce.common.errors.{FieldError, FieldsError}

trait Validatable[E <: FieldsError] {
  protected lazy val fieldErrors: Seq[FieldError] = rules.collect { case (rule, error) if !rule => error }

  // Rules for the command to be valid
  protected val rules: Seq[(Boolean, FieldError)]

  // Wrap errors in some E <: FieldsError class, so resulting Error is your domain class
  protected def getError(): E

  def errorOption: Option[E] =
    if (fieldErrors.nonEmpty) Some(getError())
    else None

  def errorEither: Either[E, Unit] =
    Either.cond(fieldErrors.isEmpty, (), getError())

  def isValid: Boolean =
    fieldErrors.isEmpty
}
