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
import uk.gov.hmrc.ui.pages.imports.{AddSADRefNumber, ImportCurrency, ImportDate, ImportSupplierAddress, ImportType, SubmitSADRefNumber, SubmitSupplierName, TotalImportAmount, TotalImportVATPaid, TotalImportVatClaim}
import uk.gov.hmrc.ui.pages.purchase.*
import uk.gov.hmrc.ui.tags.*
import uk.gov.hmrc.ui.utils.{CacheHelper, DatabaseHelper, MongoHelper}

class AddPurchaseAndImportSpec
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

  Feature("Make a new EUVAT claim - Add both purchase and import") {

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
      PurchaseType.selectPurchaseType("Luxuries, entertainment and hospitality")
      PurchaseSubcodeLuxury.verifyPageTitle(PurchaseSubcodeLuxury.pageTitle)
      PurchaseSubcodeLuxury.selectLuxuryType("Receptions, entertainment and hospitality")

      //      Invoice details
      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
      InvoiceType.selectInvoiceType("Standard invoice")
      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.submitInvoiceNumber("1234567890")
      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
      InvoiceDate.submitInvoiceDate("01", "05", "2026")

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
      TotalPurchaseAmount.submitTotalPurchaseAmount("123.45")
      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
      TotalVatPaid.submitTotalVatPaid("67.89")
      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
      TotalVatClaim.submitTotalVatClaim("9.87")

      //    Check your purchase details
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.saveAndContinue()
      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1"))

      //    Add another item - Import
      And("I add import details")
      PurchaseImportSummary.continueAsYes()
      AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
      AddPurchaseImport.selectPurchaseOrImport("Import")

      //      Invoice details
      ImportType.verifyPageTitle(ImportType.pageTitle)
      ImportType.selectImportType("Fuel")

      //      SAD details
      AddSADRefNumber.verifyPageTitle(AddSADRefNumber.pageTitle)
      AddSADRefNumber.continueAsYes()
      SubmitSADRefNumber.verifyPageTitle(SubmitSADRefNumber.pageTitle)
      SubmitSADRefNumber.submitSADRefNumber("43210")

      //      Import date
      ImportDate.verifyPageTitle(ImportDate.pageTitle)
      ImportDate.submitImportDate("13", "07", "2026")

      //      Supplier details
      SubmitSupplierName.verifyPageTitle(SubmitSupplierName.pageTitle)
      SubmitSupplierName.submitSupplierName("Test Supplier")
      ImportSupplierAddress.verifyPageTitle(ImportSupplierAddress.pageTitle)
      ImportSupplierAddress.submitImportSupplierAddress(
        "Test Street ABC 1",
        "",
        "Test State GHI 3",
        "BARBADOS"
      )
      ImportCurrency.verifyPageTitle(ImportCurrency.pageTitle)
      ImportCurrency.selectImportCurrencyType("Estonian Kroon (kr)")
      TotalImportAmount.verifyPageTitle(TotalImportAmount.pageTitle)
      TotalImportAmount.submitTotalImportAmount("1234.56")
      TotalImportVATPaid.verifyPageTitle(TotalImportVATPaid.pageTitle)
      TotalImportVATPaid.submitTotalImportVATPaid("789.01")
      TotalImportVatClaim.verifyPageTitle(TotalImportVatClaim.pageTitle)
      TotalImportVatClaim.submitTotalImportVatClaim("234.98")
      //    Check your import details
//      CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
//      CheckYourImportDetails.saveAndContinue()
//      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("2"))
      TotalImportVatClaim.clickSignOut
    }
  }
}
