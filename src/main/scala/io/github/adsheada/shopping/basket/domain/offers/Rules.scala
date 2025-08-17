package io.github.adsheada.shopping.basket.domain.offers

import io.github.adsheada.shopping.basket.domain.Item

/** List of active offers.
  *
  * The *kinds* of offers are modeled in Offer.scala
  */
object Rules:
  val currentOffers: List[Offer] =
    List(
      PercentOff(Item.Apples, 10),
      BuyXGetYHalfPrice(Item.Soup, qty = 2, get = Item.Bread)
    )