package com.bigcommerce.catalog.routers

import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import com.lonelyplanet.prometheus.api.MetricFamilySamplesEntity
import io.prometheus.client.CollectorRegistry

class PrometheusRouter(collector: CollectorRegistry, endpoint: String) {

  def route(): Route = {
    get {
      path(endpoint) {
        complete {
          MetricFamilySamplesEntity.fromRegistry(collector)
        }
      }
    }
  }
}
