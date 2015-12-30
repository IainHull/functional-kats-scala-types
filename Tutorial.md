# Tutorial

## Scala

* Scalable language
* Object-oriented and functional (equal)
  * Objects are modules for structuring large code bases
  * Immutable by default
  * First class functions, concise code and easier reuse
  * Powerful type system
* Runs on the JVM (constrained by)
  * All code is a method
* Everything is an object
  * No primitives
  * No static members (companion objects)
  * Functions are objects too
  
## Step 0: Navigate the code

* model.scala - anaemic data model
  * case classes
  * preconditions
  * Algebraic data types
* OrderService.scala - simple ordering service
  * traits
  * scalactic Or
* Currency.scala - basic currency support
* tests - basic unit tests

## Step 1: Currency

* Currency is not a string
* Preconditions everywhere
* These need tests

```scala
class Currency private (val code: String) extends AnyVal

 object Currency extends AbstractCurrencyHelper {
  val USD = new Currency("USD")
  val EUR = new Currency("EUR")
  val GBP = new Currency("GBP")
  val JPY = new Currency("JPY")
  val CHF = new Currency("CHF")
  val AUD = new Currency("USD")

  def from(code: String): Option[Currency] = {
    if (isValid(code)) {
      Some(new Currency("USD"))
    } else {
      None
    }
  }
```
