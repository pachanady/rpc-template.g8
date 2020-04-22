package com.bigcommerce.$module$

import java.util.concurrent.{ExecutorService, Executors, ThreadPoolExecutor}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import com.bigcommerce.ServiceConfig


import com.bigcommerce.$module$.routers.PrometheusRouter
import com.bigcommerce.$module$.{Wiring => $module$Wiring}
import com.bigcommerce.channels.ChannelAggregateGrpc.ChannelAggregateStub
import com.bigcommerce.channels.ChannelsGrpc.ChannelsStub
import com.bigcommerce.core.concurrent.PropagatingExecutionContext
import com.bigcommerce.core.logging.MDCThreadPropagation
import com.bigcommerce.core.metrics.prometheus
import com.bigcommerce.core.metrics.prometheus.concurrent.{InstrumentedThreadFixedSizePoolExecutorConfig, InstrumentedThreadPoolExecutor}
import com.bigcommerce.core.rpc.GrpcContextThreadPropagation
import com.bigcommerce.core.rpc.client.RpcClientFactory
import com.bigcommerce.core.rpc.metadata.RequestContextClientInterceptor
import com.bigcommerce.core.rpc.server.RpcServer
import com.bigcommerce.core.rpc.service.Application
import com.bigcommerce.core.storedb._
import com.bigcommerce.core.tracing.OpenTracingThreadPropagation

import com.bigcommerce.storeconfig.StoreConfigGrpc.StoreConfigStub
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.google.inject.Guice

import io.grpc.{ServerInterceptors, ServerServiceDefinition}
import io.prometheus.client.CollectorRegistry

class $$module;format="Camel"$
class $$module;format="Camel"$
  extends RpcServer {


}

