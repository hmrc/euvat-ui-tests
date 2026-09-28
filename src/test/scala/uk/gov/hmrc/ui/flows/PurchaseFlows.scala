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
import uk.gov.hmrc.ui.pages.claim.MakeEuvatClaim
import uk.gov.hmrc.ui.pages.purchase.*

trait PurchaseFlows {

  def startPurchaseFlow(): Unit = {
    MakeEuvatClaim.clickLinkByText("Add a purchase")
    BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
    BeforeYouStart.continue()
    AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
    AddPurchaseImport.selectPurchaseOrImport("Purchase")
  }

  /** Food/drink journey kept aligned to original working spec: PurchaseType -> FoodDrink -> WhoFoodDrink -> InvoiceType
    */
  def addFoodDrinkPurchaseUsingOriginalWorkingFlow(): Unit = {
    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
    PurchaseType.selectPurchaseType("Food, drink and restaurant services")

    PurchaseSubCodeFood.verifyPageTitle(PurchaseSubCodeFood.pageTitle)
    PurchaseSubCodeFood.selectFoodDrinkCostType("Food and drink from hotels")

    PurchaseSubCategoryFood.verifyPageTitle(PurchaseSubCategoryFood.pageTitle)
    PurchaseSubCategoryFood.selectWhoFoodDrinkFor("The taxable person")

    InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
    InvoiceType.selectInvoiceType("Standard invoice")

    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("DUP")

    InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
    InvoiceDate.submitInvoiceDate("08", "12", "2025")

    SupplierName.verifyPageTitle(SupplierName.pageTitle)
    SupplierName.submitSupplierName("Test Supplier Name")

    SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
    SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")

    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("EE0000000111")

    CheckSupplierVRN.verifyPageTitle(CheckSupplierVRN.pageTitle)
    CheckSupplierVRN.clickLinkByText("Change invoice number")

    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("DUP")

    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("EE0000000111")

    CheckSupplierVRN.verifyPageTitle(CheckSupplierVRN.pageTitle)
    CheckSupplierVRN.clickLinkByText("Change supplier’s VAT registration number")

    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("EE0000000111")

    CheckSupplierVRN.verifyPageTitle(CheckSupplierVRN.pageTitle)
    CheckSupplierVRN.continue()

    Currency.verifyPageTitle(Currency.pageTitle)
    Currency.selectCurrencyType("Euro")

    TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
    TotalPurchaseAmount.submitTotalPurchaseAmount("1000.01")

    TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalVatPaid.submitTotalVatPaid("200.01")

    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("100.01")

    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
  }

  /** Germany/Other journey kept aligned to original working spec: PurchaseType -> PurchaseTypeOther ->
    * InvoiceItemDescription -> CheckPurchaseDetails -> InvoiceType
    */
  def addGermanyOtherPurchaseUsingOriginalWorkingFlow(): Unit = {
    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
    PurchaseType.selectPurchaseType("Other")

    PurchaseSubCodeOther.verifyPageTitle(PurchaseSubCodeOther.pageTitle)
    PurchaseSubCodeOther.selectPurchaseTypeOther("None of these - give more details")

    InvoiceItemDescription.verifyPageTitle(InvoiceItemDescription.pageTitle)
    InvoiceItemDescription.submitItemDescription("")

    CheckPurchaseDetails.verifyPageTitle(CheckPurchaseDetails.pageTitle)
    CheckPurchaseDetails.continue()

    InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
    InvoiceType.selectInvoiceType("Simplified invoice")

    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("INV-1")

    InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
    InvoiceDate.submitInvoiceDate("08", "12", "2025")

    SupplierName.verifyPageTitle(SupplierName.pageTitle)
    SupplierName.submitSupplierName("Test Supplier Name")

    SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
    SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")

    SupplierTaxNumbers.verifyPageTitle(SupplierTaxNumbers.pageTitle)
    SupplierTaxNumbers.selectTaxNumber("Tax ID Number")

    SupplierTaxIDNumber.verifyPageTitle(SupplierTaxIDNumber.pageTitle)
    SupplierTaxIDNumber.submitSupplierTaxID("TID-1")

    CheckSupplierTaxIDNumber.verifyPageTitle(CheckSupplierTaxIDNumber.pageTitle)
    CheckSupplierTaxIDNumber.clickLinkByText("Change invoice number")

    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("INV-1")

    SupplierTaxIDNumber.verifyPageTitle(SupplierTaxIDNumber.pageTitle)
    SupplierTaxIDNumber.submitSupplierTaxID("TID-1")

    CheckSupplierTaxIDNumber.verifyPageTitle(CheckSupplierTaxIDNumber.pageTitle)
    CheckSupplierTaxIDNumber.clickLinkByText("Change supplier’s tax identifier number")

    SupplierTaxIDNumber.verifyPageTitle(SupplierTaxIDNumber.pageTitle)
    SupplierTaxIDNumber.submitSupplierTaxID("TID-1")

    CheckSupplierTaxIDNumber.verifyPageTitle(CheckSupplierTaxIDNumber.pageTitle)
    CheckSupplierTaxIDNumber.continue()

    TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
    TotalPurchaseAmount.submitTotalPurchaseAmount("1000.99")

    TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalVatPaid.submitTotalVatPaid("200.99")

    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("100.99")

    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
  }

