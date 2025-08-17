package io.github.adsheada.shopping.basket.adapters.cli

import org.scalatest.funsuite.AnyFunSuite
import io.github.adsheada.shopping.basket.application.{Pricing, Receipt}

final class ConsoleRendererSpec extends AnyFunSuite {

  test("renderLines: no discounts prints (No offers available) between Subtotal and Total") {
    val r = Receipt(subtotalPence = 130, discounts = Nil, totalPence = 130)
    val lines = ConsoleRenderer.renderLines(r)
    assert(lines == List(
      "Subtotal: " + Pricing.fmtGBP(130),
      "(No offers available)",
      "Total price: " + Pricing.fmtGBP(130)
    ))
  }

  test("renderLines: discount under £1 is rendered in pence (e.g., 10p)") {
    val r = Receipt(subtotalPence = 310,
      discounts = List("Apples 10% off" -> 10), // 10p
      totalPence = 300)
    val lines = ConsoleRenderer.renderLines(r)
    assert(lines == List(
      "Subtotal: " + Pricing.fmtGBP(310),
      "Apples 10% off: 10p",
      "Total price: " + Pricing.fmtGBP(300)
    ))
  }

  test("renderLines: discount >= £1 is rendered in pounds via fmtGBP") {
    val r = Receipt(subtotalPence = 1000,
      discounts = List("Promo" -> 200), // £2.00
      totalPence = 800)
    val lines = ConsoleRenderer.renderLines(r)
    assert(lines == List(
      "Subtotal: " + Pricing.fmtGBP(1000),
      "Promo: " + Pricing.fmtGBP(200),
      "Total price: " + Pricing.fmtGBP(800)
    ))
  }

  test("print: writes the rendered lines to stdout (one per line)") {
    val r = Receipt(
      subtotalPence = 310,
      discounts = List("Apples 10% off" -> 10),
      totalPence = 300
    )

    val baos = new java.io.ByteArrayOutputStream()
    val ps   = new java.io.PrintStream(baos, true, "UTF-8")
    val sep  = System.lineSeparator()

    Console.withOut(ps) {
      ConsoleRenderer.print(r)
    }
    ps.flush()

    val expected =
      s"Subtotal: ${Pricing.fmtGBP(310)}$sep" +
      s"Apples 10% off: 10p$sep" +
      s"Total price: ${Pricing.fmtGBP(300)}$sep"

    assert(baos.toString("UTF-8") == expected)
  }
}
