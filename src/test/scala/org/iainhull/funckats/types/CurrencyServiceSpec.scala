package org.iainhull.funckats.types

/**
  * Created by iain.hull on 20/12/2015.
  */
class CurrencyServiceSpec extends BasicSpec {

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

  it should "convert valid currencies" in {
    import MoneyAmount.Unsafe._

    convert(Currency.USD, Currency.USD)(MoneyAmount("1")) should be(MoneyAmount("1"))
    convert(Currency.EUR, Currency.EUR)(MoneyAmount("1")) should be(MoneyAmount("1"))
    convert(Currency.GBP, Currency.GBP)(MoneyAmount("1")) should be(MoneyAmount("1"))

    convert(Currency.USD, Currency.EUR)(MoneyAmount("1")) should be(MoneyAmount("0.8"))
    convert(Currency.EUR, Currency.USD)(MoneyAmount("1")) should be(MoneyAmount("1.25"))

    convert(Currency.USD, Currency.GBP)(MoneyAmount("1")) should be(MoneyAmount("0.5"))
    convert(Currency.GBP, Currency.USD)(MoneyAmount("1")) should be(MoneyAmount("2.0"))

    convert(Currency.EUR, Currency.GBP)(MoneyAmount("1")) should be(MoneyAmount("0.625"))
    convert(Currency.GBP, Currency.EUR)(MoneyAmount("1")) should be(MoneyAmount("1.6"))
  }
}
