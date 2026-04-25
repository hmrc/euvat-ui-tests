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

import org.openqa.selenium.{By, Keys}

object SelectEUMemberState extends BasePage {

  override def pageUrl: String = "which-eu-member-state-claiming-back-vat"

  override def pageTitle: String =
    "Which EU member state are you claiming back VAT from? - EU VAT - GOV.UK"

  val countryDropdown = "#value__listbox"
  val txtCountry: By  = By.cssSelector("#value")

  def selectCountry(country: String): Unit = {
    clearCountry(txtCountry)
    input(txtCountry, country)
    clickCountryDropdown()
    saveAndContinue()
  }

  def clickCountryDropdown(): Unit = click(By.cssSelector(countryDropdown))

  /** Clear already selected country value */
  def clearCountryDropdown(selector: By): Unit = {
    val element = waitForVisibilityOfElement(selector)
    element.sendKeys(Keys.CONTROL, "a")
    element.sendKeys(Keys.DELETE)
  }

  def clearCountry(selector: By): Unit = {
    val el      = waitForVisibilityOfElement(selector)
    el.click()
    val current = Option(el.getAttribute("value")).getOrElse("")
    if (current.nonEmpty) {
      for (_ <- 1 to current.length)
        el.sendKeys(Keys.BACK_SPACE)
    }
  }

}
