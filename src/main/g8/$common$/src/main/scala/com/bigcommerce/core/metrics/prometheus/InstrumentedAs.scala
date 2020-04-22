package com.bigcommerce.core.metrics.prometheus

import com.bigcommerce.core.metrics.prometheus.model.SummaryQuantile
import io.prometheus.client.{Histogram, Summary}

import scala.concurrent.{ExecutionContext, Future}

trait InstrumentedAs extends Instrumented {

  val defaultHistogramBuckets = Some(Seq(0.1D, 0.25D, 0.5D, 1D, 1.5D, 2D, 5D))

  val p99 = SummaryQuantile(0.99D, 0.001D)
  val p95 = SummaryQuantile(0.95D, 0.001D)
  val p50 = SummaryQuantile(0.50D, 0.001D)

  val defaultSummaryQuantiles = Some(Seq(p99))

  def instrumentAs[T](histogramTimer: Histogram.Child)(fn: T): T = {
    val timer = histogramTimer.startTimer()
    val res = fn
    timer.observeDuration()
    res
  }

  def instrumentFutureAs[T](histogramTimer: Histogram.Child)(fn: Future[T])(implicit ec: ExecutionContext): Future[T] = {
    val timer = histogramTimer.startTimer()
    fn.andThen {
      case _ ⇒ {
        timer.observeDuration()
      }
    }
  }

  def instrumentFutureAs[T](summaryTimer: Summary.Child)(fn: Future[T])(implicit ec: ExecutionContext): Future[T] = {
    val timer = summaryTimer.startTimer()
    fn.andThen {
      case _ ⇒ {
        timer.observeDuration()
      }
    }
  }

  def getParentSummary(name: String, help: String, labelNames: Option[Seq[String]], quantiles: Option[Seq[SummaryQuantile]] = defaultSummaryQuantiles) = {
    metrics.summary(name, help, labelNames, quantiles)
  }

  def getChildSummary(parentSummary: Summary, labelValue: String): Summary.Child = {
    parentSummary.labels(labelValue)
  }

  def getParentHistogram(name: String, help: String, labelNames: Option[Seq[String]], buckets: Option[Seq[Double]] = defaultHistogramBuckets) = {

    metrics.histogram(name, help, labelNames, buckets)
  }

  def getChildHistogram(parentHistogram: Histogram, labelValue: String): Histogram.Child = {
    parentHistogram.labels(labelValue)
  }

  def getParentCounter(name: String, help: String, labelNames: Option[Seq[String]]) = {
    metrics.counter(name, help, labelNames)
  }
}
