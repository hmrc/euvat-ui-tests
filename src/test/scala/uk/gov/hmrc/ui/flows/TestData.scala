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

package uk.gov.hmrc.ui.flows

final case class ClaimData(
  country: String,
  language: Option[String],
  fromMonth: String,
  fromYear: String,
  toMonth: String,
  toYear: String,
  email: String,
  phone: String
)

final case class SupplierData(
  name: String,
  address1: String,
  address2: String,
  address3: String
)

final case class AmountData(
  currency: Option[String],
  purchaseAmount: String,
  vatPaid: String,
  vatClaim: String
)

object TestData {
  val defaultSupplier: SupplierData =
    SupplierData(
      name = "Test Supplier Name",
      address1 = "Test address one",
      address2 = "Test address two",
      address3 = "Test address three"
    )

  val croatiaClaim: ClaimData =
    ClaimData(
      country = "Croatia",
      language = None,
      fromMonth = "02",
      fromYear = "2026",
      toMonth = "04",
      toYear = "2026",
      email = "test@gmail.com",
      phone = "9876543210"
    )

  val estoniaClaim: ClaimData =
    ClaimData(
      country = "Estonia",
      language = Some("English"),
      fromMonth = "06",
      fromYear = "2026",
      toMonth = "08",
      toYear = "2026",
      email = "changetest@gmail.com",
      phone = "+449876543210"
    )

  val germanyClaim: ClaimData =
    ClaimData(
      country = "Germany",
      language = Some("English"),
      fromMonth = "02",
      fromYear = "2026",
      toMonth = "04",
      toYear = "2026",
      email = "test@gmail.com",
      phone = "9876543210"
    )
}
