package io.github.adsheada.shopping.basket.adapters.cli

import org.scalatest.funsuite.AnyFunSuite
import io.github.adsheada.shopping.basket.domain._
import io.github.adsheada.shopping.basket.application.commands._

final class CliParserSpec extends AnyFunSuite {

  test("valid: PriceBasket Apples Milk Bread -> Command.PriceBasket with parsed items") {
    val cmd = CliParser.parse(Array("PriceBasket", "Apples", "Milk", "Bread"))
    cmd match {
      case Command.PriceBasket(basket) =>
        assert(basket == Basket(List(Item.Apples, Item.Milk, Item.Bread)))
    }
  }

  test("unknown items are ignored (warning is logged) when at least one known item remains") {
    val cmd = CliParser.parse(Array("PriceBasket", "Apples", "Chocolate", "Milk", "???"))
    cmd match {
      case Command.PriceBasket(basket) =>
        assert(basket == Basket(List(Item.Apples, Item.Milk)))
    }
  }

  test("tokens are trimmed and empty tokens are ignored") {
    val cmd = CliParser.parse(Array("PriceBasket", "  Apples  ", "", "  Milk"))
    cmd match {
      case Command.PriceBasket(basket) =>
        assert(basket == Basket(List(Item.Apples, Item.Milk)))
    }
  }

  test("no args -> throws with usage message") {
    val ex = intercept[IllegalArgumentException] {
      CliParser.parse(Array.empty)
    }
    assert(ex.getMessage.contains("No command line parameters found"))
  }

  test("mode only (no items) -> throws") {
    val ex = intercept[IllegalArgumentException] {
      CliParser.parse(Array("PriceBasket"))
    }
    assert(ex.getMessage == "At least one item must be specified.")
  }

  test("all items unknown -> throws") {
    val ex = intercept[IllegalArgumentException] {
      CliParser.parse(Array("PriceBasket", "Chocolate", "Bananas", "???"))
    }
    assert(ex.getMessage == "No known items provided. Please provide valid item(s).")
  }

  test("unknown mode -> throws and lists expected modes") {
    val ex = intercept[IllegalArgumentException] {
      CliParser.parse(Array("FooMode", "Apples"))
    }
    assert(ex.getMessage.contains("Unknown command: FooMode"))
    assert(ex.getMessage.contains("Expected one of:"))
  }
}
