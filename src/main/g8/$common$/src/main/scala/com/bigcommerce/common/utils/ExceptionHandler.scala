package com.bigcommerce.common.utils

import com.bigcommerce.core.logging.Logging
import com.bigcommerce.core.rpc.{InternalError, ServiceErrorException}
import io.grpc.{Status, StatusRuntimeException}

import scala.concurrent.{ExecutionContext, Future}

trait ExceptionHandler {
  def exceptionHandler[T](fn: => Future[T])(implicit ec: ExecutionContext): Future[T] =
    ExceptionHandler.wrapFutureWithExceptionHandler(fn)
}

object ExceptionHandler extends Logging {

  /**
    * DRY method that wraps a given expression and handles any exceptions
    *
    * @param fn the function to invoke and whose exceptions we need to handle
    * @param ec the ExecutionContext
    * @tparam T the function's return type
    */
  def wrapFutureWithExceptionHandler[T](fn: => Future[T])(implicit ec: ExecutionContext): Future[T] = {
    val handler: PartialFunction[Throwable, T] = {
      case ServiceErrorException(err) if err.statusCode == Status.INTERNAL => {
        log.error(err.errorMessage.getOrElse("Unknown internal error"))
        throw new ServiceErrorException(new InternalError("Unknown internal error"))
      }
      case e: ServiceErrorException => {
        throw e
      }
      case e: StatusRuntimeException => {
        log.error(e.getMessage, e)
        throw new ServiceErrorException(new InternalError(e.getMessage))
      }
      case e: Throwable => {
        log.error(e.getMessage, e)
        throw new ServiceErrorException(new InternalError("Unknown error"))
      }
    }

    try {
      fn recover {
        case ex: Throwable => handler(ex)
      }
    } catch {
      case ex: Throwable => Future.successful(handler(ex))
    }
  }
}
