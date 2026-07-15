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

object SupplierTaxNumbers extends BasePage {

  override def pageUrl: String = "supplier-tax-numbers"

  override def pageTitle: String =
    "Select the supplier tax numbers shown on the invoice - EU VAT - GOV.UK"

  val rdoVatRegNumber = "#value"
  val rdoTaxIDNumber = "#value_1"

  def selectTaxNumber(taxNumber: String): this.type = {
    val selector = taxNumber match {
      case "Vat Registration Number" => rdoVatRegNumber
      case "Tax ID Number" => rdoTaxIDNumber
      case _ => throw new IllegalArgumentException(s"Invalid Tax Number: $language")
    }
    radioButton(selector)
    continue()
    this
  }




}
