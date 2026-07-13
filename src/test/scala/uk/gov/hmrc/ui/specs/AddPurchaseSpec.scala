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
import uk.gov.hmrc.ui.utils.{CacheHelper, MongoHelper}

class AddPurchaseSpec
    extends AnyFeatureSpec
    with BaseSpec
    with GivenWhenThen
    with ShouldVerb
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Browser
    with ScreenshotOnFailure
    with MongoHelper {

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropCollections()
  }

  Feature("Make a new EUVAT claim - Add a purchase") {
    Scenario("Submit a refund request", Local) {
      Given("I login as an organisation")
      val sharedId = AuthorityWizard.login("Organisation", "999900100")
      ManageYourEuvatClaim.verifyPageTitle(ManageYourEuvatClaim.pageTitle)

      When("I start new EUVAT claim")
      CacheHelper.submitUserAnswers("claimDetails.json", sharedId)
      ManageYourEuvatClaim.clickLinkByText("Make a claim for an EU VAT refund")

      And("I see Claim details completed and Add a purchase")
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
      MakeEuvatClaim.clickLinkByText("Add a purchase")

      And("I add purchase details")
      AboutPurchase.verifyPageTitle(AboutPurchase.pageTitle)
      AboutPurchase.continue()

      InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
      InvoiceType.selectInvoiceType("Standard invoice")

      InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
      InvoiceNumber.submitInvoiceNumber("Test_Invoice_123.5")

      InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
      InvoiceDate.submitInvoiceDate("08", "12", "2025")

      SupplierName.verifyPageTitle(SupplierName.pageTitle)
      SupplierName.submitSupplierName("Test Supplier Name")

      SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
      SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")

      AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
      AddVATRegistration.continueAsYes()

      VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
      VATRegistrationNumber.submitInvoiceNumber("AB1234567890")

//      PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
//      PurchaseType.selectPurchaseType("Fuel")

      TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
      TotalPurchaseAmount.submitTotalPurchaseAmount("100")

      TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
      TotalVatPaid.submitTotalVatPaid("100")

      TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
      TotalVatClaim.submitTotalVatClaim("100")

      MakeEuvatClaim.signOut()
    }
  }
}
