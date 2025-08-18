package io.github.adsheada.shopping.basket.adapters.cli

import scala.io.Source
import scala.util.control.NonFatal
import java.io.File
import io.github.adsheada.shopping.basket.application.{Pricing, Receipt}
import io.github.adsheada.shopping.basket.application.commands._
import io.github.adsheada.shopping.basket.domain.offers.Offer

/** Runs many commands from a plain text file, one per line. */
object BatchRunner {

  /** Returns 0 if all lines succeeded; otherwise the highest non-zero code seen. */
  def runFile(path: String, offers: List[Offer]): Int = {
    var exitCode = 0

    val src = openSource(path)
    try {
      for ((raw, idx0) <- src.getLines().zipWithIndex) {
        val line = raw.trim
        if (line.nonEmpty && !line.startsWith("#")) {
          val tokens = line.split("\\s+")
          try {
            val command = CliParser.parse(tokens)
            command match {
              case Command.PriceBasket(basket) =>
                val receipt: Receipt = Pricing.price(basket, offers)
                ConsoleRenderer.print(receipt)
            }
          } catch {
            case e: IllegalArgumentException =>
              Console.err.println(s"[line ${idx0 + 1}] Error: ${e.getMessage}")
              if (exitCode == 0) exitCode = 2
            case NonFatal(t) =>
              Console.err.println(s"[line ${idx0 + 1}] Unexpected failure: ${t.getMessage}")
              exitCode = 1
          }
        }
      }
    } finally {
      src.close()
    }

    exitCode
  }

  /** Open from filesystem or classpath.
    * Usage:
    *   --scenarios src/main/resources/scenarios.txt
    *   --scenarios classpath:scenarios.txt
    */
  private def openSource(path: String): Source = {
    if (path.startsWith("classpath:")) {
      val name = path.stripPrefix("classpath:").stripPrefix("/").replace("\\", "/")
      // fromResource throws if not found, which is fine
      Source.fromResource(name, getClass.getClassLoader)
    } else {
      Source.fromFile(new File(path))
    }
  }
}
