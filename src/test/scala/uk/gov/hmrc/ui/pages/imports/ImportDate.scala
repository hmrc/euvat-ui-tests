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

package uk.gov.hmrc.ui.pages.imports

import org.openqa.selenium.By
import uk.gov.hmrc.ui.pages.BasePage

object ImportDate extends BasePage {

  override def pageUrl: String = "import/import-date"

  override def pageTitle: String = "When is the import date?"

  val txtImportDay: By   = By.ById("value.day")
  val txtImportMonth: By = By.ById("value.month")
  val txtImportYear: By  = By.ById("value.year")

  def submitImportDate(importDay: String, importMonth: String, importYear: String): Unit = {
    input(txtImportDay, importDay)
    input(txtImportMonth, importMonth)
    input(txtImportYear, importYear)
    continue()
  }

}
