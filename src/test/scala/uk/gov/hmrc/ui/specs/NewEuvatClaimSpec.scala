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
      ManageYourEuvatClaim.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Add claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Croatia")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriod("02", "2025", "04", "2025")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactAddress("Test@gmail.com", "9876543210")
      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.continueAsNo()

      And("I change claim details")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
//      Change member state
      CheckYourClaimDetails.clickChangeLink("Refunding EU member state")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Bulgaria")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("English")
      Currency.verifyPageTitle(Currency.pageTitle)
      Currency.selectCurrencyType("Euro")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriod("05", "2025", "07", "2025")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
//      Change language
      CheckYourClaimDetails.clickChangeLink("Claim language")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("Bulgarian")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
//      Change currency
      CheckYourClaimDetails.clickChangeLink("Currency")
      Currency.verifyPageTitle(Currency.pageTitle)
      Currency.selectCurrencyType("Bulgarian Lev (лв)")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
//      Change refund period
      CheckYourClaimDetails.clickChangeLink("End date")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriod("05", "2025", "10", "2025")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Start date")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriod("08", "2025", "10", "2025")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
//      Change contact details
      CheckYourClaimDetails.clickChangeLink("Email")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactAddress("ChangeTest@gmail.com", "9876543210")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickChangeLink("Phone number")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactAddress("ChangeTest@gmail.com", "+449876543210")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
//      Change SIC code
      And("I add, change and remove Second SIC code")
      CheckYourClaimDetails.clickChangeLink("First SIC code")
      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.continueAsYes()
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("4711")
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.clickLink("Change Second SIC code")
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("1101")
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.clickLink("Remove Second SIC code")
      RemoveSecondBusinessActivity.verifyPageTitle(RemoveSecondBusinessActivity.pageTitle)
      RemoveSecondBusinessActivity.continueAsYes()
      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.continueAsYes()
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("4532")
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.continueAsYes()

      And("I add, change and remove Third SIC code")
      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.enterThirdBusinessActivityCode("2534")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.clickLink("Change Third SIC code")
      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.enterThirdBusinessActivityCode("4533")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.clickLink("Remove Third SIC code")
      RemoveThirdBusinessActivity.verifyPageTitle(RemoveThirdBusinessActivity.pageTitle)
      RemoveThirdBusinessActivity.continueAsYes()
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.continueAsYes()
      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.enterThirdBusinessActivityCode("4712")
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

//      TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
//      TotalPurchaseAmount.submitTotalPurchaseAmount("100")
//
//      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
//      TotalVatPaid.submitTotalVatPaid("100")

//      TotalVatClaim.verifyPageTitle(TotalVatPaid.pageTitle)
//      TotalVatClaim.submitTotalVatClaim("100")

      MakeEuvatClaim.signOut()
    }
  }
}
