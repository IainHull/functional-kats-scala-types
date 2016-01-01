package org.iainhull.funckats.types

import org.scalactic.{Bad, Good, Or}

class Currency private (val code: String) extends AnyVal {
  override def toString: String = s"Currency($code)"
}

/**
  * Currency utilities
  */
object Currency extends AbstractCurrencyHelper {
  val USD = new Currency("USD")
  val EUR = new Currency("EUR")
  val GBP = new Currency("GBP")
  val JPY = new Currency("JPY")
  val CHF = new Currency("CHF")
  val AUD = new Currency("AUD")

  def from(code: String): Currency Or String = {
    if (isValid(code)) Good(new Currency("USD"))
    else Bad(s"Currency($code) is an invalid code")
  }

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

  def convert(source: Currency, target: Currency)(amount: BigDecimal) = {
    amount / rates(source.code) * rates(target.code)
  }
}
