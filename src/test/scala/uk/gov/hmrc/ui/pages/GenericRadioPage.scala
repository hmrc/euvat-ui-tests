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

package uk.gov.hmrc.ui.pages

import org.openqa.selenium.{By, StaleElementReferenceException}
import uk.gov.hmrc.ui.pages.BasePage

import scala.jdk.CollectionConverters.*

/**
 * Generic page object for GOV.UK-style radio button pages.
 *
 * This page object is reusable for any page that:
 * - displays radio options using the GOV.UK radios component
 * - has a known URL slug
 * - has a known page title
 *
 * It provides common functionality to:
 * - read all visible radio labels on the page
 * - select a radio option by its visible label text
 * - verify the current page title
 *
 * It also includes retry handling for stale element exceptions, which can
 * happen when the page re-renders during Selenium interaction.
 */
class GenericRadioPage(
  override val pageUrl: String,
  override val pageTitle: String
) extends BasePage {

  /**
   * CSS selector used to find the visible labels for GOV.UK radio items.
   *
   * Example HTML pattern:
   * <div class="govuk-radios__item">
   *   <input ... />
   *   <label>Some option</label>
   * </div>
   */
  private val radioLabelsBy = By.cssSelector(".govuk-radios__item label")

  /**
   * Reads all visible radio labels from the current page once.
   *
   * This method does a single pass over the DOM and:
   * - finds all matching radio labels
   * - trims their text
   * - removes any empty labels
   *
   * It does not retry if the DOM changes mid-read, so callers that need
   * resilience should use `availableLabels()`.
   */
  private def fetchLabelsOnce(): Seq[String] =
    driver
      .findElements(radioLabelsBy)
      .asScala
      .map(_.getText.trim)
      .filter(_.nonEmpty)
      .toSeq

  /**
   * Returns all currently visible radio option labels on the page.
   *
   * This method:
   * - waits for the page to load
   * - retries up to 5 times if Selenium throws StaleElementReferenceException
   *
   * This is useful because radio pages may refresh or re-render while
   * Selenium is attempting to read the label text.
   */
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

  /**
   * Selects a radio option by matching its visible label text.
   *
   * This method:
   * - waits for the page to load
   * - reads all current radio labels
   * - finds the index of the matching label
   * - clicks the corresponding radio input using the existing `radioButton` helper
   * - clicks Continue
   *
   * It retries up to 5 times if the DOM changes and Selenium throws a
   * StaleElementReferenceException.
   *
   * @param label the exact visible label text to select
   * @return this page object, to allow fluent chaining
   */
  def selectByVisibleLabel(label: String): this.type = {
    waitForPage()
    var last: Throwable = null
    var attempt         = 0
    while (attempt < 5)
      try {
        val labels = driver.findElements(radioLabelsBy).asScala.toSeq
        val idx    = labels.indexWhere(_.getText.trim == label.trim)

        // If the label does not exist, fail with a useful error that includes
        // the currently available label values for debugging.
        if (idx < 0)
          throw new IllegalArgumentException(
            s"Label not found on page '$pageUrl': '$label'. Available: ${availableLabels().mkString(", ")}"
          )

        // Radio buttons in this journey use ids like value_0, value_1, etc.
        // The label index is used to derive the corresponding radio selector.
        radioButton(s"#value_$idx")

        // Submit the selected option and move to the next page.
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

  /**
   * Asserts that the radio labels on the page contain exactly the same elements
   * as the expected values, regardless of ordering.
   *
   * This is used by mapping tests to verify that the UI options match the
   * expected labels from the spreadsheet.
   */
  def assertLabelsContain(expected: Seq[String]): Unit =
    availableLabels() must contain theSameElementsAs expected

  /**
   * Verifies that the browser is currently on the expected page by checking
   * the page title.
   *
   * This is useful after navigation or selection to confirm the flow has
   * reached the correct radio page.
   */
  def assertCurrentPage(): Unit =
    verifyPageTitle(pageTitle)
}