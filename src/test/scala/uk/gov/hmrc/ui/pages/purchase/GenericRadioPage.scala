package uk.gov.hmrc.ui.pages.purchase

import org.openqa.selenium.By
import uk.gov.hmrc.ui.pages.BasePage
import scala.jdk.CollectionConverters._

class GenericRadioPage(
  override val pageUrl: String,
  expectedTitleText: String
) extends BasePage {

  override def pageTitle: String = expectedTitleText

  def availableLabels(): Seq[String] =
    driver
      .findElements(By.cssSelector(".govuk-radios__item label"))
      .asScala
      .map(_.getText.trim)
      .filter(_.nonEmpty)
      .toSeq

  def assertTitle(): Unit =
    verifyPageTitle(pageTitle)

  def assertLabelsExactly(expected: Seq[String]): Unit =
    availableLabels() mustBe expected

  def assertLabelsContain(expected: Seq[String]): Unit =
    availableLabels() must contain theSameElementsAs expected

  def selectByVisibleLabel(label: String): this.type = {
    val labels = driver.findElements(By.cssSelector(".govuk-radios__item label")).asScala.toSeq
    val idx    = labels.indexWhere(_.getText.trim == label.trim)
    if (idx < 0) throw new IllegalArgumentException(s"Label not found on $pageUrl: $label")
    radioButton(s"#value_$idx")
    continue()
    this
  }
}
