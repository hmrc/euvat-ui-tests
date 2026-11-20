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
import uk.gov.hmrc.ui.pages.imports.*
import uk.gov.hmrc.ui.pages.purchase.*
import uk.gov.hmrc.ui.utils.{CacheHelper, FlowLogging}

trait ImportFlows extends FlowLogging {

  def startImportFlow(): Unit = {
    logStep("I start an Import")
    MakeEuvatClaim.clickLinkByText("Purchases and imports")
    BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
    BeforeYouStart.continue()
    AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
    AddPurchaseImport.selectPurchaseOrImport("Import")
  }

  def addImportFlow(): Unit = {
    logStep("I add an Import")
    AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
    AddPurchaseImport.selectPurchaseOrImport("Import")
  }

  def addFoodImportFlow(): Unit = {
    logStep("I add Import food type, subcode and subcategory")
    ImportType.verifyPageTitle(ImportType.pageTitle)
    ImportType.selectImportType("Food, drink and restaurant services")

    ImportSubcodeFood.verifyPageTitle(ImportSubcodeFood.pageTitle)
    ImportSubcodeFood.selectImportFoodType("Food and drink from hotels")

    ImportSubcategoryFood.verifyPageTitle(ImportSubcategoryFood.pageTitle)
    ImportSubcategoryFood.selectWhoFoodFor("The taxable person")
  }

  def addOtherImportFlow(): Unit = {
    logStep("I add Import Other type, subcode and document list")
    ImportType.verifyPageTitle(ImportType.pageTitle)
    ImportType.selectImportType("Other")
    ImportSubcodeOther.verifyPageTitle(ImportSubcodeOther.pageTitle)
    ImportSubcodeOther.selectImportTypeOther("None of these - give more details")
    ListImportDocumentItems.verifyPageTitle(ListImportDocumentItems.pageTitle)
    ListImportDocumentItems.submitImportDocumentList("Listed items from import document")
  }

  def addFoodImportType(): Unit = {
    logStep("I food Import type")
    ImportType.verifyPageTitle(ImportType.pageTitle)
    ImportType.selectImportType("Food, drink and restaurant services")
  }

  def injectImportDetailsAfterType(sharedId: String): String = {
    logStep("I inject Import details after adding import type")
    CacheHelper.updateUserAnswers("importDetailsAfterType.json", sharedId)
    sharedId
  }

  def injectImportDetailsAfterTypeGermany(sharedId: String): String = {
    logStep("I inject Import details after adding import type for Germany")
    CacheHelper.updateUserAnswers("importDetailsAfterTypeGermany.json", sharedId)
    sharedId
  }

  def addSADFlow(data: SadFlowData): Unit = {
    logStep("I add SAD details")
    AddSADRefNumber.verifyPageTitle(AddSADRefNumber.pageTitle)

    data match {
      case sadRef: SadReferenceData =>
        logStep("I add SAD reference number")
        AddSADRefNumber.continueAsYes()
        SADRefNumber.verifyPageTitle(SADRefNumber.pageTitle)
        SADRefNumber.submitSADRefNumber(sadRef.referenceNumber)

      case sadDoc: SadDocumentData =>
        logStep("I add SAD document details")
        AddSADRefNumber.continueAsNo()
        ImportDocumentDetails.verifyPageTitle(ImportDocumentDetails.pageTitle)
        ImportDocumentDetails.submitDocumentDetails(sadDoc.documentDetails)
    }

    ImportDate.verifyPageTitle(ImportDate.pageTitle)
    ImportDate.submitImportDate(data.importDay, data.importMonth, data.importYear)

    ImportSupplierName.verifyPageTitle(ImportSupplierName.pageTitle)
    ImportSupplierName.submitSupplierName(data.supplierName)

    ImportSupplierAddress.verifyPageTitle(ImportSupplierAddress.pageTitle)
    ImportSupplierAddress.submitImportSupplierAddress(
      data.supplierAddress1,
      data.supplierAddress2,
      data.supplierAddress3,
      data.supplierCountry
    )
  }

