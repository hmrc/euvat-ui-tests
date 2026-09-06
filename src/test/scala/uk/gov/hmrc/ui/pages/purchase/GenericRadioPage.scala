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

import org.openqa.selenium.{By, StaleElementReferenceException}
import scala.jdk.CollectionConverters._
import uk.gov.hmrc.ui.pages.BasePage

class GenericRadioPage(
  override val pageUrl: String,
  override val pageTitle: String
) extends BasePage {

  private val radioLabelsBy = By.cssSelector(".govuk-radios__item label")

  private def fetchLabelsOnce(): Seq[String] =
    driver
      .findElements(radioLabelsBy)
      .asScala
      .map(_.getText.trim)
      .filter(_.nonEmpty)
      .toSeq

  def availableLabels(): Seq[String] = {
    waitForPage()
    var last: Throwable = null
    var attempt         = 0
    while (attempt < 5)
      try return fetchLabelsOnce()
      catch {
        case e: StaleElementReferenceException =>
          last = e
          pause(1)
          attempt += 1
      }
    throw last
  }

  def selectByVisibleLabel(label: String): this.type = {
    waitForPage()
    var last: Throwable = null
    var attempt         = 0
    while (attempt < 5)
      try {
        val labels = driver.findElements(radioLabelsBy).asScala.toSeq
        val idx    = labels.indexWhere(_.getText.trim == label.trim)
        if (idx < 0)
          throw new IllegalArgumentException(
            s"Label not found on page '$pageUrl': '$label'. Available: ${availableLabels().mkString(", ")}"
          )
        radioButton(s"#value_$idx")
        continue()
        return this
      } catch {
        case e: StaleElementReferenceException =>
          last = e
          pause(1)
          attempt += 1
      }
    throw last
  }

  def assertLabelsContain(expected: Seq[String]): Unit =
    availableLabels() must contain theSameElementsAs expected

  def assertCurrentPage(): Unit =
    verifyPageTitle(pageTitle)
}
