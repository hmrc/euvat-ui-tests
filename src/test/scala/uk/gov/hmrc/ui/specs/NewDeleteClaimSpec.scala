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
import uk.gov.hmrc.ui.pages.ClaimAnEUVATRefund
import uk.gov.hmrc.ui.pages.claim.*
import uk.gov.hmrc.ui.tags.*
import uk.gov.hmrc.ui.utils.{DatabaseHelper, MongoHelper}

class NewDeleteClaimSpec
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
    with ClaimFlows {

  import TestData.*

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropMongoCollections()
    cleanupDatabaseIfNotStub()
  }

  Feature("Delete a draft EUVAT claim - Delete claim") {

    Scenario("01 - Delete a refund claim from Claim details page", Local) {
      Given("I login and start a new claim")
      loginAndOpenNewClaim()

      When("I add claim details")
      addClaimDetails(croatiaClaim)

      And("I save claim details")
      saveClaimDetails()

      And("I delete the claim from Claim details")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("View claim details")
      ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)
      ClaimDetails.clickChangeLink("EU member state")
      EUMemberStateDetails.verifyPageTitle(EUMemberStateDetails.pageTitle)
      EUMemberStateDetails.continueAsYes()

      Then("I sign out")
      signOut(Driver.instance)
    }

    Scenario("02 - Delete a refund claim from Make a claim for an EU VAT refund page", Local) {
      Given("I login and start a new claim")
      loginAndOpenNewClaim()

      When("I add claim details")
      addClaimDetails(croatiaClaim)

      And("I save claim details")
      saveClaimDetails()

      And("I delete the claim from the claim dashboard")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("Delete this claim")
      DeleteClaim.verifyPageTitle(DeleteClaim.pageTitle)
      DeleteClaim.continueAsYes()

      Then("I return to the claim landing page and sign out")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
      signOut(Driver.instance)
    }
  }
}
