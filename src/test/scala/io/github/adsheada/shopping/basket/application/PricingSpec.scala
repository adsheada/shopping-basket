package io.github.adsheada.shopping.basket.application

import org.scalatest.funsuite.AnyFunSuite
import io.github.adsheada.shopping.basket.domain._
import io.github.adsheada.shopping.basket.domain.offers._

final class PricingSpec extends AnyFunSuite {

  private def basket(items: Item*): Basket = Basket(items.toList)

  // ---------- Subtotal, no offers ----------

  test("subtotal with no offers: Apples + Milk + Bread = 310p") {
    val b = basket(Item.Soup, Item.Milk, Item.Bread) // 100 + 130 + 80
    val r = Pricing.price(b, Nil)
    assert(r.subtotalPence == 310)
    assert(r.discounts.isEmpty)
    assert(r.totalPence == 310)
  }

  // ---------- Single offer applies ----------

  test("Apples 10% off: one apple in basket -> 10p discount; total = 300p") {
    val b = basket(Item.Apples, Item.Milk, Item.Bread) // 310 subtotal
    val offers = List(PercentOff(Item.Apples, 10))
    val r = Pricing.price(b, offers)
    assert(r.subtotalPence == 310)
    assert(r.discounts == List("Apples 10% off" -> 10))
    assert(r.totalPence == 300)
  }

  // ---------- Both offers apply, order preserved ----------

  test("PercentOff + BuyXGetYHalfPrice both apply; discounts listed in offer order") {
    val b = basket(Item.Soup, Item.Soup, Item.Bread, Item.Apples) // 65+65+80+100 = 310
    val offers = List(
      PercentOff(Item.Apples, 10),                      // 10p
      BuyXGetYHalfPrice(buy = Item.Soup, 2, Item.Bread) // 40p
    )
    val r = Pricing.price(b, offers)
    assert(r.subtotalPence == 310)
    assert(r.discounts == List("Apples 10% off" -> 10, "Buy 2 Soup get Bread half price" -> 40))
    assert(r.totalPence == 260)
  }

  // ---------- Offers that don't apply are omitted (0p filtered out) ----------

  test("Offers yielding 0p are removed from the discount list") {
    val b = basket(Item.Milk) // no apples, no soup/bread combination
    val offers = List(
      PercentOff(Item.Apples, 10),                    // 0p here
      BuyXGetYHalfPrice(buy = Item.Soup, 2, Item.Bread) // 0p here
    )
    val r = Pricing.price(b, offers)
    assert(r.subtotalPence == Item.Milk.pricePence)
    assert(r.discounts.isEmpty) // 0p entries filtered
    assert(r.totalPence == Item.Milk.pricePence)
  }

  // ---------- Currency formatting ----------

  test("fmtGBP formats pence as pounds correctly") {
    assert(Pricing.fmtGBP(0)    == "£0.00")
    assert(Pricing.fmtGBP(5)    == "£0.05")
    assert(Pricing.fmtGBP(99)   == "£0.99")
    assert(Pricing.fmtGBP(100)  == "£1.00")
    assert(Pricing.fmtGBP(310)  == "£3.10")
    assert(Pricing.fmtGBP(1000) == "£10.00")
  }
}
