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

package uk.gov.hmrc.ui.pages.supportingdocuments

import org.openqa.selenium.By
import uk.gov.hmrc.ui.pages.BasePage

object UploadFile extends BasePage {

  override def pageUrl: String = "supporting-documents/upload-file"

  override def pageTitle: String = "Upload file - EU VAT - GOV.UK"

  val inputAdditionalInfo: By = By.cssSelector("#fileupload-description")

  def inputAdditionalInfo(AdditionalInfo: String): Unit =
    input(inputAdditionalInfo, AdditionalInfo)

}
