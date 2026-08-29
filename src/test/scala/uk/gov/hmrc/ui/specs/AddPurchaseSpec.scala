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
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/check-your-claim-details")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      insertDuplicatePurchaseRecordVRN()

      And("I add purchase details")
      MakeEuvatClaim.clickLinkByText("Add a purchase")
      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.continue()
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Food, drink and restaurant services")
      FoodDrink.verifyPageTitle(FoodDrink.pageTitle)
      FoodDrink.selectFoodDrinkCostType("Food and drink from hotels")
      WhoFoodDrink.verifyPageTitle(WhoFoodDrink.pageTitle)
      WhoFoodDrink.selectWhoFoodDrinkFor("The taxable person")

      //      Invoice details
      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
      InvoiceType.selectInvoiceType("Standard invoice")
      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.submitInvoiceNumber("DUP")
      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
      InvoiceDate.submitInvoiceDate("08", "12", "2025")

      //      Supplier details
      SupplierName.verifyPageTitle(SupplierName.pageTitle)
      SupplierName.submitSupplierName("Test Supplier Name")
      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
      SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")
      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.submitVATRegistrationNumber("EE0000000111")
      CheckSupplierVRN.verifyPageTitle(CheckSupplierVRN.pageTitle)
      CheckSupplierVRN.clickLinkByText("Change invoice number")
      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.submitInvoiceNumber("DUP")
      CheckSupplierVRN.verifyPageTitle(CheckSupplierVRN.pageTitle)
      CheckSupplierVRN.clickLinkByText("Change supplier’s VAT registration number")
      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.submitVATRegistrationNumber("EE0000000111")
      CheckSupplierVRN.verifyPageTitle(CheckSupplierVRN.pageTitle)
      CheckSupplierVRN.continue()

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

      And("I change purchase details")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.clickChangeLink("Food and drink for")
      WhoFoodDrink.verifyPageTitle(WhoFoodDrink.pageTitle)
      WhoFoodDrink.selectWhoFoodDrinkFor("The taxable person")

      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.clickChangeLink("Food and drink cost type")
      FoodDrink.verifyPageTitle(FoodDrink.pageTitle)
      FoodDrink.selectFoodDrinkCostType("None")

      //      Change purchase type
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.clickChangeLink("Purchase type")
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Luxuries, entertainment and hospitality")
      LuxuryEntertainment.verifyPageTitle(LuxuryEntertainment.pageTitle)
      LuxuryEntertainment.selectLuxuryType("Receptions, entertainment and hospitality")
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
      CheckYourPurchaseDetails.clickSignOut
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
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/check-your-claim-details")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      insertDuplicatePurchaseRecordTID()

      And("I add purchase details")
      MakeEuvatClaim.clickLinkByText("Add a purchase")
      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.continue()
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Other")
      PurchaseTypeOther.verifyPageTitle(PurchaseTypeOther.pageTitle)
      PurchaseTypeOther.selectPurchaseTypeOther("None of these - give more details")
      InvoiceItemDescription.verifyPageTitle(InvoiceItemDescription.pageTitle)
      InvoiceItemDescription.submitItemDescription("")
      CheckPurchaseDetails.verifyPageTitle(CheckPurchaseDetails.pageTitle)
      CheckPurchaseDetails.continue()
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
      CheckSupplierTaxIDNumber.continue()
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
      SupplierTaxIDNumber.submitSupplierTaxID("TID-1")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

      // Change Supplier tax numbers
      CheckYourPurchaseDetails.clickChangeLink("Supplier tax numbers")
      SupplierTaxNumbers.verifyPageTitle(SupplierTaxNumbers.pageTitle)
      SupplierTaxNumbers.selectTaxNumber("Vat Registration Number")
      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.submitVATRegistrationNumber("1234567890")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.clickSignOut
    }
  }
}
