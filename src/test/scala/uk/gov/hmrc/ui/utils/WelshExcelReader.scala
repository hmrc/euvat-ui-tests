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

import java.io.InputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import scala.jdk.CollectionConverters._

case class WelshPageData(
  url: String,
  pageTitle: String,
  pageHeading: String,
  messages: Seq[WelshMessage]
)

case class WelshMessage(
  key: String,
  messageType: String,
  expectedWelsh: String
)

object WelshExcelReader {

  def readSheet(sheetName: String, fileName: String = "welsh-data.xlsx"): WelshPageData = {
    val stream: InputStream =
      getClass.getClassLoader.getResourceAsStream(fileName)

    require(stream != null, s"$fileName not found in test resources")

    val workbook = new XSSFWorkbook(stream)

    try {
      val sheet = workbook.getSheet(sheetName)
      require(sheet != null, s"Sheet not found: $sheetName")

      val rows = sheet.iterator().asScala.toSeq.map(_.cellIterator().asScala.toSeq.map(cellValue))

      val url         = findValue(rows, "URL")
      val pageTitle   = findValue(rows, "Page title")
      val pageHeading = findValue(rows, "Page heading")

      val messagesStartIndex =
        rows.indexWhere(r => r.headOption.exists(_.trim.equalsIgnoreCase("Messages label")))

      val messages =
        if (messagesStartIndex == -1) Seq.empty
        else {
          rows.drop(messagesStartIndex + 1).flatMap { r =>
            val key     = r.lift(0).getOrElse("").trim
            val msgType = r.lift(1).getOrElse("").trim
            val welsh   = r.lift(2).getOrElse("").trim

            val skip =
              key.isEmpty ||
                key.equalsIgnoreCase("Error Messages") ||
                key.startsWith("*") ||
                key.toLowerCase.contains("table below") ||
                key.toLowerCase.contains("languages table")

            if (skip || welsh.isEmpty) None
            else Some(WelshMessage(key, msgType, welsh))
          }
        }

      WelshPageData(url, pageTitle, pageHeading, messages)

    } finally {
      workbook.close()
      stream.close()
    }
  }

  private def findValue(rows: Seq[Seq[String]], label: String): String =
    rows
      .find(r => r.headOption.exists(_.trim.equalsIgnoreCase(label)))
      .flatMap(_.lift(2))
      .getOrElse("")
      .trim

  private def cellValue(cell: Cell): String =
    cell.toString.trim
}
