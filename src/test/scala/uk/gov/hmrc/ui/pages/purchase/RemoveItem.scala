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
import uk.gov.hmrc.ui.pages.claim.ClaimDetails.{base, selector}

object RemoveItem extends BasePage {

  override def pageUrl: String = "purchases-imports/remove-item/P/3"

  override def pageTitle: String = "Remove item - EU VAT - GOV.UK"

  val rdoDeleteItem      = "#value"
  val rdoDoNotDeleteItem = "#value-no"

  private val base =
    "#main-content > div > div > form > div:nth-child(%d) > dl > div:nth-child(%d) > dd.govuk-summary-list__actions > a"

  private def selector(section: Int, row: Int, link: Int): String =
    base.format(section, row, link)

  private val linkSelectors: Map[String, String] =
    Seq(
      "Change" -> (2, 1, 1),
      "Remove" -> (2, 1, 2)
    ).map { case (label, (section, row, link)) =>
      label -> selector(section, row, link)
    }.toMap

  def clickChangeLink(changeLink: String): this.type = {
    val linkCSS = linkSelectors.getOrElse(changeLink, throw new IllegalArgumentException(s"Invalid link: $changeLink"))
    super.clickLinkByCSS(linkCSS)
    this
  }

  def selectDeleteItem(radio: String): this.type = {
    val selector = radio match {
      case "Yes" => rdoDeleteItem
      case "No"  => rdoDoNotDeleteItem
      case _     => throw new IllegalArgumentException(s"Invalid option: $radio")
    }
    radioButton(selector)
    continue()
    this
  }

}
