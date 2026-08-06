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

object CheckVATClaim extends BasePage {

  override def pageUrl: String = "check-vat-claim"

  override def pageTitle: String =
    "Are you sure the amount of VAT you're claiming is correct? - EU VAT - GOV.UK"

  def continueAsYes(): Unit =
    continue()

  def continueAsNo(): Unit =
    clickLinkByText("No, change the VAT claim amount")

}
