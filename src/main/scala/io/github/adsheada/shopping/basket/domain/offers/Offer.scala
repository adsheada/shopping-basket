package io.github.adsheada.shopping.basket.domain.offers

import io.github.adsheada.shopping.basket.domain.{Basket,Item}

sealed trait Offer:
  def name: String
  def discountPence(basket: Basket): Int

final case class PercentOff(item: Item, percent: Int) extends Offer:
  val name = s"${item.label} $percent% off"
  def discountPence(b: Basket): Int =
    val n = b.counts.getOrElse(item, 0)
    (item.pricePence * n * percent) / 100

final case class BuyXGetYHalfPrice(buy: Item, qty: Int, get: Item) extends Offer:
  val name = s"Buy $qty ${buy.label} get ${get.label} half price"
  def discountPence(b: Basket): Int =
    val eligible = math.min(b.counts.getOrElse(get, 0), b.counts.getOrElse(buy, 0) / qty)
    (get.pricePence / 2) * eligible