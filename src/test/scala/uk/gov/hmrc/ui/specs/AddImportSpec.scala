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

class AddImportSpec
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
    with ImportFlows {

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropMongoCollections()
    cleanupDatabaseIfNotStub()
  }

  Feature("Make a new EUVAT claim - Add a duplicate import") {
    Scenario("01 - Create 2 imports with a duplicate SAD reference number", Local) {
      val sharedId = loginAndInjectClaimDetails("999900001")
      goToCheckYourClaimDetails()
      saveClaimDetails()

      startImportFlow()
      addFoodImportType()
      injectImportDetailsAfterType(sharedId)
      goToCheckYourImportDetails()
      saveImport()
      summaryFlowYes()

      addImportFlow()
      addFoodImportFlow()
      duplicateSADFlow(sadRefData)
      addImportCurrencyFlow()
      addImportAmountFlow()
      saveImport()
      summaryFlowNo()
      clickSignOut(Driver.instance)
    }

    Scenario("02 - Create 2 imports with duplicate import document details for Germany", Local) {
      val sharedId = loginAndInjectClaimDetailsGermany("999900001")
      goToCheckYourClaimDetails()
      saveClaimDetails()

      startImportFlow()
      addFoodImportType()
      injectImportDetailsAfterTypeGermany(sharedId)
      goToCheckYourImportDetails()
      saveImport()
      summaryFlowYes()

      addImportFlow()
      addOtherImportFlow()
      duplicateSADFlow(sadDocumentData)
      addImportAmountFlow()
      saveImport()
      summaryFlowNo()
      clickSignOut(Driver.instance)
    }

    Scenario("03 - Change your answers for an Import refund request", Jenkins, Local) {
      loginAndOpenClaim("999900001")
      addClaimDetails(estoniaClaim)
      saveClaimDetails()

      startImportFlow()
      addFoodImportFlow()
      addSADFlow(sadRefData)
      addImportCurrencyFlow()
      addImportAmountFlow()
      saveImport()

      changeImportSummaryFlow("Import (Food, drink and restaurant services)")
      changeImportAfterSave()
      clickSignOut(Driver.instance)
    }
  }
}
