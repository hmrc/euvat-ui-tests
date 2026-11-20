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
import uk.gov.hmrc.ui.utils.{CacheHelper, FlowLogging}

trait PurchaseFlows extends FlowLogging{

  def startPurchaseFlow(): Unit = {
    logStep("I start a Purchase")
    MakeEuvatClaim.clickLinkByText("Purchases and imports")
    BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
    BeforeYouStart.continue()
    AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
    AddPurchaseImport.selectPurchaseOrImport("Purchase")
  }

  def addPurchaseFlow(): Unit = {
    logStep("I add a Purchase")
    AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
    AddPurchaseImport.selectPurchaseOrImport("Purchase")
  }

  def addFoodPurchaseFlow(): Unit = {
    logStep("I add Purchase food type, subcode and subcategory")
    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
    PurchaseType.selectPurchaseType("Food, drink and restaurant services")

    PurchaseSubcodeFood.verifyPageTitle(PurchaseSubcodeFood.pageTitle)
    PurchaseSubcodeFood.selectFoodCostType("Food and drink from hotels")

    PurchaseSubcategoryFood.verifyPageTitle(PurchaseSubcategoryFood.pageTitle)
    PurchaseSubcategoryFood.selectWhoFoodFor("The taxable person")
  }

  def addOtherPurchaseFlow(): Unit = {
    logStep("I add Purchase Other type, subcode and Invoice description")
    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
    PurchaseType.selectPurchaseType("Other")

    PurchaseSubcodeOther.verifyPageTitle(PurchaseSubcodeOther.pageTitle)
    PurchaseSubcodeOther.selectSubcategoryOther("None of these - give more details")

    InvoiceItemDescription.verifyPageTitle(InvoiceItemDescription.pageTitle)
    InvoiceItemDescription.submitItemDescription("")

    CheckPurchaseDetails.verifyPageTitle(CheckPurchaseDetails.pageTitle)
    CheckPurchaseDetails.continue()
  }

  def addFoodPurchaseType(): Unit = {
    logStep("I add food Purchase type")
    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
    PurchaseType.selectPurchaseType("Food, drink and restaurant services")
  }

  def addOtherPurchaseType(): Unit = {
    logStep("I add Other Purchase type")
    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
    PurchaseType.selectPurchaseType("Other")
  }

  def injectPurchaseDetailsAfterType(sharedId: String): String = {
    logStep("I inject Purchase details after adding Purchase type")
    CacheHelper.updateUserAnswers("purchaseDetailsAfterType.json", sharedId)
    sharedId
  }

  def injectPurchaseDetailsAfterTypeGermany(sharedId: String): String = {
    logStep("I inject Purchase details after adding Purchase type for Germany")
    CacheHelper.updateUserAnswers("purchaseDetailsAfterTypeGermany.json", sharedId)
    sharedId
  }

  def addStandardInvoiceFlow(): Unit = {
    logStep("I add Standard invoice")
    InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
    InvoiceType.selectInvoiceType("Standard invoice")

    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("1234567890")

    InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
    InvoiceDate.submitInvoiceDate("08", "12", "2025")

    SupplierName.verifyPageTitle(SupplierName.pageTitle)
    SupplierName.submitSupplierName("Test Supplier Name")

    SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
    SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")

    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("EE0000000111")
  }

  def duplicateVRNFlow(): Unit = {
    logStep("I add duplicate VRN for Standard invoice")
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
  }

  def addSimplifiedInvoiceFlow(): Unit = {
    logStep("I add Simplified invoice")
    InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
    InvoiceType.selectInvoiceType("Simplified invoice")

    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("1234567890")

    InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
    InvoiceDate.submitInvoiceDate("01", "01", "2026")

    SupplierName.verifyPageTitle(SupplierName.pageTitle)
    SupplierName.submitSupplierName("Test Supplier Name")

    SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
    SupplierAddress.submitSupplierAddress("Test address one", "Test address two", "Test address three")

    SupplierTaxNumbers.verifyPageTitle(SupplierTaxNumbers.pageTitle)
    SupplierTaxNumbers.selectTaxNumber("Tax ID Number")

    SupplierTaxIDNumber.verifyPageTitle(SupplierTaxIDNumber.pageTitle)
    SupplierTaxIDNumber.submitSupplierTaxID("TID-1")
  }

  def duplicateTIDFlow(): Unit = {
    logStep("I add duplicate TID for Simplified invoice")
    InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
    InvoiceType.selectInvoiceType("Simplified invoice")

    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("INV-1")

    InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
    InvoiceDate.submitInvoiceDate("01", "01", "2026")

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
  }

  def addPurchaseCurrencyFlow(): Unit = {
    logStep("I add Purchase currency type")
    Currency.verifyPageTitle(Currency.pageTitle)
    Currency.selectCurrencyType("Euro")
  }

