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

package uk.gov.hmrc.ui.pages.claim

import org.openqa.selenium.By
import uk.gov.hmrc.ui.pages.BasePage

import java.time.LocalDate

object RefundPeriod extends BasePage {

  override def pageUrl: String   = "claim-details/refund-period"
  override def pageTitle: String = "Refund period - EU VAT - GOV.UK"

  val txtStartMonth: By = By.id("start.month")
  val txtStartYear: By  = By.id("start.year")
  val txtEndMonth: By   = By.id("end.month")
  val txtEndYear: By    = By.id("end.year")

  private def currentYear: Int          = LocalDate.now().getYear
  private def currentYearString: String = currentYear.toString
  private def lastYearString: String    = (currentYear - 1).toString
  private def twoYearsAgoString: String = (currentYear - 2).toString

  def submitRefundPeriod(startMonth: String, startYear: String, endMonth: String, endYear: String): Unit = {
    input(txtStartMonth, startMonth)
    input(txtStartYear, startYear)
    input(txtEndMonth, endMonth)
    input(txtEndYear, endYear)
    continue()
  }

  def submitRefundPeriodUsingCurrentYear(startMonth: String, endMonth: String): Unit =
    submitRefundPeriod(startMonth, currentYearString, endMonth, currentYearString)

  def submitRefundPeriodUsingStartLastYear(startMonth: String, endMonth: String): Unit =
    submitRefundPeriod(startMonth, lastYearString, endMonth, currentYearString)

  def submitRefundPeriodUsingTwoYearsAgo(startMonth: String, endMonth: String): Unit =
    submitRefundPeriod(startMonth, twoYearsAgoString, endMonth, twoYearsAgoString)

  def isOnOrBefore30September(today: LocalDate = LocalDate.now()): Boolean =
    !today.isAfter(LocalDate.of(today.getYear, 9, 30))

  def checkRefundStartDateMessageUsingTwoYearsAgo(startMonth: String): String =
    s"You’ve told us the refund period start date is $startMonth/$twoYearsAgoString. The refund period start date cannot be before 01/$lastYearString."

  def checkRefundStartDateMessageUsingTwoYearsAgoAndCurrentYear(startMonth: String): String =
    s"You’ve told us the refund period start date is $startMonth/$twoYearsAgoString. The refund period start date cannot be before 01/$currentYearString."

  def checkRefundEndDateMessageUsingCurrentYear(endMonth: String): String =
    s"You’ve told us the refund period end date is $endMonth/$currentYearString. The refund period end date must be in the past."

}
