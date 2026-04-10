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

package uk.gov.hmrc.ui.util

object Env {
  val env: String = Option(System.getProperty("environment"))
    .getOrElse("local")
    .toLowerCase

  // Determine the base URL based on the environment
  val baseUrl: String = Option(System.getProperty("environment")) match {
    case Some("dev")   => Urls.DEV
    case Some("local") => Urls.LOCAL
    case Some("qa")    => Urls.QA
    case _             => Urls.LOCAL
  }

  // Determine whether to use a stub based on the environment
  val USE_STUB: Boolean = Option(System.getProperty("useStub"))
    .map(_.toBoolean)
    .getOrElse(env match {
      case "local"   => true // Default to stub in local unless explicitly overridden
      case "qa"      => false
      case "staging" => true
      case _         => true
    })

  println(s"********** ENVIRONMENT: $env, USE_STUB: $USE_STUB **********")
}
