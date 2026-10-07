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
import uk.gov.hmrc.ui.tags.*
import uk.gov.hmrc.ui.utils.{DatabaseHelper, MongoHelper}
import TestData.*

class NewEuvatClaimSpec
  extends BaseSpec
    with BeforeAndAfterAll
    with MongoHelper
    with DatabaseHelper
    with ClaimFlows
    with PurchaseFlows {

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropMongoCollections()
    cleanupDatabaseIfNotStub()
  }

  Feature("Make a new EUVAT claim - New claim") {

    Scenario("01 - Submit a refund request", Local) {
      loginAndOpenNewClaim()
      addClaimDetails(croatiaClaim)
      changeClaimDetailsJourney()
      saveClaimDetails()

      insertDuplicatePurchaseRecordVRN()
      startPurchaseFlow()
      addFoodPurchaseFlow()
      addStandardInvoiceFlow()
      addCurrencyFlow()
      addPurchaseAmountFlow()

      changePurchaseJourney()
      vatWarningsJourney()
      savePurchase()
      clickSignOut(Driver.instance)
    }

    Scenario("02 - Submit a refund request for Germany", Local, WIP) {
      loginAndOpenNewClaim()
      addClaimDetails(germanyClaim)
      saveClaimDetails()

      insertDuplicatePurchaseRecordTID()
      startPurchaseFlow()
      addOtherPurchaseFlow()
      addSimplifiedInvoiceFlow()
      addPurchaseAmountFlow()

      changeTaxIdDetails()
      savePurchase()
      clickSignOut(Driver.instance)
    }
  }
}
