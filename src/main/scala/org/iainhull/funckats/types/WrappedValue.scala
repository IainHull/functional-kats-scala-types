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
