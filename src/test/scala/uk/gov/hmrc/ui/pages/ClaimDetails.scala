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

object ClaimDetails extends BasePage {

  override def pageUrl: String = "claim-details"

  override def pageTitle: String =
    "Claim details - EU VAT - GOV.UK"

  private val linkSelectors = Map(
    "Refunding EU member state" -> "#main-content > div > div > form > div:nth-child(2) > dl > div > dd.govuk-summary-list__actions > a",
    "Claim language"            -> "#main-content > div > div > form > div:nth-child(3) > dl > div > dd.govuk-summary-list__actions > a"
  )

  def clickChangeLink(changeLink: String): this.type = {
    val linkCSS = linkSelectors.getOrElse(changeLink, throw new IllegalArgumentException(s"Invalid link: $changeLink"))
    super.clickLinkByCSS(linkCSS)
    this
  }

}
