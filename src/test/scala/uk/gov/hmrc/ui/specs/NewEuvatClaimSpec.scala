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

package uk.gov.hmrc.ui.specs

import org.scalatest.*
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.verbs.ShouldVerb
import uk.gov.hmrc.selenium.webdriver.{Browser, ScreenshotOnFailure}
import uk.gov.hmrc.ui.pages.*
import uk.gov.hmrc.ui.tags.*

class NewEuvatClaimSpec
    extends AnyFeatureSpec
    with BaseSpec
    with GivenWhenThen
    with ShouldVerb
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Browser
    with ScreenshotOnFailure {

  Feature("Make a new EUVAT claim - New claim") {
    Scenario("Submit a refund request", Local) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "123456")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

      When("I start new EUVAT claim")
      ManageYourEuvatClaim.clickLink("Make a new EU VAT claim")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLink("Add claim details")
      SelectEUMemberState.verifyPageTitle(SelectEUMemberState.pageTitle)
      SelectEUMemberState.selectCountry("France")
      SelectLanguage.verifyPageTitle(SelectLanguage.pageTitle)
      SelectLanguage.continueAsEnglish()
      WhatRefundPeriod.verifyPageTitle(WhatRefundPeriod.pageTitle)
      WhatRefundPeriod.submitRefundPeriod("08", "2025", "12", "2025")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactAddress("Test@gmail.com", "9876543210")
      BusinessActivity.verifyPageTitle(BusinessActivity.pageTitle)
      BusinessActivity.continueAsYes()
      Add2ndBusinessActivityCode.verifyPageTitle(Add2ndBusinessActivityCode.pageTitle)
      Add2ndBusinessActivityCode.selectSecondBusinessActivityCode("47110 (Retail sale in non-specialised stores)")
      BusinessActivityTwo.verifyPageTitle(BusinessActivityTwo.pageTitle)
      BusinessActivityTwo.continueAsYes()
      Add3rdBusinessActivityCode.verifyPageTitle(Add3rdBusinessActivityCode.pageTitle)
      Add3rdBusinessActivityCode.selectThirdBusinessActivityCode("11010 (Manufacture of beverages)")
      BusinessActivityThree.verifyPageTitle(BusinessActivityThree.pageTitle)
      BusinessActivityThree.clickLink("Change business activity code two")

      And("I add purchase details")
      AboutPurchase.verifyPageTitle(AboutPurchase.pageTitle)
//      AboutPurchase.saveAndContinue()

//      SelectPurchaseType.verifyPageTitle(SelectPurchaseType.pageTitle)
//      SelectPurchaseType.clickRadio("Fuel")

//      AddInvoiceNumber.verifyPageTitle(AddInvoiceNumber.pageTitle)
//      AddInvoiceNumber.submitInvoiceNumber("Test_Invoice_123.5")
//
//      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
//      SupplierAddress.submitSupplierAddress("Test address one","Test address two","Test address three")

    }

  }
}
