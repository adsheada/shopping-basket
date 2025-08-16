package shopping.basket.reader

import org.scalatest.funsuite.AnyFunSuite
import shopping.basket.domain._
import shopping.basket.domain.Command
import shopping.basket.domain.ExecutionMode
import shopping.basket.domain.Item
import shopping.basket.domain.Basket

class CmdLineSpec extends AnyFunSuite {

  test("valid PriceBasket with known items returns Command.PriceBasket(...)") {
    val cmd = CmdLine.validate(Array("PriceBasket", "Apples", "Milk", "Bread"))
    cmd match {
      case Command.PriceBasket(mode, basket) =>
        assert(mode == ExecutionMode.PriceBasket)
        assert(basket == Basket(List(Item.Apples, Item.Milk, Item.Bread)))
      case null =>
        fail(s"Expected Command.PriceBasket, got: null")
    }
  }

  test("pricing will be calculated for only known items") {
    val cmd = CmdLine.validate(Array("PriceBasket", "Apples", "Chocolate", "Milk"))
    cmd match {
      case Command.PriceBasket(_, basket) =>
        assert(basket == Basket(List(Item.Apples, Item.Milk)))
      case _ =>
        fail("Expected Command.PriceBasket")
    }
  }

  test("throws when no command-line parameters are provided") {
    val ex = intercept[IllegalArgumentException] {
      CmdLine.validate(Array.empty)
    }
    assert(ex.getMessage.startsWith("No command line parameters found"))
  }

  test("throws when no items are provided") {
    val ex = intercept[IllegalArgumentException] {
      CmdLine.validate(Array("PriceBasket"))
    }
    assert(ex.getMessage == "At least one item must be specified.")
  }

  test("throws when only unknown items are provided") {
    val ex = intercept[IllegalArgumentException] {
      CmdLine.validate(Array("PriceBasket", "Chocolate", "Bananas"))
    }
    assert(ex.getMessage == "No known items provided. Please provide valid items.")
  }

  test("throws when execution mode is unknown and lists expected values") {
    val ex = intercept[IllegalArgumentException] {
      CmdLine.validate(Array("FooMode", "Apples"))
    }
    assert(ex.getMessage.contains("Unknown command: FooMode."))
    assert(ex.getMessage.contains("Expected one of:"))
  }
}
