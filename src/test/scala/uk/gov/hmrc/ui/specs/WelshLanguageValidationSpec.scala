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
import uk.gov.hmrc.ui.utils.{DatabaseHelper, MongoHelper, WelshPageVerifier}

class WelshLanguageValidationSpec
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

  Feature("Make a new EUVAT claim - New claim") {

    Scenario("01 - Validate Welsh content", Local, WIP) {
      Given("I login as an organisation")
      AuthorityWizard.login("Organisation", "999900001")
      ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)
      ClaimAnEUVATRefund.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
//      WelshPageVerifier.verify("AA1.0", ClaimAnEUVATRefund)
      ClaimAnEUVATRefund.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")

      When("I start new EUVAT claim")
      ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")

      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
//      WelshPageVerifier.verify("RA1.1", MakeEuvatClaim)
      MakeEuvatClaim.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")

      And("I add claim details")
      MakeEuvatClaim.clickLinkByText("Add claim details")

      EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
      EUMemberState.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      EUMemberState.continue()
//      WelshPageVerifier.verify("RA2.1", EUMemberState)
      EUMemberState.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      EUMemberState.selectCountry("Estonia")

      Language.verifyPageTitle(Language.pageTitle)
      Language.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      Language.continue()
//      WelshPageVerifier.verify("RA3.0", Language)
      Language.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      Language.selectLanguage("Estonian")

      RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
      RefundPeriod.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      RefundPeriod.continue()
//      WelshPageVerifier.verify("RA2.2", RefundPeriod)
      RefundPeriod.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      RefundPeriod.submitRefundPeriod("02", "2025", "04", "2025")

      ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
      ContactDetails.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      ContactDetails.continue()
//      WelshPageVerifier.verify("RA2.4", ContactDetails)
      ContactDetails.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      ContactDetails.submitContactAddress("Test@gmail.com", "9876543210")

      AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
      AddBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      AddBusinessActivity.continue()
//      WelshPageVerifier.verify("RA", AddBusinessActivity)
      AddBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      AddBusinessActivity.continueAsYes()

      SecondBusinessActivity.verifyPageTitle(SecondBusinessActivity.pageTitle)
      SecondBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      SecondBusinessActivity.continue()
      //      WelshPageVerifier.verify("RA2.5", SecondBusinessActivity)
      SecondBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      SecondBusinessActivity.enterSecondBusinessActivityCode("4711")

      AddSecondBusinessActivity.verifyPageTitle(AddSecondBusinessActivity.pageTitle)
      AddSecondBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      AddSecondBusinessActivity.continue()
      //      WelshPageVerifier.verify("RA1.7", AddSecondBusinessActivity)
      AddSecondBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      AddSecondBusinessActivity.continueAsYes()

      ThirdBusinessActivity.verifyPageTitle(ThirdBusinessActivity.pageTitle)
      ThirdBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      ThirdBusinessActivity.continue()
//      WelshPageVerifier.verify("RA2.6", ThirdBusinessActivity)
      ThirdBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      ThirdBusinessActivity.enterThirdBusinessActivityCode("2534")

      AddThirdBusinessActivity.verifyPageTitle(AddThirdBusinessActivity.pageTitle)
      AddThirdBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
//      WelshPageVerifier.verify("RA1.10", AddThirdBusinessActivity)
      AddThirdBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")

      AddThirdBusinessActivity.clickLink("Remove Third SIC code")
      RemoveThirdBusinessActivity.verifyPageTitle(RemoveThirdBusinessActivity.pageTitle)
      RemoveThirdBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      RemoveThirdBusinessActivity.continue()
//      WelshPageVerifier.verify("RA1.11", RemoveThirdBusinessActivity)
      RemoveThirdBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      RemoveThirdBusinessActivity.continueAsYes()

      AddSecondBusinessActivity.clickLink("Remove Second SIC code")
      RemoveSecondBusinessActivity.verifyPageTitle(RemoveSecondBusinessActivity.pageTitle)
      RemoveSecondBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      RemoveSecondBusinessActivity.continue()
      WelshPageVerifier.verify("RA2.7", RemoveSecondBusinessActivity)
      RemoveSecondBusinessActivity.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      RemoveSecondBusinessActivity.continueAsYes()

      AddBusinessActivity.continueAsNo()

      And("I change claim details")
      CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
      CheckYourClaimDetails.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
//      WelshPageVerifier.verify("RA4.0", CheckYourClaimDetails)
      CheckYourClaimDetails.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      CheckYourClaimDetails.saveAndContinue()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      insertDuplicatePurchaseRecordVRN()

      And("I add purchase details")
      MakeEuvatClaim.clickLinkByText("Add a purchase")

      BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
      BeforeYouStart.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
//      WelshPageVerifier.verify("RA2.1", BeforeYouStart)
      BeforeYouStart.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      BeforeYouStart.continue()

      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
      PurchaseType.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      PurchaseType.continue()
//      WelshPageVerifier.verify("RA2.2", PurchaseType)
      PurchaseType.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      PurchaseType.selectPurchaseType("Food, drink and restaurant services")

      FoodDrink.verifyPageTitle(FoodDrink.pageTitle)
      FoodDrink.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      FoodDrink.continue()
//      WelshPageVerifier.verify("RA2.3", FoodDrink)
      FoodDrink.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      FoodDrink.selectFoodDrinkCostType("Food and drink from hotels")

      WhoFoodDrink.verifyPageTitle(WhoFoodDrink.pageTitle)
      WhoFoodDrink.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      WhoFoodDrink.continue()
//      WelshPageVerifier.verify("RA2.4", WhoFoodDrink)
      WhoFoodDrink.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      WhoFoodDrink.selectWhoFoodDrinkFor("The taxable person")

      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
      InvoiceType.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      InvoiceType.continue()
//      WelshPageVerifier.verify("RA2.5", InvoiceType)
      InvoiceType.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      InvoiceType.selectInvoiceType("Standard invoice")

      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      InvoiceNumber.continue()
//      WelshPageVerifier.verify("RA2.6", InvoiceNumber)
      InvoiceNumber.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      InvoiceNumber.submitInvoiceNumber("DUP")

      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
      InvoiceDate.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      InvoiceDate.continue()
//      WelshPageVerifier.verify("RA2.7", InvoiceDate)
      InvoiceDate.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      InvoiceDate.submitInvoiceDate("08", "12", "2025")

      SupplierName.verifyPageTitle(SupplierName.pageTitle)
      SupplierName.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      SupplierName.continue()
//      WelshPageVerifier.verify("RA2.8", SupplierName)
      SupplierName.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      SupplierName.submitSupplierName("Test Supplier Name")

      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
      SupplierAddress.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      SupplierAddress.continue()
//      WelshPageVerifier.verify("RA2.9", SupplierAddress)
      SupplierAddress.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")

      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      VATRegistrationNumber.continue()
//      WelshPageVerifier.verify("RA2.10", VATRegistrationNumber)
      VATRegistrationNumber.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      VATRegistrationNumber.submitVATRegistrationNumber("EE0000000111")

      CheckSupplierVRN.verifyPageTitle(CheckSupplierVRN.pageTitle)
      CheckSupplierVRN.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
//      WelshPageVerifier.verify("RA2.10", CheckSupplierVRN)
      CheckSupplierVRN.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      CheckSupplierVRN.continue()

      Currency.verifyPageTitle(Currency.pageTitle)
      Currency.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      Currency.continue()
      WelshPageVerifier.verify("RA3.1", Currency)
      Currency.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      Currency.selectCurrencyType("Euro")

      TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
      TotalPurchaseAmount.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      TotalPurchaseAmount.continue()
//      WelshPageVerifier.verify("RA3.2", TotalPurchaseAmount)
      TotalPurchaseAmount.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      TotalPurchaseAmount.submitTotalPurchaseAmount("1000.01")

      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
      TotalVatPaid.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      TotalVatPaid.continue()
//      WelshPageVerifier.verify("RA3.3", TotalVatPaid)
      TotalVatPaid.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      TotalVatPaid.submitTotalVatPaid("200.01")

      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
      TotalVatClaim.clickByXpath("/html/body/header/section/div/nav/ul/li[2]/a")
      TotalVatClaim.continue()
//      WelshPageVerifier.verify("RA3.4", TotalVatClaim)
      TotalVatClaim.clickByXpath("/html/body/header/section/div/nav/ul/li[1]/a")
      TotalVatClaim.submitTotalVatClaim("100.01")

      CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
      CheckYourPurchaseDetails.clickSignOut
    }
  }
}
