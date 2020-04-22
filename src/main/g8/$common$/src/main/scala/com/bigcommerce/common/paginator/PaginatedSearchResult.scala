package com.bigcommerce.common.paginator

case class PaginatedSearchResult[T](items: Seq[T], pagination: PaginationV2)
