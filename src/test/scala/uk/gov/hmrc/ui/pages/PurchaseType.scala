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

object PurchaseType extends BasePage {

  override def pageUrl: String = "purchase-type"

  override def pageTitle: String = "Purchase type - EU VAT - GOV.UK"

  val rdoFuel      = "#value_0"
  val rdoTransport = "#value_1"
  val rdoFood      = "#value_2"
  val rdoLuxuries  = "#value_3"
  val rdoOther     = "#value_4"

  def selectPurchaseType(radio: String): this.type = {
    val selector = radio match {
      case "Fuel"                                    => rdoFuel
      case "Transport costs"                         => rdoTransport
      case "Food, drink and restaurant services"     => rdoFood
      case "Luxuries, entertainment and hospitality" => rdoLuxuries
      case "Other"                                   => rdoOther
      case _                                         => throw new IllegalArgumentException(s"Invalid option: $radio")
    }
    radioButton(selector)
    continue()
    this
  }

}
