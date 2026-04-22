/*
 * Copyright 2023 HM Revenue & Customs
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

import com.typesafe.scalalogging.LazyLogging
import driver.BrowserDriver
import org.openqa.selenium.support.ui.{ExpectedConditions, FluentWait, Wait, WebDriverWait}
import org.openqa.selenium.{By, JavascriptExecutor, WebDriver, WebElement}
import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.must.Matchers
import uk.gov.hmrc.selenium.component.PageObject
import uk.gov.hmrc.selenium.webdriver.Driver

import java.time.Duration
import scala.jdk.CollectionConverters.*

trait BasePage extends PageObject with Eventually with Matchers with LazyLogging with BrowserDriver {

  /** Implicit wait */
  implicit def w: WebDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30))

  logger.info(
    s"Instantiating Browser: ${sys.props.getOrElse("browser", "'browser' System property not set. This is required")}"
  )

  /** Locator values */
  object Locators {
    val btnSubmit           = ".govuk-button"
    val lnkBack             = "Back"
    val btnContinue         = ".govuk-button"
    val rdoYes              = "#value_0"
    val rdoNo               = "#value_1"
    val txtEmailAddress: By = By.cssSelector("#value")
    val txtTelephone: By    = By.cssSelector("#telephone")
    val txtFirstName: By    = By.cssSelector("#firstName")
    val txtLastName: By     = By.cssSelector("#lastName")


  }

  def pageUrl: String

  def pageTitle: String = throw new NotImplementedError("Static pageTitle not implemented")

  def pageTitle(args: String*): String = throw new NotImplementedError("Dynamic pageTitle not implemented")

  /** Wait for visibility of an element */
  def waitForVisibilityOfElement(selector: By): WebElement =
    w.until(ExpectedConditions.visibilityOfElementLocated(selector))

  /** Generic input method */
  def input(selector: By, value: String): Unit = {
    val element = waitForVisibilityOfElement(selector)
    element.clear()
    element.sendKeys(value)
  }

  /** Method to input values into month and year fields */
  def inputMonthAndYear(monthSelector: By, yearSelector: By, monthValue: String, yearValue: String): Unit = {
    val monthElement = waitForVisibilityOfElement(monthSelector)
    monthElement.clear()
    monthElement.sendKeys(monthValue)
    val yearElement  = waitForVisibilityOfElement(yearSelector)
    yearElement.clear()
    yearElement.sendKeys(yearValue)
  }

  /** Generic click method */
  override def click(selector: By): Unit = {
    val element = waitForVisibilityOfElement(selector)
    element.click()
  }

  def clickBy(by: By): Unit = {
    w.until(ExpectedConditions.presenceOfElementLocated(by))
    driver.findElement(by).click()
  }

  /** Generic clear method */
  def clear(selector: By): Unit = {
    val element = waitForVisibilityOfElement(selector)
    element.clear()
  }

  /** Specific actions */
  def saveAndContinue(): Unit = click(By.cssSelector(Locators.btnContinue))

  def clickSubmitButton(): Unit = click(By.cssSelector(Locators.btnSubmit))

  def clickBackLink(): Unit = click(By.linkText(Locators.lnkBack))

  /** Navigation methods */
  def navigateToPage(url: String): Unit = driver.navigate().to(url)

  /** Page validation methods */
  def isCurrentPage: Boolean = pageTitle.startsWith(getPageTitle)

  def isCurrentUrl: Boolean = getCurrentUrlInBrowser.contains(pageUrl)

  def getCurrentUrlInBrowser: String = driver.getCurrentUrl

  def getPageTitle: String = driver.getTitle

  /** Wait for page to load */
  def waitForPage(): Unit = fluentWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("footer")))

  def fluentWait: Wait[WebDriver] = new FluentWait[WebDriver](Driver.instance)
    .withTimeout(Duration.ofSeconds(120))
    .pollingEvery(Duration.ofMillis(500))
    .ignoring(classOf[NoSuchElementException])

  /** Radio button interaction */
  def radioButton(optionalValue: String): Unit = {
    val element: WebElement    = driver.findElement(By.cssSelector(optionalValue))
    val ex: JavascriptExecutor = driver.asInstanceOf[JavascriptExecutor]
    ex.executeScript("arguments[0].click()", element)
  }

  /** Checkbox interaction */
  def checkbox(optionalValue: String, shouldSelect: Boolean): Unit = {
    val element: WebElement    = driver.findElement(By.cssSelector(optionalValue))
    val ex: JavascriptExecutor = driver.asInstanceOf[JavascriptExecutor]
    val isChecked: Boolean     = element.isSelected
    if (shouldSelect && !isChecked) {
      ex.executeScript("arguments[0].click()", element)
    } else if (!shouldSelect && isChecked) {
      ex.executeScript("arguments[0].click()", element)
    }
  }

  def clickLinkById(linkId: String): Unit =
    try {
      click(By.id(linkId))
      println(s"Successfully clicked the link with ID: $linkId")
    } catch {
      case e: Exception =>
        println(s"Failed to click the link with ID: $linkId. Error: ${e.getMessage}")
    }

  def clickLinkByCSS(linkCSS: String): Unit =
    try {
      click(By.cssSelector(linkCSS))
      println(s"Successfully clicked the link with selector: $linkCSS")
    } catch {
      case e: Exception =>
        println(s"Failed to click the link with CSS Selector: $linkCSS. Error: ${e.getMessage}")
    }

  def clickLinkByText(linkText: String): Unit =
    try {
      click(By.linkText(linkText))
      println(s"Successfully clicked the link with text: $linkText")
    } catch {
      case e: Exception =>
        println(s"Failed to click the link with text: $linkText. Error: ${e.getMessage}")
    }

  def verifyPageTitle(expectedTitle: String): Unit = {
    waitForPageTitle(expectedTitle)
    assert(
      getPageTitle == expectedTitle,
      s"Page title mismatch! Expected: $expectedTitle, Actual: $getPageTitle"
    )
    println("Actual page title is: " + getPageTitle)
  }

  def clickByXpath(xpath: String): Unit = clickBy(By.xpath(xpath))

  def findByXpath(xpath: String): String = driver.findElement(By.xpath(xpath)).getText

  def waitForPageTitle(expectedTitle: String): Unit =
    fluentWait.until(ExpectedConditions.titleIs(expectedTitle))

}
