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

class UpdateDatabaseSpec
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

      And("I reopen claim details and update them again after database save")
      MakeEuvatClaim.clickLinkByText("View claim details")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

      ClaimDetails.clickChangeLink("Claim language")
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("English")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

      ClaimDetails.clickChangeLink("End date")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriodUsingCurrentYear("06", "09")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

      ClaimDetails.clickChangeLink("Start date")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriodUsingCurrentYear("07", "09")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

      ClaimDetails.clickChangeLink("Email")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactDetails("secondchange@gmail.com", "+441234567890")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

      ClaimDetails.clickChangeLink("Phone number")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactDetails("secondchange@gmail.com", "+449999999999")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

      ClaimDetails.clickChangeLink("View first SIC code")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.clickLink("Change Third SIC code")
      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.enterThirdBusinessActivityCode("3333")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)

      AddThirdBusinessActivity.clickLink("Change Second SIC code")
      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.enterSecondBusinessActivityCode("2222")
      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.continue()
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

      ClaimDetails.saveAndContinue()
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
      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/check-your-claim-details")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I reopen claim details and update them again after database save")
      MakeEuvatClaim.clickLinkByText("View claim details")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

      ClaimDetails.clickChangeLink("Phone number")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.submitContactDetails("germany-update@gmail.com", "")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

      ClaimDetails.clickChangeLink("View first SIC code")
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
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

      ClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      MakeEuvatClaim.clickSignOut
    }
  }
}
