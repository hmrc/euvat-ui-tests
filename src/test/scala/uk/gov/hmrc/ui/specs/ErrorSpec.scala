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
import uk.gov.hmrc.ui.tags.*
import uk.gov.hmrc.ui.utils.{CacheHelper, DatabaseHelper, MongoHelper}

class ErrorSpec
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
//Business rule to trigger validation if the application status is draft or the submission status is null.
//The real rule will become application status is draft AND the submission status is null.
//then is the application status is draft AND the submission status is Submitted,
//then the validation check is to see if the refund period overlaps
  Feature("01 - Validate a single draft claim for each EU member state - Error checking") {

    Scenario("Validate a draft refund request with a submission status of Submitted", Error) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900002")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

      When("I start new EUVAT claim")
      ManageYourEuvatClaim.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Add claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      //      Submission status 'S'
      EUMemberState.selectCountry("Poland")
      EUMemberState.errorSummaryDisplayed("You cannot have more than one draft claim for each EU member state")
      EUMemberState.errorMessageDisplayed("You cannot have more than one draft claim for each EU member state")
      EUMemberState.clickSignOut
    }

    Scenario("02 - Validate a draft refund request that has not been submitted", Error) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900002")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

      When("I start new EUVAT claim")
      ManageYourEuvatClaim.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Add claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      //      Submission status 'null'
      EUMemberState.selectCountry("Greece")
      EUMemberState.errorSummaryDisplayed("You cannot have more than one draft claim for each EU member state")
      EUMemberState.errorMessageDisplayed("You cannot have more than one draft claim for each EU member state")
      EUMemberState.clickSignOut
    }

    Scenario("03 - Validate a draft refund from the Check your claim details page", Error, WIP) {
      Given("I login as an organisation")
      val sharedId = AuthorityWizard.login("Organisation", "999900002")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

      When("I start new EUVAT claim")
      CacheHelper.submitUserAnswers("claimDetailsGermany.json", sharedId)
      ManageYourEuvatClaim.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/check-your-claim-details")

      And("I see Check your claim details page completed")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

      CheckYourClaimDetails.saveAndContinue()

      SystemError.verifyPageTitle(SystemError.pageTitle)
      SystemError.clickSignOut
    }
  }
}
