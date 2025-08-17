package io.github.adsheada.shopping.basket.domain.offers

import org.scalatest.funsuite.AnyFunSuite
import io.github.adsheada.shopping.basket.domain._

final class RulesSpec extends AnyFunSuite {

  test("currentOffers contains the configured offers in order") {
    val expected =
      List(
        PercentOff(Item.Apples, 10),
        BuyXGetYHalfPrice(buy = Item.Soup, qty = 2, get = Item.Bread)
      )

    assert(Rules.currentOffers == expected)
  }

  test("offer names are stable and human-friendly") {
    val offers = Rules.currentOffers
    assert(offers.head.name == "Apples 10% off")
    assert(offers(1).name   == "Buy 2 Soup get Bread half price")
  }

  test("discounts: no items -> all offers yield 0p") {
    val b = Basket(Nil)
    val discounts = Rules.currentOffers.map(_.discountPence(b))
    assert(discounts.forall(_ == 0))
  }

  test("discounts: a basket where both offers apply") {
    // 2x Apples (10% each) + 2x Soup + 1x Bread -> half-price bread
    val b = Basket(List(
      Item.Apples, Item.Apples, // 2 apples -> 2 * 100 * 10% = 20p
      Item.Soup, Item.Soup,     // 2 soup -> enables 1 half-price bread
      Item.Bread,               // bread 80p -> 40p discount
      Item.Milk                 // extra item for realism
    ))

    val discounts = Rules.currentOffers.map(_.discountPence(b))
    assert(discounts == List(20, 40)) // Apples 20p, Soup/Bread 40p
    assert(discounts.sum == 60)
  }

  test("discounts: soup rule requires full groups and available bread") {
    val offer = BuyXGetYHalfPrice(Item.Soup, qty = 2, get = Item.Bread)

    val b0 = Basket(List(Item.Soup))                            // 1 soup only
    val b1 = Basket(List(Item.Soup, Item.Soup))                 // 2 soups, no bread
    val b2 = Basket(List(Item.Soup, Item.Soup, Item.Bread))     // 2 soups + 1 bread
    val b3 = Basket(List(Item.Soup, Item.Soup, Item.Soup, Item.Bread)) // 3 soups + 1 bread

    assert(offer.discountPence(b0) == 0)
    assert(offer.discountPence(b1) == 0)
    assert(offer.discountPence(b2) == Item.Bread.pricePence / 2) // 40p
    assert(offer.discountPence(b3) == Item.Bread.pricePence / 2) // still only 1 eligible bread
  }
}
