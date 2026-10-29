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
import uk.gov.hmrc.ui.pages.purchase.*
import uk.gov.hmrc.ui.tags.*
import uk.gov.hmrc.ui.utils.{CacheHelper, DatabaseHelper, MongoHelper}

class AddPurchaseSpec
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

  Feature("Make a new EUVAT claim - Add a purchase") {

    Scenario("01 - Submit a refund request", Local) {
      Given("I login as an organisation")
      val sharedId = AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I start new EUVAT claim")
//      Inject Claim details
      CacheHelper.submitUserAnswers("claimDetails.json", sharedId)
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I see claim details page completed")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/claim-details/check-your-answers")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add purchase details")
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

      //    Check your purchase details
      And("I change purchase details")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.clickChangeLink("Food and drink for")
      PurchaseSubcategoryFood.verifyPageTitle(PurchaseSubcategoryFood.pageTitle)
      PurchaseSubcategoryFood.selectWhoFoodFor("Someone other")

      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.clickChangeLink("Food and drink cost type")
      PurchaseSubcodeFood.verifyPageTitle(PurchaseSubcodeFood.pageTitle)
      PurchaseSubcodeFood.selectFoodCostType("None")

      //      Change purchase type
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.clickChangeLink("Purchase type")
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Luxuries, entertainment and hospitality")
      PurchaseSubcodeLuxury.verifyPageTitle(PurchaseSubcodeLuxury.pageTitle)
      PurchaseSubcodeLuxury.selectLuxuryType("Receptions, entertainment and hospitality")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change invoice type
      CheckYourPurchaseDetails.clickChangeLink("Invoice type")
      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
      InvoiceType.selectInvoiceType("Simplified invoice")
      AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
      AddVATRegistration.continueAsNo()
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change invoice number
      CheckYourPurchaseDetails.clickChangeLink("Invoice number")
      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.submitInvoiceNumber("XR123456789")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change invoice date
      CheckYourPurchaseDetails.clickChangeLink("Invoice date")
      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
      InvoiceDate.submitInvoiceDate("01", "01", "2026")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change supplier name
      CheckYourPurchaseDetails.clickChangeLink("Supplier name")
      SupplierName.verifyPageTitle(SupplierName.pageTitle)
      SupplierName.submitSupplierName("Update Test Supplier Name")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change supplier address
      CheckYourPurchaseDetails.clickChangeLink("Supplier address")
      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
      SupplierAddress.submitSupplierAddress("Updated Street", "Updated City", "Updated Country")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change supplier VAT registration check
      CheckYourPurchaseDetails.clickChangeLink("Supplier VAT registration check")
      AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
      AddVATRegistration.continueAsYes()
      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.submitVATRegistrationNumber("AA987654321")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change supplier VAT registration number
      CheckYourPurchaseDetails.clickChangeLink("Supplier VAT registration number")
      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.submitVATRegistrationNumber("BB987654321")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change currency
      CheckYourPurchaseDetails.clickChangeLink("Currency")
      Currency.verifyPageTitle(Currency.pageTitle)
      Currency.selectCurrencyType("Estonian Kroon (kr)")
      TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
      TotalPurchaseAmount.submitTotalPurchaseAmount("1000.01")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change VAT claim
      CheckYourPurchaseDetails.clickChangeLink("VAT claim")
      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
      TotalVatClaim.submitTotalVatClaim("3000.99")
      CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
      CheckVATClaim.continue()
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change VAT paid
      CheckYourPurchaseDetails.clickChangeLink("VAT paid")
      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
      TotalVatPaid.submitTotalVatPaid("3000.99")
      CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
      CheckVATAmount.continue()
      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
      TotalVatClaim.submitTotalVatClaim("4000.99")
      CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
      CheckVATClaim.continue()
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      //      Change amount before VAT
      CheckYourPurchaseDetails.clickChangeLink("Amount before VAT")
      TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
      TotalPurchaseAmount.submitTotalPurchaseAmount("300.99")
      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
      TotalVatPaid.submitTotalVatPaid("5000.99")
      CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
      CheckVATAmount.continue()
      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
      TotalVatClaim.submitTotalVatClaim("6000.99")
      CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
      CheckVATClaim.continue()
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.saveAndContinue()
      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1"))
      PurchaseImportSummary.continueAsNo()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickSignOut
    }

    Scenario("02 - Submit a refund request for Germany", Local) {
      Given("I login as an organisation")
      val sharedId = AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I start new EUVAT claim")
      //      Inject Claim details
      CacheHelper.submitUserAnswers("claimDetailsGermany.json", sharedId)
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I see claim details page completed")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/claim-details/check-your-answers")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add purchase details")
      MakeEuvatClaim.clickLinkByText("Purchases and imports")
      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.continue()
      AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
      AddPurchaseImport.selectPurchaseOrImport("Purchase")
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Other")
      PurchaseSubcodeOther.verifyPageTitle(PurchaseSubcodeOther.pageTitle)
      PurchaseSubcodeOther.selectSubcategoryOther("None of these - give more details")
      InvoiceItemDescription.verifyPageTitle(InvoiceItemDescription.pageTitle)
      InvoiceItemDescription.submitItemDescription("")
      CheckPurchaseDetails.verifyPageTitle(CheckPurchaseDetails.pageTitle)
      CheckPurchaseDetails.continue()
      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
      InvoiceType.selectInvoiceType("Simplified invoice")
      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.submitInvoiceNumber("1234567890")
      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
      InvoiceDate.submitInvoiceDate("08", "12", "2025")
      SupplierName.verifyPageTitle(SupplierName.pageTitle)
      SupplierName.submitSupplierName("Test Supplier Name")
      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
      SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")
      SupplierTaxNumbers.verifyPageTitle(SupplierTaxNumbers.pageTitle)
      SupplierTaxNumbers.selectTaxNumber("Tax ID Number")
      SupplierTaxIDNumber.verifyPageTitle(SupplierTaxIDNumber.pageTitle)
      SupplierTaxIDNumber.submitSupplierTaxID("TID-1")
      TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
      TotalPurchaseAmount.submitTotalPurchaseAmount("1000.99")
      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
      TotalVatPaid.submitTotalVatPaid("200.99")
      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
      TotalVatClaim.submitTotalVatClaim("100.99")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      // Change Supplier tax identifier
      CheckYourPurchaseDetails.clickChangeLink("Supplier tax identifier")
      SupplierTaxIDNumber.verifyPageTitle(SupplierTaxIDNumber.pageTitle)
      SupplierTaxIDNumber.submitSupplierTaxID("12345")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      // Change Supplier tax numbers
      CheckYourPurchaseDetails.clickChangeLink("Supplier tax numbers")
      SupplierTaxNumbers.verifyPageTitle(SupplierTaxNumbers.pageTitle)
      SupplierTaxNumbers.selectTaxNumber("Vat Registration Number")
      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.submitVATRegistrationNumber("1234567890")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.saveAndContinue()
      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1"))
      PurchaseImportSummary.continueAsNo()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickSignOut
    }

    Scenario("03 - Change your answers for a refund request", Local) {
      Given("I login as an organisation")
      val sharedId = AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I start new EUVAT claim")
      //      Inject Claim details
      CacheHelper.submitUserAnswers("claimDetails.json", sharedId)
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I see claim details page completed")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/claim-details/check-your-answers")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add purchase details")
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

      //    Check your purchase details
      And("I change purchase details")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.saveAndContinue()
      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle) // ====
      PurchaseImportSummary.clickChangeLink(1)

      //      Change purchase type
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)
      ChangeYourAnswers.clickChangeLink("Purchase type")
      ChangePurchaseType.verifyPageTitle(ChangePurchaseType.pageTitle)
      ChangePurchaseType.selectPurchaseType("Luxuries, entertainment and hospitality")
      ChangePurchaseSubcodeLuxury.verifyPageTitle(ChangePurchaseSubcodeLuxury.pageTitle)
      ChangePurchaseSubcodeLuxury.selectLuxuryType("Receptions, entertainment and hospitality")
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change invoice type
      ChangeYourAnswers.clickChangeLink("Invoice type")
      ChangeInvoiceType.verifyPageTitle(ChangeInvoiceType.pageTitle)
      ChangeInvoiceType.selectInvoiceType("Simplified invoice")
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change invoice number
      ChangeYourAnswers.clickChangeLink("Invoice number")
      ChangeInvoiceNumber.verifyPageTitle(ChangeInvoiceNumber.pageTitle)
      ChangeInvoiceNumber.submitInvoiceNumber("XR123456789")
      ChangeVATRegistrationNumber.verifyPageTitle(ChangeVATRegistrationNumber.pageTitle)
      ChangeVATRegistrationNumber.submitVATRegistrationNumber("AA987654321")
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change invoice date
      ChangeYourAnswers.clickChangeLink("Invoice date")
      ChangeInvoiceDate.verifyPageTitle(ChangeInvoiceDate.pageTitle)
      ChangeInvoiceDate.submitInvoiceDate("01", "01", "2026")
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change supplier name
      ChangeYourAnswers.clickChangeLink("Supplier name")
      ChangeSupplierName.verifyPageTitle(ChangeSupplierName.pageTitle)
      ChangeSupplierName.submitSupplierName("Update Test Supplier Name")
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change supplier address
      ChangeYourAnswers.clickChangeLink("Supplier address")
      ChangeSupplierAddress.verifyPageTitle(ChangeSupplierAddress.pageTitle)
      ChangeSupplierAddress.submitSupplierAddress("Updated Street", "Updated City", "Updated Country")
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change supplier VAT registration check
      ChangeYourAnswers.clickChangeLink("Supplier VAT registration check")
      ChangeAddVATRegistration.verifyPageTitle(ChangeAddVATRegistration.pageTitle)
      ChangeAddVATRegistration.continueAsYes()
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change supplier VAT registration number
      ChangeYourAnswers.clickChangeLink("Supplier VAT registration number")
      ChangeVATRegistrationNumber.verifyPageTitle(ChangeVATRegistrationNumber.pageTitle)
      ChangeVATRegistrationNumber.submitVATRegistrationNumber("BB987654321")
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change currency
      ChangeYourAnswers.clickChangeLink("Currency")
      ChangeCurrency.verifyPageTitle(ChangeCurrency.pageTitle)
      ChangeCurrency.selectCurrencyType("Estonian Kroon (kr)")
      ChangeTotalPurchaseAmount.verifyPageTitle(ChangeTotalPurchaseAmount.pageTitle)
      ChangeTotalPurchaseAmount.submitTotalPurchaseAmount("1000.01")
      ChangeTotalVatPaid.verifyPageTitle(ChangeTotalVatPaid.pageTitle)
      ChangeTotalVatPaid.submitTotalVatPaid("3000.99")
      ChangeCheckVATAmount.verifyPageTitle(ChangeCheckVATAmount.pageTitle)
      ChangeCheckVATAmount.continue()
      ChangeTotalVatClaim.verifyPageTitle(ChangeTotalVatClaim.pageTitle)
      ChangeTotalVatClaim.submitTotalVatClaim("4000.99")
      ChangeCheckVATClaim.verifyPageTitle(ChangeCheckVATClaim.pageTitle)
      ChangeCheckVATClaim.continue()
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change VAT claim
      ChangeYourAnswers.clickChangeLink("VAT claim")
      ChangeTotalVatClaim.verifyPageTitle(ChangeTotalVatClaim.pageTitle)
      ChangeTotalVatClaim.submitTotalVatClaim("2000.99")
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change VAT paid
      ChangeYourAnswers.clickChangeLink("VAT paid")
      ChangeTotalVatPaid.verifyPageTitle(ChangeTotalVatPaid.pageTitle)
      ChangeTotalVatPaid.submitTotalVatPaid("1500.99")
      ChangeCheckVATAmount.verifyPageTitle(ChangeCheckVATAmount.pageTitle)
      ChangeCheckVATAmount.continue()
      ChangeTotalVatClaim.verifyPageTitle(ChangeTotalVatClaim.pageTitle)
      ChangeTotalVatClaim.submitTotalVatClaim("4100.99")
      ChangeCheckVATClaim.verifyPageTitle(ChangeCheckVATClaim.pageTitle)
      ChangeCheckVATClaim.continue()
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

      //      Change amount before VAT
      ChangeYourAnswers.clickChangeLink("Amount before VAT")
      ChangeTotalPurchaseAmount.verifyPageTitle(ChangeTotalPurchaseAmount.pageTitle)
      ChangeTotalPurchaseAmount.submitTotalPurchaseAmount("300.99")
      ChangeTotalVatPaid.verifyPageTitle(ChangeTotalVatPaid.pageTitle)
      ChangeTotalVatPaid.submitTotalVatPaid("5000.99")
      ChangeCheckVATAmount.verifyPageTitle(ChangeCheckVATAmount.pageTitle)
      ChangeCheckVATAmount.continue()
      ChangeTotalVatClaim.verifyPageTitle(ChangeTotalVatClaim.pageTitle)
      ChangeTotalVatClaim.submitTotalVatClaim("6000.99")
      ChangeCheckVATClaim.verifyPageTitle(ChangeCheckVATClaim.pageTitle)
      ChangeCheckVATClaim.continue()
      ChangeYourAnswers.verifyPageTitle(ChangeYourAnswers.pageTitle)

    }

  }
}
