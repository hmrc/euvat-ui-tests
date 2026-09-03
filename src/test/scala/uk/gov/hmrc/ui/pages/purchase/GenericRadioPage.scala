package uk.gov.hmrc.ui.pages.purchase

import org.openqa.selenium.By
import scala.jdk.CollectionConverters._
import uk.gov.hmrc.ui.pages.BasePage

class GenericRadioPage(
                        override val pageUrl: String,
                        override val pageTitle: String
                      ) extends BasePage {

  def availableLabels(): Seq[String] =
    driver.findElements(By.cssSelector(".govuk-radios__item label")).asScala
      .map(_.getText.trim)
      .filter(_.nonEmpty)
      .toSeq

  def selectByVisibleLabel(label: String): this.type = {
    val labels = driver.findElements(By.cssSelector(".govuk-radios__item label")).asScala.toSeq
    val idx = labels.indexWhere(_.getText.trim == label.trim)

    if (idx < 0) {
      throw new IllegalArgumentException(
        s"Label not found on page '$pageUrl': '$label'. Available: ${availableLabels().mkString(", ")}"
      )
    }

    radioButton(s"#value_$idx")
    continue()
    this
  }

  def assertLabelsContain(expected: Seq[String]): Unit =
    availableLabels() must contain theSameElementsAs expected

  def assertCurrentPage(): Unit =
    verifyPageTitle(pageTitle)
}