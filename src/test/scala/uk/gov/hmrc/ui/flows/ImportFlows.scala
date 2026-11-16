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

trait ImportFlows {

  def addImportFlow(): Unit = {
    AddPurchaseImport.verifyPageTitle(AddPurchaseImport.pageTitle)
    AddPurchaseImport.selectPurchaseOrImport("Import")
  }

  def addFoodImportFlow(): Unit = {
    ImportType.verifyPageTitle(ImportType.pageTitle)
    ImportType.selectImportType("Food, drink and restaurant services")

    ImportSubcodeFood.verifyPageTitle(ImportSubcodeFood.pageTitle)
    ImportSubcodeFood.selectImportFoodType("Food and drink from hotels")

    ImportSubcategoryFood.verifyPageTitle(ImportSubcategoryFood.pageTitle)
    ImportSubcategoryFood.selectWhoFoodFor("The taxable person")
  }

  def addSADFlow(data: SadFlowData): Unit = {
    AddSADRefNumber.verifyPageTitle(AddSADRefNumber.pageTitle)

    data match {
      case sadRef: SadReferenceData =>
        AddSADRefNumber.continueAsYes()
        SubmitSADRefNumber.verifyPageTitle(SubmitSADRefNumber.pageTitle)
        SubmitSADRefNumber.submitSADRefNumber(sadRef.referenceNumber)

      case sadDoc: SadDocumentData =>
        AddSADRefNumber.continueAsNo()
        ImportDocumentDetails.verifyPageTitle(ImportDocumentDetails.pageTitle)
        ImportDocumentDetails.submitDocumentDetails(sadDoc.documentDetails)
    }

    ImportDate.verifyPageTitle(ImportDate.pageTitle)
    ImportDate.submitImportDate(data.importDay, data.importMonth, data.importYear)

    SubmitSupplierName.verifyPageTitle(SubmitSupplierName.pageTitle)
    SubmitSupplierName.submitSupplierName(data.supplierName)

    ImportSupplierAddress.verifyPageTitle(ImportSupplierAddress.pageTitle)
    ImportSupplierAddress.submitImportSupplierAddress(
      data.supplierAddress1,
      data.supplierAddress2,
      data.supplierAddress3,
      data.supplierCountry
    )
  }

  def addImportCurrencyFlow(): Unit = {
    ImportCurrency.verifyPageTitle(ImportCurrency.pageTitle)
    ImportCurrency.selectImportCurrencyType("Euro")
  }

  def addImportAmountFlow(): Unit = {
    TotalImportAmount.verifyPageTitle(TotalImportAmount.pageTitle)
    TotalImportAmount.submitTotalImportAmount("1000.01")

    TotalImportVatPaid.verifyPageTitle(TotalVatPaid.pageTitle)
    TotalImportVatPaid.submitTotalImportVATPaid("200.01")

    TotalImportVatClaim.verifyPageTitle(TotalVatClaim.pageTitle)
    TotalImportVatClaim.submitTotalImportVatClaim("100.01")

    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
  }

  /** Food/drink journey kept aligned to original working spec: ImportType -> FoodDrink -> WhoFoodDrink -> InvoiceType
    */

  /** Germany/Other journey kept aligned to original working spec: ImportType -> ImportTypeOther ->
    * InvoiceItemDescription -> CheckImportDetails -> InvoiceType
    */
  def addOtherImportFlow(): Unit = {
    ImportType.verifyPageTitle(ImportType.pageTitle)
    ImportType.selectImportType("Other")
    ImportSubcodeOther.verifyPageTitle(ImportSubcodeOther.pageTitle)
    ImportSubcodeOther.selectImportTypeOther("None of these - give more details")
    ListImportDocumentItems.verifyPageTitle(ListImportDocumentItems.pageTitle)
    ListImportDocumentItems.submitImportDocumentList("Listed items from import document")
  }

  def saveImport(): Unit =
    CheckYourImportDetails.saveAndContinue()

  def changeImportJourney(): Unit = {
    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
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
    SubmitSupplierName.verifyPageTitle(SubmitSupplierName.pageTitle)
    SubmitSupplierName.submitSupplierName("Update Test Supplier Name")
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
    CheckYourImportDetails.saveAndContinue()
  }

  def summaryFlow2(): Unit =
    PurchaseImportSummary.verifyPageTitle(PurchaseImportSummary.pageTitle("2"))
    PurchaseImportSummary.continueAsNo()
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

  def vatWarningsImportJourney(): Unit = {
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

  def goToSaveImportDetails(): Unit = {
    MakeEuvatClaim.navigateToPage("http://localhost:18501/file-eu-vat/import/check-your-answers")
    CheckYourImportDetails.verifyPageTitle(CheckYourImportDetails.pageTitle)
    CheckYourImportDetails.saveAndContinue()
  }

}
