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

import org.apache.poi.ss.usermodel.{CellStyle, FillPatternType, IndexedColors}
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.verbs.ShouldVerb
import uk.gov.hmrc.selenium.webdriver.{Browser, ScreenshotOnFailure}
import uk.gov.hmrc.ui.pages.{AuthorityWizard, ClaimAnEUVATRefund}
import uk.gov.hmrc.ui.pages.claim.*
import uk.gov.hmrc.ui.pages.purchase.*
import uk.gov.hmrc.ui.tags.Local
import uk.gov.hmrc.ui.utils.{CountryCodeMappingReader, MappingRow, MongoHelper, PurchaseFlowRouter}

import java.io.{File, FileOutputStream}
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
    with MongoHelper
    with ScreenshotOnFailure {

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropMongoCollections()
  }

  private val NoneLabel = "None"

  private val rows     = CountryCodeMappingReader.loadFromResource()
  private val failures = ListBuffer.empty[MappingFailure]

  private def writeXlsxReport(): Unit = {
    val dir = new File("target")
    if (!dir.exists()) dir.mkdirs()

    val file = new File(dir, "vat-label-mapping-failures.xlsx")

    val workbook = new XSSFWorkbook()
    try {
      val sheet = workbook.createSheet("Failures")

      val headerStyle: CellStyle = {
        val style = workbook.createCellStyle()
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex)
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND)
        style
      }

      val wrapStyle: CellStyle = {
        val style = workbook.createCellStyle()
        style.setWrapText(true)
        style
      }

      val headers = Seq(
        "countryCode",
        "countryName",
        "code",
        "subCode",
        "expectedPage",
        "actualPage",
        "message",
        "expectedLabels",
        "actualLabels"
      )

      val headerRow = sheet.createRow(0)
      headers.zipWithIndex.foreach { case (h, i) =>
        val cell = headerRow.createCell(i)
        cell.setCellValue(h)
        cell.setCellStyle(headerStyle)
      }

      failures.zipWithIndex.foreach { case (f, idx) =>
        val row = sheet.createRow(idx + 1)

        row.createCell(0).setCellValue(f.countryCode)
        row.createCell(1).setCellValue(f.countryName)
        row.createCell(2).setCellValue(f.code)
        row.createCell(3).setCellValue(f.subCode.getOrElse(""))
        row.createCell(4).setCellValue(f.expectedPage)
        row.createCell(5).setCellValue(f.actualPage)

        val messageCell = row.createCell(6)
        messageCell.setCellValue(f.message)
        messageCell.setCellStyle(wrapStyle)

        val expectedCell = row.createCell(7)
        expectedCell.setCellValue(f.expectedLabels.mkString(" | "))
        expectedCell.setCellStyle(wrapStyle)

        val actualCell = row.createCell(8)
        actualCell.setCellValue(f.actualLabels.mkString(" | "))
        actualCell.setCellStyle(wrapStyle)
      }

      sheet.createFreezePane(0, 1)
      sheet.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, headers.size - 1))
      headers.indices.foreach(sheet.autoSizeColumn)

      val out = new FileOutputStream(file)
      try workbook.write(out)
      finally out.close()
    } finally workbook.close()
  }

  private def topLevelRows(countryCode: String, code: String): Seq[MappingRow] =
    rows.filter(r => r.countryCode == countryCode && r.code == code)

  private def expectedSubCodeLabels(countryCode: String, code: String): Seq[String] =
    topLevelRows(countryCode, code)
      .flatMap(_.subCodeLabel)
      .filter(_.nonEmpty)
      .distinct

  private def expectedSubCategoryLabels(countryCode: String, code: String, subCode: String): Seq[String] =
    rows
      .filter(r => r.countryCode == countryCode && r.code == code && r.subCode.contains(subCode))
      .flatMap(_.subCategoryLabel)
      .filter(_.nonEmpty)
      .distinct

  private def navigateToPurchaseType(countryName: String): Unit = {
    AuthorityWizard.login("Organisation", "999900001")
    ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

    ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

    MakeEuvatClaim.clickLinkByText("Add claim details")
    EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
    EUMemberState.selectCountry(countryName)

    countryName match {
      case "Croatia" | "Czech Republic" =>
        RefundPeriod.waitForPage()
      case _                            =>
        Language.waitForPage()
    }

    val purchaseTypeUrl = "http://localhost:18501/file-eu-vat/purchase-type"

    var attempts = 0
    var loaded   = false

    while (attempts < 3 && !loaded) {
      Language.navigateToPage(purchaseTypeUrl)
      try {
        PurchaseType.waitForPageTitle(PurchaseType.pageTitle)
        loaded = true
      } catch {
        case _: Throwable =>
          attempts += 1
          PurchaseType.pause(1)
      }
    }

    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
  }

  private def possibleRoutingHint(actualLabels: Seq[String]): String =
    if (actualLabels.isEmpty) "\nHint: possible page load/routing issue" else ""

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
      message = message + possibleRoutingHint(actualLabels)
    )

  private def verifyTopLevelLabels(
    countryCode: String,
    countryName: String,
    code: String,
    page: GenericRadioPage,
    expectedLabels: Seq[String]
  ): Unit = {
    page.waitForPage()
    page.assertCurrentPage()
    val actualLabels = page.availableLabels()

    withClue(
      s"""
         |Sub-code label mismatch
         |country=$countryCode
         |countryName=$countryName
         |code=$code
         |expected=${expectedLabels.sorted.mkString("[", ", ", "]")}
         |actual=${actualLabels.sorted.mkString("[", ", ", "]")}
         |${possibleRoutingHint(actualLabels)}
         |""".stripMargin
    ) {
      page.assertLabelsContain(expectedLabels)
    }
  }

  private def verifySubCategoryLabels(
    countryCode: String,
    countryName: String,
    code: String,
    subCode: String,
    subCodeLabel: String,
    page: GenericRadioPage,
    expectedLabels: Seq[String]
  ): Unit = {
    page.waitForPage()
    page.assertCurrentPage()
    val actualLabels = page.availableLabels()

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
         |expectedPage=${page.pageTitle}
         |actualPage=${page.getPageTitle}
         |${possibleRoutingHint(actualLabels)}
         |""".stripMargin
    ) {
      page.assertLabelsContain(expectedLabels)
    }
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
        writeXlsxReport()
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
            val page = PurchaseFlowRouter.topLevelPageFor(code)

            try
              verifyTopLevelLabels(countryCode, countryName, code, page, subLabels)
            catch {
              case e: Throwable =>
                val actualLabels =
                  try page.availableLabels()
                  catch { case _: Throwable => Seq.empty[String] }
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
        .filter(r =>
          r.countryCode == countryCode &&
            r.subCode.nonEmpty &&
            r.subCodeLabel.nonEmpty &&
            !r.subCode.contains(NoneLabel)
        )
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
              topPage.waitForPage()
              topPage.assertCurrentPage()
            }

            And(s"I select sub code $subCodeLabel")
            topPage.selectByVisibleLabel(subCodeLabel)

            Then(s"I should see the expected sub category labels")
            val subPage = PurchaseFlowRouter.subCategoryPageFor(code, subCode)

            try
              verifySubCategoryLabels(countryCode, countryName, code, subCode, subCodeLabel, subPage, expectedLabels)
            catch {
              case e: Throwable =>
                val actualLabels =
                  try subPage.availableLabels()
                  catch { case _: Throwable => Seq.empty[String] }
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
