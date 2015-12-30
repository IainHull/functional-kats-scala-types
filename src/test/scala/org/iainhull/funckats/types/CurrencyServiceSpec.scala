package org.iainhull.funckats.types

/**
  * Created by iain.hull on 20/12/2015.
  */
class CurrencyServiceSpec extends ServiceSpec {
  object TestCurrency extends AbstractCurrencyHelper {
    override val rates = Map(
      "USD" -> BigDecimal("1"),
      "EUR" -> BigDecimal("0.8"),
      "GBP" -> BigDecimal("0.5")
    )
  }

  import TestCurrency._

  "CurrencyService" should "validate currencies" in {
    isValid("USD") should be(true)
    isValid("EUR") should be(true)
    isValid("GBP") should be(true)

    isValid("SFO") should be(false)
  }

  it should "not convert invalid currencies" in {
    an [IllegalArgumentException] should be thrownBy {
      convert("SFO", "EUR")(BigDecimal("1"))
    }
    an [IllegalArgumentException] should be thrownBy {
      convert("EUR", "SFO")(BigDecimal("1"))
    }
  }

  it should "convert valid currencies" in {
    convert("USD", "USD")(BigDecimal("1")) should be(BigDecimal("1"))
    convert("EUR", "EUR")(BigDecimal("1")) should be(BigDecimal("1"))
    convert("GBP", "GBP")(BigDecimal("1")) should be(BigDecimal("1"))

    convert("USD", "EUR")(BigDecimal("1")) should be(BigDecimal("0.8"))
    convert("EUR", "USD")(BigDecimal("1")) should be(BigDecimal("1.25"))

    convert("USD", "GBP")(BigDecimal("1")) should be(BigDecimal("0.5"))
    convert("GBP", "USD")(BigDecimal("1")) should be(BigDecimal("2.0"))

    convert("EUR", "GBP")(BigDecimal("1")) should be(BigDecimal("0.625"))
    convert("GBP", "EUR")(BigDecimal("1")) should be(BigDecimal("1.6"))
  }
}
