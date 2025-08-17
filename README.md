
# Shopping Basket
This repository contains a simple TDD application that can price a basket of goods taking into account some special offers. See the [Background](Background) section for further details.

## How does it work?
At a high-level, the application:
1. Parses the user's command from the command line
2. Builds a typed command with the basket
3. Runs the command, for example pricing the basket, applying discounts, and
4. Prints the exact output format 

### Project Structure
The application follows a ports-and-adapters design pattern keeping it simple and easily extensible
- domain provides the types and rules
- application orchestrates
- adapters handle the input and output communication

```
src/main/scala/io/github/adsheada/shopping/basket/
└─ adapters/cli/
│  ├─ CliParser.scala                // parse args and return the Command
│  └─ ConsoleRenderer.scala          // prints subtotal/discounts/Total
├─ application/
│  ├─ commands/
│  │  └─ Command.scala
│  │  ├─ ExecutionMode.scala
│  └─ Pricing.scala                  // subtotal/discount/total
├─ domain/
│  ├─ Basket.scala
│  ├─ Item.scala
│  └─ offers/
│     ├─ Offer.scala
│     └─ Rules.scala                 // Apples 10%; 2x Soup ⇒ Bread half price
└─ Main.scala                        // orchestration
```

### Running the Code
If not already installed, download and install sbt from [here](https://www.scala-sbt.org/download).

To run the code using sbt from the root directory of this project, changing the basket items to ones you want a price for:
```
sbt 
run PriceBasket Apples Milk Bread
```

To build the code:
```
sbt compile
```

### Running the Unit Tests
To run the test:
```
sbt
test
```

## Support

### Execution Mode
The Execution Mode is provided by the user at the head of the list of parameters they input, e.g. for the input 'PriceBasket Apples Milk Bread', the Execution Mode is PriceBasket, used to price a basket of goods taking into account some special offers.

To add a new Execution Mode:
1. Add a new value to the ExecutionMode enum
2. Extend the Command object with a new case class
3. Add the routing in the Main class to point to the new logic

### Currency
All monetary values are in a base currency unit at the lowest denomination, e.g. Pence for GBP. The currency object provides context and formatting of the currency in use.

## Background
Write a program driven by unit tests that can price a basket of goods taking into account some special offers. 
The goods that can be purchased, together with their normal prices are:

| Product | Cost               |
|---------|--------------------|
| Soup    | 65p per tin        |
| Bread   | 80p per loaf       |
| Milk    | £1.30 per bottle   |
| Apples  | £1.00 per bag      |

The program should accept a list of items in the basket and output the subtotal, the special offer discounts and
the final price.

Input should be via the command line in the form `PriceBasket item1 item2 item3....`

### Current Special Offers
* Apples have a 10% discount off their normal price this week.
* Buy 2 tins of soup and get a loaf of bread for half price.

### Example 

`PriceBasket Apples Milk Bread`

Should output: -
```
Subtotal: £3.10
Apples 10% off: 10p
Total price: £3.00
```

If no special offers are applicable the code should output:
```
Subtotal: £1.30
(No offers available)
Total price: £1.30
```