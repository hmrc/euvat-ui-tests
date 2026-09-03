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

import org.scalatest.BeforeAndAfterEach
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.verbs.ShouldVerb
import uk.gov.hmrc.selenium.webdriver.{Browser, ScreenshotOnFailure}
import uk.gov.hmrc.ui.pages.{AuthorityWizard, ClaimAnEUVATRefund}
import uk.gov.hmrc.ui.pages.claim.*
import uk.gov.hmrc.ui.pages.purchase.*
import uk.gov.hmrc.ui.tags.Local
import uk.gov.hmrc.ui.utils.CountryCodeMappingReader
import uk.gov.hmrc.ui.utils.PurchaseFlowRouter

class VatLabelsStrictSpec
    extends AnyFeatureSpec
    with BaseSpec
    with GivenWhenThen
    with ShouldVerb
    with BeforeAndAfterEach
    with Browser
    with ScreenshotOnFailure {

  private val rows = CountryCodeMappingReader.loadFromResource()

  private def expectedSubCodeLabels(countryCode: String, code: String): Seq[String] =
    rows
      .filter(r => r.countryCode == countryCode && r.code == code)
      .flatMap(_.subCodeLabel)
      .distinct

  private def expectedSubCategoryLabels(countryCode: String, code: String, subCode: String): Seq[String] =
    rows
      .filter(r => r.countryCode == countryCode && r.code == code && r.subCode.contains(subCode))
      .flatMap(_.subCategoryLabel)
      .distinct

  private def navigateToPurchaseType(countryName: String): Unit = {
    AuthorityWizard.login("Organisation", "999900001")
    ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

    ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

    MakeEuvatClaim.clickLinkByText("Add claim details")
    EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
    EUMemberState.selectCountry(countryName)

    if (
      Language.getCurrentUrlInBrowser.contains(Language.pageUrl) || Language.getPageTitle.contains("claim language")
    ) {
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("English")
    }

    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
    RefundPeriod.submitRefundPeriod("02", "2025", "04", "2025")

    ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
    ContactDetails.submitContactAddress("mapping@test.com", "07700900000")

    AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
    AddBusinessActivity.continueAsNo()

    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
    CheckYourClaimDetails.saveAndContinue()

    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
    MakeEuvatClaim.clickLinkByText("Add a purchase")

    BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
    BeforeYouStart.continue()

    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
  }

  Feature("Purchase mapping labels from CountryCodeMapping.xlsx") {

    rows.map(r => (r.countryCode, r.country)).distinct.foreach { case (countryCode, countryName) =>
      Scenario(s"Validate sub code labels for $countryCode", Local) {
        Given(s"I navigate quickly to PurchaseType for $countryName")
        navigateToPurchaseType(countryName)

        val codesForCountry = rows.filter(_.countryCode == countryCode).map(_.code).distinct.sorted

        codesForCountry.foreach { code =>
          val expectedLabels = expectedSubCodeLabels(countryCode, code)

          if (expectedLabels.nonEmpty) {
            When(s"I select purchase type code $code for $countryCode")
            PurchaseType.selectPurchaseType(PurchaseFlowRouter.purchaseTypeLabelFor(code))

            Then(s"I should see the expected sub code labels for $countryCode / $code")
            val page = PurchaseFlowRouter.topLevelPageFor(code)
            page.assertCurrentPage()
            page.assertLabelsContain(expectedLabels)

            PurchaseType.navigateBack()
            PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
          }
        }
      }

      val subCodeGroups = rows
        .filter(r => r.countryCode == countryCode && r.subCode.nonEmpty)
        .groupBy(r => (r.code, r.subCode.get))

      subCodeGroups.foreach { case ((code, subCode), groupedRows) =>
        val expectedLabels = expectedSubCategoryLabels(countryCode, code, subCode)

        if (expectedLabels.nonEmpty) {
          Scenario(s"Validate sub category labels for $countryCode code=$code subCode=$subCode", Local) {
            Given(s"I navigate quickly to PurchaseType for $countryName")
            navigateToPurchaseType(countryName)

            When(s"I select purchase type $code")
            PurchaseType.selectPurchaseType(PurchaseFlowRouter.purchaseTypeLabelFor(code))

            val topPage = PurchaseFlowRouter.topLevelPageFor(code)
            topPage.assertCurrentPage()

            val subCodeLabel = groupedRows.head.subCodeLabel.get

            And(s"I select sub code $subCodeLabel")
            topPage.selectByVisibleLabel(subCodeLabel)

            Then(s"I should see the expected sub category labels for $countryCode / $code / $subCode")
            val subPage = PurchaseFlowRouter.subCategoryPageFor(code, subCode)
            subPage.assertCurrentPage()
            subPage.assertLabelsContain(expectedLabels)
          }
        }
      }
    }
  }
}
