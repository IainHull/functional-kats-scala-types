package org.iainhull.funckats

import org.scalatest.{Inside, Matchers, FlatSpec}

package object types {
  abstract class ServiceSpec extends FlatSpec with Matchers with Inside
}
