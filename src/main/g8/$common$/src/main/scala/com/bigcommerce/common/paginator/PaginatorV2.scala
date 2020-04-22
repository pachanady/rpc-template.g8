package com.bigcommerce.common.paginator

case class PaginatorV2(limit: Int, offset: Long)
object PaginatorV2 {
  val DefaultLimit = 10
}

object DefaultPaginatorV2 extends PaginatorV2(PaginatorV2.DefaultLimit, 0)
