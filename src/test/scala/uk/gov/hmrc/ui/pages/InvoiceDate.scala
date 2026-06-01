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

import org.openqa.selenium.By

object InvoiceDate extends BasePage {

  override def pageUrl: String = "what-is-the-invoice-date"

  override def pageTitle: String = "What is the invoice date? - EU VAT - GOV.UK"

  val txtInvoiceDay: By   = By.ById("value.day")
  val txtInvoiceMonth: By = By.ById("value.month")
  val txtInvoiceYear: By  = By.ById("value.year")

  def submitInvoiceDate(invoiceDay: String, invoiceMonth: String, invoiceYear: String): Unit = {
    input(txtInvoiceDay, invoiceDay)
    input(txtInvoiceMonth, invoiceMonth)
    input(txtInvoiceYear, invoiceYear)
    saveAndContinue()
  }

}
