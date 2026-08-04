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

package uk.gov.hmrc.ui.pages

object LuxuryEntertainment extends BasePage {

  override def pageUrl: String = "luxury-entertainment-hospitality-cost"

  override def pageTitle: String = "What is the type of luxury, entertainment or hospitality cost? - EU VAT - GOV.UK"

  val rdoReceptions = "#value_0"
  val rdoNone       = "#value_1"

  def selectLuxuryType(radio: String): this.type = {
    val selector = radio match {
      case "Receptions, entertainment and hospitality" => rdoReceptions
      case "None"                                      => rdoNone
      case _                                           => throw new IllegalArgumentException(s"Invalid option: $radio")
    }
    radioButton(selector)
    continue()
    this
  }

}
