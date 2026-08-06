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
import uk.gov.hmrc.ui.utils.{DatabaseHelper, MongoHelper}

class ErrorSpec
    extends AnyFeatureSpec
    with BaseSpec
    with GivenWhenThen
    with ShouldVerb
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Browser
    with ScreenshotOnFailure
    with MongoHelper
    with DatabaseHelper {

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropMongoCollections()
    cleanupDatabaseIfNotStub()
  }

  Feature("Validate one draft claim for each EU member state - Error checking") {
    Scenario("Submit a refund request", Local, WIP) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900002")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

      When("I start new EUVAT claim")
      ManageYourEuvatClaim.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Add claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Poland")
      EUMemberState.errorMessageDisplayed("You cannot have more than one draft claim for each EU member state")
      EUMemberState.errorSummaryDisplayed("You cannot have more than one draft claim for each EU member state")
      EUMemberState.selectCountry("Greece")
      EUMemberState.errorMessageDisplayed("You cannot have more than one draft claim for each EU member state")
      EUMemberState.errorSummaryDisplayed("You cannot have more than one draft claim for each EU member state")

//      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
//      RefundPeriod.submitRefundPeriod("02", "2025", "04", "2025")
//      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
//      ContactDetails.submitContactAddress("Test@gmail.com", "9876543210")
//      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
//      AddBusinessActivity.continueAsNo()
//      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
//      CheckYourClaimDetails.saveAndContinue()
//      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

//      And("I add purchase details")
//      MakeEuvatClaim.clickLinkByText("Add a purchase")
//      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
//      BeforeYouStart.continue()
//      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
//      PurchaseType.selectPurchaseType("Luxuries, entertainment and hospitality")
//      LuxuryEntertainment.verifyPageTitle(LuxuryEntertainment.pageTitle)
//      LuxuryEntertainment.selectLuxuryType("Receptions, entertainment and hospitality")
//      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
//      InvoiceType.selectInvoiceType("Standard invoice")
//      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
//      InvoiceNumber.submitInvoiceNumber("Test_Invoice_123.5")
//      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
//      InvoiceDate.submitInvoiceDate("08", "12", "2025")
//      SupplierName.verifyPageTitle(SupplierName.pageTitle)
//      SupplierName.submitSupplierName("Test Supplier Name")
//      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
//      SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")
//      AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
//      AddVATRegistration.continueAsYes()
//      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
//      VATRegistrationNumber.submitInvoiceNumber("AB1234567890")
//      TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
//      TotalPurchaseAmount.submitTotalPurchaseAmount("100")
//      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
//      TotalVatPaid.submitTotalVatPaid("100")
//      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
//      TotalVatClaim.submitTotalVatClaim("100")
//      MakeEuvatClaim.clickSignOut
    }

    Scenario("Submit a refund request for Germany", Local) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900001")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

      When("I start new EUVAT claim")
      ManageYourEuvatClaim.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Add claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Germany")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("English")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriod("02", "2025", "04", "2025")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactAddress("Test@gmail.com", "9876543210")
      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.continueAsNo()
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add purchase details")
      MakeEuvatClaim.clickLinkByText("Add a purchase")
      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.continue()
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Other")
      PurchaseTypeOther.verifyPageTitle(PurchaseTypeOther.pageTitle)
      PurchaseTypeOther.selectPurchaseTypeOther("None of these - give more details")
      InvoiceItemDescription.verifyPageTitle(InvoiceItemDescription.pageTitle)
      InvoiceItemDescription.submitItemDescription("Test item description")
      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
      InvoiceType.selectInvoiceType("Simplified invoice")
      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.submitInvoiceNumber("Test_Invoice_123.5")
      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
      InvoiceDate.submitInvoiceDate("08", "12", "2025")
      SupplierName.verifyPageTitle(SupplierName.pageTitle)
      SupplierName.submitSupplierName("Test Supplier Name")
      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
      SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")
      SupplierTaxNumbers.verifyPageTitle(SupplierTaxNumbers.pageTitle)
      SupplierTaxNumbers.selectTaxNumber("Tax ID Number")
      SupplierTaxIDNumber.verifyPageTitle(SupplierTaxIDNumber.pageTitle)
      SupplierTaxIDNumber.submitSupplierTaxID("12/345/67890")
      MakeEuvatClaim.clickSignOut
    }

    Scenario("Delete a refund request", Local) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900001")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

      When("I start new EUVAT claim")
      ManageYourEuvatClaim.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Add claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("France")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("English")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriod("02", "2025", "04", "2025")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactAddress("Test@gmail.com", "9876543210")
      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.continueAsNo()
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.saveAndContinue()

      And("I delete the claim")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("View claim details")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)
      ClaimDetails.clickChangeLink("Refunding EU member state")
      DeleteClaim.verifyPageTitle(DeleteClaim.pageTitle)
      DeleteClaim.continueAsYes()
      MakeEuvatClaim.clickSignOut
    }
  }
}
