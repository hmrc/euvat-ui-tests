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
import uk.gov.hmrc.selenium.webdriver.Driver
import uk.gov.hmrc.ui.flows.*
import uk.gov.hmrc.ui.flows.TestData.*
import uk.gov.hmrc.ui.tags.*
import uk.gov.hmrc.ui.utils.{DatabaseHelper, MongoHelper}

class DeleteClaimSpec
    extends BaseSpec
    with BeforeAndAfterAll
    with MongoHelper
    with DatabaseHelper
    with ClaimFlows
    with PurchaseFlows
    with ImportFlows {

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropMongoCollections()
    cleanupDatabaseIfNotStub()
  }

  Feature("Delete a draft EUVAT claim - Delete claim") {

    Scenario("01 - Delete a refund claim from Claim details page", Local) {
      loginAndOpenClaim("999900001")
      addClaimDetails(estoniaClaim)
      saveClaimDetails()

      deleteClaimFromClaimDetails()
      clickSignOut(Driver.instance)
    }

    Scenario("02 - Delete a refund claim from Make a claim for an EU VAT refund page", Local) {
      loginAndOpenClaim("999900001")
      addClaimDetails(estoniaClaim)
      saveClaimDetails()

      startPurchaseFlow()
      addFoodPurchaseFlow()
      addStandardInvoiceFlow()
      addPurchaseCurrencyFlow()
      addPurchaseAmountFlow()
      savePurchase()
      summaryFlowYes()

      addImportFlow()
      addFoodImportFlow()
      addSADFlow(sadRefData)
      addImportCurrencyFlow()
      addImportAmountFlow()
      saveImport()
      summaryFlowNo()

      deleteClaimFromMakeClaim()
      clickSignOut(Driver.instance)
    }

    Scenario("03 - Delete a purchase and import from the Purchase & Import Summary page", Jenkins, Local) {
      loginAndOpenClaim("999900001")
      addClaimDetails(estoniaClaim)
      saveClaimDetails()

      startPurchaseFlow()
      addFoodPurchaseFlow()
      addStandardInvoiceFlow()
      addPurchaseCurrencyFlow()
      addPurchaseAmountFlow()
      savePurchase()
      summaryFlowYes()

      addImportFlow()
      addFoodImportFlow()
      addSADFlow(sadRefData)
      addImportCurrencyFlow()
      addImportAmountFlow()
      saveImport()

      deleteClaimFromSummary()
      clickSignOut(Driver.instance)
    }

  }
}
