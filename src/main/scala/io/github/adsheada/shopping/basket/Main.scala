package io.github.adsheada.shopping.basket

import io.github.adsheada.shopping.basket.application.commands.Command
import io.github.adsheada.shopping.basket.application.{Pricing, Receipt}
import io.github.adsheada.shopping.basket.adapters.cli._
import io.github.adsheada.shopping.basket.domain.offers.{Offer,Rules}
import com.typesafe.scalalogging.LazyLogging
import scala.util.control.NonFatal

object Main extends LazyLogging:

    /** Testable entrypoint, returns an exit code. */
    def run(args: Array[String], offers: List[Offer] = Rules.currentOffers): Int =
        try {
            val command = CliParser.parse(args)

            // execute the business logic based on ExecutionMode returned from CliParser.parse
            command match {
                case Command.PriceBasket(basket) =>
                    val receipt: Receipt = Pricing.price(basket, Rules.currentOffers)
                    ConsoleRenderer.print(receipt)
            }

            logger.debug(s"Command executed successfully: $command")
            0
        } catch {
            case e: IllegalArgumentException =>
                logger.error(s"Error: ${e.getMessage}")
                logger.info("Usage: PriceBasket item1 item2 item3 ...")
                2
            case NonFatal(t) =>
                logger.error("Unexpected failure", t)
                1 
        }

    /** JVM entrypoint delegates to the testable runner. */
    def main(args: Array[String]): Unit =
        val code = run(args)
        if code != 0 then println(s"Exited with code $code")