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

import uk.gov.hmrc.ui.pages.*
import uk.gov.hmrc.ui.pages.claim.*
import uk.gov.hmrc.ui.utils.{CacheHelper, FlowLogging}

trait ClaimFlows extends FlowLogging {

  def loginAndOpenClaim(vrn: String): Unit = {
    logStep("I login and open a claim")
    AuthorityWizard.login("Organisation", vrn)
    ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
    ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
  }

  def addClaimDetails(data: ClaimData): Unit = {
    logStep("I add claim details")
    MakeEuvatClaim.clickLinkByText("Claim details")
    EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
    EUMemberState.selectCountry(data.country)

    data.language.foreach { lang =>
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage(lang)
    }

    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
    RefundPeriod.submitRefundPeriodUsingDynamicValidYear(data.fromMonth, data.toMonth)

    ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
    ContactDetails.submitContactDetails(data.email, data.phone)

    AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)

    data.businessActivity match {
      case Some(activity) =>
        AddBusinessActivity.continueAsYes()
        addBusinessActivityDetails(activity)
      case None           =>
        AddBusinessActivity.continueAsNo()
    }
  }

  def addBusinessActivityDetails(activity: BusinessActivityData): Unit = {
    logStep("I add business activity details")
    SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
    SecondBusinessActivity.enterSecondBusinessActivityCode(activity.secondSicCode)

    AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
    AddSecondBusinessActivity.continueAsYes()

    ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
    ThirdBusinessActivity.enterThirdBusinessActivityCode(activity.thirdSicCode)

    AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
    AddThirdBusinessActivity.continue()
  }

  def changeEUMemberState(newCountry: String): Unit = {
    logStep("I change EU member state")
    CheckYourClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)
    CheckYourClaimDetails.clickChangeLink("EU member state")
    EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
    EUMemberState.selectCountry(newCountry)
    Language.verifyPageTitle(Language.pageTitle)
    Language.selectLanguage("Estonian")
    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
    RefundPeriod.submitRefundPeriodUsingDynamicValidYear("02", "04")
  }

  def saveClaimDetails(): Unit = {
    logStep("I save claim details")
    CheckYourClaimDetails.saveAndContinue()
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
  }

  def changeClaimDetailsAfterSave(): Unit = {
    logStep("I change claim details after saving to database")
    MakeEuvatClaim.clickLinkByText("Claim details")
    ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

    //      Change language
    ClaimDetails.clickChangeLink("Claim language")
    Language.verifyPageTitle(Language.pageTitle)
    Language.selectLanguage("English")
    ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

    //      Change refund period
    ClaimDetails.clickChangeLink("End date")
    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
    RefundPeriod.submitRefundPeriodUsingDynamicValidYear("05", "07")
    ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

    ClaimDetails.clickChangeLink("Start date")
    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
    RefundPeriod.submitRefundPeriodUsingDynamicValidYear("07", "09")
    ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

    //      Change contact details
    ClaimDetails.clickChangeLink("Email")
    ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
    ContactDetails.submitContactDetails("firstchange@gmail.com", "+441234567890")
    ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

    ClaimDetails.clickChangeLink("Phone number")
    ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
    ContactDetails.submitContactDetails("secondchange@gmail.com", "+449999999999")
    ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

    //      Change SIC Codes
    ClaimDetails.clickChangeLink("View first SIC code")
    AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
    AddThirdBusinessActivity.clickLink("Change Third SIC code")
    ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
    ThirdBusinessActivity.enterThirdBusinessActivityCode("5555")
    AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)

    AddThirdBusinessActivity.clickLink("Change Second SIC code")
    SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
    SecondBusinessActivity.enterSecondBusinessActivityCode("4444")
    AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
    AddThirdBusinessActivity.continue()
    ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)
  }

  def changeClaimDetailsAddSicCodesAfterSave(): Unit = {
    logStep("I add business activities after saving to database")
    MakeEuvatClaim.clickLinkByText("Claim details")
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
  }

  def changeClaimDetailsRemoveSicCodesAfterSave(): Unit = {
    logStep("I remove business activities after saving to database")
    MakeEuvatClaim.clickLinkByText("Claim details")
    ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)

    ClaimDetails.clickChangeLink("View first SIC code")
    AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)

    AddThirdBusinessActivity.clickLink("Remove Third SIC code")
    RemoveThirdBusinessActivity.verifyPageTitle(RemoveThirdBusinessActivity.pageTitle)
    RemoveThirdBusinessActivity.continueAsYes()

    AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
    AddSecondBusinessActivity.clickLink("Remove Second SIC code")
    RemoveSecondBusinessActivity.verifyPageTitle(RemoveSecondBusinessActivity.pageTitle)
    RemoveSecondBusinessActivity.continueAsYes()

    AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
    AddBusinessActivity.continueAsNo()

    ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)
    ClaimDetails.saveAndContinue()
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
  }

  def addClaimDetailsErrors(data: ClaimData): Unit = {
    logStep("I add member state for refund period validation")
    MakeEuvatClaim.clickLinkByText("Claim details")
    EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
    EUMemberState.selectCountry(data.country)

    data.language.foreach { lang =>
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage(lang)
    }

    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
  }

  def loginAndInjectClaimDetails(vrn: String): String = {
    logStep("I login and inject claim details")
    val sharedId = AuthorityWizard.login("Organisation", vrn)
    ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
    CacheHelper.submitUserAnswers("claimDetails.json", sharedId)
    ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
    sharedId
  }

  def loginAndInjectClaimDetailsGermany(vrn: String): String = {
    logStep("I login and inject claim details for Germany")
    val sharedId = AuthorityWizard.login("Organisation", vrn)
    ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
    CacheHelper.submitUserAnswers("claimDetailsGermany.json", sharedId)
    ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
    sharedId
  }

  def goToCheckYourClaimDetails(): Unit = {
    logStep("I navigate to claim details CYA")
    MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/claim-details/check-your-answers")
    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
  }

  def deleteClaimFromClaimDetails(): Unit = {
    logStep("I delete a claim from Claim details CYA")
    MakeEuvatClaim.clickLinkByText("Claim details")
    ClaimDetails.verifyPageTitle(ClaimDetails.pageTitle)
    ClaimDetails.clickChangeLink("EU member state")
    CannotChangeEUMemberState.verifyPageTitle(CannotChangeEUMemberState.pageTitle)
    CannotChangeEUMemberState.continueAsYes()
    ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
  }

  def deleteClaimFromMakeClaim(): Unit = {
    logStep("I delete application from Make a claim page")
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
    MakeEuvatClaim.clickLinkByText("Delete this claim")
    DeleteClaim.verifyPageTitle(DeleteClaim.pageTitle)
    DeleteClaim.continueAsYes()
    ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
  }

}
