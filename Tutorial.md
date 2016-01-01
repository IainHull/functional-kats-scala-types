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

## Step 2: MoneyAmount and Quantity

```scala

class MoneyAmount private (val value: BigDecimal) extends AnyVal {

  def + (rhs: MoneyAmount): MoneyAmount = new MoneyAmount(value + rhs.value)

  def * (rhs: Quantity) = new MoneyAmount(value * rhs.value)
  def / (rhs: Quantity) = new MoneyAmount(value / rhs.value)
}

object MoneyAmount {
  val Zero: MoneyAmount = new MoneyAmount(0)

  def from(value: BigDecimal): MoneyAmount Or String = {
    if (value >= 0) Good(new MoneyAmount(value))
    else Bad(s"MoneyAmount($value) must be greater than or equal to zero")
  }
}

class Quantity private (val value: Int) extends AnyVal

object Quantity {
  def from(value: Int): Quantity Or String = {
    if (value > 0) Good(new Quantity(value))
    else Bad(s"Quantity($value) must be greater than zero")
  }
}
```

Update sources and modelSpecs (delete lots of tests). Update the other tests (notice the how hard MoneyAmount is to use in the tests - this is not fun).

Add the `EnableUnsafe` type class.

```scala
@implicitNotFound("Unsafe access not enabled for ${A}. import ${A}.Unsafe._")
trait EnableUnsafe[A]

object MoneyAmount {
  // ...
  
  object Unsafe {
    implicit object Enable extends EnableUnsafe[MoneyAmount]
  }

  def apply(value: BigDecimal)(implicit ev: EnableUnsafe[MoneyAmount]): MoneyAmount = {
    from(value) match {
      case Good(amount) => amount
      case Bad(message) => throw new IllegalArgumentException(message)
    }
  }

  def apply(value: String)(implicit ev: EnableUnsafe[MoneyAmount]): MoneyAmount = {
    apply(BigDecimal(value))
  }
}
```

Simplify MoneyAmount constructors in the unit tests.

## Step 3: WrapperValue

Create WrapperValue.scala

```scala
package org.iainhull.funckats.types

import org.scalactic.{Bad, Good, Or}

import scala.annotation.implicitNotFound

/**
  * Base class for defining wrapper types, used in conjunction with
  * `WrappedValue.Companion`
  */
trait WrappedValue[T] extends Any {
  def value: T

  override def toString = s"${this.getClass.getSimpleName}(${value})"
}

object WrappedValue {

  trait Companion[A, W <: WrappedValue[A]] {

    type ErrorMessage = String

    def from(value: A): W Or ErrorMessage

    def unapply(wrapped: W): Option[A] = Some(wrapped.value)

    def apply(value: A)(implicit ev: EnableUnsafe[W]): W = {
      from(value) match {
        case Good(amount) => amount
        case Bad(message) => throw new IllegalArgumentException(message)
      }
    }
  }

  @implicitNotFound("Unsafe access not enabled for ${B}. `import WrappedValue.Unsafe.enable` to enable it.")
  trait EnableUnsafe[B]

  object Unsafe {
    implicit def enable[W]: EnableUnsafe[W] = new EnableUnsafe[W] {}
  }
}
```

* Update the wrapper types to use  `WrappedValue`.
* Update `OrderServiceSpec` to use unsafe `Quantity` constructor.

## Step 4: Non Empty List

Replace `Vector` in `Order` with scalactic `Every` (a non empty list with the same characteristics as `Vector`.

## Step 5: Order

Lets play in the REPL

```scala
$ sbt

> console

scala> import org.iainhull.funckats.types._

scala> import WrappedValue.Unsafe.enable

scala> Vector(2, 3, 1).sorted

scala> Vector("B", "C", "A").sorted

scala> Vector(Quantity(2), Quantity(3), Quantity(1)).sorted

scala> Vector(Currency.USD, Currency.EUR, Currency.AUD).sorted
```

Look at the [`Vector.sorted` scaladoc](http://www.scala-lang.org/api/current/index.html#scala.collection.immutable.Vector@sorted[B>:A](implicitord:scala.math.Ordering[B]):Repr)

Numbers and strings have an implicit ordering, add an implicit ordering to wrapper types, where the wrapped type has an implicit conversion.

```scala
    implicit def ordering(implicit ord: Ordering[A]): Ordering[W] = Ordering.by(a => a.value)
```

Now retry the REPL experiment.

## Next




```
trait PositiveRate[-A] {
  def asBigDecimal(value: A): BigDecimal
}


  def * [A](rhs: A)(implicit ev: PositiveRate[A]) = new MoneyAmount(value * ev.asBigDecimal(rhs))
  def / [A](rhs: A)(implicit ev: PositiveRate[A]) = new MoneyAmount(value / ev.asBigDecimal(rhs))

  implicit object ThePositiveRate extends PositiveRate[Quantity] {
    def asBigDecimal(quantity: Quantity): BigDecimal = quantity.value
  }

```

