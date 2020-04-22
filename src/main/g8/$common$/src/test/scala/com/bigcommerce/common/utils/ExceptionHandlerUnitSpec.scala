package com.bigcommerce.common.utils

import com.bigcommerce.core.rpc.{InternalError, ResourceNotFoundError, ServiceErrorException}
import io.grpc.{Status, StatusRuntimeException}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Span}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ExceptionHandlerUnitSpec extends FlatSpec with ScalaFutures with Matchers {

  val underTest = ExceptionHandler

  val interval = Span(400, Millis)
  val timeout = Span(10000, Millis)

  implicit override val patienceConfig = PatienceConfig(timeout, interval)

  behavior of "wrapFutureWithExceptionHandler"

  it should "Return an exception that's not an internal error on a future" in {
    val exception = intercept[ServiceErrorException] {
      ExceptionHandler.wrapFutureWithExceptionHandler(Future.failed(throw new ServiceErrorException(new ResourceNotFoundError())))
    }
    exception.error.statusCode should be (Status.NOT_FOUND)
  }

  it should "reset the message on an Internal Error on a future" in {
    val exception = intercept[ServiceErrorException] {
      ExceptionHandler.wrapFutureWithExceptionHandler(Future.failed(throw new ServiceErrorException(new InternalError("this is bad"))))
    }
    exception.error.statusCode should be (Status.INTERNAL)
  }

  it should "handle a status runtime exception on a future" in {
    val exception = intercept[ServiceErrorException] {
      ExceptionHandler.wrapFutureWithExceptionHandler(Future.failed(throw new StatusRuntimeException(Status.DEADLINE_EXCEEDED)))
    }
    exception.error.statusCode should be (Status.INTERNAL)
  }

  it should "Return an exception that's not an internal error" in {
    val exception = intercept[ServiceErrorException] {
      ExceptionHandler.wrapFutureWithExceptionHandler(throw new ServiceErrorException(new ResourceNotFoundError()))
    }
    exception.error.statusCode should be (Status.NOT_FOUND)
  }

  it should "reset the message on an Internal Error" in {
    val exception = intercept[ServiceErrorException] {
      ExceptionHandler.wrapFutureWithExceptionHandler(throw new ServiceErrorException(new InternalError("this is bad")))
    }
    exception.error.statusCode should be (Status.INTERNAL)
  }

  it should "handle a status runtime exception" in {
    val exception = intercept[ServiceErrorException] {
      ExceptionHandler.wrapFutureWithExceptionHandler(throw new StatusRuntimeException(Status.DEADLINE_EXCEEDED))
    }
    exception.error.statusCode should be (Status.INTERNAL)
  }

  it should "catch a vanilla runtime Exception and turn into service error exception" in {

    val exception = intercept[ServiceErrorException] {
      ExceptionHandler.wrapFutureWithExceptionHandler(throw new Exception("This is bad"))
    }
    exception.error.statusCode should be (Status.INTERNAL)
  }

  it should "not throw an exception if the result is success" in {
    val result = whenReady(ExceptionHandler.wrapFutureWithExceptionHandler(Future.successful({"hello"}))) { res =>
      res should be ("hello")
    }
  }
}
