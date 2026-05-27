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

object CheckYourClaimDetails extends BasePage {

  override def pageUrl: String = "check-your-claim-details"

  override def pageTitle: String =
    "Check your claim details - EU VAT - GOV.UK"

  val lnkChangeRefundingEUMemberState = "#main-content > div > div > form > div:nth-child(2) > h2 > a"
  val lnkChangeClaimLanguage          = "#main-content > div > div > form > div:nth-child(3) > h2 > a"
  val lnkChangeRefundPeriod           = "#main-content > div > div > form > div:nth-child(4) > h2 > a"
  val lnkChangeContactDetails         = "#main-content > div > div > form > div:nth-child(5) > h2 > a"
  val lnkChangeBusinessActivity       = "#main-content > div > div > form > div:nth-child(6) > h2 > a"

  def clickChangeLink(changeLink: String): this.type = {
    val linkCSS = changeLink match {
      case "Refunding EU member state" => lnkChangeRefundingEUMemberState
      case "Claim language"            => lnkChangeClaimLanguage
      case "Refund period"             => lnkChangeRefundPeriod
      case "Contact details"           => lnkChangeContactDetails
      case "Business activity"         => lnkChangeBusinessActivity
      case _                           => throw new IllegalArgumentException(s"Invalid link: $changeLink")
    }
    clickLinkByCSS(linkCSS)
    this
  }

}
