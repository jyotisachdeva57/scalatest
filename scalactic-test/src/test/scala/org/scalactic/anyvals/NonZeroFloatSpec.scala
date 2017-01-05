/*
 * Copyright 2001-2016 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalactic.anyvals

import org.scalatest._
import OptionValues._
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.prop.PropertyChecks
// SKIP-SCALATESTJS-START
import scala.collection.immutable.NumericRange
// SKIP-SCALATESTJS-END
import scala.util.{Failure, Success, Try}
import org.scalactic.{Pass, Fail}
import org.scalactic.{Good, Bad}

class NonZeroFloatSpec extends FunSpec with Matchers with PropertyChecks with TypeCheckedTripleEquals {

  val nonZeroFloatGen: Gen[NonZeroFloat] =
    for {i <- choose(Float.MinValue, Float.MaxValue)} yield {
      if (i == 0.0f)
        NonZeroFloat.ensuringValid(1.0f)
      else
        NonZeroFloat.ensuringValid(i)
    }

  implicit val arbNonZeroFloat: Arbitrary[NonZeroFloat] = Arbitrary(nonZeroFloatGen)

  describe("A NonZeroFloat") {
    describe("should offer a from factory method that") {
      it("returns Some[NonZeroFloat] if the passed Float is greater than 0") {
        NonZeroFloat.from(50.23F).value.value shouldBe 50.23F
        NonZeroFloat.from(100.0F).value.value shouldBe 100.0F
      }
      it("returns Some[NonZeroFloat] if the passed Float is lesser than 0") {
        NonZeroFloat.from(-0.00001F).value.value shouldBe -0.00001F
        NonZeroFloat.from(-99.9F).value.value shouldBe -99.9F
      }
      it("returns None if the passed Float is  0") {
        NonZeroFloat.from(0.0F) shouldBe None
      }
    }
    describe("should offer an ensuringValid factory method that") {
      it("returns NonZeroFloat if the passed Float is greater than 0") {
        NonZeroFloat.ensuringValid(50.23F).value shouldBe 50.23F
        NonZeroFloat.ensuringValid(100.0F).value shouldBe 100.0F
        NonZeroFloat.ensuringValid(Float.PositiveInfinity).value shouldBe Float.PositiveInfinity
      }
      it("returns NonZeroFloat if the passed Float is lesser than 0") {
        NonZeroFloat.ensuringValid(-0.00001F).value shouldBe -0.00001F
        NonZeroFloat.ensuringValid(-99.9F).value shouldBe -99.9F
        NonZeroFloat.ensuringValid(Float.NegativeInfinity).value shouldBe Float.NegativeInfinity
      }
      it("throws AssertionError if the passed Float is NaN") {
        an [AssertionError] should be thrownBy NonZeroFloat.ensuringValid(Float.NaN)
      }
      it("throws AssertionError if the passed Float is 0") {
        an [AssertionError] should be thrownBy NonZeroFloat.ensuringValid(0.0F)
      }
    }
    describe("should offer a tryingValid factory method that") {
      import TryValues._
      it("returns a NonZeroFloat wrapped in a Success if the passed Float is non-zero") {
        NonZeroFloat.tryingValid(50.23F).success.value.value shouldBe 50.23F
        NonZeroFloat.tryingValid(100.0F).success.value.value shouldBe 100F
        NonZeroFloat.tryingValid(-50.23F).success.value.value shouldBe -50.23F
        NonZeroFloat.tryingValid(-100.0F).success.value.value shouldBe -100.0F
      }

      it("returns an AssertionError wrapped in a Failure if the passed Float is NOT non-zero") {
        NonZeroFloat.tryingValid(0.0F).failure.exception shouldBe an [AssertionError]
      }
    }
    describe("should offer a passOrElse factory method that") {
      it("returns a Pass if the given Float is non-zero") {
        NonZeroFloat.passOrElse(50.23F)(i => i) shouldBe Pass
        NonZeroFloat.passOrElse(100.0F)(i => i) shouldBe Pass

        NonZeroFloat.passOrElse(-1.23F)(i => i) shouldBe Pass
        NonZeroFloat.passOrElse(-99.0F)(i => i) shouldBe Pass
      }
      it("returns an error value produced by passing the given Float to the given function if the passed Float is NOT non-zero, wrapped in a Fail") {
        NonZeroFloat.passOrElse(0.0F)(i => s"$i did not taste good") shouldBe Fail("0.0 did not taste good")
      }
    }
    describe("should offer a goodOrElse factory method that") {
      it("returns a NonZeroFloat wrapped in a Good if the given Float is non-zero") {
        NonZeroFloat.goodOrElse(50.23F)(i => i) shouldBe Good(NonZeroFloat(50.23F))
        NonZeroFloat.goodOrElse(100.0F)(i => i) shouldBe Good(NonZeroFloat(100.0F))

        NonZeroFloat.goodOrElse(-1.23F)(i => i) shouldBe Good(NonZeroFloat(-1.23F))
        NonZeroFloat.goodOrElse(-99.0F)(i => i) shouldBe Good(NonZeroFloat(-99.0F))
      }
      it("returns an error value produced by passing the given Float to the given function if the passed Float is NOT non-zero, wrapped in a Bad") {
        NonZeroFloat.goodOrElse(0.0F)(i => s"$i did not taste good") shouldBe Bad("0.0 did not taste good")
      }
    }
    describe("should offer a rightOrElse factory method that") {
      it("returns a NonZeroFloat wrapped in a Right if the given Long is non-zero") {
        NonZeroFloat.rightOrElse(50.23F)(i => i) shouldBe Right(NonZeroFloat(50.23F))
        NonZeroFloat.rightOrElse(100.0F)(i => i) shouldBe Right(NonZeroFloat(100.0F))

        NonZeroFloat.rightOrElse(-1.23F)(i => i) shouldBe Right(NonZeroFloat(-1.23F))
        NonZeroFloat.rightOrElse(-99.0F)(i => i) shouldBe Right(NonZeroFloat(-99.0F))
      }
      it("returns an error value produced by passing the given Float to the given function if the passed Float is NOT non-zero, wrapped in a Left") {
        NonZeroFloat.rightOrElse(0.0F)(i => s"$i did not taste good") shouldBe Left("0.0 did not taste good")
      }
    }
    describe("should offer an isValid predicate method that") {
      it("returns true if the passed Float is non-zero") {
        NonZeroFloat.isValid(50.23f) shouldBe true
        NonZeroFloat.isValid(100.0f) shouldBe true
        NonZeroFloat.isValid(0.0f) shouldBe false
        NonZeroFloat.isValid(-0.0f) shouldBe false
        NonZeroFloat.isValid(-0.00001f) shouldBe true
        NonZeroFloat.isValid(-99.9f) shouldBe true
      }
    }
    describe("should offer a fromOrElse factory method that") {
      it("returns a NonZeroFloat if the passed Float is greater than 0") {
        NonZeroFloat.fromOrElse(50.23f, NonZeroFloat(42.0f)).value shouldBe 50.23f
        NonZeroFloat.fromOrElse(100.0f, NonZeroFloat(42.0f)).value shouldBe 100.0f
      }
      it("returns a NonZeroFloat if the passed Float is lesser than 0") {
        NonZeroFloat.fromOrElse(-0.00001f, NonZeroFloat(42.0f)).value shouldBe -0.00001f
        NonZeroFloat.fromOrElse(-99.9f, NonZeroFloat(42.0f)).value shouldBe -99.9f
      }
      it("returns a given default if the passed Float is 0") {
        NonZeroFloat.fromOrElse(0.0f, NonZeroFloat(42.0f)).value shouldBe 42.0f
      }
    }
    it("should offer MaxValue and MinValue factory methods") {
      NonZeroFloat.MaxValue shouldEqual NonZeroFloat.from(Float.MaxValue).get
      NonZeroFloat.MinValue shouldEqual
        NonZeroFloat.from(Float.MinValue).get
    }
    it("should offer a PositiveInfinity factory method") {
      NonZeroFloat.PositiveInfinity shouldEqual NonZeroFloat.ensuringValid(Float.PositiveInfinity)
    }
    it("should offer a NegativeInfinity factory method") {
      NonZeroFloat.NegativeInfinity shouldEqual NonZeroFloat.ensuringValid(Float.NegativeInfinity)
    }
    it("should offer a MinPositiveValue factory method") {
      NonZeroFloat.MinPositiveValue shouldEqual NonZeroFloat.ensuringValid(Float.MinPositiveValue)
    }

    it("should be sortable") {
      val xs = List(NonZeroFloat(2.2F), NonZeroFloat(4.4F), NonZeroFloat(1.1F),
        NonZeroFloat(3.3F))
      xs.sorted shouldEqual List(NonZeroFloat(1.1F), NonZeroFloat(2.2F), NonZeroFloat(3.3F),
        NonZeroFloat(4.4F))
    }

    describe("when created with apply method") {

      it("should compile when 8 is passed in") {
        "NonZeroFloat(8)" should compile
        NonZeroFloat(8).value shouldEqual 8.0F
        "NonZeroFloat(8L)" should compile
        NonZeroFloat(8L).value shouldEqual 8.0F
        "NonZeroFloat(8.0F)" should compile
        NonZeroFloat(8.0F).value shouldEqual 8.0F
      }

      it("should not compile when 0 is passed in") {
        "NonZeroFloat(0)" shouldNot compile
        "NonZeroFloat(0L)" shouldNot compile
        "NonZeroFloat(0.0F)" shouldNot compile
      }


      it("should compile when -8 is passed in") {
        "NonZeroFloat(-8)" should compile
        NonZeroFloat(-8).value shouldEqual -8.0F
        "NonZeroFloat(-8L)" should compile
        NonZeroFloat(-8L).value shouldEqual -8.0F
        "NonZeroFloat(-8.0F)" should compile
        NonZeroFloat(-8.0F).value shouldEqual -8.0F
      }

      it("should not compile when x is passed in") {
        val a: Int = -8
        "NonZeroFloat(a)" shouldNot compile
        val b: Long = -8L
        "NonZeroFloat(b)" shouldNot compile
        val c: Float = -8.0F
        "NonZeroFloat(c)" shouldNot compile
      }
    }
    describe("when specified as a plain-old Float") {

      def takesNonZeroFloat(pos: NonZeroFloat): Float = pos.value

      it("should compile when 8 is passed in") {
        "takesNonZeroFloat(8)" should compile
        takesNonZeroFloat(8) shouldEqual 8.0F
        "takesNonZeroFloat(8L)" should compile
        takesNonZeroFloat(8L) shouldEqual 8.0F
        "takesNonZeroFloat(8.0F)" should compile
        takesNonZeroFloat(8.0F) shouldEqual 8.0F
      }

      it("should not compile when 0 is passed in") {
        "takesNonZeroFloat(0)" shouldNot compile
        "takesNonZeroFloat(0L)" shouldNot compile
        "takesNonZeroFloat(0.0F)" shouldNot compile
      }

      it("should compile when -8 is passed in") {
        "takesNonZeroFloat(-8)" should compile
        takesNonZeroFloat(-8) shouldEqual -8.0F
        "takesNonZeroFloat(-8L)" should compile
        takesNonZeroFloat(-8L) shouldEqual -8.0F
        "takesNonZeroFloat(-8.0F)" should compile
        takesNonZeroFloat(-8.0F) shouldEqual -8.0F
      }

      it("should not compile when x is passed in") {
        val x: Int = -8
        "takesNonZeroFloat(x)" shouldNot compile
        val b: Long = -8L
        "takesNonZeroFloat(b)" shouldNot compile
        val c: Float = -8.0F
        "takesNonZeroFloat(c)" shouldNot compile
      }
    }

    it("should offer a unary + method that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat) =>
        (+pfloat).toFloat shouldEqual (+(pfloat.toFloat))
      }
    }

    it("should offer a unary - method that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat) =>
        (-pfloat) shouldEqual (-(pfloat.toFloat))
      }
    }

    it("should offer '<' comparison that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat, byte: Byte) =>
        (pfloat < byte) shouldEqual (pfloat.toFloat < byte)
      }
      forAll { (pfloat: NonZeroFloat, short: Short) =>
        (pfloat < short) shouldEqual (pfloat.toFloat < short)
      }
      forAll { (pfloat: NonZeroFloat, char: Char) =>
        (pfloat < char) shouldEqual (pfloat.toFloat < char)
      }
      forAll { (pfloat: NonZeroFloat, int: Int) =>
        (pfloat < int) shouldEqual (pfloat.toFloat < int)
      }
      forAll { (pfloat: NonZeroFloat, long: Long) =>
        (pfloat < long) shouldEqual (pfloat.toFloat < long)
      }
      forAll { (pfloat: NonZeroFloat, float: Float) =>
        (pfloat < float) shouldEqual (pfloat.toFloat < float)
      }
      forAll { (pfloat: NonZeroFloat, double: Double) =>
        (pfloat < double) shouldEqual (pfloat.toFloat < double)
      }
    }

    it("should offer '<=' comparison that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat, byte: Byte) =>
        (pfloat <= byte) shouldEqual (pfloat.toFloat <= byte)
      }
      forAll { (pfloat: NonZeroFloat, char: Char) =>
        (pfloat <= char) shouldEqual (pfloat.toFloat <= char)
      }
      forAll { (pfloat: NonZeroFloat, short: Short) =>
        (pfloat <= short) shouldEqual (pfloat.toFloat <= short)
      }
      forAll { (pfloat: NonZeroFloat, int: Int) =>
        (pfloat <= int) shouldEqual (pfloat.toFloat <= int)
      }
      forAll { (pfloat: NonZeroFloat, long: Long) =>
        (pfloat <= long) shouldEqual (pfloat.toFloat <= long)
      }
      forAll { (pfloat: NonZeroFloat, float: Float) =>
        (pfloat <= float) shouldEqual (pfloat.toFloat <= float)
      }
      forAll { (pfloat: NonZeroFloat, double: Double) =>
        (pfloat <= double) shouldEqual (pfloat.toFloat <= double)
      }
    }

    it("should offer '>' comparison that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat, byte: Byte) =>
        (pfloat > byte) shouldEqual (pfloat.toFloat > byte)
      }
      forAll { (pfloat: NonZeroFloat, short: Short) =>
        (pfloat > short) shouldEqual (pfloat.toFloat > short)
      }
      forAll { (pfloat: NonZeroFloat, char: Char) =>
        (pfloat > char) shouldEqual (pfloat.toFloat > char)
      }
      forAll { (pfloat: NonZeroFloat, int: Int) =>
        (pfloat > int) shouldEqual (pfloat.toFloat > int)
      }
      forAll { (pfloat: NonZeroFloat, long: Long) =>
        (pfloat > long) shouldEqual (pfloat.toFloat > long)
      }
      forAll { (pfloat: NonZeroFloat, float: Float) =>
        (pfloat > float) shouldEqual (pfloat.toFloat > float)
      }
      forAll { (pfloat: NonZeroFloat, double: Double) =>
        (pfloat > double) shouldEqual (pfloat.toFloat > double)
      }
    }

    it("should offer '>=' comparison that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat, byte: Byte) =>
        (pfloat >= byte) shouldEqual (pfloat.toFloat >= byte)
      }
      forAll { (pfloat: NonZeroFloat, short: Short) =>
        (pfloat >= short) shouldEqual (pfloat.toFloat >= short)
      }
      forAll { (pfloat: NonZeroFloat, char: Char) =>
        (pfloat >= char) shouldEqual (pfloat.toFloat >= char)
      }
      forAll { (pfloat: NonZeroFloat, int: Int) =>
        (pfloat >= int) shouldEqual (pfloat.toFloat >= int)
      }
      forAll { (pfloat: NonZeroFloat, long: Long) =>
        (pfloat >= long) shouldEqual (pfloat.toFloat >= long)
      }
      forAll { (pfloat: NonZeroFloat, float: Float) =>
        (pfloat >= float) shouldEqual (pfloat.toFloat >= float)
      }
      forAll { (pfloat: NonZeroFloat, double: Double) =>
        (pfloat >= double) shouldEqual (pfloat.toFloat >= double)
      }
    }

    it("should offer a '+' method that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat, byte: Byte) =>
        (pfloat + byte) shouldEqual (pfloat.toFloat + byte)
      }
      forAll { (pfloat: NonZeroFloat, short: Short) =>
        (pfloat + short) shouldEqual (pfloat.toFloat + short)
      }
      forAll { (pfloat: NonZeroFloat, char: Char) =>
        (pfloat + char) shouldEqual (pfloat.toFloat + char)
      }
      forAll { (pfloat: NonZeroFloat, int: Int) =>
        (pfloat + int) shouldEqual (pfloat.toFloat + int)
      }
      forAll { (pfloat: NonZeroFloat, long: Long) =>
        (pfloat + long) shouldEqual (pfloat.toFloat + long)
      }
      forAll { (pfloat: NonZeroFloat, float: Float) =>
        (pfloat + float) shouldEqual (pfloat.toFloat + float)
      }
      forAll { (pfloat: NonZeroFloat, double: Double) =>
        (pfloat + double) shouldEqual (pfloat.toFloat + double)
      }
    }

    it("should offer a '-' method that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat, byte: Byte) =>
        (pfloat - byte) shouldEqual (pfloat.toFloat - byte)
      }
      forAll { (pfloat: NonZeroFloat, short: Short) =>
        (pfloat - short) shouldEqual (pfloat.toFloat - short)
      }
      forAll { (pfloat: NonZeroFloat, char: Char) =>
        (pfloat - char) shouldEqual (pfloat.toFloat - char)
      }
      forAll { (pfloat: NonZeroFloat, int: Int) =>
        (pfloat - int) shouldEqual (pfloat.toFloat - int)
      }
      forAll { (pfloat: NonZeroFloat, long: Long) =>
        (pfloat - long) shouldEqual (pfloat.toFloat - long)
      }
      forAll { (pfloat: NonZeroFloat, float: Float) =>
        (pfloat - float) shouldEqual (pfloat.toFloat - float)
      }
      forAll { (pfloat: NonZeroFloat, double: Double) =>
        (pfloat - double) shouldEqual (pfloat.toFloat - double)
      }
    }

    it("should offer a '*' method that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat, byte: Byte) =>
        (pfloat * byte) shouldEqual (pfloat.toFloat * byte)
      }
      forAll { (pfloat: NonZeroFloat, short: Short) =>
        (pfloat * short) shouldEqual (pfloat.toFloat * short)
      }
      forAll { (pfloat: NonZeroFloat, char: Char) =>
        (pfloat * char) shouldEqual (pfloat.toFloat * char)
      }
      forAll { (pfloat: NonZeroFloat, int: Int) =>
        (pfloat * int) shouldEqual (pfloat.toFloat * int)
      }
      forAll { (pfloat: NonZeroFloat, long: Long) =>
        (pfloat * long) shouldEqual (pfloat.toFloat * long)
      }
      forAll { (pfloat: NonZeroFloat, float: Float) =>
        (pfloat * float) shouldEqual (pfloat.toFloat * float)
      }
      forAll { (pfloat: NonZeroFloat, double: Double) =>
        (pfloat * double) shouldEqual (pfloat.toFloat * double)
      }
    }

    it("should offer a '/' method that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat, byte: Byte) =>
        pfloat / byte shouldEqual pfloat.toFloat / byte
      }
      forAll { (pfloat: NonZeroFloat, short: Short) =>
        pfloat / short shouldEqual pfloat.toFloat / short
      }
      forAll { (pfloat: NonZeroFloat, char: Char) =>
        pfloat / char shouldEqual pfloat.toFloat / char
      }
      forAll { (pfloat: NonZeroFloat, int: Int) =>
        pfloat / int shouldEqual pfloat.toFloat / int
      }
      forAll { (pfloat: NonZeroFloat, long: Long) =>
        pfloat / long shouldEqual pfloat.toFloat / long
      }
      forAll { (pfloat: NonZeroFloat, float: Float) =>
        pfloat / float shouldEqual pfloat.toFloat / float
      }
      forAll { (pfloat: NonZeroFloat, double: Double) =>
        pfloat / double shouldEqual pfloat.toFloat / double
      }
    }

    // note: since a PosInt % 0 is NaN (as opposed to PosInt / 0, which is Infinity)
    // extra logic is needed to convert to a comparable type (boolean, in this case)
    it("should offer a '%' method that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat, byte: Byte) =>
        val res = pfloat % byte
        if (res.isNaN)
          (pfloat.toFloat % byte).isNaN shouldBe true
        else
          res shouldEqual pfloat.toFloat % byte
      }
      forAll { (pfloat: NonZeroFloat, short: Short) =>
        val res = pfloat % short
        if (res.isNaN)
          (pfloat.toFloat % short).isNaN shouldBe true
        else
          res shouldEqual pfloat.toFloat % short
      }
      forAll { (pfloat: NonZeroFloat, char: Char) =>
        val res = pfloat % char
        if (res.isNaN)
          (pfloat.toFloat % char).isNaN shouldBe true
        else
          res shouldEqual pfloat.toFloat % char
      }
      forAll { (pfloat: NonZeroFloat, int: Int) =>
        val res = pfloat % int
        if (res.isNaN)
          (pfloat.toFloat % int).isNaN shouldBe true
        else
          res shouldEqual pfloat.toFloat % int
      }
      forAll { (pfloat: NonZeroFloat, long: Long) =>
        val res = pfloat % long
        if (res.isNaN)
          (pfloat.toFloat % long).isNaN shouldBe true
        else
          res shouldEqual pfloat.toFloat % long
      }
      forAll { (pfloat: NonZeroFloat, float: Float) =>
        val res = pfloat % float
        if (res.isNaN)
          (pfloat.toFloat % float).isNaN shouldBe true
        else
          res shouldEqual pfloat.toFloat % float
      }
      forAll { (pfloat: NonZeroFloat, double: Double) =>
        val res = pfloat % double
        if (res.isNaN)
          (pfloat.toFloat % double).isNaN shouldBe true
        else
          res shouldEqual pfloat.toFloat % double
      }
    }

    it("should offer 'min' and 'max' methods that are consistent with Float") {
      forAll { (pfloat1: NonZeroFloat, pfloat2: NonZeroFloat) =>
        pfloat1.max(pfloat2).toFloat shouldEqual pfloat1.toFloat.max(pfloat2.toFloat)
        pfloat1.min(pfloat2).toFloat shouldEqual pfloat1.toFloat.min(pfloat2.toFloat)
      }
    }

    it("should offer an 'isWhole' method that is consistent with Float") {
      forAll { (pfloat: NonZeroFloat) =>
        pfloat.isWhole shouldEqual pfloat.toFloat.isWhole
      }
    }

    it("should offer 'toRadians' and 'toDegrees' methods that are consistent with Float") {
      forAll { (pfloat: NonZeroFloat) =>
        pfloat.toRadians shouldEqual pfloat.toFloat.toRadians
      }
    }

    // SKIP-SCALATESTJS-START
    it("should offer 'to' and 'until' method that is consistent with Float") {
      def rangeEqual[T](a: NumericRange[T], b: NumericRange[T]): Boolean =
        a.start == b.start && a.end == b.end && a.step == b.step

      forAll { (pfloat: NonZeroFloat, end: Float, step: Float) =>
        rangeEqual(pfloat.until(end).by(1f), pfloat.toFloat.until(end).by(1f)) shouldBe true
        rangeEqual(pfloat.until(end, step), pfloat.toFloat.until(end, step)) shouldBe true
        rangeEqual(pfloat.to(end).by(1f), pfloat.toFloat.to(end).by(1f)) shouldBe true
        rangeEqual(pfloat.to(end, step), pfloat.toFloat.to(end, step)) shouldBe true
      }
    }
    // SKIP-SCALATESTJS-END

    it("should offer widening methods for basic types that are consistent with Float") {
      forAll { (pfloat: NonZeroFloat) =>
        def widen(value: Float): Float = value
        widen(pfloat) shouldEqual widen(pfloat.toFloat)
      }
      forAll { (pfloat: NonZeroFloat) =>
        def widen(value: Double): Double = value
        widen(pfloat) shouldEqual widen(pfloat.toFloat)
      }
      /*forAll { (pfloat: NonZeroFloat) =>
        def widen(value: NonZeroDouble): NonZeroDouble = value
        widen(pfloat) shouldEqual widen(NonZeroDouble.from(pfloat.toFloat).get)
      }*/
    }
  }
  it("should offer an ensuringValid method that takes a Float => Float, throwing AssertionError if the result is invalid") {
    NonZeroFloat(33.0f).ensuringValid(_ + 1.0f) shouldEqual NonZeroFloat(34.0f)
    NonZeroFloat(33.0f).ensuringValid(_ => Float.PositiveInfinity) shouldEqual NonZeroFloat.ensuringValid(Float.PositiveInfinity)
    NonZeroFloat(33.0f).ensuringValid(_ => Float.NegativeInfinity) shouldEqual NonZeroFloat.ensuringValid(Float.NegativeInfinity)
    an [AssertionError] should be thrownBy { NonZeroFloat.MaxValue.ensuringValid(_ => Float.NaN) }
    an [AssertionError] should be thrownBy { NonZeroFloat.MaxValue.ensuringValid(_ - NonZeroFloat.MaxValue) }
  }
}