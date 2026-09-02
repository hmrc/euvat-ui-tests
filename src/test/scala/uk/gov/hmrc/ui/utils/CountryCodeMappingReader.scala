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

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import java.io.InputStream
import scala.jdk.CollectionConverters.*

final case class MappingRow(
  country: String,
  countryCode: String,
  code: String,
  codeLabel: String,
  subCode: Option[String],
  subCodeLabel: Option[String],
  subCategoryCode: Option[String],
  subCategoryLabel: Option[String],
  title: Option[String]
)

object CountryCodeMappingReader {

  def loadFromResource(resourcePath: String = "/CountryCodeMapping.xlsx"): Seq[MappingRow] = {
    val is = Option(getClass.getResourceAsStream(resourcePath))
      .getOrElse(throw new IllegalArgumentException(s"Missing resource: $resourcePath"))
    load(is)
  }

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

  private def parseRow(row: Row): Option[MappingRow] = {
    val country     = cell(row, 0)
    val countryCode = cell(row, 1)
    val codeRaw     = cell(row, 2)
    if (countryCode.isEmpty || codeRaw.isEmpty) None
    else {
      val (code, codeLabel)       = split(codeRaw)
      val (subCode, subCodeLabel) = splitOptional(cell(row, 3))
      val (subCat, subCatLabel)   = splitOptional(cell(row, 4))
      val title                   = opt(cell(row, 7))

      Some(
        MappingRow(
          country = country,
          countryCode = countryCode,
          code = code,
          codeLabel = codeLabel,
          subCode = subCode,
          subCodeLabel = subCodeLabel,
          subCategoryCode = subCat,
          subCategoryLabel = subCatLabel,
          title = title
        )
      )
    }
  }

  private def cell(row: Row, idx: Int): String =
    Option(row.getCell(idx)).map(_.toString.trim).getOrElse("")

  private def opt(s: String): Option[String] =
    Option(s).map(_.trim).filter(_.nonEmpty)

  private def split(s: String): (String, String) = {
    val t = s.trim
    val i = t.indexWhere(_.isWhitespace)
    if (i < 0) (t, "")
    else (t.substring(0, i).trim, t.substring(i + 1).trim)
  }

  private def splitOptional(raw: String): (Option[String], Option[String]) = {
    val t = raw.trim
    if (t.isEmpty || t.equalsIgnoreCase("none")) (None, None)
    else {
      val (a, b) = split(t)
      (Some(a), Option(b).filter(_.nonEmpty))
    }
  }
}
