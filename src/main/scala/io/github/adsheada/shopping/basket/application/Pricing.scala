package io.github.adsheada.shopping.basket.application

import io.github.adsheada.shopping.basket.domain.*
import io.github.adsheada.shopping.basket.domain.offers.Offer

final case class Receipt(
  subtotalPence: Int,
  discounts: List[(String, Int)],
  totalPence: Int
)

object Pricing:

  /** Compute subtotal, discounts, and final total. */
  def price(b: Basket, offers: List[Offer]): Receipt =
    val subtotal = b.items.map(_.pricePence).sum

    // collect all discounts
    val discounts = offers
      .map(o => o.name -> o.discountPence(b))
      .filter { case (_, p) => p > 0 }

    val total = math.max(0,subtotal - discounts.map(_._2).sum)
    Receipt(subtotal, discounts, total)

  /** Format pence as GBP, e.g. 310 -> "£3.10". */
  def fmtGBP(pence: Int): String =
    val pounds = pence / 100
    val pennies = pence % 100
    f"£$pounds%d.${pennies}%02d"
