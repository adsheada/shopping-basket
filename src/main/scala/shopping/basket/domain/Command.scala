package shopping.basket.domain

sealed trait Command
object Command:
    final case class PriceBasket(executionMode: ExecutionMode, basket: Basket) extends Command