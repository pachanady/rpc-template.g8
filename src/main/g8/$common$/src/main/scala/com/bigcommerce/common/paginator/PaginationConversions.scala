package com.bigcommerce.common.paginator

import com.bigcommerce.common.{Pagination, PaginationParams, PaginationParamsV2, PaginationV2 => GrpcPaginationV2}

trait PaginationConversions {

  val defaultLimit = 50
  val defaultPage = 1
  val defaultOffset = 0

  def fromGrpcPagination(paginatorGrpc: Option[PaginationParams], max: Int): Paginator = {
    paginatorGrpc match {
      case Some(pager) => {

        val limit = pager.limit match {
          // if 1000 kills our service for records, then we can use 250 like the other external BC APIs
          case lim if lim > max => max
          case lim if lim < 1 => defaultLimit
          case lim => lim
        }

        val page = pager.page match {
          case p if p < 1 => defaultPage
          case p => p
        }

        Paginator(limit, page)
      }
      // if no pagination and there should be, use default pagination values
      case None => Paginator
    }
  }

  val paginationV2DefaultMaxLimit = 50

  def toPaginatorV2(paginatorGrpc: Option[PaginationParamsV2], max: Int = paginationV2DefaultMaxLimit): PaginatorV2 =
    paginatorGrpc
      .map(toPaginatorV2(_, max))
      .getOrElse(DefaultPaginatorV2)

  def toPaginatorV2(pParams: PaginationParamsV2, max: Int): PaginatorV2 = {
    val limit = pParams.limit match {
      case lim if lim > max => max
      case lim if lim < 1 => DefaultPaginatorV2.limit
      case lim => lim
    }
    val offset = pParams.offset match {
      case o if o < 1 => DefaultPaginatorV2.offset
      case o => o
    }
    PaginatorV2(limit, offset)
  }

  def toGrpcPagination(paginator: Paginator, total: Int, count: Int): Pagination = {
    // calculate total pages
    val pages = total / paginator.limit
    val totalPages = if (total % paginator.limit == 0) {
      pages
    } else {
      pages + 1
    }

    Pagination(total, count, paginator.limit, paginator.page, totalPages)
  }

  def toGrpcPaginationV2(pagination: PaginationV2): GrpcPaginationV2 =
    GrpcPaginationV2(pagination.offset, pagination.limit, pagination.total)

  def toGrpcPaginationParamsV2(paginator: PaginatorV2): PaginationParamsV2 =
    PaginationParamsV2(paginator.offset, paginator.limit)
}
