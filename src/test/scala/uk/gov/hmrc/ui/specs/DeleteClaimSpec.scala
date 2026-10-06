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
  }

  Feature("Delete a draft EUVAT claim - Delete claim") {

    Scenario("01 - Delete Claim details from Check your claim details page", Local) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I start new EUVAT claim")
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Add claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Czech Republic")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriodUsingCurrentYear("02", "04")
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
      ClaimDetails.clickChangeLink("EU member state")
      EUMemberStateDetails.verifyPageTitle(EUMemberStateDetails.pageTitle)
      EUMemberStateDetails.continueAsYes()
      ClaimAnEUVATRefund.clickSignOut
    }

    Scenario("02 - Delete a full refund claim from Make a claim for an EU VAT refund page", Local) {
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

      And("I add purchase details")
      MakeEuvatClaim.clickLinkByText("Add a purchase")
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
      VATRegistrationNumber.submitVATRegistrationNumber("CZ1234567890")

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

      And("I delete the claim")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("Delete this claim")
      DeleteClaim.verifyPageTitle(DeleteClaim.pageTitle)
      DeleteClaim.continueAsYes()
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
      ClaimAnEUVATRefund.clickSignOut
    }
  }
}
