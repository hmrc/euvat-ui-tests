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
import uk.gov.hmrc.ui.utils.{DatabaseHelper, MongoHelper}

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
    cleanupDatabaseIfNotStub()
  }

  Feature("Error and warning message validation check - New claim") {

    Scenario("01 - Refund period start and end date validation", Local, Error) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900002")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I start new EUVAT claim")
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I check refund period validation")
      MakeEuvatClaim.clickLinkByText("Claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Croatia")
      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)

      RefundPeriod.submitRefundPeriodUsingCurrentYear("05", "04")
      RefundPeriod.errorSummaryDisplayed("Refund period start date must be earlier than the refund period end date")
      RefundPeriod.errorMessageDisplayed("Refund period start date must be earlier than the refund period end date")

      RefundPeriod.submitRefundPeriodUsingCurrentYear("01", "02")
      RefundPeriod.errorSummaryDisplayed(
        "Refund period must be at least 3 months long unless the period ends in December"
      )
      RefundPeriod.errorMessageDisplayed(
        "Refund period must be at least 3 months long unless the period ends in December"
      )

      RefundPeriod.submitRefundPeriodUsingStartLastYear("01", "02")
      RefundPeriod.errorSummaryDisplayed("Refund period start date and end date must be in the same calendar year")
      RefundPeriod.errorMessageDisplayed("Refund period start date and end date must be in the same calendar year")

      RefundPeriod.submitRefundPeriodUsingTwoYearsAgo("01", "03")
      if (RefundPeriod.isOnOrBefore30September()) {
        CheckRefundStartDate.verifyPageTitle(CheckRefundStartDate.pageTitle)
        CheckRefundStartDate.textDisplayed(
          RefundPeriod.checkRefundStartDateMessageUsingTwoYearsAgo("01")
        )
        CheckRefundStartDate.clickLinkByText("No, change the start date")
      } else {
        CheckRefundStartDate.verifyPageTitle(CheckRefundStartDate.pageTitle)
        CheckRefundStartDate.textDisplayed(
          RefundPeriod.checkRefundStartDateMessageUsingTwoYearsAgoAndCurrentYear("01")
        )
        CheckRefundStartDate.clickLinkByText("No, change the start date")
      }

      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriod("01", "2023", "03", "2023")
      RefundPeriod.errorSummaryDisplayed(
        "Refund period start date must be after the VAT registration date if you registered for VAT during the first quarter"
      )
      RefundPeriod.errorMessageDisplayed(
        "Refund period start date must be after the VAT registration date if you registered for VAT during the first quarter"
      )

      RefundPeriod.submitRefundPeriodUsingCurrentYear("06", "11")
      CheckRefundEndDate.verifyPageTitle(CheckRefundEndDate.pageTitle)
      CheckRefundEndDate.textDisplayed(
        RefundPeriod.checkRefundEndDateMessageUsingCurrentYear("11")
      )
      CheckRefundEndDate.continue()
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.clickSignOut
    }

    Scenario("02 - Validate a duplicate draft refund from the EU member state page", Local, Error) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900003")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I start new EUVAT claim")
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Austria")
      EUMemberState.errorSummaryDisplayed("You cannot have more than one draft claim for each EU member state")
      EUMemberState.errorMessageDisplayed("You cannot have more than one draft claim for each EU member state")
      EUMemberState.clickSignOut
    }

    Scenario("03 - Validate VAT registration start and end date", Local, Error) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900004")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

      When("I start new EUVAT claim")
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

      And("I verify VAT registration date")
      MakeEuvatClaim.clickLinkByText("Claim details")
      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.selectCountry("Croatia")

      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.submitRefundPeriodUsingCurrentYear("01", "07")
      RefundPeriod.errorSummaryDisplayed(
        "Refund period start date must be within three months before or anytime after the VAT registration date if you did not register for VAT during the first quarter"
      )
      RefundPeriod.errorMessageDisplayed(
        "Refund period start date must be within three months before or anytime after the VAT registration date if you did not register for VAT during the first quarter"
      )

      RefundPeriod.submitRefundPeriodUsingCurrentYear("05", "08")
      RefundPeriod.errorSummaryDisplayed(
        "Refund period end date must not be after the VAT deregistration date"
      )
      RefundPeriod.errorMessageDisplayed(
        "Refund period end date must not be after the VAT deregistration date"
      )

      RefundPeriod.submitRefundPeriodUsingCurrentYear("02", "06")
      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.clickSignOut
    }
  }
}
