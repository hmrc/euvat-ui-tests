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

class AddPurchaseSpec
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

  Feature("Make a new EUVAT claim - Add a duplicate purchase") {
    Scenario("01 - Create 2 purchases with a duplicate Invoice number and Tax Identifier number", Local) {
      val sharedId = loginAndInjectClaimDetails("999900001")
      goToCheckYourClaimDetails()
      saveClaimDetails()

      startPurchaseFlow()
      addFoodPurchaseType()
      injectPurchaseDetailsAfterType(sharedId)
      goToCheckYourPurchaseDetails()
      savePurchase()
      summaryFlowYes()

      addPurchaseFlow()
      addFoodPurchaseFlow()
      duplicateVRNFlow()
      addPurchaseCurrencyFlow()
      addPurchaseAmountFlow()
      savePurchase()
      summaryFlowNo()
      clickSignOut(Driver.instance)
    }

    Scenario("02 - Create 2 purchases with a duplicate Invoice number and VRN for Germany", Local) {
      val sharedId = loginAndInjectClaimDetailsGermany("999900001")
      goToCheckYourClaimDetails()
      saveClaimDetails()

      startPurchaseFlow()
      addOtherPurchaseType()
      injectPurchaseDetailsAfterTypeGermany(sharedId)
      goToCheckYourPurchaseDetails()
      savePurchase()
      summaryFlowYes()

      addPurchaseFlow()
      addOtherPurchaseFlow()
      duplicateTIDFlow()
      addPurchaseAmountFlow()
      savePurchase()
      summaryFlowNo()

      clickSignOut(Driver.instance)
    }
//  This scenario is incomplete due to a bug with the stub data
    Scenario("03 - Change your answers for a purchase after saving", Jenkins, Local) {
      loginAndOpenClaim("999900001")
      addClaimDetails(estoniaClaim)
      saveClaimDetails()

      startPurchaseFlow()
      addFoodPurchaseFlow()
      addStandardInvoiceFlow()
      addPurchaseCurrencyFlow()
      addPurchaseAmountFlow()
      savePurchase()

      changeSummaryFlow()
//      Cannot run this step due to the stub data
//      changePurchaseAfterSave()

      clickSignOut(Driver.instance)
    }
  }
}
