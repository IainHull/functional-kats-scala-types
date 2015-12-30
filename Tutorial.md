# Tutorial

## Scala

* Scalable language
* Object-oriented and functional (equal)
  * Objects are modules for structuring large code bases
  * Immutable by default
  * First class functions, concise code and easier reuse
  * Type inference
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

```
$ sbt

> compile

> test
```

## Step 1: Currency

* Currency is not a string
* Preconditions everywhere
* These need tests

Initial Currency wrapper type
 * private constructor
 * val parameter
 * value object
 * public constructor via `from` method
 * Option forces you to handle failure

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

Change signatures, fix compile errors, run tests.

Experiment in the REPL

```scala
$ sbt

> console

scala> import org.iainhull.funckats.types._

scala> Currency.USD

scala> Currency.from("USD")

scala> val Some(usd) = Currency.from("USD")

scala> usd == Currency.USD

scala> usd.hashCode == Currency.USD.hashCode
```

Override `toString`.

```scala
class Currency private (val code: String) extends AnyVal {
  override def toString: String = s"Currency($code)"
}
```
