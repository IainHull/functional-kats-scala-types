package org.iainhull.funckats.types

/**
  * Created by iain.hull on 20/12/2015.
  */
object Currency extends AbstractCurrencyHelper {
  override protected val rates = Map(
    "USD" -> BigDecimal("1"),
    "EUR" -> BigDecimal("0.92010"),
    "GBP" -> BigDecimal("0.54040"),
    "JPY" -> BigDecimal("121.189"),
    "CHF" -> BigDecimal("0.99255"),
    "AUD" -> BigDecimal("1.39342")
  )
}

trait AbstractCurrencyHelper {
  protected def rates: Map[String, BigDecimal]

  def isValid(currency: String): Boolean = rates.contains(currency)

  def convert(source: String, target: String)(amount: BigDecimal) = {
    require(isValid(source))
    require(isValid(target))

    amount / rates(source) * rates(target)
  }
}
