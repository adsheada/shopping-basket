package io.github.adsheada.shopping.basket.domain.money

/** ISO-like currency descriptor */
enum Currency(val code: String, val symbol: String, val fractionDigits: Int):
  case GBP extends Currency("GBP", "£", 2)
  // Later: case USD extends Currency("USD", "$", 2), case JPY extends Currency("JPY", "¥", 0)