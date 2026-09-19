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

import uk.gov.hmrc.ui.pages.GenericRadioPage

/**
 * Central routing helper for both Purchase and Import flows.
 *
 * Responsibilities:
 *  - map a top-level code (1/3/7/9/10) to the correct first radio page
 *  - map a (code, subCode) pair to the correct sub-category radio page
 *  - build the correct URL slug for Purchase vs Import
 *  - provide the display label used on the PurchaseType / ImportType page
 *
 * Purchase URLs are under /purchase/...
 * Import URLs are under /import/...
 */
object PurchaseFlowRouter {

  /**
   * Defines which journey is being tested.
   * pathSegment controls the URL prefix.
   * entrySlug is the first page slug for that journey.
   */
  sealed trait FlowType {
    def pathSegment: String
    def entrySlug: String
  }

  /** Purchase journey: /purchase/... */
  case object PurchaseFlow extends FlowType {
    override val pathSegment: String = "purchase"
    override val entrySlug: String   = "purchase-type"
  }

  /** Import journey: /import/... */
  case object ImportFlow extends FlowType {
    override val pathSegment: String = "import"
    override val entrySlug: String   = "import-type"
  }

  /**
   * Builds a journey-specific page slug.
   * Example:
   *  - PurchaseFlow + fuel-use  => purchase/fuel-use
   *  - ImportFlow   + fuel-use  => import/fuel-use
   */
  private def slug(flowType: FlowType, page: String): String =
    s"${flowType.pathSegment}/$page"

  /**
   * Special-case slug for the "Other" top-level page because
   * purchases use purchase-type-other and imports use import-type-other.
   */
  private def otherSlug(flowType: FlowType): String = flowType match {
    case PurchaseFlow => slug(flowType, "purchase-type-other")
    case ImportFlow   => slug(flowType, "import-type-other")
  }

  /**
   * Special-case title for the "Other" top-level page because
   * purchase and import journeys use different wording.
   */
  private def otherTitle(flowType: FlowType): String = flowType match {
    case PurchaseFlow => "What other category best describes the item on your invoice? - EU VAT - GOV.UK"
    case ImportFlow   => "What other category best describes the item? - EU VAT - GOV.UK"
  }

  // Top-level sub-code pages

  private def fuelUsePage(flowType: FlowType) =
    new GenericRadioPage(slug(flowType, "fuel-use"), "What is the fuel used for? - EU VAT - GOV.UK")

  private def transportCostPage(flowType: FlowType) =
    new GenericRadioPage(slug(flowType, "transport-cost"), "What is the type of transport cost? - EU VAT - GOV.UK")

  private def foodDrinkPage(flowType: FlowType) =
    new GenericRadioPage(
      slug(flowType, "food-drink-restaurant-cost"),
      "What is the type of food, drink or restaurant cost? - EU VAT - GOV.UK"
    )

  private def luxuriesPage(flowType: FlowType) =
    new GenericRadioPage(
      slug(flowType, "luxury-entertainment-hospitality-cost"),
      "What is the type of luxury, entertainment or hospitality cost? - EU VAT - GOV.UK"
    )

  private def otherPage(flowType: FlowType) =
    new GenericRadioPage(
      otherSlug(flowType),
      otherTitle(flowType)
    )

  // Sub-category pages

  private def fuelTypePage(flowType: FlowType) =
    new GenericRadioPage(slug(flowType, "fuel-type"), "What is the type of fuel? - EU VAT - GOV.UK")

  private def fuelTypeOrVehiclePage(flowType: FlowType) =
    new GenericRadioPage(slug(flowType, "fuel-type-or-vehicle"), "What is the type of fuel or vehicle? - EU VAT - GOV.UK")

  private def vehicleUsePage(flowType: FlowType) =
    new GenericRadioPage(slug(flowType, "vehicle-use"), "How is the vehicle used? - EU VAT - GOV.UK")

  private def whatTransportCostPage(flowType: FlowType) =
    new GenericRadioPage(slug(flowType, "what-transport-cost"), "What option best describes this transport cost? - EU VAT - GOV.UK")

  private def whoFoodDrinkForPage(flowType: FlowType) =
    new GenericRadioPage(slug(flowType, "who-food-drink-for"), "Who is the food and drink for? - EU VAT - GOV.UK")

  private def publicityPage(flowType: FlowType) =
    new GenericRadioPage(slug(flowType, "cost-for-publicity-purposes"), "Is this cost for publicity purposes? - EU VAT - GOV.UK")

  private def propertyPurchaseTypePage(flowType: FlowType) =
    new GenericRadioPage(slug(flowType, "property-purchase-type"), "What is the type of property purchase? - EU VAT - GOV.UK")

  private def propertyCostTypePage(flowType: FlowType) =
    new GenericRadioPage(slug(flowType, "property-cost-type"), "What is the type of property cost? - EU VAT - GOV.UK")

  /**
   * Returns the journey entry page slug.
   * Example:
   *  - PurchaseFlow => purchase/purchase-type
   *  - ImportFlow   => import/import-type
   */
  def entryPageSlug(flowType: FlowType = PurchaseFlow): String =
    s"${flowType.pathSegment}/${flowType.entrySlug}"

  /**
   * Maps the selected top-level code to the page that shows its sub-codes.
   */
  def topLevelPageFor(code: String, flowType: FlowType = PurchaseFlow): GenericRadioPage = code match {
    case "1"  => fuelUsePage(flowType)
    case "3"  => transportCostPage(flowType)
    case "7"  => foodDrinkPage(flowType)
    case "9"  => luxuriesPage(flowType)
    case "10" => otherPage(flowType)
    case x    => throw new IllegalArgumentException(s"No top-level page mapping for code: $x")
  }

  /**
   * Maps a (code, subCode) pair to the page that shows its sub-category options.
   */
  def subCategoryPageFor(code: String, subCode: String, flowType: FlowType = PurchaseFlow): GenericRadioPage =
    (code, subCode) match {
      case ("1", "1") | ("1", "3") | ("1", "10") | ("1", "11") => fuelTypePage(flowType)
      case ("1", "2")                                           => fuelTypeOrVehiclePage(flowType)
      case ("1", "8") | ("1", "9")                              => vehicleUsePage(flowType)

      case ("3", "1") | ("3", "2") | ("3", "3") | ("3", "4") => whatTransportCostPage(flowType)
      case ("3", "5") | ("3", "6") | ("3", "7") | ("3", "8") => vehicleUsePage(flowType)

      case ("7", "1") | ("7", "2") => whoFoodDrinkForPage(flowType)
      case ("9", "3")              => publicityPage(flowType)
      case ("10", "5")             => propertyPurchaseTypePage(flowType)
      case ("10", "17")            => propertyCostTypePage(flowType)

      case x => throw new IllegalArgumentException(s"No subcategory page mapping for: $x")
    }

  /**
   * Maps the numeric code to the label displayed on the PurchaseType / ImportType page.
   */
  def purchaseTypeLabelFor(code: String): String = code match {
    case "1"  => "Fuel"
    case "3"  => "Transport costs"
    case "7"  => "Food, drink and restaurant services"
    case "9"  => "Luxuries, entertainment and hospitality"
    case "10" => "Other"
    case x    => throw new IllegalArgumentException(s"No purchase type label for code: $x")
  }
}