package shopping.basket.domain

enum Item(val label: String, val pricePence: Int):
  case Soup   extends Item("Soup",   65)
  case Bread  extends Item("Bread",  80)
  case Milk   extends Item("Milk",  130)
  case Apples extends Item("Apples", 100)

object Item:
  def parse(s: String): Option[Item] =
    val t = s.trim
    Item.values.find(_.label.equalsIgnoreCase(t))
