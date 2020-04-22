package com.bigcommerce.common.paginator

case class PaginationV2(limit: Int, offset: Long, total: Long)
object PaginationV2 {
  def apply(paginator: PaginatorV2, total: Long): PaginationV2 =
    PaginationV2(paginator.limit, paginator.offset, total)
}
