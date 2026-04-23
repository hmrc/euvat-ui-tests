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
import uk.gov.hmrc.ui.tags.*

class NewEuvatClaimSpec
    extends AnyFeatureSpec
    with BaseSpec
    with GivenWhenThen
    with ShouldVerb
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Browser
    with ScreenshotOnFailure {

  Feature("Make an EUVAT Claim - Adding New Claim") {
    Scenario("Sign in to Landing page", Local) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "123456")
      When("User confirm from the landing page and moving further to create new claim")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)
      And("User select to create new EUVAT claim")
      ManageYourEuvatClaim.clickLink("Make a new EU VAT claim")
//      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
//      And("User start adding the claim details")
//      MakeEuvatClaim.clickLink("Add claim details")
//      SelectEUMemberState.verifyPageTitle(SelectEUMemberState.pageTitle)
//      And("User select EU country and add refund period details")
//      SelectEUMemberState.selectCountry("France")
//      WhatRefundPeriod.verifyPageTitle(WhatRefundPeriod.pageTitle)
//      WhatRefundPeriod.submitRefundPeriod("03", "2025", "03", "2026")
//      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
//      And("User add contact address details")
//      ContactDetails.submitContactAddress("Test@gmail.com", "First Test Name", "Last Test Name", "9876543210")
    }

  }
}
