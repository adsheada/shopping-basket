package io.github.adsheada.shopping.basket

import io.github.adsheada.shopping.basket.application.commands.Command
import io.github.adsheada.shopping.basket.adapters.cli.CliParser
import com.typesafe.scalalogging.LazyLogging

object Main extends LazyLogging {

    def main(args: Array[String]): Unit = {
        try {
            val command = CliParser.parse(args)

            // execute the business logic based on ExecutionMode returned from CliParser.parse
            command match {
                case Command.PriceBasket(basket) =>
                    
                    val subtotal: Int = command.basket.subtotalBase()
                    logger.debug(s"subtotalBase = $subtotal")

                    logger.info(s"Running in PriceBasket mode with items: ${command.basket}")
            }

            logger.info(s"Command executed successfully: $command")
        } catch {
            case e: IllegalArgumentException =>
                logger.error(s"Error: ${e.getMessage}")
                logger.info("Usage: PriceBasket item1 item2 item3 ...")
        }
    }
}