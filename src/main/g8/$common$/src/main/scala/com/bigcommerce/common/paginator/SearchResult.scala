package com.bigcommerce.common.paginator

case class SearchResult[T](items: Seq[T], total: Int)
