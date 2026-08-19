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
    Scenario("01 - Submit a refund request", Local, WIP) {
      Given("I login as an organisation")
      val sharedId = AuthorityWizard.login("Organisation", "999900001")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

      When("I start new EUVAT claim")
//      Inject Claim details
      CacheHelper.submitUserAnswers("claimDetails.json", sharedId)
      ManageYourEuvatClaim.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I see claim details page completed")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/check-your-claim-details")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

      CheckYourClaimDetails.saveAndContinue()

      And("I add purchase details")
      MakeEuvatClaim.clickLinkByText("Add a purchase")
      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.continue()
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Luxuries, entertainment and hospitality")
      LuxuryEntertainment.verifyPageTitle(LuxuryEntertainment.pageTitle)
      LuxuryEntertainment.selectLuxuryType("Receptions, entertainment and hospitality")
      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
      InvoiceType.selectInvoiceType("Standard invoice")
      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.submitInvoiceNumber("Test_Invoice_123.5")
      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
      InvoiceDate.submitInvoiceDate("08", "12", "2025")
      SupplierName.verifyPageTitle(SupplierName.pageTitle)
      SupplierName.submitSupplierName("Test Supplier Name")
      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
      SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")
      AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
      AddVATRegistration.continueAsYes()
      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.submitInvoiceNumber("AB1234567890")
      Currency.verifyPageTitle(Currency.pageTitle)
      Currency.selectCurrencyType("Euro")
      TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
      TotalPurchaseAmount.submitTotalPurchaseAmount("100")
      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
      TotalVatPaid.submitTotalVatPaid("200")
      CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
      CheckVATAmount.continueAsYes()
      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
      TotalVatClaim.submitTotalVatClaim("210")
      CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
      CheckVATClaim.continueAsYes()
      MakeEuvatClaim.clickSignOut
    }
  }

  Scenario("02 - Submit a refund request for Germany", Local) {
    Given("I login as an organisation")
    val sharedId = AuthorityWizard.login("Organisation", "999900001")
    ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

    When("I start new EUVAT claim")
    //      Inject Claim details
    CacheHelper.submitUserAnswers("claimDetailsGermany.json", sharedId)
    ManageYourEuvatClaim.clickLinkByText("Make a claim for an EU VAT refund")
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

    And("I see claim details page completed")
    MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/check-your-claim-details")
    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

    CheckYourClaimDetails.saveAndContinue()

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
    InvoiceNumber.submitInvoiceNumber("INV-1")
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
    CheckSupplierTaxIDNumber.verifyPageTitle(CheckSupplierTaxIDNumber.pageTitle)
    CheckSupplierTaxIDNumber.clickLinkByText("Change invoice number")
    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("INV-1")
    CheckSupplierTaxIDNumber.verifyPageTitle(CheckSupplierTaxIDNumber.pageTitle)
    CheckSupplierTaxIDNumber.clickLinkByText("Change supplier’s tax identifier number")
    SupplierTaxIDNumber.verifyPageTitle(SupplierTaxIDNumber.pageTitle)
    SupplierTaxIDNumber.submitSupplierTaxID("TID-1")
    CheckSupplierTaxIDNumber.verifyPageTitle(CheckSupplierTaxIDNumber.pageTitle)
    CheckSupplierTaxIDNumber.continueAsYes()
    TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
    TotalPurchaseAmount.submitTotalPurchaseAmount("100")
    TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalVatPaid.submitTotalVatPaid("200")
    CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
    CheckVATAmount.continueAsYes()
    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("210")
    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continueAsYes()
    MakeEuvatClaim.clickSignOut
  }

}
