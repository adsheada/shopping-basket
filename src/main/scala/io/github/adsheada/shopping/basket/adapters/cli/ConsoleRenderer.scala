package io.github.adsheada.shopping.basket.adapters.cli

import io.github.adsheada.shopping.basket.application.Pricing
import io.github.adsheada.shopping.basket.application.Receipt

/** Renders a receipt to the console. */
object ConsoleRenderer:

  /** Create the lines to be printed */
  def renderLines(receipt: Receipt): List[String] =
    val lines = List.newBuilder[String]
    lines += s"Subtotal: ${Pricing.fmtGBP(receipt.subtotalPence)}"

    if receipt.discounts.isEmpty then
      lines += "(No offers available)"
    else
      // Rule: discounts under £1 show in pence, e.g. 10p otherwise in pounds
      receipt.discounts.foreach { case (name, pence) =>
        val amount =
          if pence < 100 then s"${pence}p"
          else Pricing.fmtGBP(pence)
        lines += s"$name: $amount"
      }

    lines += s"Total price: ${Pricing.fmtGBP(receipt.totalPence)}"
    lines.result()

  /** print the rendered lines. */
  def print(receipt: Receipt): Unit =
    renderLines(receipt).foreach(println)