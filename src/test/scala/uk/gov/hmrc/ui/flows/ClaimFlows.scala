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

package uk.gov.hmrc.ui.flows

import org.openqa.selenium.WebDriver
import uk.gov.hmrc.ui.pages.*
import uk.gov.hmrc.ui.pages.claim.*

trait ClaimFlows {

  def loginAndOpenNewClaim(): Unit = {
    AuthorityWizard.login("Organisation", "999900001")
    ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
    ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
  }

  def addClaimDetails(data: ClaimData, addBusinessActivity: Boolean = false): Unit = {
    MakeEuvatClaim.clickLinkByText("Add claim details")
    EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
    EUMemberState.selectCountry(data.country)

    data.language.foreach { lang =>
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage(lang)
    }

    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
    RefundPeriod.submitRefundPeriod(data.fromMonth, data.fromYear, data.toMonth, data.toYear)

    ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
    ContactDetails.submitContactAddress(data.email, data.phone)

    AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
    if (addBusinessActivity) AddBusinessActivity.continueAsYes()
    else AddBusinessActivity.continueAsNo()
  }

  def saveClaimDetails(): Unit = {
    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
    CheckYourClaimDetails.saveAndContinue()
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
  }

  def signOut(webDriver: WebDriver): Unit =
    MakeEuvatClaim.clickSignOut(webDriver)

  def editClaimDetailsJourney(): Unit = {
    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

    CheckYourClaimDetails.clickChangeLink("Refunding EU member state")
    EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
    EUMemberState.selectCountry("Estonia")
    Language.verifyPageTitle(Language.pageTitle)
    Language.selectLanguage("English")
    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
    RefundPeriod.submitRefundPeriod("05", "2025", "07", "2025")
    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

    CheckYourClaimDetails.clickChangeLink("Claim language")
    Language.verifyPageTitle(Language.pageTitle)
    Language.selectLanguage("Estonian")
    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

    CheckYourClaimDetails.clickChangeLink("End date")
    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
    RefundPeriod.submitRefundPeriod("05", "2025", "10", "2025")
    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

    CheckYourClaimDetails.clickChangeLink("Start date")
    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
    RefundPeriod.submitRefundPeriod("08", "2025", "10", "2025")
    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

    CheckYourClaimDetails.clickChangeLink("Email")
    ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
    ContactDetails.submitContactAddress("changetest@gmail.com", "9876543210")
    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

    CheckYourClaimDetails.clickChangeLink("Phone number")
    ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
    ContactDetails.submitContactAddress("changetest@gmail.com", "+449876543210")
    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)

    CheckYourClaimDetails.clickChangeLink("First SIC code")
    AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
    AddBusinessActivity.continueAsYes()

    SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
    SecondBusinessActivity.enterSecondBusinessActivityCode("4711")
    AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
    AddSecondBusinessActivity.clickLink("Change Second SIC code")
    SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
    SecondBusinessActivity.enterSecondBusinessActivityCode("1101")
    AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
    AddSecondBusinessActivity.clickLink("Remove Second SIC code")
    RemoveSecondBusinessActivity.verifyPageTitle(RemoveSecondBusinessActivity.pageTitle)
    RemoveSecondBusinessActivity.continueAsYes()

    AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
    AddBusinessActivity.continueAsYes()
    SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
    SecondBusinessActivity.enterSecondBusinessActivityCode("4532")
    AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
    AddSecondBusinessActivity.continueAsYes()

    ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
    ThirdBusinessActivity.enterThirdBusinessActivityCode("2534")
    AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
    AddThirdBusinessActivity.clickLink("Change Third SIC code")
    ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
    ThirdBusinessActivity.enterThirdBusinessActivityCode("4533")
    AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
    AddThirdBusinessActivity.clickLink("Remove Third SIC code")
    RemoveThirdBusinessActivity.verifyPageTitle(RemoveThirdBusinessActivity.pageTitle)
    RemoveThirdBusinessActivity.continueAsYes()

    AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
    AddSecondBusinessActivity.continueAsYes()
    ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
    ThirdBusinessActivity.enterThirdBusinessActivityCode("4712")
    AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
    AddThirdBusinessActivity.continue()
  }
}
