package shopping.basket.reader

import shopping.basket.domain.{Basket, Command, ExecutionMode, Item}
import shopping.basket.domain.Command.PriceBasket

object CmdLine:

  // this class should validate the command line arguments and the Command based on the domain objects
  def validate(args: Array[String]): Command =
    if args.isEmpty then
      throw new IllegalArgumentException(
        "No command line parameters found. Usage: PriceBasket item1 item2 item3 ..."
      )

    val items = args.tail.toList
    if items.isEmpty then
      throw new IllegalArgumentException("At least one item must be specified.")

    // 1) Validate the Items and Basket
    // TODO: warn for each unknown item
    val knownItems = items.flatMap(Item.parse)
    if knownItems.isEmpty then
      throw new IllegalArgumentException("No known items provided. Please provide valid items.")

    val basket = Basket(knownItems)

    // 2) Validate the ExecutionMode and return the Command
    val modeStr = args.head
    ExecutionMode.parse(modeStr) match
      case Some(_) =>
        PriceBasket(ExecutionMode.PriceBasket, basket)
      case None =>
        val executionModeValues = ExecutionMode.allValuesString
        throw new IllegalArgumentException(
          s"Unknown command: $modeStr. Expected one of: $executionModeValues"
        )
