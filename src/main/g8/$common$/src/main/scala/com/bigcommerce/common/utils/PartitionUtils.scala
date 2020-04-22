package com.bigcommerce.common.utils

object PartitionUtils {
  def splitEitherSeq[A,B](el: Seq[Either[A,B]]): (Seq[A], Seq[B]) = {
    val (lefts, rights) = el.partition(_.isLeft)
    (lefts.map(_.left.get), rights.map(_.right.get))
  }
}
