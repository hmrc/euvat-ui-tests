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
import uk.gov.hmrc.ui.pages.claim.*
import uk.gov.hmrc.ui.pages.imports.*
import uk.gov.hmrc.ui.pages.purchase.*
import uk.gov.hmrc.ui.tags.*
import uk.gov.hmrc.ui.utils.{DatabaseHelper, MongoHelper}

class DeleteClaimSpec
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

  Feature("Delete a draft EUVAT claim - Delete claim") {

    Scenario("01 - Delete Claim details from Check your claim details page", Jenkins, Local) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I start new EUVAT claim")
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Estonia")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("Estonian")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriodUsingDynamicValidYear("01", "03")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactDetails("Test@gmail.com", "9876543210")
      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.continueAsYes()
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("2222")
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.continueAsYes()

      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.enterThirdBusinessActivityCode("3333")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.continue()

      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.saveAndContinue()

      Then("I return to the Claim details page to delete the claim")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("Claim details")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)
      ClaimDetails.clickChangeLink("EU member state")
      CannotChangeEUMemberState.verifyPageTitle(CannotChangeEUMemberState.pageTitle)
      CannotChangeEUMemberState.continueAsYes()
      ClaimAnEUVATRefund.clickSignOut
    }

    Scenario("02 - Delete a full refund claim from Make a claim for an EU VAT refund page", Jenkins, Local) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I start new EUVAT claim")
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Estonia")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("Estonian")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriodUsingDynamicValidYear("01", "03")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactDetails("Test@gmail.com", "9876543210")
      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.continueAsYes()
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("2222")
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.continueAsYes()

      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.enterThirdBusinessActivityCode("3333")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.continue()

      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.saveAndContinue()

      And("I create the purchase record")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("Purchases and imports")
      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.continue()
      AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
      AddPurchaseImport.selectPurchaseOrImport("Purchase")
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Food, drink and restaurant services")
      PurchaseSubcodeFood.verifyPageTitle(PurchaseSubcodeFood.pageTitle)
      PurchaseSubcodeFood.selectFoodCostType("Food and drink from hotels")
      PurchaseSubcategoryFood.verifyPageTitle(PurchaseSubcategoryFood.pageTitle)
      PurchaseSubcategoryFood.selectWhoFoodFor("The taxable person")

      //      Invoice details
      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
      InvoiceType.selectInvoiceType("Standard invoice")
      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.submitInvoiceNumber("1234567890")
      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
      InvoiceDate.submitInvoiceDate("08", "12", "2025")

      //      Supplier details
      SupplierName.verifyPageTitle(SupplierName.pageTitle)
      SupplierName.submitSupplierName("Test Supplier Name")
      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
      SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")
      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.submitVATRegistrationNumber("EE0000000111")

      //      Purchase amounts
      Currency.verifyPageTitle(Currency.pageTitle)
      Currency.selectCurrencyType("Euro")
      TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
      TotalPurchaseAmount.submitTotalPurchaseAmount("1000.01")
      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
      TotalVatPaid.submitTotalVatPaid("200.01")
      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
      TotalVatClaim.submitTotalVatClaim("100.01")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.saveAndContinue()

