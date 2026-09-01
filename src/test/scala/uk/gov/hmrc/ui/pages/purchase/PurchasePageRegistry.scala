package uk.gov.hmrc.ui.pages.purchase

object PurchasePageRegistry {

  val FuelUse   = new GenericRadioPage("fuel-use", "What is the fuel used for? - EU VAT - GOV.UK")
  val Transport = new GenericRadioPage("transport-cost", "What is the type of transport cost? - EU VAT - GOV.UK")
  val FoodDrink = new GenericRadioPage(
    "food-drink-restaurant-cost",
    "What is the type of food, drink or restaurant cost? - EU VAT - GOV.UK"
  )
  val Luxuries  = new GenericRadioPage(
    "luxury-entertainment-hospitality-cost",
    "What is the type of luxury, entertainment or hospitality cost? - EU VAT - GOV.UK"
  )
  val Other     = new GenericRadioPage(
    "purchase-type-other",
    "What other category best describes the item on your invoice? - EU VAT - GOV.UK"
  )

  val FuelType             = new GenericRadioPage("fuel-type", "What is the type of fuel? - EU VAT - GOV.UK")
  val FuelTypeOrVehicle    =
    new GenericRadioPage("fuel-type-or-vehicle", "What is the type of fuel or vehicle? - EU VAT - GOV.UK")
  val VehicleUse           = new GenericRadioPage("vehicle-use", "How is the vehicle used? - EU VAT - GOV.UK")
  val WhatTransportCost    =
    new GenericRadioPage("what-transport-cost", "What option best describes this transport cost? - EU VAT - GOV.UK")
  val WhoFoodDrinkFor      = new GenericRadioPage("who-food-drink-for", "Who is the food and drink for? - EU VAT - GOV.UK")
  val Publicity            =
    new GenericRadioPage("cost-for-publicity-purposes", "Is this cost for publicity purposes? - EU VAT - GOV.UK")
  val PropertyPurchaseType =
    new GenericRadioPage("property-purchase-type", "What is the type of property purchase? - EU VAT - GOV.UK")
  val PropertyCostType     =
    new GenericRadioPage("property-cost-type", "What is the type of property cost? - EU VAT - GOV.UK")

  def topLevelPageFor(code: String): GenericRadioPage = code match {
    case "1"  => FuelUse
    case "3"  => Transport
    case "7"  => FoodDrink
    case "9"  => Luxuries
    case "10" => Other
  }

  def subCategoryPageFor(code: String, subCode: String): GenericRadioPage = (code, subCode) match {
    case ("1", "1")  => FuelType
    case ("1", "2")  => FuelTypeOrVehicle
    case ("1", "3")  => FuelType
    case ("1", "8")  => VehicleUse
    case ("1", "9")  => VehicleUse
    case ("1", "10") => FuelType
    case ("1", "11") => FuelType

    case ("3", "1") => WhatTransportCost
    case ("3", "2") => WhatTransportCost
    case ("3", "3") => WhatTransportCost
    case ("3", "4") => WhatTransportCost
    case ("3", "5") => VehicleUse
    case ("3", "6") => VehicleUse
    case ("3", "7") => VehicleUse
    case ("3", "8") => VehicleUse

    case ("7", "1") => WhoFoodDrinkFor
    case ("7", "2") => WhoFoodDrinkFor

    case ("9", "3")   => Publicity
    case ("10", "5")  => PropertyPurchaseType
    case ("10", "17") => PropertyCostType
    case _            => throw new IllegalArgumentException(s"No subcategory page for code=$code subCode=$subCode")
  }
}
