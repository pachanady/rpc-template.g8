package com.bigcommerce

import com.typesafe.config.{Config, ConfigFactory}

case class $module;format="Camel"$LimitWarn(value: Int)

case class ServiceConfig(prometheusConfig: PrometheusConfig,
                         $module$LimitWarn: $module;format="Camel"$LimitWarn,
                         interface: String,
                         port: Int)

object ServiceConfig {
  def apply(): ServiceConfig = this.apply(ConfigFactory.load().getConfig("service"))

  def apply(config: Config): ServiceConfig = {
    this.apply(
      PrometheusConfig(config.getConfig("prometheus")),
      $module;format="Camel"$LimitWarn(config.getInt("$module$.$module$-limit-warn")),
      config.getString("interface"),
      config.getInt("port")
    )
  }
}

case class PrometheusConfig(interface: String,
                            port: Int,
                            endpoint: String)

object PrometheusConfig {
  def apply(config: Config): PrometheusConfig =
    this.apply(
      config.getString("interface"),
      config.getInt("port"),
      config.getString("endpoint")
    )
}
