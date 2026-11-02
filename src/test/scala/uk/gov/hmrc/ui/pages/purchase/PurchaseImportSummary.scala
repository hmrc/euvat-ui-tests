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

object PurchaseImportSummary extends BasePage {

  override def pageUrl: String = "purchases-imports/summary"

  override def pageTitle(args: String*): String = {
    val count = args.head.toInt
    val noun  = if (count == 1) "item" else "items"
    s"You have added $count $noun to this claim - EU VAT - GOV.UK"
  }

  def continueAsYes(): Unit = {
    radioButton(Locators.rdoYes)
    continue()
  }

  def continueAsNo(): Unit = {
    radioButton(Locators.rdoNo)
    continue()
  }

  private def changeLinkSelector(itemIndex: Int): String = {
    require(itemIndex > 0, "Item index must be greater than 0")

    s"dl.govuk-summary-list > div.govuk-summary-list__row:nth-child($itemIndex) > dd.govuk-summary-list__actions > ul > li:first-child > a"
  }

  def clickChangeLink(itemIndex: Int): this.type = {
    super.clickLinkByCSS(changeLinkSelector(itemIndex))
    this
  }

}