  def addPurchaseAmountFlow(): Unit = {
    logStep("I add Purchase VAT amounts")
    TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
    TotalPurchaseAmount.submitTotalPurchaseAmount("1000.01")

    TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalVatPaid.submitTotalVatPaid("200.01")

    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("100.01")

    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
  }

  def savePurchase(): Unit = {
    logStep("I save Purchase details")
    CheckYourPurchaseDetails.saveAndContinue()
    //      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1")) // Only works for the database
  }

  def changePurchaseJourney(): Unit = {
    logStep("I change Purchase details")
    CheckYourPurchaseDetails.clickChangeLink("Food and drink for")
    PurchaseSubcategoryFood.verifyPageTitle(PurchaseSubcategoryFood.pageTitle)
    PurchaseSubcategoryFood.selectWhoFoodFor("Someone other")

    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
    CheckYourPurchaseDetails.clickChangeLink("Food and drink cost type")
    PurchaseSubcodeFood.verifyPageTitle(PurchaseSubcodeFood.pageTitle)
    PurchaseSubcodeFood.selectFoodCostType("None")

    //      Change purchase type
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
    CheckYourPurchaseDetails.clickChangeLink("Purchase type")
    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
    PurchaseType.selectPurchaseType("Luxuries, entertainment and hospitality")
    PurchaseSubcodeLuxury.verifyPageTitle(PurchaseSubcodeLuxury.pageTitle)
    PurchaseSubcodeLuxury.selectLuxuryType("Receptions, entertainment and hospitality")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    //      Change invoice type
    CheckYourPurchaseDetails.clickChangeLink("Invoice type")
    InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
    InvoiceType.selectInvoiceType("Simplified invoice")
    AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
    AddVATRegistration.continueAsNo()
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    //      Change invoice number
    CheckYourPurchaseDetails.clickChangeLink("Invoice number")
    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("XR123456789")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    //      Change invoice date
    CheckYourPurchaseDetails.clickChangeLink("Invoice date")
    InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
    InvoiceDate.submitInvoiceDate("01", "01", "2026")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    //      Change supplier name
    CheckYourPurchaseDetails.clickChangeLink("Supplier name")
    SupplierName.verifyPageTitle(SupplierName.pageTitle)
    SupplierName.submitSupplierName("Update Test Supplier Name")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    //      Change supplier address
    CheckYourPurchaseDetails.clickChangeLink("Supplier address")
    SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
    SupplierAddress.submitSupplierAddress("Updated Street", "Updated City", "Updated Country")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    //      Change supplier VAT registration check
    CheckYourPurchaseDetails.clickChangeLink("Supplier VAT registration check")
    AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
    AddVATRegistration.continueAsYes()
    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("AA987654321")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    //      Change supplier VAT registration number
    CheckYourPurchaseDetails.clickChangeLink("Supplier VAT registration number")
    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("BB987654321")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    //      Change currency
    CheckYourPurchaseDetails.clickChangeLink("Currency")
    Currency.verifyPageTitle(Currency.pageTitle)
    Currency.selectCurrencyType("Estonian Kroon (kr)")
    TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
    TotalPurchaseAmount.submitTotalPurchaseAmount("1000.01")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    //      Change VAT claim
    CheckYourPurchaseDetails.clickChangeLink("VAT claim")
    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("3000.99")
    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    //      Change VAT paid
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

    //      Change amount before VAT
    CheckYourPurchaseDetails.clickChangeLink("Amount before VAT")
    TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
    TotalPurchaseAmount.submitTotalPurchaseAmount("300.99")
    TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalVatPaid.submitTotalVatPaid("5000.99")
    CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
    CheckVATAmount.continue()
    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("6000.99")
    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
    CheckYourPurchaseDetails.saveAndContinue()
    //      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1")) // Only works for the database
  }

  def summaryFlowYes(): Unit = {
    logStep("I select Yes on the Purchase Import Summary")
    PurchaseImportSummary.continueAsYes()
  }

