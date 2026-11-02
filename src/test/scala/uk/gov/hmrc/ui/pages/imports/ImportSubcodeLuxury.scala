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

import uk.gov.hmrc.ui.pages.BasePage

object ImportSubcodeLuxury extends BasePage {

  override def pageUrl: String = "import/luxury-entertainment-hospitality-cost"

  override def pageTitle: String = "What is the type of luxury, entertainment or hospitality cost? - EU VAT - GOV.UK"

  // TODO options differ based on country, value attribute may be better option than ID
//  val rdoAlcohol = "#value_0"
//  val rdoTobaccoProd       = "#value_1"
  val rdoReceptions = "#value_0"
//  val rdoMainPleasureCraft = "#value_3"
//  val rdoArtAntiques = "#value_4"
//  val rdoLuxEntHospAdv = "#value_5"
//  val rdoOtherLuxEndHosp = "#value_6"
  val rdoNone       = "#value_1"

  def selectLuxuryType(radio: String): this.type = {
    val selector = radio match {
//      case "Alcohol"                                                 => rdoAlcohol
//      case "Tobacco products"                                        => rdoTobaccoProd
      case "Receptions, entertainment and hospitality" => rdoReceptions
//      case "Maintenance of pleasure craft"                           => rdoMainPleasureCraft
//      case "Works of art, collectors’ items and antiques"            => rdoArtAntiques
//      case "Luxuries, entertainment and hospitality for advertising" => rdoLuxEntHospAdv
//      case "Other luxuries, entertainment and hospitality"           => rdoOtherLuxEndHosp
      case "None"                                      => rdoNone
      case _                                           => throw new IllegalArgumentException(s"Invalid option: $radio")
    }
    radioButton(selector)
    continue()
    this
  }

}