  def duplicateSADFlow(data: SadFlowData): Unit = {
    logStep("I add duplicate SAD details")
    AddSADRefNumber.verifyPageTitle(AddSADRefNumber.pageTitle)

    data match {
      case sadRef: SadReferenceData =>
        logStep("I add a duplicate SAD reference number")
        AddSADRefNumber.continueAsYes()
        SADRefNumber.verifyPageTitle(SADRefNumber.pageTitle)
        SADRefNumber.submitSADRefNumber(sadRef.duplicateReferenceNumber)
        CheckSADRefNumber.continue()

      case sadDoc: SadDocumentData =>
        logStep("I add a duplicate SAD document details")
        AddSADRefNumber.continueAsNo()
        ImportDocumentDetails.verifyPageTitle(ImportDocumentDetails.pageTitle)
        ImportDocumentDetails.submitDocumentDetails(sadDoc.duplicateDocumentDetails)
        CheckImportDocumentDetails.continue()
    }

    ImportDate.verifyPageTitle(ImportDate.pageTitle)
    ImportDate.submitImportDate(data.importDay, data.importMonth, data.importYear)

    ImportSupplierName.verifyPageTitle(ImportSupplierName.pageTitle)
    ImportSupplierName.submitSupplierName(data.supplierName)

    ImportSupplierAddress.verifyPageTitle(ImportSupplierAddress.pageTitle)
    ImportSupplierAddress.submitImportSupplierAddress(
      data.supplierAddress1,
      data.supplierAddress2,
      data.supplierAddress3,
      data.supplierCountry
    )
  }

  def addImportCurrencyFlow(): Unit = {
    logStep("I add Import currency type")
    ImportCurrency.verifyPageTitle(ImportCurrency.pageTitle)
    ImportCurrency.selectImportCurrencyType("Euro")
  }

  def addImportAmountFlow(): Unit = {
    logStep("I add Import VAT amounts")
    TotalImportAmount.verifyPageTitle(TotalImportAmount.pageTitle)
    TotalImportAmount.submitTotalImportAmount("1000.01")

    TotalImportVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalImportVatPaid.submitTotalImportVATPaid("200.01")

    TotalImportVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalImportVatClaim.submitTotalImportVatClaim("100.01")

    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
  }

  def saveImport(): Unit = {
    logStep("I save Import details")
    CheckYourImportDetails.saveAndContinue()
  }

  def changeImportJourney(): Unit = {
    logStep("I change Import details")
    CheckYourImportDetails.clickChangeLink("Food and drink for")
    ImportSubcategoryFood.verifyPageTitle(ImportSubcategoryFood.pageTitle)
    ImportSubcategoryFood.selectWhoFoodFor("Someone other")

    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
    CheckYourImportDetails.clickChangeLink("Food, drink or restaurant cost type")
    ImportSubcodeFood.verifyPageTitle(ImportSubcodeFood.pageTitle)
    ImportSubcodeFood.selectImportFoodType("None")

    //      Change import type
    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
    CheckYourImportDetails.clickChangeLink("Import type")
    ImportType.verifyPageTitle(ImportType.pageTitle)
    ImportType.selectImportType("Luxuries, entertainment and hospitality")
    ImportSubcodeLuxury.verifyPageTitle(ImportSubcodeLuxury.pageTitle)
    ImportSubcodeLuxury.selectLuxuryType("Receptions, entertainment and hospitality")

    //      Change import doc details
    CheckYourImportDetails.verifyPageTitle(CheckYourPurchaseDetails.pageTitle)
    CheckYourImportDetails.clickChangeLink("Single Administrative Document (SAD)")
    AddSADRefNumber.verifyPageTitle(AddSADRefNumber.pageTitle)
    AddSADRefNumber.continueAsNo()
    ImportDocumentDetails.verifyPageTitle(ImportDocumentDetails.pageTitle)
    ImportDocumentDetails.submitDocumentDetails("TEST INPUT")

    //      Change invoice date
    CheckYourImportDetails.clickChangeLink("Import date")
    ImportDate.verifyPageTitle(ImportDate.pageTitle)
    ImportDate.submitImportDate("01", "01", "2026")
    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)

