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

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.verbs.ShouldVerb
import uk.gov.hmrc.selenium.webdriver.{Browser, ScreenshotOnFailure}
import uk.gov.hmrc.ui.pages.{AuthorityWizard, ClaimAnEUVATRefund}
import uk.gov.hmrc.ui.pages.claim.*
import uk.gov.hmrc.ui.pages.purchase.*
import uk.gov.hmrc.ui.utils.PurchaseFlowRouter
import uk.gov.hmrc.ui.tags.Local
import uk.gov.hmrc.ui.utils.{CountryCodeMappingReader, MappingRow}

import java.io.{File, PrintWriter}
import scala.collection.mutable.ListBuffer

final case class MappingFailure(
  countryCode: String,
  countryName: String,
  code: String,
  subCode: Option[String],
  expectedPage: String,
  actualPage: String,
  expectedLabels: Seq[String],
  actualLabels: Seq[String],
  message: String
)

class VatLabelsStrictSpec
    extends AnyFeatureSpec
    with BaseSpec
    with GivenWhenThen
    with ShouldVerb
    with BeforeAndAfterEach
    with BeforeAndAfterAll
    with Browser
    with ScreenshotOnFailure {

  private val rows     = CountryCodeMappingReader.loadFromResource()
  private val failures = ListBuffer.empty[MappingFailure]

  private def topLevelRows(countryCode: String, code: String): Seq[MappingRow] =
    rows.filter(r => r.countryCode == countryCode && r.code == code)

  private def expectedSubCodeLabels(countryCode: String, code: String): Seq[String] =
    topLevelRows(countryCode, code)
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

  private def recordFailure(
    countryCode: String,
    countryName: String,
    code: String,
    subCode: Option[String],
    expectedPage: String,
    actualPage: String,
    expectedLabels: Seq[String],
    actualLabels: Seq[String],
    message: String
  ): Unit =
    failures += MappingFailure(
      countryCode = countryCode,
      countryName = countryName,
      code = code,
      subCode = subCode,
      expectedPage = expectedPage,
      actualPage = actualPage,
      expectedLabels = expectedLabels.sorted,
      actualLabels = actualLabels.sorted,
      message = message
    )

  private def writeCsvReport(): Unit = {
    val dir = new File("target")
    if (!dir.exists()) dir.mkdirs()

    val file = new File(dir, "vat-label-mapping-failures.csv")
    val pw   = new PrintWriter(file)

    try {
      pw.println("countryCode,countryName,code,subCode,expectedPage,actualPage,message,expectedLabels,actualLabels")
      failures.foreach { f =>
        def esc(s: String): String = "\"" + s.replace("\"", "\"\"") + "\""

        pw.println(
          Seq(
            esc(f.countryCode),
            esc(f.countryName),
            esc(f.code),
            esc(f.subCode.getOrElse("")),
            esc(f.expectedPage),
            esc(f.actualPage),
            esc(f.message),
            esc(f.expectedLabels.mkString(" | ")),
            esc(f.actualLabels.mkString(" | "))
          ).mkString(",")
        )
      }
    } finally pw.close()
  }

  override def afterAll(): Unit =
    try
      if (failures.nonEmpty) {
        println("\n================ VAT LABEL MAPPING FAILURE SUMMARY ================")
        failures.foreach { f =>
          println(
            s"""
               |countryCode=${f.countryCode}
               |countryName=${f.countryName}
               |code=${f.code}
               |subCode=${f.subCode.getOrElse("-")}
               |message=${f.message}
               |expectedPage=${f.expectedPage}
               |actualPage=${f.actualPage}
               |expectedLabels=${f.expectedLabels.mkString("[", ", ", "]")}
               |actualLabels=${f.actualLabels.mkString("[", ", ", "]")}
               |----------------------------------------------------------------
               |""".stripMargin
          )
        }
        writeCsvReport()
      }
    finally
      super.afterAll()

  Feature("Purchase mapping labels from CountryCodeMapping.xlsx") {

    rows.map(r => (r.countryCode, r.country)).distinct.foreach { case (countryCode, countryName) =>
      Seq("1", "3", "7", "9", "10").foreach { code =>
        val subLabels = expectedSubCodeLabels(countryCode, code)

        if (subLabels.nonEmpty) {
          Scenario(s"Validate sub code labels for country=$countryCode code=$code", Local) {
            println(s"[DEBUG] Starting scenario: country=$countryCode countryName=$countryName code=$code subCode=-")

            Given(s"I navigate to PurchaseType for $countryName")
            navigateToPurchaseType(countryName)

            When(s"I select purchase type code $code")
            PurchaseType.selectPurchaseType(PurchaseFlowRouter.purchaseTypeLabelFor(code))

            Then(s"I should see the expected sub code labels")
            val page         = PurchaseFlowRouter.topLevelPageFor(code)
            val actualLabels = page.availableLabels()

            try
              withClue(
                s"""
                   |Sub-code label mismatch
                   |country=$countryCode
                   |countryName=$countryName
                   |code=$code
                   |expected=${subLabels.sorted.mkString("[", ", ", "]")}
                   |actual=${actualLabels.sorted.mkString("[", ", ", "]")}
                   |""".stripMargin
              ) {
                page.assertCurrentPage()
                page.assertLabelsContain(subLabels)
              }
            catch {
              case e: Throwable =>
                recordFailure(
                  countryCode = countryCode,
                  countryName = countryName,
                  code = code,
                  subCode = None,
                  expectedPage = page.pageTitle,
                  actualPage = page.getPageTitle,
                  expectedLabels = subLabels,
                  actualLabels = actualLabels,
                  message = e.getMessage
                )
                throw e
            }
          }
        }
      }

      val subCodeGroups = rows
        .filter(r => r.countryCode == countryCode && r.subCode.nonEmpty)
        .groupBy(r => (r.code, r.subCode.get))

      subCodeGroups.foreach { case ((code, subCode), groupedRows) =>
        val expectedLabels = expectedSubCategoryLabels(countryCode, code, subCode)

        if (expectedLabels.nonEmpty) {
          Scenario(s"Validate sub category labels for country=$countryCode code=$code subCode=$subCode", Local) {
            println(
              s"[DEBUG] Starting scenario: country=$countryCode countryName=$countryName code=$code subCode=$subCode"
            )

            Given(s"I navigate to PurchaseType for $countryName")
            navigateToPurchaseType(countryName)

            When(s"I select purchase type code $code")
            PurchaseType.selectPurchaseType(PurchaseFlowRouter.purchaseTypeLabelFor(code))

            val topPage      = PurchaseFlowRouter.topLevelPageFor(code)
            val subCodeLabel = groupedRows.head.subCodeLabel.get

            withClue(
              s"""
                 |Top-level page mismatch before sub-code selection
                 |country=$countryCode
                 |countryName=$countryName
                 |code=$code
                 |subCode=$subCode
                 |expectedPage=${topPage.pageTitle}
                 |actualPage=${topPage.getPageTitle}
                 |""".stripMargin
            ) {
              topPage.assertCurrentPage()
            }

            And(s"I select sub code $subCodeLabel")
            topPage.selectByVisibleLabel(subCodeLabel)

            Then(s"I should see the expected sub category labels")
            val subPage      = PurchaseFlowRouter.subCategoryPageFor(code, subCode)
            val actualLabels = subPage.availableLabels()

            try
              withClue(
                s"""
                   |Sub-category label mismatch
                   |country=$countryCode
                   |countryName=$countryName
                   |code=$code
                   |subCode=$subCode
                   |subCodeLabel=$subCodeLabel
                   |expected=${expectedLabels.sorted.mkString("[", ", ", "]")}
                   |actual=${actualLabels.sorted.mkString("[", ", ", "]")}
                   |expectedPage=${subPage.pageTitle}
                   |actualPage=${subPage.getPageTitle}
                   |""".stripMargin
              ) {
                subPage.assertCurrentPage()
                subPage.assertLabelsContain(expectedLabels)
              }
            catch {
              case e: Throwable =>
                recordFailure(
                  countryCode = countryCode,
                  countryName = countryName,
                  code = code,
                  subCode = Some(subCode),
                  expectedPage = subPage.pageTitle,
                  actualPage = subPage.getPageTitle,
                  expectedLabels = expectedLabels,
                  actualLabels = actualLabels,
                  message = e.getMessage
                )
                throw e
            }
          }
        }
      }
    }
  }
}
