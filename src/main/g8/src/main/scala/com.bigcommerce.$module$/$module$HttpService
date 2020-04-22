package com.bigcommerce.$module$

import akka.http.scaladsl.server.Directives.{complete, path}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.settings.ServerSettings
import com.bigcommerce.ServiceConfig
import io.opentracing.Tracer

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions


class $module;format="Camel"$HttpService(val akkaHttpSettings: ServerSettings,
                         serviceConfig: ServiceConfig,
                         val tracer: Tracer)
                        (implicit val ec: ExecutionContext)
{
  def route(): Route = {
    path("_monit_ping") {
      complete("ok")
    }
  }
}
