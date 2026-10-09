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

      And("I see the Check your claim details page completed")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/check-your-claim-details")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.saveAndContinue()

      Then("I return to the Claim details page to delete the claim")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("View claim details")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)
      ClaimDetails.clickChangeLink("EU member state")
      EUMemberStateDetails.verifyPageTitle(EUMemberStateDetails.pageTitle)
      EUMemberStateDetails.continueAsYes()
      ClaimAnEUVATRefund.clickSignOut
    }

    Scenario("02 - Delete a full refund claim from Make a claim for an EU VAT refund page", Local, WIP) {
      Given("I login as an organisation")
      val sharedId = AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I inject Claim details")
      CacheHelper.submitUserAnswers("claimDetails.json", sharedId)
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I see the Check your claim details page completed")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/check-your-claim-details")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.saveAndContinue()

      And("I inject purchase details into the existing session")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      CacheHelper.updateUserAnswers("purchaseDetails.json", sharedId)

      And("I see the Check your purchase details page completed")
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/purchase/check-your-purchase-details")
      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.saveAndContinue()

      Then("I delete the claim")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("Delete this claim")
      DeleteClaim.verifyPageTitle(DeleteClaim.pageTitle)
      DeleteClaim.continueAsYes()
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
      ClaimAnEUVATRefund.clickSignOut
    }
  }
}
