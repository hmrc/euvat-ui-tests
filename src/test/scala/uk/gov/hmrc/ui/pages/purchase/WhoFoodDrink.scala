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

package uk.gov.hmrc.ui.pages.purchase

import uk.gov.hmrc.ui.pages.BasePage

object WhoFoodDrink extends BasePage {

  override def pageUrl: String = "who-food-drink-for"

  override def pageTitle: String = "Who is the food and drink for? - EU VAT - GOV.UK"

  val rdoTaxablePerson = "#value_0"
  val rdoSomeoneOther  = "#value_1"
  val rdoNone          = "#value_2"

  def selectWhoFoodDrinkFor(radio: String): this.type = {
    val selector = radio match {
      case "The taxable person" => rdoTaxablePerson
      case "Someone other"      => rdoSomeoneOther
      case "None"               => rdoNone
      case _                    => throw new IllegalArgumentException(s"Invalid option: $radio")
    }
    radioButton(selector)
    continue()
    this
  }

}
