package io.github.adsheada.shopping.basket.adapters.cli

import io.github.adsheada.shopping.basket.domain.{Basket, Item}
import io.github.adsheada.shopping.basket.application.commands._
import io.github.adsheada.shopping.basket.application.commands.Command.PriceBasket
import com.typesafe.scalalogging.LazyLogging

object CliParser extends LazyLogging:

  private inline def fail(msg: String): Nothing =
    throw new IllegalArgumentException(msg)

  private val usage =
    "Usage: PriceBasket <item1> <item2> ..."

  def parse(args: Array[String]): Command =
    // 1) Split mode and items safely
    val (modeStr, rawItems) = args.toList match
      case Nil           => fail("No command line parameters found. ")
      case m :: Nil      => fail("At least one item must be specified.")
      case m :: itemList => (m, itemList)

    // 2) Normalise items to be checked
    val itemTokens = rawItems.iterator.map(_.trim).filter(_.nonEmpty).toList

    // 3) validate/partition items
    val items:List[Item] = validateItems(itemTokens)

    val basket = Basket(items)

    // 4) parse mode and build the Command
    ExecutionMode.parse(modeStr).fold {
      fail(s"Unknown command: $modeStr. Expected one of: ${ExecutionMode.allValuesString}")
    } {
      case ExecutionMode.PriceBasket =>
        PriceBasket(basket)
    }

  private def validateItems(tokens: List[String]): List[Item] =
    // partition items into Known and Unknown
    val (knownItems, unknownItems) =
      tokens.partitionMap { s =>
        Item.parse(s) match
          case Some(item) => Left(item)
          case None       => Right(s)}

    if unknownItems.nonEmpty then 
      logger.warn(s"Unknown item(s) ignored: ${unknownItems.mkString(", ")}")

    if knownItems.isEmpty then
      fail("No known items provided. Please provide valid item(s).")
    else
      knownItems

