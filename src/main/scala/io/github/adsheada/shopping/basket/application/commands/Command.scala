package io.github.adsheada.shopping.basket.application.commands

import io.github.adsheada.shopping.basket.domain.Basket

/** A user command the application can execute. */
sealed trait Command:
  def executionMode: ExecutionMode
  def basket: Basket

object Command:
  final case class PriceBasket(basket: Basket) extends Command:
    val executionMode: ExecutionMode = ExecutionMode.PriceBasket