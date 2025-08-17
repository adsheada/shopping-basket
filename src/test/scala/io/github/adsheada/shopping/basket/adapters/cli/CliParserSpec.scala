package io.github.adsheada.shopping.basket.adapters.cli

import org.scalatest.funsuite.AnyFunSuite
import io.github.adsheada.shopping.basket.domain._
import io.github.adsheada.shopping.basket.application.commands._


class CliParserSpec extends AnyFunSuite {

  test("valid PriceBasket with known items returns Command.PriceBasket(...)") {
    val cmd = CliParser.parse(Array("PriceBasket", "Apples", "Milk", "Bread"))
    cmd match {
      case Command.PriceBasket(basket) =>
        assert(cmd.executionMode == ExecutionMode.PriceBasket)
        assert(basket == Basket(List(Item.Apples, Item.Milk, Item.Bread)))
      case null =>
        fail(s"Expected Command.PriceBasket, got: null")
    }
  }
}

/*

  test("pricing will be calculated for only known items") {
    val cmd = CliParser.parse(Array("PriceBasket", "Apples", "Chocolate", "Milk"))
    cmd match {
      case Command.PriceBasket(_, basket) =>
        assert(basket == Basket(List(Item.Apples, Item.Milk)))
      case null =>
        fail("Expected Command.PriceBasket")
    }
  }

  test("throws when no command-line parameters are provided") {
    val ex = intercept[IllegalArgumentException] {
      CliParser.parse(Array.empty)
    }
    assert(ex.getMessage.startsWith("No command line parameters found"))
  }

  test("throws when no items are provided") {
    val ex = intercept[IllegalArgumentException] {
      CliParser.parse(Array("PriceBasket"))
    }
    assert(ex.getMessage == "At least one item must be specified.")
  }

  test("throws when only unknown items are provided") {
    val ex = intercept[IllegalArgumentException] {
      CliParser.parse(Array("PriceBasket", "Chocolate", "Bananas"))
    }
    assert(ex.getMessage == "No known items provided. Please provide valid item(s).")
  }

  test("throws when execution mode is unknown and lists expected values") {
    val ex = intercept[IllegalArgumentException] {
      CliParser.parse(Array("FooMode", "Apples"))
    }
    assert(ex.getMessage.contains("Unknown command: FooMode."))
    assert(ex.getMessage.contains("Expected one of:"))
  }
}
*/