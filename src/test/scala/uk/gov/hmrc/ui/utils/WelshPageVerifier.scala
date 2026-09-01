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

package uk.gov.hmrc.ui.utils

import org.openqa.selenium.By
import org.scalatest.matchers.should.Matchers._
import uk.gov.hmrc.ui.pages.BasePage

object WelshPageVerifier {

  def normalize(s: String): String =
    Option(s).getOrElse("").replaceAll("\\s+", " ").trim

  def stripErrorPrefix(title: String): String =
    Option(title).getOrElse("").replaceFirst("""^\s*Gwall:\s*""", "").trim

  def verify(sheetName: String, page: BasePage): Unit = {
    val data = WelshExcelReader.readSheet(sheetName)

    require(
      data.pageTitle.nonEmpty || data.pageHeading.nonEmpty || data.messages.nonEmpty,
      s"No Welsh content found in Excel for sheet: $sheetName"
    )

    if (data.pageTitle.nonEmpty) {
      val actualTitle = normalize(stripErrorPrefix(page.getPageTitle))
      println(s"[CHECK] Title | expected='${normalize(data.pageTitle)}' | actual='$actualTitle'")
      actualTitle shouldBe normalize(data.pageTitle)
      println(s"[PASS] Title matched for sheet: $sheetName")
    } else {
      println(s"[INFO] No page title found in Excel for sheet: $sheetName")
    }

    if (data.pageHeading.nonEmpty) {
      val actualHeading = normalize(page.waitForVisibilityOfElement(By.cssSelector("h1")).getText)
      require(actualHeading.nonEmpty, s"Rendered page heading is empty for sheet: $sheetName")
      println(s"[CHECK] Heading | expected='${normalize(data.pageHeading)}' | actual='$actualHeading'")
      actualHeading shouldBe normalize(data.pageHeading)
      println(s"[PASS] Heading matched for sheet: $sheetName")
    } else {
      println(s"[INFO] No page heading found in Excel for sheet: $sheetName")
    }

    require(data.messages.nonEmpty, s"No Welsh message rows to verify for sheet: $sheetName")

    data.messages.foreach { msg =>
      require(
        msg.expectedWelsh.nonEmpty,
        s"Expected Welsh content is empty in Excel for key=${msg.key}, sheet=$sheetName"
      )

      if (!msg.key.contains(".error.")) {
        val actual = normalize(textFor(msg.key, page))
        require(
          actual.nonEmpty,
          s"Rendered Welsh content is empty on page for key=${msg.key}, sheet=$sheetName"
        )

        println(
          s"[CHECK] key=${msg.key} | expected='${normalize(msg.expectedWelsh)}' | actual='$actual'"
        )

        withClue(s"Sheet=$sheetName key=${msg.key}: ") {
          actual shouldBe normalize(msg.expectedWelsh)
        }

        println(s"[PASS] key=${msg.key} matched for sheet: $sheetName")
      } else {
        println(s"[INFO] Skipping error key during standard verification: ${msg.key}")
      }
    }

    println(s"[PASS] Welsh verification completed successfully for sheet: $sheetName")
  }

  private def textFor(key: String, page: BasePage): String = key match {
    case "ite.back" | "site.back" =>
      page.waitForVisibilityOfElement(By.cssSelector("a.govuk-back-link")).getText

    case k if k.contains(".caption.") =>
      page.waitForVisibilityOfElement(By.cssSelector(".govuk-caption-l")).getText

    case k if k.endsWith(".title") || k.endsWith(".heading") =>
      page.waitForVisibilityOfElement(By.cssSelector("h1")).getText

    case k if k.endsWith(".hint") =>
      page.waitForVisibilityOfElement(By.cssSelector(".govuk-hint")).getText

    case "site.continue" =>
      page.waitForVisibilityOfElement(By.cssSelector(".govuk-button")).getText

    case "site.yes" =>
      page
        .waitForVisibilityOfElement(
          By.xpath("//*[normalize-space()='Iawn' or normalize-space()='Yes']")
        )
        .getText

    case "site.no" =>
      page
        .waitForVisibilityOfElement(
          By.xpath("//*[normalize-space()='Na' or normalize-space()='No']")
        )
        .getText

    case "refundingCurrency.euro" =>
      page.waitForVisibilityOfElement(By.cssSelector("label[for='value']")).getText

    case "refundingCurrency.estonianKroon" =>
      page.waitForVisibilityOfElement(By.cssSelector("label[for='value_1'], label[for='value-2']")).getText

    case other =>
      throw new IllegalArgumentException(s"Add Welsh locator mapping for key: $other")
  }
}
