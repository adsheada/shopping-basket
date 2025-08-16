package shopping.basket.domain

enum ExecutionMode:
  case PriceBasket

object ExecutionMode:
  def parse(s: String): Option[ExecutionMode] =
    values.find(_.toString.equalsIgnoreCase(s))

  def allValuesString: String =
    values.map(_.toString).mkString(", ")