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
import uk.gov.hmrc.selenium.webdriver.{Browser, Driver, ScreenshotOnFailure}
import uk.gov.hmrc.ui.flows.*
import uk.gov.hmrc.ui.flows.TestData.sadRefData
import uk.gov.hmrc.ui.pages.*
import uk.gov.hmrc.ui.pages.claim.*
import uk.gov.hmrc.ui.pages.imports.*
import uk.gov.hmrc.ui.pages.purchase.*
import uk.gov.hmrc.ui.tags.*
import uk.gov.hmrc.ui.utils.{DatabaseHelper, MongoHelper}

class AddImportSpec
    extends AnyFeatureSpec
    with BaseSpec
    with GivenWhenThen
    with ShouldVerb
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Browser
    with ScreenshotOnFailure
    with MongoHelper
    with DatabaseHelper
    with ClaimFlows
    with PurchaseFlows
    with ImportFlows {

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropMongoCollections()
    cleanupDatabaseIfNotStub()
  }

  Feature("Make a new EUVAT claim - Add a duplicate import") {
    Scenario("01 - Create 2 imports with a duplicate SAD reference number", Local) {
      val sharedId = loginAndInjectClaimDetails("999900001")
      goToCheckYourClaimDetails()
      saveClaimDetails()

      startImportFlow()
      addFoodImportType()
      injectImportDetailsAfterType(sharedId)
      goToCheckYourImportDetails()
      saveImport()
      summaryFlowYes()

      addImportFlow()
      addFoodImportFlow()
      duplicateSADFlow(sadRefData)
      addImportCurrencyFlow()
      addImportAmountFlow()
      saveImport()
      summaryFlowNo()
      clickSignOut(Driver.instance)
    }

    Scenario("02 - Create 2 imports with duplicate import document details for Germany", Local, WIP) {
      val sharedId = loginAndInjectClaimDetailsGermany("999900001")
      goToCheckYourClaimDetails()
      saveClaimDetails()

      startImportFlow()
      addFoodImportType()
      injectImportDetailsAfterTypeGermany(sharedId) // needs updating
      goToCheckYourImportDetails()
      saveImport()
      summaryFlowYes()

      addImportFlow()
      addOtherImportFlow()
      //Jira ticket raised to fix stub to trigger the CheckImportDocumentDetails page
//      duplicateSADFlow(sadDocumentData)
//      addImportAmountFlow()
//      saveImport()
//      summaryFlowNo()
      clickSignOut(Driver.instance)
    }

    Scenario("03 - Change your answers for a Import refund request", Jenkins, Local) {
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
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add import details")
      MakeEuvatClaim.clickLinkByText("Purchases and imports")
      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.continue()
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
      SADRefNumber.verifyPageTitle(SADRefNumber.pageTitle)
      SADRefNumber.submitSADRefNumber("SAD123456789")
      ImportDate.verifyPageTitle(ImportDate.pageTitle)
      ImportDate.submitImportDate("01", "01", "2026")
      ImportSupplierName.verifyPageTitle(ImportSupplierName.pageTitle)
      ImportSupplierName.submitSupplierName("Test Supplier")
      ImportSupplierAddress.verifyPageTitle(ImportSupplierAddress.pageTitle)
      ImportSupplierAddress.submitImportSupplierAddress(
        "Test address one",
        "Test address two",
        "Test address three",
        "HEARD ISLAND & MCDONALD ISLANDS"
      )
      ImportCurrency.verifyPageTitle(ImportCurrency.pageTitle)
      ImportCurrency.selectImportCurrencyType("Euro")
      TotalImportAmount.verifyPageTitle(TotalImportAmount.pageTitle)
      TotalImportAmount.submitTotalImportAmount("1000.01")
      TotalImportVatPaid.verifyPageTitle(TotalImportVatPaid.pageTitle)
      TotalImportVatPaid.submitTotalImportVATPaid("2000.01")
      CheckImportVATAmount.verifyPageTitle(CheckImportVATAmount.pageTitle)
      CheckImportVATAmount.continue()
      TotalImportVatClaim.verifyPageTitle(TotalImportVatClaim.pageTitle)
      TotalImportVatClaim.submitTotalImportVatClaim("2500.01")
      CheckImportVATClaim.verifyPageTitle(CheckImportVATClaim.pageTitle)
      CheckImportVATClaim.continue()
      CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
      CheckYourImportDetails.saveAndContinue()

//      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1")) // Only works for the database
      PurchaseImportSummary.clickActionLink(
        2,
        "Change"
      ) // Does not work for the database, but works for the stubbed version. Change the number 2 to 1 to make it work for the DB

      And("I change import details")

      //      Change import type
      ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)
      ChangeYourImportDetails.clickChangeLink("Import type")
      ImportType.verifyPageTitle(ImportType.pageTitle)
      ImportType.selectImportType("Luxuries, entertainment and hospitality")
      ImportSubcodeLuxury.verifyPageTitle(ImportSubcodeLuxury.pageTitle)
      ImportSubcodeLuxury.selectLuxuryType("Receptions, entertainment and hospitality")

      //      Change import doc details
      ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)
      ChangeYourImportDetails.clickChangeLink("Single Administrative Document (SAD)")
      AddSADRefNumber.verifyPageTitle(AddSADRefNumber.pageTitle)
      AddSADRefNumber.continueAsNo()
      ImportDocumentDetails.verifyPageTitle(ImportDocumentDetails.pageTitle)
      ImportDocumentDetails.submitDocumentDetails("TEST INPUT")

      //      Change invoice date
      ChangeYourImportDetails.clickChangeLink("Import date")
      ImportDate.verifyPageTitle(ImportDate.pageTitle)
      ImportDate.submitImportDate("01", "01", "2026")
      ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)

      //      Change supplier name
      ChangeYourImportDetails.clickChangeLink("Supplier name")
      ImportSupplierName.verifyPageTitle(ImportSupplierName.pageTitle)
      ImportSupplierName.submitSupplierName("Update Test Supplier Name")
      ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)

      //      Change supplier address
      ChangeYourImportDetails.clickChangeLink("Supplier address")
      ImportSupplierAddress.verifyPageTitle(ImportSupplierAddress.pageTitle)
      ImportSupplierAddress.submitImportSupplierAddress(
        "Test Street ABC",
        "Test City 321",
        "Test State XYZ",
        "HEARD ISLAND & MCDONALD ISLANDS"
      )
      ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)

      //      Change currency
      ChangeYourImportDetails.clickChangeLink("Currency")
      ImportCurrency.verifyPageTitle(ImportCurrency.pageTitle)
      ImportCurrency.selectImportCurrencyType("Estonian Kroon (kr)")
      TotalImportAmount.verifyPageTitle(TotalImportAmount.pageTitle)
      TotalImportAmount.submitTotalImportAmount("1000.01")

      //      Change VAT paid
      TotalImportVatPaid.verifyPageTitle(TotalImportVatPaid.pageTitle)
      TotalImportVatPaid.submitTotalImportVATPaid("3000.99")
      CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
      CheckVATAmount.continue()

      //      Change VAT claim
      TotalImportVatClaim.verifyPageTitle(TotalImportVatClaim.pageTitle)
      TotalImportVatClaim.submitTotalImportVatClaim("4000.99")
      CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
      CheckVATClaim.continue()

      ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)
      ChangeYourImportDetails.saveAndContinue()
      clickSignOut(Driver.instance)
    }

  }
}
