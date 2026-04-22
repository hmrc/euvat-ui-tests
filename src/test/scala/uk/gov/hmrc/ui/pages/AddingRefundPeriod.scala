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

package uk.gov.hmrc.ui.pages

object AddingRefundPeriod extends BasePage {

  override def pageUrl: String = "what-refund-period"

  override def pageTitle: String = "What is the refund period? EU-VAT - GOV.UK"

  val txtStartMonth: By = By.ById("value.startMonth")
  val txtStartYear: By = By.ById("value.startYear")
  val txtEndMonth: By = By.ById("value.endMonth")
  val txtEndYear: By = By.ById("value.endYear")

  def submitRefundPeriod(
   startMonth: String,
   startYear: String,
   endMonth: String,
   endYear: String
  ): Unit = {
    input(Locators.txtStartMonth, startMonth)
    input(Locators.txtStartYear, startYear)
    input(Locators.txtEndMonth, endMonth)
    input(Locators.txtEndYear, endYear)
    saveAndContinue()
  }

}