  def savePurchase(): Unit = {
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
    CheckYourPurchaseDetails.saveAndContinue()
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
  }

  def editPurchaseJourney(): Unit = {
    CheckYourPurchaseDetails.clickChangeLink("Food and drink for")
    PurchaseSubCategoryFood.verifyPageTitle(PurchaseSubCategoryFood.pageTitle)
    PurchaseSubCategoryFood.selectWhoFoodDrinkFor("Someone other")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Food and drink cost type")
    PurchaseSubCodeFood.verifyPageTitle(PurchaseSubCodeFood.pageTitle)
    PurchaseSubCodeFood.selectFoodDrinkCostType("None")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Purchase type")
    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
    PurchaseType.selectPurchaseType("Luxuries, entertainment and hospitality")

    PurchaseSubCodeLuxury.verifyPageTitle(PurchaseSubCodeLuxury.pageTitle)
    PurchaseSubCodeLuxury.selectLuxuryType("Receptions, entertainment and hospitality")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Invoice type")
    InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
    InvoiceType.selectInvoiceType("Simplified invoice")

    AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
    AddVATRegistration.continueAsNo()
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Invoice number")
    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("XR123456789")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Invoice date")
    InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
    InvoiceDate.submitInvoiceDate("01", "01", "2026")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Supplier name")
    SupplierName.verifyPageTitle(SupplierName.pageTitle)
    SupplierName.submitSupplierName("Updated Test Supplier Name")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Supplier address")
    SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
    SupplierAddress.submitSupplierAddress("Updated Street", "Updated City", "Updated Country")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Supplier VAT registration check")
    AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
    AddVATRegistration.continueAsYes()

    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("AA987654321")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Supplier VAT registration number")
    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("BB987654321")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Currency")
    Currency.verifyPageTitle(Currency.pageTitle)
    Currency.selectCurrencyType("Estonian Kroon (kr)")

    TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
    TotalPurchaseAmount.submitTotalPurchaseAmount("1000.01")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
  }

  def germanyTaxIdEdits(): Unit = {
    CheckYourPurchaseDetails.clickChangeLink("Supplier tax identifier")
    SupplierTaxIDNumber.verifyPageTitle(SupplierTaxIDNumber.pageTitle)
    SupplierTaxIDNumber.submitSupplierTaxID("12345")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("Supplier tax numbers")
    SupplierTaxNumbers.verifyPageTitle(SupplierTaxNumbers.pageTitle)
    SupplierTaxNumbers.selectTaxNumber("Vat Registration Number")

    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("1234567890")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
  }

  def vatWarningsJourney(): Unit = {
    CheckYourPurchaseDetails.clickChangeLink("VAT claim")
    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("3000.99")

    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    CheckYourPurchaseDetails.clickChangeLink("VAT paid")
    TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalVatPaid.submitTotalVatPaid("3000.99")

    CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
    CheckVATAmount.continue()

    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("4000.99")

    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
  }
}
