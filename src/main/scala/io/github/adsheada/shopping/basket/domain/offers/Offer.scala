/*
package io.github.adsheada.shopping.basket.domain.offers


sealed trait Offer:
  def discountBase(basket: Basket)(using Catalog): Int

/** Apples 10% off (item-level) */
final case class PercentOff(item: Item, percent: Int) extends Offer:
  def discountBase(b: Basket)(using cat: Catalog): Int =
    val count = b.counts.getOrElse(item, 0)
    val unit  = cat.spec(item).basePricePence
    ((unit * count) * percent) / 100

/** Buy 2 Soup -> Bread half price (basket-level) */
final case class BuyXGetYHalfPrice(buyItem: Item, buyQty: Int, getItem: Item) extends Offer:
  def discountBase(b: Basket)(using cat: Catalog): Int =
    val eligibleBread = math.min(b.counts.getOrElse(getItem, 0), b.counts.getOrElse(buyItem, 0) / buyQty)
    (cat.spec(getItem).basePricePence / 2) * eligibleBread
*/