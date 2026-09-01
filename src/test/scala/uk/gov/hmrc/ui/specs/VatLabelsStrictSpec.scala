package uk.gov.hmrc.ui.specs

import org.scalatestplus.play.*
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.scalatestplus.selenium.WebBrowser
import uk.gov.hmrc.ui.pages.purchase.{PurchasePageRegistry, PurchaseType}
import uk.gov.hmrc.ui.pages.claim.EUMemberState
import uk.gov.hmrc.ui.utils.{CountryCodeMappingReader, MappingRow}

class VatLabelsStrictSpec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerTest with WebBrowser {

  private val rows = CountryCodeMappingReader.loadFromResource()

  private def topLevelRows(countryCode: String, code: String): Seq[MappingRow] =
    rows.filter(r => r.countryCode == countryCode && r.code == code)

  private def expectedSubCodeLabels(countryCode: String, code: String): Seq[String] =
    topLevelRows(countryCode, code)
      .flatMap(_.subCodeLabel)
      .distinct

  private def expectedSubCategoryLabels(countryCode: String, code: String, subCode: String): Seq[String] =
    rows
      .filter(r => r.countryCode == countryCode && r.code == code && r.subCode.contains(subCode))
      .flatMap(_.subCategoryLabel)
      .distinct

  "VAT labels" should {

    rows.map(_.countryCode).distinct.foreach { countryCode =>
      Seq("1", "3", "7", "9", "10").foreach { code =>
        val subLabels = expectedSubCodeLabels(countryCode, code)

        if (subLabels.nonEmpty) {
          s"show exact subcode labels for country=$countryCode code=$code" in {
            go to s"http://localhost:$port/start"
            EUMemberState.selectCountry(countryCode)
            PurchaseType.selectPurchaseType(code match {
              case "1"  => "Fuel"
              case "3"  => "Transport costs"
              case "7"  => "Food, drink and restaurant services"
              case "9"  => "Luxuries, entertainment and hospitality"
              case "10" => "Other"
            })

            val page = PurchasePageRegistry.topLevelPageFor(code)
            page.assertLabelsContain(subLabels)
          }
        }
      }

      val subCodeGroups = rows
        .filter(r => r.countryCode == countryCode && r.subCode.nonEmpty)
        .groupBy(r => (r.code, r.subCode.get))

      subCodeGroups.foreach { case ((code, subCode), groupedRows) =>
        val expected = groupedRows.flatMap(_.subCategoryLabel).distinct

        if (expected.nonEmpty) {
          s"show exact subcategory labels for country=$countryCode code=$code subCode=$subCode" in {
            go to s"http://localhost:$port/start"
            EUMemberState.selectCountry(countryCode)

            PurchaseType.selectPurchaseType(code match {
              case "1"  => "Fuel"
              case "3"  => "Transport costs"
              case "7"  => "Food, drink and restaurant services"
              case "9"  => "Luxuries, entertainment and hospitality"
              case "10" => "Other"
            })

            val topPage      = PurchasePageRegistry.topLevelPageFor(code)
            val subCodeLabel = groupedRows.head.subCodeLabel.get
            topPage.selectByVisibleLabel(subCodeLabel)

            val subPage = PurchasePageRegistry.subCategoryPageFor(code, subCode)
            subPage.assertLabelsContain(expected)
          }
        }
      }
    }
  }
}
