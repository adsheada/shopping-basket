package io.github.adsheada.shopping.basket.domain

final case class Basket(items: List[Item]):
  def isEmpty: Boolean = items.isEmpty
  def counts: Map[Item, Int] =
    items.groupBy(identity).view.mapValues(_.size).toMap

  def subtotalBase(): Int =
    items.map(_.priceBase).sum