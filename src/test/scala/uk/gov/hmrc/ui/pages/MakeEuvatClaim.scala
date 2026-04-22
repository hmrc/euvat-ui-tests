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

object MakeEuvatClaim extends BasePage {

  override def pageUrl: String = "make-an-eu-vat-claim"

  override def pageTitle: String = "Make an EU VAT claim - EU VAT - GOV.UK"

  val lnkAddClaimDetails = "Add claim details"
  val lnkAddPurchase     = "Add a purchase"
  val lnkAddImport       = "Add an import"
  val lnkAddSupportDocs  = "Add or view supporting documents"
  val lnkAddBankDetails  = "Add bank details"
  val lnkDeleteClaim     = "Delete claim"
  val lnkClaimsDashboard = "Go to the claims dashboard"
  val linkBack           = "Back"

  def clickLink(link: String): this.type = {
    val linkText = link match {
      case "Add claim details"                => lnkAddClaimDetails
      case "Add a purchase"                   => lnkAddPurchase
      case "Add an import"                    => lnkAddImport
      case "Add or view supporting documents" => lnkAddSupportDocs
      case "Add bank details"                 => lnkAddBankDetails
      case "Delete claim"                     => lnkDeleteClaim
      case "Go to the claims dashboard"       => lnkClaimsDashboard
      case "Back"                             => linkBack
      case _                                  => throw new IllegalArgumentException(s"Invalid link: $link")
    }
    clickLinkByText(linkText)
    this
  }

}