  def changePurchaseSummaryFlow(itemText: String): Unit = {
    logStep("I change the Purchase from the Summary page")
    PurchaseImportSummary.clickActionLink(itemText, "Change")
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)
  }

  def changePurchaseAfterSave(): Unit = {
    logStep("I change a Purchase after saving to database")

    ChangeYourPurchaseDetails.clickChangeLink("Food and drink for")
    PurchaseSubcategoryFood.verifyPageTitle(PurchaseSubcategoryFood.pageTitle)
    PurchaseSubcategoryFood.selectWhoFoodFor("Someone other")

    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)
    ChangeYourPurchaseDetails.clickChangeLink("Food and drink cost type")
    PurchaseSubcodeFood.verifyPageTitle(PurchaseSubcodeFood.pageTitle)
    PurchaseSubcodeFood.selectFoodCostType("None")

    //      Change purchase type
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)
    ChangeYourPurchaseDetails.clickChangeLink("Purchase type")
    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
    PurchaseType.selectPurchaseType("Luxuries, entertainment and hospitality")
    PurchaseSubcodeLuxury.verifyPageTitle(PurchaseSubcodeLuxury.pageTitle)
    PurchaseSubcodeLuxury.selectLuxuryType("Receptions, entertainment and hospitality")
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change invoice type
    ChangeYourPurchaseDetails.clickChangeLink("Invoice type")
    InvoiceType.verifyPageTitle(InvoiceType.pageTitle)
    InvoiceType.selectInvoiceType("Simplified invoice")
    AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
    AddVATRegistration.continueAsNo()
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change invoice number
    ChangeYourPurchaseDetails.clickChangeLink("Invoice number")
    InvoiceNumber.verifyPageTitle(InvoiceNumber.pageTitle)
    InvoiceNumber.submitInvoiceNumber("XR123456789")
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change invoice date
    ChangeYourPurchaseDetails.clickChangeLink("Invoice date")
    InvoiceDate.verifyPageTitle(InvoiceDate.pageTitle)
    InvoiceDate.submitInvoiceDate("01", "01", "2026")
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change supplier name
    ChangeYourPurchaseDetails.clickChangeLink("Supplier name")
    SupplierName.verifyPageTitle(SupplierName.pageTitle)
    SupplierName.submitSupplierName("Update Test Supplier Name")
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change supplier address
    ChangeYourPurchaseDetails.clickChangeLink("Supplier address")
    SupplierAddress.verifyPageTitle(SupplierAddress.pageTitle)
    SupplierAddress.submitSupplierAddress("Updated Street", "Updated City", "Updated Country")
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change supplier VAT registration check
    ChangeYourPurchaseDetails.clickChangeLink("Supplier VAT registration check")
    AddVATRegistration.verifyPageTitle(AddVATRegistration.pageTitle)
    AddVATRegistration.continueAsYes()
    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("AA987654321")
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change supplier VAT registration number
    ChangeYourPurchaseDetails.clickChangeLink("Supplier VAT registration number")
    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("BB987654321")
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change currency
    ChangeYourPurchaseDetails.clickChangeLink("Currency")
    Currency.verifyPageTitle(Currency.pageTitle)
    Currency.selectCurrencyType("Estonian Kroon (kr)")
    TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
    TotalPurchaseAmount.submitTotalPurchaseAmount("1000.01")
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change VAT claim
    ChangeYourPurchaseDetails.clickChangeLink("VAT claim")
    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("3000.99")
    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change VAT paid
    ChangeYourPurchaseDetails.clickChangeLink("VAT paid")
    TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalVatPaid.submitTotalVatPaid("3000.99")
    CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
    CheckVATAmount.continue()
    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("4000.99")
    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)

    //      Change amount before VAT
    ChangeYourPurchaseDetails.clickChangeLink("Amount before VAT")
    TotalPurchaseAmount.verifyPageTitle(TotalPurchaseAmount.pageTitle)
    TotalPurchaseAmount.submitTotalPurchaseAmount("300.99")
    TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalVatPaid.submitTotalVatPaid("5000.99")
    CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
    CheckVATAmount.continue()
    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("6000.99")
    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()
    ChangeYourPurchaseDetails.verifyPageTitle(ChangeYourPurchaseDetails.pageTitle)
    ChangeYourPurchaseDetails.saveAndContinue()
  }

  def vatWarningsPurchaseJourney(): Unit = {
    logStep("I check Purchase VAT amounts")
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

  def goToCheckYourPurchaseDetails(): Unit = {
    logStep("I navigate to the Purchase CYA page")
    MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/purchase/check-your-answers")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
  }

  def changeTaxIdDetails(): Unit = {
    logStep("I change tax details")
    // Change Supplier tax identifier
    CheckYourPurchaseDetails.clickChangeLink("Supplier tax identifier")
    SupplierTaxIDNumber.verifyPageTitle(SupplierTaxIDNumber.pageTitle)
    SupplierTaxIDNumber.submitSupplierTaxID("12345")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)

    // Change Supplier tax numbers
    CheckYourPurchaseDetails.clickChangeLink("Supplier tax numbers")
    SupplierTaxNumbers.verifyPageTitle(SupplierTaxNumbers.pageTitle)
    SupplierTaxNumbers.selectTaxNumber("Vat Registration Number")
    VATRegistrationNumber.verifyPageTitle(VATRegistrationNumber.pageTitle)
    VATRegistrationNumber.submitVATRegistrationNumber("1234567890")
    CheckYourPurchaseDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
  }


  def deleteClaimFromSummary(itemText: String): Unit = {
    logStep("I delete a Purchase or Import from the Summary page")
//    PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("2")) //Cannot run this with the database
    PurchaseImportSummary.clickActionLink(itemText, "Remove")
    RemoveItem.verifyPageTitle(RemoveItem.pageTitle)
    RemoveItem.selectDeleteItem("Yes")
    //      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle) // Only works for the database
    //      MakeEuvatClaim.clickSignOut // Only works for the database
  }

}
