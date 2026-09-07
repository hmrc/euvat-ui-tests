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

import uk.gov.hmrc.ui.pages.purchase.GenericRadioPage

object PurchaseFlowRouter {

  // Top-level subcode pages
  private val fuelUsePage =
    new GenericRadioPage("fuel-use", "What is the fuel used for? - EU VAT - GOV.UK")

  private val transportCostPage =
    new GenericRadioPage("transport-cost", "What is the type of transport cost? - EU VAT - GOV.UK")

  private val foodDrinkPage =
    new GenericRadioPage(
      "food-drink-restaurant-cost",
      "What is the type of food, drink or restaurant cost? - EU VAT - GOV.UK"
    )

  private val luxuriesPage =
    new GenericRadioPage(
      "luxury-entertainment-hospitality-cost",
      "What is the type of luxury, entertainment or hospitality cost? - EU VAT - GOV.UK"
    )

  private val otherPage =
    new GenericRadioPage(
      "purchase-type-other",
      "What other category best describes the item on your invoice? - EU VAT - GOV.UK"
    )

  // Subcategory pages
  private val fuelTypePage =
    new GenericRadioPage("fuel-type", "What is the type of fuel? - EU VAT - GOV.UK")

  private val fuelTypeOrVehiclePage =
    new GenericRadioPage("fuel-type-or-vehicle", "What is the type of fuel or vehicle? - EU VAT - GOV.UK")

  private val vehicleUsePage =
    new GenericRadioPage("vehicle-use", "How is the vehicle used? - EU VAT - GOV.UK")

  private val whatTransportCostPage =
    new GenericRadioPage("what-transport-cost", "What option best describes this transport cost? - EU VAT - GOV.UK")

  private val whoFoodDrinkForPage =
    new GenericRadioPage("who-food-drink-for", "Who is the food and drink for? - EU VAT - GOV.UK")

  private val publicityPage =
    new GenericRadioPage("cost-for-publicity-purposes", "Is this cost for publicity purposes? - EU VAT - GOV.UK")

  private val propertyPurchaseTypePage =
    new GenericRadioPage("property-purchase-type", "What is the type of property purchase? - EU VAT - GOV.UK")

  private val propertyCostTypePage =
    new GenericRadioPage("property-cost-type", "What is the type of property cost? - EU VAT - GOV.UK")

  def topLevelPageFor(code: String): GenericRadioPage = code match {
    case "1"  => fuelUsePage
    case "3"  => transportCostPage
    case "7"  => foodDrinkPage
    case "9"  => luxuriesPage
    case "10" => otherPage
    case x    => throw new IllegalArgumentException(s"No top-level page mapping for code: $x")
  }

  def subCategoryPageFor(code: String, subCode: String): GenericRadioPage = (code, subCode) match {
    case ("1", "1") | ("1", "3") | ("1", "10") | ("1", "11") => fuelTypePage
    case ("1", "2")                                          => fuelTypeOrVehiclePage
    case ("1", "8") | ("1", "9")                             => vehicleUsePage

    case ("3", "1") | ("3", "2") | ("3", "3") | ("3", "4") => whatTransportCostPage
    case ("3", "5") | ("3", "6") | ("3", "7") | ("3", "8") => vehicleUsePage

    case ("7", "1") | ("7", "2") => whoFoodDrinkForPage
    case ("9", "3")              => publicityPage
    case ("10", "5")             => propertyPurchaseTypePage
    case ("10", "17")            => propertyCostTypePage

    case x => throw new IllegalArgumentException(s"No subcategory page mapping for: $x")
  }

  def purchaseTypeLabelFor(code: String): String = code match {
    case "1"  => "Fuel"
    case "3"  => "Transport costs"
    case "7"  => "Food, drink and restaurant services"
    case "9"  => "Luxuries, entertainment and hospitality"
    case "10" => "Other"
    case x    => throw new IllegalArgumentException(s"No purchase type label for code: $x")
  }
}
