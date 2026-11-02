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

package uk.gov.hmrc.ui.pages.imports

import uk.gov.hmrc.ui.pages.BasePage

object CheckYourImportDetails extends BasePage {

  override def pageUrl: String = "import/check-your-answers"

  override def pageTitle: String =
    "Check your answers - EU VAT - GOV.UK"

  private val linkSelectors: Map[String, String] =
    Seq(
      "Import type"                                    -> (2, 1),
      "Fuel use"                                       -> (2, 2),
      "Transport cost type"                            -> (2, 2),
      "Food, drink or restaurant cost type"            -> (2, 2),
      "Luxury, entertainment or hospitality cost type" -> (2, 2),
      "Other import type"                              -> (2, 2),
      "Import description"                             -> (2, 2),
      "Fuel type"                                      -> (2, 3),
      "Fuel or vehicle type"                           -> (2, 3),
      "Vehicle use"                                    -> (2, 3),
      "Additional transport cost type"                 -> (2, 3),
      "Food and drink for"                             -> (2, 3),
      "Cost for publicity purposes"                    -> (2, 3),
      "Property purchase type"                         -> (2, 3),
      "Property cost type"                             -> (2, 3),
      "Single Administrative Document (SAD)"           -> (3, 1),
      "SAD reference number"                           -> (3, 2),
      "Import document information"                    -> (3, 2),
      "Import date"                                    -> (3, 3),
      "Supplier name"                                  -> (4, 1),
      "Supplier address"                               -> (4, 2),
      "Currency"                                       -> (5, 1),
      "Amount without VAT"                             -> (5, 2),
      "VAT paid"                                       -> (5, 3),
      "VAT claim"                                      -> (5, 4)
    ).map { case (label, (section, row)) =>
      label -> s"#main-content > div > div > form > div:nth-child($section) > dl > div:nth-child($row) > dd.govuk-summary-list__actions > a"
    }.toMap

  def clickChangeLink(changeLink: String): this.type = {
    val linkCSS = linkSelectors.getOrElse(changeLink, throw new IllegalArgumentException(s"Invalid link: $changeLink"))
    super.clickLinkByCSS(linkCSS)
    this
  }

}
