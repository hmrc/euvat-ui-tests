/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.ui.utils

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration.*

object Retry extends LazyLogging {

  def eventually(
    timeout: FiniteDuration = 30.seconds,
    interval: FiniteDuration = 500.millis,
    description: String = "condition"
  )(block: => Boolean): Boolean = {
    val deadline = timeout.fromNow
    var result   = false
    var attempts = 0

    while (!result && deadline.hasTimeLeft()) {
      attempts += 1

      result =
        try
          block
        catch {
          case e: Throwable =>
            logger.warn(s"Retry attempt $attempts failed for [$description]: ${e.getMessage}")
            false
        }

      if (!result && deadline.hasTimeLeft()) {
        Thread.sleep(interval.toMillis)
      }
    }

    logger.info(
      s"Retry finished for [$description]. success=$result attempts=$attempts timeout=$timeout interval=$interval"
    )
    result
  }
}
