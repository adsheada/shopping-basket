package io.github.adsheada.shopping.basket.domain

final case class Basket(items: List[Item]):
  def isEmpty: Boolean = items.isEmpty

  lazy val counts: Map[Item, Int] =
    items.groupMapReduce(identity)(_ => 1)(_ + _)