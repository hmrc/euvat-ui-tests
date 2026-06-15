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
      SelectEUMemberState.selectCountry("Luxembourg")
      SelectLanguage.verifyPageTitle(SelectLanguage.pageTitle)
      SelectLanguage.selectLanguage("French")
      WhatRefundPeriod.verifyPageTitle(WhatRefundPeriod.pageTitle)
      WhatRefundPeriod.submitRefundPeriod("08", "2025", "12", "2025")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactAddress("Test@gmail.com", "9876543210")

      And("I add, change and remove business activity details")
      BusinessActivity.verifyPageTitle(BusinessActivity.pageTitle)
      BusinessActivity.continueAsYes()
      Add2ndBusinessActivityCode.verifyPageTitle(Add2ndBusinessActivityCode.pageTitle)
      Add2ndBusinessActivityCode.selectSecondBusinessActivityCode("47110 (Retail sale in non-specialised stores)")
      BusinessActivityTwo.verifyPageTitle(BusinessActivityTwo.pageTitle)
      BusinessActivityTwo.clickLink("Change business activity code two")
      Add2ndBusinessActivityCode.verifyPageTitle(Add2ndBusinessActivityCode.pageTitle)
      Add2ndBusinessActivityCode.selectSecondBusinessActivityCode("11010 (Manufacture of beverages)")
      BusinessActivityTwo.verifyPageTitle(BusinessActivityTwo.pageTitle)
      BusinessActivityTwo.clickLink("Remove business activity code two")
      Remove2ndBusinessActivityCode.verifyPageTitle(Remove2ndBusinessActivityCode.pageTitle)
      Remove2ndBusinessActivityCode.continueAsYes()
      BusinessActivity.verifyPageTitle(BusinessActivity.pageTitle)
      BusinessActivity.continueAsYes()
      Add2ndBusinessActivityCode.verifyPageTitle(Add2ndBusinessActivityCode.pageTitle)
      Add2ndBusinessActivityCode.selectSecondBusinessActivityCode("45320 (Wholesale of motor vehicle parts)")
      BusinessActivityTwo.verifyPageTitle(BusinessActivityTwo.pageTitle)
      BusinessActivityTwo.continueAsYes()
      Add3rdBusinessActivityCode.verifyPageTitle(Add3rdBusinessActivityCode.pageTitle)
      Add3rdBusinessActivityCode.selectThirdBusinessActivityCode("25344 (Growing of fibre crops)")
      BusinessActivityThree.verifyPageTitle(BusinessActivityThree.pageTitle)
      BusinessActivityThree.clickLink("Change business activity code two")
      Add2ndBusinessActivityCode.verifyPageTitle(Add2ndBusinessActivityCode.pageTitle)
      Add2ndBusinessActivityCode.selectSecondBusinessActivityCode("45200 (Repair of motor vehicles)")
      BusinessActivityThree.verifyPageTitle(BusinessActivityThree.pageTitle)
      BusinessActivityThree.clickLink("Remove business activity code two")
      Remove2ndBusinessActivityCode.verifyPageTitle(Remove2ndBusinessActivityCode.pageTitle)
      Remove2ndBusinessActivityCode.continueAsYes()
      BusinessActivityTwo.verifyPageTitle(BusinessActivityTwo.pageTitle)
      BusinessActivityTwo.continueAsYes()
      Add3rdBusinessActivityCode.verifyPageTitle(Add3rdBusinessActivityCode.pageTitle)
      Add3rdBusinessActivityCode.selectThirdBusinessActivityCode("11010 (Manufacture of beverages)")
      BusinessActivityThree.verifyPageTitle(BusinessActivityThree.pageTitle)
      BusinessActivityThree.clickLink("Change business activity code three")
      Add3rdBusinessActivityCode.verifyPageTitle(Add3rdBusinessActivityCode.pageTitle)
      Add3rdBusinessActivityCode.selectThirdBusinessActivityCode("45320 (Wholesale of motor vehicle parts)")
      BusinessActivityThree.verifyPageTitle(BusinessActivityThree.pageTitle)
      BusinessActivityThree.clickLink("Remove business activity code three")
      Remove3rdBusinessActivityCode.verifyPageTitle(Remove3rdBusinessActivityCode.pageTitle)
      Remove3rdBusinessActivityCode.continueAsYes()
      BusinessActivityTwo.verifyPageTitle(BusinessActivityTwo.pageTitle)
      BusinessActivityTwo.continueAsYes()
      Add3rdBusinessActivityCode.verifyPageTitle(Add3rdBusinessActivityCode.pageTitle)
      Add3rdBusinessActivityCode.selectThirdBusinessActivityCode("47110 (Retail sale in non-specialised stores)")
      BusinessActivityThree.verifyPageTitle(BusinessActivityThree.pageTitle)
      BusinessActivityThree.continue()

      And("I change claim details")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Refunding EU member state")
      SelectEUMemberState.verifyPageTitle(SelectEUMemberState.pageTitle)
      SelectEUMemberState.selectCountry("Germany")
      SelectLanguage.verifyPageTitle(SelectLanguage.pageTitle)
      SelectLanguage.selectLanguage("German")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Claim language")
      SelectLanguage.verifyPageTitle(SelectLanguage.pageTitle)
      SelectLanguage.selectLanguage("English")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Refund period")
      WhatRefundPeriod.verifyPageTitle(WhatRefundPeriod.pageTitle)
      WhatRefundPeriod.submitRefundPeriod("01", "2026", "04", "2026")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Contact details")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactAddress("ChangeTest@gmail.com", "+449876543210")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Business activity")
      BusinessActivityThree.verifyPageTitle(BusinessActivityThree.pageTitle)
      BusinessActivityThree.clickLink("Change business activity code three")
      Add3rdBusinessActivityCode.verifyPageTitle(Add3rdBusinessActivityCode.pageTitle)
      Add3rdBusinessActivityCode.selectThirdBusinessActivityCode("11010 (Manufacture of beverages)")
      BusinessActivityThree.verifyPageTitle(BusinessActivityThree.pageTitle)
      BusinessActivityThree.continue()
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add purchase details")
//      AboutPurchase.verifyPageTitle(AboutPurchase.pageTitle)
//      AboutPurchase.continue()

