package com.bigcommerce.catalog

import com.bigcommerce.$module;format="Camel"$LimitWarn
import com.bigcommerce.core.storedb.{StoreDb, StoreDbPoolProvider}
import com.bigcommerce.search.SearchGrpc.Search
import com.google.inject.AbstractModule
import io.opentracing.Tracer
import net.codingwell.scalaguice.ScalaModule
import scala.concurrent.ExecutionContext
import com.bigcommerce.catalog.catalog.overrides.repository.CatalogOverrideRepository
import com.bigcommerce.catalog.catalog.overrides.repository.db.DbCatalogOverrideRepository
import com.bigcommerce.catalog.catalog.overrides.service.{CatalogOverrideService, CatalogOverrideServiceImpl}
import com.bigcommerce.channels.ChannelAggregateGrpc.ChannelAggregate
import com.bigcommerce.channels.ChannelsGrpc.Channels

class Wiring(search: Search,
             channels: Channels,
             channelAggregate: ChannelAggregate,
             blockingThreadPool: ExecutionContext,
             poolProvider: StoreDbPoolProvider,
             storeDb: StoreDb, tracer: Tracer,
             $module$LimitWarn: $module;format="Camel"$LimitWarn)
            (implicit ec: ExecutionContext)
  extends AbstractModule
    with ScalaModule {

  override def configure(): Unit = {
    bind[StoreDbPoolProvider].toInstance(poolProvider)
    bind[ExecutionContext].toInstance(ec)
    bind[Tracer].toInstance(tracer)
    bind[StoreDb].toInstance(storeDb)
    bind[Search].toInstance(search)
    bind[Channels].toInstance(channels)
    bind[ChannelAggregate].toInstance(channelAggregate)

    // repositories

    // services
  }
}
