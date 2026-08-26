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

object CheckYourClaimDetails extends BasePage {

  override def pageUrl: String = "check-your-claim-details"

  override def pageTitle: String =
    "Check your claim details - EU VAT - GOV.UK"

  private val base =
    "#main-content > div > div > form > div:nth-child(%d) > dl > div:nth-child(%d) > dd.govuk-summary-list__actions > a"

  private def selector(section: Int, row: Int): String =
    base.format(section, row)

  private val linkSelectors: Map[String, String] =
    Seq(
      "Refunding EU member state" -> (2, 1),
      "Claim language"            -> (3, 1),
      "Start date"                -> (4, 1),
      "End date"                  -> (4, 1),
      "Email"                     -> (5, 1),
      "Phone number"              -> (5, 2),
      "First SIC code"            -> (6, 1),
      "Second SIC code"           -> (6, 2),
      "Third SIC code"            -> (6, 3)
    ).map { case (label, (section, row)) =>
      label -> selector(section, row)
    }.toMap

  def clickChangeLink(changeLink: String): this.type = {
    val linkCSS = linkSelectors.getOrElse(changeLink, throw new IllegalArgumentException(s"Invalid link: $changeLink"))
    super.clickLinkByCSS(linkCSS)
    this
  }

}
