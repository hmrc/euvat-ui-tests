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
      ManageYourEuvatClaim.clickLinkByText("Make a new EU VAT claim")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Add claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Bulgaria")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("English")
      Currency.verifyPageTitle(Currency.pageTitle)
      Currency.selectCurrencyType("Bulgarian Lev (лв)")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriod("08", "2025", "12", "2025")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactAddress("Test@gmail.com", "9876543210")

      And("I add, change and remove business activity details")
      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.continueAsYes()
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("4711")
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.clickLink("Change business activity code two")
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("1101")
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.clickLink("Remove business activity code two")
      RemoveSecondBusinessActivity.verifyPageTitle(RemoveSecondBusinessActivity.pageTitle)
      RemoveSecondBusinessActivity.continueAsYes()
      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.continueAsYes()
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("4532")
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.continueAsYes()
      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.selectThirdBusinessActivityCode("2534")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.clickLink("Change business activity code two")
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("4520")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.clickLink("Remove business activity code two")
      RemoveSecondBusinessActivity.verifyPageTitle(RemoveSecondBusinessActivity.pageTitle)
      RemoveSecondBusinessActivity.continueAsYes()
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.continueAsYes()
      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.selectThirdBusinessActivityCode("1101")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.clickLink("Change business activity code three")
      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.selectThirdBusinessActivityCode("4532")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.clickLink("Remove business activity code three")
      RemoveThirdBusinessActivity.verifyPageTitle(RemoveThirdBusinessActivity.pageTitle)
      RemoveThirdBusinessActivity.continueAsYes()
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.continueAsYes()
      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.selectThirdBusinessActivityCode("4711")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.continue()

      And("I change claim details")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Refunding EU member state")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Germany")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("German")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Claim language")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("English")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Refund period")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriod("01", "2026", "04", "2026")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Contact details")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactAddress("ChangeTest@gmail.com", "+449876543210")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Business activity")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.clickLink("Change business activity code three")
      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.selectThirdBusinessActivityCode("1101")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.continue()
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

//      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
//      VATRegistrationNumber.submitInvoiceNumber("AB1234567890")

//      PurchaseType.verifyPageTitle(SelectPurchaseType.pageTitle)
//      PurchaseType.selectPurchaseType("Fuel")
    }
  }
}