//      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1")) // Only works for the database
      PurchaseImportSummary.continueAsYes()

      AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
      AddPurchaseImport.selectPurchaseOrImport("Import")
      ImportType.verifyPageTitle(ImportType.pageTitle)
      ImportType.selectImportType("Food, drink and restaurant services")
      ImportSubcodeFood.verifyPageTitle(ImportSubcodeFood.pageTitle)
      ImportSubcodeFood.selectImportFoodType("Food and drink from hotels")
      ImportSubcategoryFood.verifyPageTitle(ImportSubcategoryFood.pageTitle)
      ImportSubcategoryFood.selectWhoFoodFor("Someone other")
      AddSADRefNumber.verifyPageTitle(AddSADRefNumber.pageTitle)
      AddSADRefNumber.continueAsYes()
      SubmitSADRefNumber.verifyPageTitle(SubmitSADRefNumber.pageTitle)
      SubmitSADRefNumber.submitSADRefNumber("98765")
      ImportDate.verifyPageTitle(ImportDate.pageTitle)
      ImportDate.submitImportDate("01", "01", "2026")
      SubmitSupplierName.verifyPageTitle(SubmitSupplierName.pageTitle)
      SubmitSupplierName.submitSupplierName("Test Supplier")
      ImportSupplierAddress.verifyPageTitle(ImportSupplierAddress.pageTitle)
      ImportSupplierAddress.submitImportSupplierAddress(
        "Test Street123",
        "Test City456",
        "Test State789",
        "HEARD ISLAND & MCDONALD ISLANDS"
      )
      ImportCurrency.verifyPageTitle(ImportCurrency.pageTitle)
      ImportCurrency.selectImportCurrencyType("Euro")
      TotalImportAmount.verifyPageTitle(TotalImportAmount.pageTitle)
      TotalImportAmount.submitTotalImportAmount("1000.01")
      TotalImportVATPaid.verifyPageTitle(TotalImportVATPaid.pageTitle)
      TotalImportVATPaid.submitTotalImportVATPaid("200.01")
      TotalImportVatClaim.verifyPageTitle(TotalImportVatClaim.pageTitle)
      TotalImportVatClaim.submitTotalImportVatClaim("100.01")

      CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
      CheckYourImportDetails.saveAndContinue()

      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("2"))
      PurchaseImportSummary.continueAsNo()

      Then("I delete the claim")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("Delete this claim")
      DeleteClaim.verifyPageTitle(DeleteClaim.pageTitle)
      DeleteClaim.continueAsYes()
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
      ClaimAnEUVATRefund.clickSignOut
    }

    Scenario("03 - Delete a purchase and import from the Purchase & Import Summary page", Jenkins, Local) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I start new EUVAT claim")
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Estonia")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("Estonian")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriodUsingDynamicValidYear("01", "03")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactDetails("Test@gmail.com", "9876543210")
      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.continueAsYes()
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("2222")
      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.continueAsYes()

      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.enterThirdBusinessActivityCode("3333")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.continue()

      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.saveAndContinue()

      And("I create the purchase record")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("Purchases and imports")
      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.continue()
      AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
      AddPurchaseImport.selectPurchaseOrImport("Purchase")
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Food, drink and restaurant services")
      PurchaseSubcodeFood.verifyPageTitle(PurchaseSubcodeFood.pageTitle)
      PurchaseSubcodeFood.selectFoodCostType("Food and drink from hotels")
      PurchaseSubcategoryFood.verifyPageTitle(PurchaseSubcategoryFood.pageTitle)
      PurchaseSubcategoryFood.selectWhoFoodFor("The taxable person")

      //      Invoice details
      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
      InvoiceType.selectInvoiceType("Standard invoice")
      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.submitInvoiceNumber("1234567890")
      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
      InvoiceDate.submitInvoiceDate("08", "12", "2025")

      //      Supplier details
      SupplierName.verifyPageTitle(SupplierName.pageTitle)
      SupplierName.submitSupplierName("Test Supplier Name")
      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
      SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")
      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.submitVATRegistrationNumber("EE0000000111")

      //      Purchase amounts
      Currency.verifyPageTitle(Currency.pageTitle)
      Currency.selectCurrencyType("Euro")
      TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
      TotalPurchaseAmount.submitTotalPurchaseAmount("1000.01")
      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
      TotalVatPaid.submitTotalVatPaid("200.01")
      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
      TotalVatClaim.submitTotalVatClaim("100.01")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.saveAndContinue()

//      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1")) // Only works for the database
      PurchaseImportSummary.continueAsYes()

      AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
      AddPurchaseImport.selectPurchaseOrImport("Import")
      ImportType.verifyPageTitle(ImportType.pageTitle)
      ImportType.selectImportType("Food, drink and restaurant services")
      ImportSubcodeFood.verifyPageTitle(ImportSubcodeFood.pageTitle)
      ImportSubcodeFood.selectImportFoodType("Food and drink from hotels")
      ImportSubcategoryFood.verifyPageTitle(ImportSubcategoryFood.pageTitle)
      ImportSubcategoryFood.selectWhoFoodFor("Someone other")
      AddSADRefNumber.verifyPageTitle(AddSADRefNumber.pageTitle)
      AddSADRefNumber.continueAsYes()
      SubmitSADRefNumber.verifyPageTitle(SubmitSADRefNumber.pageTitle)
      SubmitSADRefNumber.submitSADRefNumber("98765")
      ImportDate.verifyPageTitle(ImportDate.pageTitle)
      ImportDate.submitImportDate("01", "01", "2026")
      SubmitSupplierName.verifyPageTitle(SubmitSupplierName.pageTitle)
      SubmitSupplierName.submitSupplierName("Test Supplier")
      ImportSupplierAddress.verifyPageTitle(ImportSupplierAddress.pageTitle)
      ImportSupplierAddress.submitImportSupplierAddress(
        "Test Street123",
        "Test City456",
        "Test State789",
        "HEARD ISLAND & MCDONALD ISLANDS"
      )
      ImportCurrency.verifyPageTitle(ImportCurrency.pageTitle)
      ImportCurrency.selectImportCurrencyType("Euro")
      TotalImportAmount.verifyPageTitle(TotalImportAmount.pageTitle)
      TotalImportAmount.submitTotalImportAmount("1000.01")
      TotalImportVATPaid.verifyPageTitle(TotalImportVATPaid.pageTitle)
      TotalImportVATPaid.submitTotalImportVATPaid("200.01")
      TotalImportVatClaim.verifyPageTitle(TotalImportVatClaim.pageTitle)
      TotalImportVatClaim.submitTotalImportVatClaim("100.01")

      CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
      CheckYourImportDetails.saveAndContinue()

      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("2"))

      Then("I delete the purchase and import")
      PurchaseImportSummary.clickActionLink(2, "Remove")
      RemoveItem.verifyPageTitle(RemoveItem.pageTitle)
      RemoveItem.selectDeleteItem("Yes")
//      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1")) // Only works for the database
      PurchaseImportSummary.clickActionLink(1, "Remove")
      RemoveItem.verifyPageTitle(RemoveItem.pageTitle)
      RemoveItem.selectDeleteItem("Yes")

//      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle) // Only works for the database
//      MakeEuvatClaim.clickSignOut // Only works for the database

    }
  }
}