    //      Change supplier name
    CheckYourImportDetails.clickChangeLink("Supplier name")
    ImportSupplierName.verifyPageTitle(ImportSupplierName.pageTitle)
    ImportSupplierName.submitSupplierName("Update Test Supplier Name")
    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)

    //      Change supplier address
    CheckYourImportDetails.clickChangeLink("Supplier address")
    ImportSupplierAddress.verifyPageTitle(ImportSupplierAddress.pageTitle)
    ImportSupplierAddress.submitImportSupplierAddress(
      "Test Street ABC",
      "Test City 321",
      "Test State XYZ",
      "HEARD ISLAND & MCDONALD ISLANDS"
    )
    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)

    //      Change currency
    CheckYourImportDetails.clickChangeLink("Currency")
    ImportCurrency.verifyPageTitle(ImportCurrency.pageTitle)
    ImportCurrency.selectImportCurrencyType("Estonian Kroon (kr)")
    TotalImportAmount.verifyPageTitle(TotalImportAmount.pageTitle)
    TotalImportAmount.submitTotalImportAmount("1000.01")

    //      Change VAT paid
    TotalImportVatPaid.verifyPageTitle(TotalImportVatPaid.pageTitle)
    TotalImportVatPaid.submitTotalImportVATPaid("3000.99")
    CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
    CheckVATAmount.continue()

    //      Change VAT claim
    TotalImportVatClaim.verifyPageTitle(TotalImportVatClaim.pageTitle)
    TotalImportVatClaim.submitTotalImportVatClaim("4000.99")
    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()

    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
  }

  def summaryFlowNo(): Unit = {
    logStep("I select No on the Purchase Import Summary")
    PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("2"))
    PurchaseImportSummary.continueAsNo()
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
  }

  def changeImportSummaryFlow(itemText: String): Unit = {
    logStep("I change an Import from the summary page")
    //      PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("1")) // Only works for the database
    PurchaseImportSummary.clickActionLink(itemText, "Change")
    ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)
  }

  def changeImportAfterSave(): Unit = {
    logStep("I change an Import after saving to database")
    //      Change import type
    ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)
    ChangeYourImportDetails.clickChangeLink("Import type")
    ImportType.verifyPageTitle(ImportType.pageTitle)
    ImportType.selectImportType("Luxuries, entertainment and hospitality")
    ImportSubcodeLuxury.verifyPageTitle(ImportSubcodeLuxury.pageTitle)
    ImportSubcodeLuxury.selectLuxuryType("Receptions, entertainment and hospitality")

    //      Change import doc details
    ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)
    ChangeYourImportDetails.clickChangeLink("Single Administrative Document (SAD)")
    AddSADRefNumber.verifyPageTitle(AddSADRefNumber.pageTitle)
    AddSADRefNumber.continueAsNo()
    ImportDocumentDetails.verifyPageTitle(ImportDocumentDetails.pageTitle)
    ImportDocumentDetails.submitDocumentDetails("TEST INPUT")

    //      Change invoice date
    ChangeYourImportDetails.clickChangeLink("Import date")
    ImportDate.verifyPageTitle(ImportDate.pageTitle)
    ImportDate.submitImportDate("01", "01", "2026")
    ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)

    //      Change supplier name
    ChangeYourImportDetails.clickChangeLink("Supplier name")
    ImportSupplierName.verifyPageTitle(ImportSupplierName.pageTitle)
    ImportSupplierName.submitSupplierName("Update Test Supplier Name")
    ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)

    //      Change supplier address
    ChangeYourImportDetails.clickChangeLink("Supplier address")
    ImportSupplierAddress.verifyPageTitle(ImportSupplierAddress.pageTitle)
    ImportSupplierAddress.submitImportSupplierAddress(
      "Test Street ABC",
      "Test City 321",
      "Test State XYZ",
      "HEARD ISLAND & MCDONALD ISLANDS"
    )
    ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)

    //      Change currency
    ChangeYourImportDetails.clickChangeLink("Currency")
    ImportCurrency.verifyPageTitle(ImportCurrency.pageTitle)
    ImportCurrency.selectImportCurrencyType("Estonian Kroon (kr)")
    TotalImportAmount.verifyPageTitle(TotalImportAmount.pageTitle)
    TotalImportAmount.submitTotalImportAmount("1000.01")

    //      Change VAT paid
    TotalImportVatPaid.verifyPageTitle(TotalImportVatPaid.pageTitle)
    TotalImportVatPaid.submitTotalImportVATPaid("3000.99")
    CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
    CheckVATAmount.continue()

    //      Change VAT claim
    TotalImportVatClaim.verifyPageTitle(TotalImportVatClaim.pageTitle)
    TotalImportVatClaim.submitTotalImportVatClaim("4000.99")
    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()

    ChangeYourImportDetails.verifyPageTitle(ChangeYourImportDetails.pageTitle)
    ChangeYourImportDetails.saveAndContinue()
  }

  def vatWarningsImportJourney(): Unit = {
    logStep("I check Import VAT amounts")
    CheckYourImportDetails.clickChangeLink("VAT claim")
    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("3000.99")

    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()
    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)

    CheckYourImportDetails.clickChangeLink("VAT paid")
    TotalVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalVatPaid.submitTotalVatPaid("3000.99")

    CheckVATAmount.verifyPageTitle(CheckVATAmount.pageTitle)
    CheckVATAmount.continue()

    TotalVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalVatClaim.submitTotalVatClaim("4000.99")

    CheckVATClaim.verifyPageTitle(CheckVATClaim.pageTitle)
    CheckVATClaim.continue()
    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
  }

  def goToCheckYourImportDetails(): Unit = {
    logStep("I navigate to the Import CYA page")
    MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/import/check-your-answers")
    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
  }

}
