
# Shopping Basket
This repository contains a simple TDD application that can price a basket of goods taking into account some special offers. See the [Background](Background) section for further details.

## Project Structure

## Running the Code
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


## Running the Unit Tests


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

should output: -
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