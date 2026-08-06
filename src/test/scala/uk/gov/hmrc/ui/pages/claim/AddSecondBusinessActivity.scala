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

import uk.gov.hmrc.ui.pages.BasePage

object AddSecondBusinessActivity extends BasePage {

  override def pageUrl: String = "business-activity-2"

  override def pageTitle: String = "Business activity for this claim - EU VAT - GOV.UK"

  private val linkSelectors = Map(
    "Change Second SIC code" -> "#main-content > div > div > form > dl > div > dd.govuk-summary-list__actions > ul > li:nth-child(1) > a",
    "Remove Second SIC code" -> "#main-content > div > div > form > dl > div > dd.govuk-summary-list__actions > ul > li:nth-child(2) > a"
  )

  def clickLink(link: String): this.type = {
    val linkCSS = linkSelectors.getOrElse(link, throw new IllegalArgumentException(s"Invalid link: $link"))
    super.clickLinkByCSS(linkCSS)
    this
  }

  def continueAsYes(): Unit = {
    radioButton(Locators.rdoYes)
    continue()
  }

  def continueAsNo(): Unit = {
    radioButton(Locators.rdoNo)
    continue()
  }

}