//      InvoiceType.verifyPageTitle(SelectInvoiceType.pageTitle)
//      InvoiceType.selectInvoiceType("standard invoice")

//      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
//      InvoiceNumber.submitInvoiceNumber("Test_Invoice_123.5")

//      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
//      InvoiceDate.submitInvoiceDate("08", "12", "2025")

//      SupplierName.verifyPageTitle(SupplierName.pageTitle)
//      SupplierName.submitSupplierName("Test Supplier Name")

//      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
//      SupplierAddress.submitSupplierAddress("Test address one","Test address two","Test address three")

//      AddVATRegistration.verifyPageTitle((AddVATRegistration.pageTitle))
//      AddVATRegistration.continueAsYes()

//      SelectPurchaseType.verifyPageTitle(SelectPurchaseType.pageTitle)
//      SelectPurchaseType.selectPurchaseType("Fuel")

    }

    Scenario("Select Currency type", Local) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "123456")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

      When("I start new EUVAT claim")
      ManageYourEuvatClaim.clickLink("Make a new EU VAT claim")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLink("Add claim details")
      SelectEUMemberState.verifyPageTitle(SelectEUMemberState.pageTitle)
      SelectEUMemberState.selectCountry("Bulgaria")
      SelectLanguage.verifyPageTitle(SelectLanguage.pageTitle)
      SelectLanguage.selectLanguage("English")
      And("I select currency type")
      WhichCurrency.verifyPageTitle(WhichCurrency.pageTitle)
      WhichCurrency.selectCurrencyType("Bulgarian Lev (лв)")
      And("I navigate to refund period page")
      WhatRefundPeriod.verifyPageTitle(WhatRefundPeriod.pageTitle)
    }
  }
}
