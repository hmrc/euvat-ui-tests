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

package uk.gov.hmrc.ui.specs

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterEach, GivenWhenThen}
import uk.gov.hmrc.selenium.webdriver.{Browser, ScreenshotOnFailure}
import org.openqa.selenium.{By, NoSuchElementException, StaleElementReferenceException, WebDriver}
import org.openqa.selenium.support.ui.FluentWait
import java.time.Duration

trait BaseSpec
    extends AnyFeatureSpec
    with GivenWhenThen
    with Matchers
    with BeforeAndAfterEach
    with Browser
    with ScreenshotOnFailure {

  override def beforeEach(): Unit =
    startBrowser()

  override def afterEach(): Unit =
    quitBrowser()

  def clickSignOut(driver: WebDriver): Unit = {
    val signOut = By.linkText("Sign out")

    val wait = new FluentWait[WebDriver](driver)
      .withTimeout(Duration.ofSeconds(10))
      .pollingEvery(Duration.ofMillis(200))
      .ignoring(classOf[StaleElementReferenceException])
      .ignoring(classOf[NoSuchElementException])

    wait.until { (d: WebDriver) =>
      val elements = d.findElements(signOut)
      if (!elements.isEmpty) {
        val el = elements.get(0)
        if (el.isDisplayed && el.isEnabled) {
          el.click()
          true
        } else false
      } else false
    }
  }
}
