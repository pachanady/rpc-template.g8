package com.bigcommerce.common.errors

trait Error {
  def message: String
  val code: String = ""
}

trait NotFoundError extends Error

trait FieldsError extends Error {
  val fieldErrors: Seq[FieldError]
  def message = s"The following fields are invalid: //TO DO ${fieldErrors.map(_.field).mkString(", ")}."
}

case class FieldError(field: String, message: String, code: String = "")
