package io.github.adsheada.shopping.basket.domain.offers

import org.scalatest.funsuite.AnyFunSuite
import io.github.adsheada.shopping.basket.domain._

final class OfferSpec extends AnyFunSuite {

  private def basket(items: Item*): Basket = Basket(items.toList)

  // ---------------- PercentOff ----------------

  test("PercentOff: name formats as '<Item> <percent>% off'") {
    val offer = PercentOff(Item.Apples, 10)
    assert(offer.name == "Apples 10% off")
  }

  test("PercentOff: no target items -> 0p discount") {
    val offer = PercentOff(Item.Apples, 10)
    val b = basket(Item.Soup, Item.Bread)
    assert(offer.discountPence(b) == 0)
  }

  test("PercentOff: one apple, 10% -> 10p") {
    val offer = PercentOff(Item.Apples, 10)
    val b = basket(Item.Apples)
    assert(offer.discountPence(b) == (Item.Apples.pricePence * 1 * 10) / 100)
    assert(offer.discountPence(b) == 10)
  }

  test("PercentOff: three apples, 10% -> 30p") {
    val offer = PercentOff(Item.Apples, 10)
    val b = basket(Item.Apples, Item.Milk, Item.Apples, Item.Apples)
    assert(offer.discountPence(b) == (Item.Apples.pricePence * 3 * 10) / 100)
    assert(offer.discountPence(b) == 30)
  }

  // ---------------- BuyXGetYHalfPrice ----------------

  test("BuyXGetYHalfPrice: name formats as 'Buy <qty> <buy> get <get> half price'") {
    val offer = BuyXGetYHalfPrice(buy = Item.Soup, qty = 2, get = Item.Bread)
    assert(offer.name == "Buy 2 Soup get Bread half price")
  }

  test("BuyXGetYHalfPrice: 0 soup, 1 bread -> 0p") {
    val offer = BuyXGetYHalfPrice(Item.Soup, 2, Item.Bread)
    val b = basket(Item.Bread)
    assert(offer.discountPence(b) == 0)
  }

  test("BuyXGetYHalfPrice: 2 soup, 0 bread -> 0p") {
    val offer = BuyXGetYHalfPrice(Item.Soup, 2, Item.Bread)
    val b = basket(Item.Soup, Item.Soup)
    assert(offer.discountPence(b) == 0)
  }

  test("BuyXGetYHalfPrice: 2 soup, 1 bread -> one bread half price (40p)") {
    val offer = BuyXGetYHalfPrice(Item.Soup, 2, Item.Bread)
    val b = basket(Item.Soup, Item.Soup, Item.Bread)
    assert(offer.discountPence(b) == (Item.Bread.pricePence / 2) * 1)
    assert(offer.discountPence(b) == 40)
  }

  test("BuyXGetYHalfPrice: 4 soup, 3 bread -> two breads half price (80p)") {
    val offer = BuyXGetYHalfPrice(Item.Soup, 2, Item.Bread)
    val b = basket(Item.Soup, Item.Soup, Item.Soup, Item.Soup, Item.Bread, Item.Bread, Item.Bread)
    assert(offer.discountPence(b) == (Item.Bread.pricePence / 2) * 2) // min(breads=3, soups/2=2)
    assert(offer.discountPence(b) == 80)
  }

  test("BuyXGetYHalfPrice: 5 soup, 2 bread -> two breads half price (80p)") {
    val offer = BuyXGetYHalfPrice(Item.Soup, 2, Item.Bread)
    val b = basket(Item.Soup, Item.Soup, Item.Soup, Item.Soup, Item.Soup, Item.Bread, Item.Bread)
    assert(offer.discountPence(b) == (Item.Bread.pricePence / 2) * 2) // min(2, floor(5/2)=2)
    assert(offer.discountPence(b) == 80)
  }

  test("BuyXGetYHalfPrice: qty honored (need full groups of buy items)") {
    val offer = BuyXGetYHalfPrice(Item.Soup, 3, Item.Bread) // require 3 soups per bread half-price
    val b1 = basket(Item.Soup, Item.Soup, Item.Bread)       // only 2 soups -> 0
    val b2 = basket(Item.Soup, Item.Soup, Item.Soup, Item.Bread) // 3 soups -> 1 half-price bread
    assert(offer.discountPence(b1) == 0)
    assert(offer.discountPence(b2) == (Item.Bread.pricePence / 2) * 1)
  }
}
