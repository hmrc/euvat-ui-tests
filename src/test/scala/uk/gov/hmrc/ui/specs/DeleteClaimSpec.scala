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
import uk.gov.hmrc.ui.pages.imports.{CheckYourImportDetails, ImportType}
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
    cleanupDatabaseIfNotStub()
  }

  Feature("Delete a draft EUVAT claim - Delete claim") {

    Scenario("01 - Delete Claim details from Check your claim details page", Local) {
      Given("I login as an organisation")
      val sharedId = AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I inject Claim details")
      CacheHelper.submitUserAnswers("claimDetails.json", sharedId)
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I see claim details page completed")
      MakeEuvatClaim.navigateToPageWithRetry(
        "http://localhost:18501/file-eu-vat/claim-details/check-your-answers",
        CheckYourClaimDetails.pageTitle
      )
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

    Scenario("02 - Delete a full refund claim from Make a claim for an EU VAT refund page", Local) {
      Given("I login as an organisation")
      val sharedId = AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I inject claim details")
      CacheHelper.submitUserAnswers("claimDetails.json", sharedId)
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I see claim details page completed")
      MakeEuvatClaim.navigateToPageWithRetry(
        "http://localhost:18501/file-eu-vat/claim-details/check-your-answers",
        CheckYourClaimDetails.pageTitle
      )
      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I create the purchase record through the UI")
      MakeEuvatClaim.clickLinkByText("Purchases and imports")
      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.continue()
      AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
      AddPurchaseImport.selectPurchaseOrImport("Purchase")
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Food, drink and restaurant services")

      And("I inject the remaining purchase details")
      CacheHelper.updateUserAnswers("purchaseDetailsAfterType.json", sharedId)

      And("I go to check your purchase details")
      MakeEuvatClaim.navigateToPageWithRetry(
        "http://localhost:18501/file-eu-vat/purchase/check-your-answers",
        CheckYourPurchaseDetails.pageTitle
      )
      CheckYourPurchaseDetails.saveAndContinue()
//      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1")) // Only works for the database
      PurchaseImportSummary.continueAsYes()

      AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
      AddPurchaseImport.selectPurchaseOrImport("Import")
      ImportType.verifyPageTitle(ImportType.pageTitle)
      ImportType.selectImportType("Food, drink and restaurant services")

      And("I inject the remaining import details")
      CacheHelper.updateUserAnswers("importDetailsAfterType.json", sharedId)

      And("I go to check your import details")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/import/check-your-answers")
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

    Scenario("03 - Delete a purchase and import from the Purchase & Import Summary page", Local) {
      Given("I login as an organisation")
      val sharedId = AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I inject claim details")
      CacheHelper.submitUserAnswers("claimDetails.json", sharedId)

      And("I start the claim")
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I save the injected claim details")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/claim-details/check-your-answers")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I create the purchase record through the UI")
      MakeEuvatClaim.clickLinkByText("Purchases and imports")
      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.continue()
      AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
      AddPurchaseImport.selectPurchaseOrImport("Purchase")
      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.selectPurchaseType("Food, drink and restaurant services")

      And("I inject the remaining purchase details")
      CacheHelper.updateUserAnswers("purchaseDetailsAfterType.json", sharedId)

      And("I go to check your purchase details")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/purchase/check-your-answers")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.saveAndContinue()
//      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1")) // Only works for the database
      PurchaseImportSummary.continueAsYes()

      AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
      AddPurchaseImport.selectPurchaseOrImport("Import")
      ImportType.verifyPageTitle(ImportType.pageTitle)
      ImportType.selectImportType("Food, drink and restaurant services")

      And("I inject the remaining import details")
      CacheHelper.updateUserAnswers("importDetailsAfterType.json", sharedId)

      And("I go to check your import details")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/import/check-your-answers")
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
