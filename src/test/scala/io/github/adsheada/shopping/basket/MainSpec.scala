package io.github.adsheada.shopping.basket

import org.scalatest.funsuite.AnyFunSuite

final class MainSpec extends AnyFunSuite {

  /** Run Main.run while capturing stdout (so we can assert on rendered lines). */
  private def runAndCaptureOut(args: Array[String]): (Int, String) = {
    val baos = new java.io.ByteArrayOutputStream()
    val ps   = new java.io.PrintStream(baos, true, "UTF-8")
    val code = Console.withOut(ps) {
      Main.run(args)
    }
    ps.flush()
    (code, baos.toString("UTF-8"))
  }

  test("PriceBasket Apples Milk Bread -> exit code 0 and prints receipt lines") {
    val (code, out) = runAndCaptureOut(Array("PriceBasket", "Apples", "Milk", "Bread"))
    assert(code == 0)

    // check the expected structure.
    assert(out.contains("Subtotal: "))
    assert(out.contains("Total price: "))
  }

  test("unknown mode -> exit code 2") {
    val (code, _) = runAndCaptureOut(Array("NoSuchMode", "Apples"))
    assert(code == 2)
  }

  test("no args -> exit code 2") {
    val (code, _) = runAndCaptureOut(Array.empty)
    assert(code == 2)
  }

  test("mode only (no items) -> exit code 2") {
    val (code, _) = runAndCaptureOut(Array("PriceBasket"))
    assert(code == 2)
  }

  test("only unknown items -> exit code 2") {
    val (code, _) = runAndCaptureOut(Array("PriceBasket", "Chocolate", "Bananas"))
    assert(code == 2)
  }
}
