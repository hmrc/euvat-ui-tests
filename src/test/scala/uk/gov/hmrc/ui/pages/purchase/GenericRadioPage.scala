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

import org.openqa.selenium.By
import scala.jdk.CollectionConverters._
import uk.gov.hmrc.ui.pages.BasePage

class GenericRadioPage(
  override val pageUrl: String,
  override val pageTitle: String
) extends BasePage {

  def availableLabels(): Seq[String] =
    driver
      .findElements(By.cssSelector(".govuk-radios__item label"))
      .asScala
      .map(_.getText.trim)
      .filter(_.nonEmpty)
      .toSeq

  def selectByVisibleLabel(label: String): this.type = {
    val labels = driver.findElements(By.cssSelector(".govuk-radios__item label")).asScala.toSeq
    val idx    = labels.indexWhere(_.getText.trim == label.trim)

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
