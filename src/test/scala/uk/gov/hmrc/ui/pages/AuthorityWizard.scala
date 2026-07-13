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
import uk.gov.hmrc.ui.pages.AuthorityWizard.{click, sendKeys}
import uk.gov.hmrc.ui.utils.{Env, RandomUtils, Urls}

object AuthorityWizard extends BasePage {

  override def pageUrl: String   = s"${Env.baseUrl}/auth-login-stub/gg-sign-in"
  override def pageTitle: String = "auth login stub"

  val url: String = s"${Env.baseUrl}/auth-login-stub/gg-sign-in"

  val authorityId: By     = By.id("authorityId")
  val redirectUrl: By     = By.id("redirectionUrl")
  val affinityGroup: By   = By.id("affinityGroupSelect")
  val confidenceLevel: By = By.id("confidenceLevel")
  val nino: By            = By.id("nino")
  val enrolmentKey: By    = By.id("enrolment[0].name")
  val enrolmentId: By     = By.name("enrolment[0].taxIdentifier[0].name")
  val enrolmentValue: By  = By.name("enrolment[0].taxIdentifier[0].value")
  val oAuthIdToken: By    = By.id("idToken")
  val addPreset: By       = By.id("add-ident-btn-0")
  val saPreset: By        = By.id("presets-dropdown")
  val btnSubmit: By       = By.id("submit")
  val btnAddEnrolment: By = By.id("add-ident-btn-0")

  def buildRedirectUrl(): String =
    Env.baseUrl match {
      case Urls.LOCAL => "http://localhost:18500/manage-eu-vat"
      case _          => "/manage-eu-vat"
    }

  def fillInputs(userType: String, vatRegNo: String): String = {
    val generatedCredId = RandomUtils.generateCredId()

    userType match {
      case "Organisation" =>
        driver.findElement(authorityId).sendKeys(generatedCredId)
        driver.findElement(affinityGroup).sendKeys(userType)
        driver.findElement(enrolmentKey).sendKeys("HMRC-EU-REF-ORG")
        driver.findElement(enrolmentId).sendKeys("VATRegNo")
        driver.findElement(enrolmentValue).sendKeys(vatRegNo)

      case "Agent" =>
        driver.findElement(authorityId).sendKeys(generatedCredId)
        driver.findElement(affinityGroup).sendKeys(userType)
        driver.findElement(enrolmentKey).sendKeys("HMCE-VAT-AGNT")
        driver.findElement(enrolmentId).sendKeys("AgentRefNo")
        driver.findElement(enrolmentValue).sendKeys(vatRegNo)
    }
    generatedCredId
  }

  def login(userType: String, vatRegNo: String): String = {
    AuthorityWizard.navigateToPage(url)
    sendKeys(redirectUrl, buildRedirectUrl())
    val generatedCredId = fillInputs(userType, vatRegNo)
    click(btnSubmit)
    generatedCredId
  }
}
