package uk.gov.hmrc.ui.specs

import org.scalatest.BeforeAndAfterEach
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.verbs.ShouldVerb
import org.scalatest.GivenWhenThen
import uk.gov.hmrc.selenium.webdriver.{Browser, ScreenshotOnFailure}
import uk.gov.hmrc.ui.pages._
import uk.gov.hmrc.ui.pages.claim._
import uk.gov.hmrc.ui.pages.purchase._
import uk.gov.hmrc.ui.tags.Local
import uk.gov.hmrc.ui.utils.{CountryCodeMappingReader, MappingRow}

class VatLabelsStrictSpec
  extends AnyFeatureSpec
    with BaseSpec
    with GivenWhenThen
    with ShouldVerb
    with BeforeAndAfterEach
    with Browser
    with ScreenshotOnFailure {

  private val rows = CountryCodeMappingReader.loadFromResource()

  private def expectedSubCodeLabels(countryCode: String, code: String): Seq[String] =
    rows
      .filter(r => r.countryCode == countryCode && r.code == code)
      .flatMap(_.subCodeLabel)
      .distinct

  private def purchaseTypeLabelFor(code: String): String = code match {
    case "1"  => "Fuel"
    case "3"  => "Transport costs"
    case "7"  => "Food, drink and restaurant services"
    case "9"  => "Luxuries, entertainment and hospitality"
    case "10" => "Other"
    case _    => throw new IllegalArgumentException(s"Unsupported top-level code: $code")
  }

  private def navigateToPurchaseType(countryName: String): Unit = {
    AuthorityWizard.login("Organisation", "999900001")
    ClaimAnEUVATRefund.verifyPageTitle(ClaimAnEUVATRefund.pageTitle)

    ClaimAnEUVATRefund.clickLinkByText("Make a claim for an EU VAT refund")
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)

    MakeEuvatClaim.clickLinkByText("Add claim details")
    EUMemberState.verifyPageTitle(EUMemberState.pageTitle)
    EUMemberState.selectCountry(countryName)

    // some countries go to Language first
    if (Language.getCurrentUrlInBrowser.contains(Language.pageUrl) || Language.getPageTitle.contains("claim language")) {
      Language.verifyPageTitle(Language.pageTitle)
      Language.selectLanguage("English")
    }

    RefundPeriod.verifyPageTitle(RefundPeriod.pageTitle)
    RefundPeriod.submitRefundPeriod("02", "2025", "04", "2025")

    ContactDetails.verifyPageTitle(ContactDetails.pageTitle)
    ContactDetails.submitContactAddress("mapping@test.com", "07700900000")

    AddBusinessActivity.verifyPageTitle(AddBusinessActivity.pageTitle)
    AddBusinessActivity.continueAsNo()

    CheckYourClaimDetails.verifyPageTitle(CheckYourClaimDetails.pageTitle)
    CheckYourClaimDetails.saveAndContinue()

    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
    MakeEuvatClaim.clickLinkByText("Add a purchase")

    BeforeYouStart.verifyPageTitle(BeforeYouStart.pageTitle)
    BeforeYouStart.continue()

    PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
  }

  Feature("Purchase mapping labels from CountryCodeMapping.xlsx") {

    rows.map(r => (r.countryCode, r.country)).distinct.foreach { case (countryCode, countryName) =>

      Scenario(s"Validate sub code labels for $countryCode", Local) {
        Given(s"I navigate quickly to PurchaseType for $countryName")
        navigateToPurchaseType(countryName)

        val codesForCountry = rows.filter(_.countryCode == countryCode).map(_.code).distinct.sorted

        codesForCountry.foreach { code =>
          val expectedLabels = expectedSubCodeLabels(countryCode, code)

          if (expectedLabels.nonEmpty) {
            When(s"I select purchase type code $code for $countryCode")
            PurchaseType.selectPurchaseType(purchaseTypeLabelFor(code))

            Then(s"I should see the expected sub code labels for $countryCode / $code")
            val page = PurchasePageRegistry.topLevelPageFor(code)
            page.assertLabelsContain(expectedLabels)

            // return to PurchaseType for next code
            PurchaseType.navigateBack()
            PurchaseType.verifyPageTitle(PurchaseType.pageTitle)
          }
        }
      }

      val subCodeGroups = rows
        .filter(r => r.countryCode == countryCode && r.subCode.nonEmpty)
        .groupBy(r => (r.code, r.subCode.get))

      subCodeGroups.foreach { case ((code, subCode), groupedRows) =>
        val expectedSubCategoryLabels = groupedRows.flatMap(_.subCategoryLabel).distinct

        if (expectedSubCategoryLabels.nonEmpty) {
          Scenario(s"Validate sub category labels for $countryCode code=$code subCode=$subCode", Local) {
            Given(s"I navigate quickly to PurchaseType for $countryName")
            navigateToPurchaseType(countryName)

            When(s"I select purchase type $code")
            PurchaseType.selectPurchaseType(purchaseTypeLabelFor(code))

            val topPage = PurchasePageRegistry.topLevelPageFor(code)
            val subCodeLabel = groupedRows.head.subCodeLabel.get

            And(s"I select sub code $subCodeLabel")
            topPage.selectByVisibleLabel(subCodeLabel)

            Then(s"I should see the expected sub category labels for $countryCode / $code / $subCode")
            val subPage = PurchasePageRegistry.subCategoryPageFor(code, subCode)
            subPage.assertLabelsContain(expectedSubCategoryLabels)
          }
        }
      }
    }
  }
}