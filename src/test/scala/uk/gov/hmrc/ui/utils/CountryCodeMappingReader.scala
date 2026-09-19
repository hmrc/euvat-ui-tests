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

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.io.InputStream
import scala.jdk.CollectionConverters.*

/**
 * Represents one parsed row from the CountryCodeMapping.xlsx spreadsheet.
 *
 * Each row describes one valid mapping combination for a country:
 * - top-level code (for example Fuel or Transport)
 * - optional sub code
 * - optional sub-category code
 *
 * Labels are stored separately from codes so tests can assert the UI text
 * shown to the user, not just the numeric identifiers.
 */
final case class MappingRow(
  country: String,
  countryCode: String,
  code: String,
  codeLabel: String,
  subCode: Option[String],
  subCodeLabel: Option[String],
  subCategoryCode: Option[String],
  subCategoryLabel: Option[String]
)

/**
 * Reads CountryCodeMapping.xlsx from the test resources and converts it into
 * structured MappingRow objects that can be used by data-driven UI tests.
 *
 * Expected spreadsheet shape:
 * 0 = Country
 * 1 = Country Code
 * 2 = Code
 * 3 = Sub Codes
 * 4 = Sub Category Codes
 */
object CountryCodeMappingReader {

  /**
   * Special literal used in the spreadsheet to represent a real selectable
   * "None" option in the UI.
   */
  private val NoneValue = "None"

  /**
   * Loads the workbook from the classpath.
   *
   * By default this reads:
   *   /CountryCodeMapping.xlsx
   *
   * @param resourcePath the classpath resource path
   * @return all parsed mapping rows from Sheet1
   */
  def loadFromResource(resourcePath: String = "/CountryCodeMapping.xlsx"): Seq[MappingRow] = {
    val is = Option(getClass.getResourceAsStream(resourcePath))
      .getOrElse(throw new IllegalArgumentException(s"Missing resource: $resourcePath"))
    load(is)
  }

  /**
   * Loads the workbook from an InputStream and parses rows from Sheet1.
   *
   * The header row is skipped and every remaining row is converted to an
   * optional MappingRow. Invalid or incomplete rows are ignored.
   *
   * @param inputStream Excel file input stream
   * @return sequence of parsed MappingRow values
   */
  def load(inputStream: InputStream): Seq[MappingRow] = {
    val wb = new XSSFWorkbook(inputStream)
    try {
      val sheet = wb.getSheet("Sheet1")
      sheet.iterator().asScala.drop(1).flatMap(parseRow).toSeq
    } finally {
      wb.close()
      inputStream.close()
    }
  }

  /**
   * Parses a single Excel row into a MappingRow.
   *
   * A row is ignored if either:
   * - country code is blank
   * - code column is blank
   *
   * Code column is mandatory.
   * Sub code and sub-category code are optional.
   *
   * @param row Excel row
   * @return Some(MappingRow) if valid, otherwise None
   */
  private def parseRow(row: Row): Option[MappingRow] = {
    val country     = cell(row, 0)
    val countryCode = cell(row, 1)
    val codeRaw     = cell(row, 2)

    if (countryCode.isEmpty || codeRaw.isEmpty) None
    else {
      val (code, codeLabel)       = splitRequired(codeRaw)
      val (subCode, subCodeLabel) = splitOptional(cell(row, 3))
      val (subCat, subCatLabel)   = splitOptional(cell(row, 4))

      Some(
        MappingRow(
          country = country,
          countryCode = countryCode,
          code = code,
          codeLabel = codeLabel,
          subCode = subCode,
          subCodeLabel = subCodeLabel,
          subCategoryCode = subCat,
          subCategoryLabel = subCatLabel
        )
      )
    }
  }

  /**
   * Safely reads a cell as trimmed text.
   *
   * If the cell is missing, returns an empty string.
   *
   * @param row row containing the cell
   * @param idx zero-based cell index
   * @return trimmed cell text or empty string
   */
  private def cell(row: Row, idx: Int): String =
    Option(row.getCell(idx)).map(_.toString.trim).getOrElse("")

  /**
   * Splits a required spreadsheet field into:
   * - code/id portion
   * - label portion
   *
   * Example:
   *   "1 Fuel" -> ("1", "Fuel")
   *
   * If no whitespace exists, the same token is used for both code and label.
   *
   * @param raw raw cell text
   * @return (code, label)
   */
  private def splitRequired(raw: String): (String, String) = {
    val t = raw.trim
    val i = t.indexWhere(_.isWhitespace)
    if (i < 0) (t, t)
    else (t.substring(0, i).trim, t.substring(i + 1).trim)
  }

  /**
   * Splits an optional spreadsheet field into:
   * - optional code/id
   * - optional label
   *
   * Rules:
   * - blank cell -> (None, None)
   * - "None" -> (Some("None"), Some("None"))
   * - "17 Property-related costs" -> (Some("17"), Some("Property-related costs"))
   * - "17" -> (Some("17"), Some("17"))
   *
   * This allows the reader to preserve "None" as a real selectable UI option,
   * rather than treating it as missing data.
   *
   * @param raw raw cell text
   * @return optional code and optional label
   */
  private def splitOptional(raw: String): (Option[String], Option[String]) = {
    val t = raw.trim

    if (t.isEmpty) {
      (None, None)
    } else if (t.equalsIgnoreCase(NoneValue)) {
      (Some(NoneValue), Some(NoneValue))
    } else {
      val i = t.indexWhere(_.isWhitespace)
      if (i < 0) {
        // single token like "17" or "None"
        (Some(t), Some(t))
      } else {
        val code  = t.substring(0, i).trim
        val label = t.substring(i + 1).trim
        (Some(code), Some(label))
      }
    }
  }
}