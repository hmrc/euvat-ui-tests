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

package uk.gov.hmrc.ui.pages.purchase

import uk.gov.hmrc.ui.pages.BasePage

object CheckYourPurchaseDetails extends BasePage {

  override def pageUrl: String = "check-your-purchase-details"

  override def pageTitle: String =
    "Check your purchase details - EU VAT - GOV.UK"

  private val linkSelectors: Map[String, String] =
    Seq(
      "Purchase type"                    -> (2, 1),
      "Luxury type"                      -> (2, 2),
      "Other purchase type"              -> (2, 2),
      "Food and drink cost type"         -> (2, 2),
      "Purchase description"             -> (2, 3),
      "Food and drink for"               -> (2, 3),
      "Invoice type"                     -> (3, 1),
      "Invoice number"                   -> (3, 2),
      "Invoice date"                     -> (3, 3),
      "Supplier name"                    -> (4, 1),
      "Supplier address"                 -> (4, 2),
      "Supplier VAT registration check"  -> (4, 3),
      "Supplier VAT registration number" -> (4, 4),
      "Supplier tax numbers"             -> (4, 3),
      "Supplier tax identifier"          -> (4, 4),
      "Currency"                         -> (5, 1),
      "Amount before VAT"                -> (5, 1),
      "VAT paid"                         -> (5, 2),
      "VAT claim"                        -> (5, 3)
    ).map { case (label, (section, row)) =>
      label -> s"#main-content > div > div > form > div:nth-child($section) > dl > div:nth-child($row) > dd.govuk-summary-list__actions > a"
    }.toMap

  def clickChangeLink(changeLink: String): this.type = {
    val linkCSS = linkSelectors.getOrElse(changeLink, throw new IllegalArgumentException(s"Invalid link: $changeLink"))
    super.clickLinkByCSS(linkCSS)
    this
  }

}
