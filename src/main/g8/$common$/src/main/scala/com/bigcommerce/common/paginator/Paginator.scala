package com.bigcommerce.common.paginator

case class Paginator (limit: Int, page: Int) {
  val offset = limit * (page - 1)
}

object Paginator extends Paginator(50, 1)

