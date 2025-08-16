package shopping.basket


object Main {

    def main(args: Array[String]): Unit = {
        try {
            val command = reader.CmdLine.validate(args)

            // TODO: add pricing logic

            println(s"Command executed successfully: $command")
        } catch {
            case e: IllegalArgumentException =>
                println(s"Error: ${e.getMessage}")
                println("Usage: PriceBasket item1 item2 item3 ...")
        }
    }
}