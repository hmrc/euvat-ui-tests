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

object BusinessActivityThree extends BasePage {

  override def pageUrl: String = "business-activity-3"

  override def pageTitle: String = "Business activities for this claim - EU VAT - GOV.UK"

  val lnkChangeBusinessActivityTwo   =
    "#main-content > div > div > form > dl > div:nth-child(1) > dd.govuk-summary-list__actions > ul > li:nth-child(1) > a"
  val lnkDeleteBusinessActivityTwo   =
    "#main-content > div > div > form > dl > div:nth-child(1) > dd.govuk-summary-list__actions > ul > li:nth-child(2) > a"
  val lnkChangeBusinessActivityThree =
    "#main-content > div > div > form > dl > div:nth-child(2) > dd.govuk-summary-list__actions > ul > li:nth-child(1) > a"
  val lnkDeleteBusinessActivityThree =
    "#main-content > div > div > form > dl > div:nth-child(2) > dd.govuk-summary-list__actions > ul > li:nth-child(2) > a"

  def clickLink(link: String): this.type = {
    val linkCSS = link match {
      case "Change business activity code two"   => lnkChangeBusinessActivityTwo
      case "Delete business activity code two"   => lnkDeleteBusinessActivityTwo
      case "Change business activity code three" => lnkChangeBusinessActivityThree
      case "Delete business activity code three" => lnkDeleteBusinessActivityThree
      case _                                     => throw new IllegalArgumentException(s"Invalid link: $link")
    }
    clickLinkByCSS(linkCSS)
    this
  }

}
