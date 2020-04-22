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

class $bootclass$   extends App
  with RpcServer {

  override def serverName: String = "$module$-rpc"

  val prometheusMetrics = prometheus.Metrics()

  val MyAsyncExecutor: ExecutorService = Executors.newWorkStealingPool()
  val MyAsyncThreadPool: ExecutionContext = PropagatingExecutionContext(
    ExecutionContext.fromExecutorService(MyAsyncExecutor),
    defaultThreadPropagations
  )

  val dbThreadPoolExecutor = InstrumentedThreadPoolExecutor(
    "$module$_rpc",
    prometheusMetrics,
    InstrumentedThreadFixedSizePoolExecutorConfig("service.db.thread-pool"),
    new ThreadFactoryBuilder().setNameFormat(s"pool-databases-%d").build(),
    new ThreadPoolExecutor.AbortPolicy()
  )

  implicit val actorSystem: ActorSystem = ActorSystem(serverName, Some(Application.config), None, Some(MyAsyncThreadPool))
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = MyAsyncThreadPool

  override lazy val defaultThreadPropagations = Seq(
    new GrpcContextThreadPropagation(),
    new MDCThreadPropagation,
    new OpenTracingThreadPropagation(Application.tracer)
  )

  val storeDbThreadPoolEC: ExecutionContext =
    new PropagatingExecutionContext(
      ExecutionContext.fromExecutor(dbThreadPoolExecutor),
      defaultThreadPropagations
    )

  val clients = new RpcClientFactory(Some(prometheusMetrics), tracer)

  val requestContextClientInterceptor = new RequestContextClientInterceptor
  val starSearchServiceStub = clients[SearchStub]
    .withInterceptors(requestContextClientInterceptor)

  val channelsStub = clients[ChannelsStub]
    .withCompression("gzip")
    .withInterceptors(requestContextClientInterceptor)

  val channelAggregate = clients[ChannelAggregateStub]
    .withCompression("gzip")
    .withInterceptors(requestContextClientInterceptor)

  val storeConfigStub = clients[StoreConfigStub]
    .withCompression("gzip")
    .withInterceptors(requestContextClientInterceptor)

  // setup to read from service environment configs
  val serviceConfig = ServiceConfig()

  val storeDatabaseProvider = new GrpcContextStoreConfigProvider(new StoreConfigProvider(storeConfigStub))

  // db setup
  val storeDbConfig = StoreDbConfig()
  val storeDbPoolProvider = StoreDbPoolProvider(
    storeDbConfig,
    storeDatabaseProvider,
    storeDbThreadPoolEC,
    tracer,
    prometheusMetrics = Some(prometheusMetrics)
  )(ec)
  val storeDb = new StoreDb(storeDbPoolProvider, MyAsyncThreadPool, tracer = tracer)


  val $module;format="Camel"$Injector = Guice.createInjector(
    new $module;format="Camel"$Wiring(
      starSearchServiceStub,
      channelsStub,
      channelAggregate,
      storeDbThreadPoolEC,
      storeDbPoolProvider,
      storeDb,
      tracer,
      serviceConfig.categoryLimitWarn
    )(MyAsyncThreadPool)
  )
  val $module$Impl = $module;format="Camel"$Injector.instance[$module;format="Camel"$Impl]



  registerService($module;format="Camel"$Grpc.bindService($module$Impl, MyAsyncThreadPool))

  // Http server setup for metrics

  val akkaHttpSettings = ServerSettings(actorSystem)

  val router = new $module;format="Camel"$HttpService(akkaHttpSettings, serviceConfig, tracer)

  val collector = CollectorRegistry.defaultRegistry

  val metricsRouter = new PrometheusRouter(
    collector,
    serviceConfig.prometheusConfig.endpoint
  )

  val bindingFuture =
    Http().bindAndHandle(
      handler = router.route(),
      interface = serviceConfig.interface,
      port = serviceConfig.port,
      settings = akkaHttpSettings
    )

  bindingFuture.onComplete {
    case Success(b) =>
      log.info(s"$module;format="Camel"$ service listening on \${b.localAddress}")
    case Failure(err) =>
      log.error(s"Error binding to socket\${serviceConfig.interface}:\${serviceConfig.port}, exiting...", err)
      sys.exit(-1)
  }

  val metricsBindingFuture =
    Http().bindAndHandle(
      handler = metricsRouter.route(),
      interface = serviceConfig.prometheusConfig.interface,
      port = serviceConfig.prometheusConfig.port,
      settings = akkaHttpSettings
    )

  metricsBindingFuture.onComplete {
    case Success(b) =>
      log.info(s"$module;format="Camel"$ service prometheus endpoint listening on \${b.localAddress}")
    case Failure(err) =>
      log.error(s"Error binding to socket \${serviceConfig.prometheusConfig.interface}:\${serviceConfig.prometheusConfig.port}, prometheus metrics unavailable", err)
  }

  start()

  private def registerService(service: ServerServiceDefinition): Unit =
    register(
      service
    )
}

