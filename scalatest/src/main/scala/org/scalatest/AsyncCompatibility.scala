/*
 * Copyright 2001-2015 Artima, Inc.
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
package org.scalatest

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait LowPriorityAsyncCompatibility extends Compatibility {

  implicit def executionContext: ExecutionContext

  implicit def convertFutureOfTToFutureOfAssertion[T](o: Future[T]): Future[Assertion] = o.map(t => Succeeded)
}

trait AsyncCompatibility extends LowPriorityAsyncCompatibility {

  implicit def convertFutureOfExpectationToFutureOfAssertion(o: Future[Expectation]): Future[Assertion] = o.map(e => e.toAssertion)
}