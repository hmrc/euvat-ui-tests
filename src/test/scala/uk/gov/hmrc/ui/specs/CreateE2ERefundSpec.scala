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
import uk.gov.hmrc.selenium.webdriver.{Browser, Driver, ScreenshotOnFailure}
import uk.gov.hmrc.ui.flows.*
import uk.gov.hmrc.ui.flows.TestData.*
import uk.gov.hmrc.ui.tags.*
import uk.gov.hmrc.ui.utils.{DatabaseHelper, MongoHelper}

class CreateE2ERefundSpec
    extends AnyFeatureSpec
    with BaseSpec
    with GivenWhenThen
    with ShouldVerb
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Browser
    with ScreenshotOnFailure
    with MongoHelper
    with DatabaseHelper
    with ClaimFlows
    with PurchaseFlows
    with ImportFlows
    with BankFlows
    with SupportingDocumentsFlows {

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropMongoCollections()
    cleanupDatabaseIfNotStub()
  }

  Feature("Make a new EUVAT claim - New end to end submission") {

    Scenario("01 - Submit an end to end refund request", Jenkins, Local) {
      loginAndOpenClaim("999900001")
      addClaimDetails(croatiaClaim)
      changeEUMemberState("Estonia")
      saveClaimDetails()
      changeClaimDetailsAfterSave()
      saveClaimDetails()

      startPurchaseFlow()
      addFoodPurchaseFlow()
      addStandardInvoiceFlow()
      addPurchaseCurrencyFlow()
      addPurchaseAmountFlow()

      changePurchaseJourney()
      summaryFlowYes()

      addImportFlow()
      addFoodImportFlow()
      addSADFlow(sadRefData)
      addImportCurrencyFlow()
      addImportAmountFlow()

      changeImportJourney()
      summaryFlowNo()

      startBankDetailsFlow()
      addBankDetails(initialBankDetails)
      changeBankDetails(changedBankDetails)
      saveBankDetails()

      startSupportingDocumentsFlow()
      addSupportingDocuments(supportingDocumentsYes)
      clickSignOut(Driver.instance)
    }

    Scenario("02 - Submit an end to end refund request for Germany", Jenkins, Local) {
      loginAndOpenClaim("999900001")
      addClaimDetails(germanyClaim)
      saveClaimDetails()
      changeClaimDetailsAddSicCodesAfterSave()
      changeClaimDetailsRemoveSicCodesAfterSave()

      startPurchaseFlow()
      addOtherPurchaseFlow()
      addSimplifiedInvoiceFlow()
      addPurchaseAmountFlow()
      savePurchase()

      changeTaxIdDetails()
      summaryFlowYes()

      addImportFlow()
      addOtherImportFlow()
      addSADFlow(sadDocumentData)
      addImportAmountFlow()
      saveImport()
      summaryFlowNo()

      startBankDetailsFlow()
      addBankDetails(initialBankDetails)
      saveBankDetails()

      startSupportingDocumentsFlow()
      addSupportingDocuments(supportingDocumentsNo)
      clickSignOut(Driver.instance)
    }
  }
}
