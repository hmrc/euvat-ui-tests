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

object SupplierAddress extends BasePage {

  override def pageUrl: String = "what-supplier-address"

  override def pageTitle: String = "What is the supplier's address? - EU VAT - GOV.UK"

  val txtAddressLine1: By = By.cssSelector("#addressLine1")
  val txtAddressLine2: By = By.cssSelector("#addressLine2")
  val txtAddressLine3: By = By.cssSelector("#addressLine3")

  def submitSupplierAddress(addressLine1: String, addressLine2: String, addressLine3: String): Unit = {
    input(txtAddressLine1, addressLine1)
    input(txtAddressLine2, addressLine2)
    input(txtAddressLine3, addressLine3)
    saveAndContinue()
  }
}
