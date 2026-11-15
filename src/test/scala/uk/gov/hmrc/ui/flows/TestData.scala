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

sealed trait AccountHolderTypeData {
  def value: String
}

case object ApplicantYou extends AccountHolderTypeData {
  val value = "Applicant (you)"
}

case object ThirdPartySomeoneElse extends AccountHolderTypeData {
  val value = "Third party (someone else)"
}

final case class BankDetailsData(
  accountHolderType: AccountHolderTypeData,
  accountName: String,
  bic: String,
  iban: String,
  currency: String
)

sealed trait SadFlowData {
  def importDay: String
  def importMonth: String
  def importYear: String
  def supplierName: String
  def supplierAddress1: String
  def supplierAddress2: String
  def supplierAddress3: String
  def supplierCountry: String
}

final case class SadReferenceData(
  referenceNumber: String,
  importDay: String,
  importMonth: String,
  importYear: String,
  supplierName: String,
  supplierAddress1: String,
  supplierAddress2: String,
  supplierAddress3: String,
  supplierCountry: String
) extends SadFlowData

final case class SadDocumentData(
  documentDetails: String,
  importDay: String,
  importMonth: String,
  importYear: String,
  supplierName: String,
  supplierAddress1: String,
  supplierAddress2: String,
  supplierAddress3: String,
  supplierCountry: String
) extends SadFlowData

final case class BusinessActivityData(
  secondSicCode: String,
  thirdSicCode: String
)

final case class ClaimData(
  country: String,
  language: Option[String],
  fromMonth: String,
  fromYear: String,
  toMonth: String,
  toYear: String,
  email: String,
  phone: String,
  businessActivity: Option[BusinessActivityData] = None
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

final case class SupportingDocumentsData(
  hasSupportingDocuments: Boolean
)

object TestData {

  val supportingDocumentsYes: SupportingDocumentsData =
    SupportingDocumentsData(
      hasSupportingDocuments = true
    )

  val supportingDocumentsNo: SupportingDocumentsData =
    SupportingDocumentsData(
      hasSupportingDocuments = false
    )

  val initialBankDetails: BankDetailsData =
    BankDetailsData(
      accountHolderType = ApplicantYou,
      accountName = "ACCOUNTNAME123456789",
      bic = "FAKEGB21XXX",
      iban = "GB00TEST00000000000000",
      currency = "Communauté Financière Africaine BCEAO, Francs"
    )

  val changedBankDetails: BankDetailsData =
    BankDetailsData(
      accountHolderType = ThirdPartySomeoneElse,
      accountName = "CHANGENAME123456789",
      bic = "TESTXX00",
      iban = "GB00TESTIBANXX",
      currency = "GBP - United Kingdom, Pounds"
    )

  val sadRefData: SadFlowData =
    SadReferenceData(
      referenceNumber = "98765",
      importDay = "01",
      importMonth = "01",
      importYear = "2026",
      supplierName = "Test Supplier",
      supplierAddress1 = "Test Street123",
      supplierAddress2 = "Test City456",
      supplierAddress3 = "Test State789",
      supplierCountry = "HEARD ISLAND & MCDONALD ISLANDS"
    )

  val sadDocumentData: SadFlowData =
    SadDocumentData(
      documentDetails = "Test Import Document Details",
      importDay = "01",
      importMonth = "01",
      importYear = "2026",
      supplierName = "Test Supplier",
      supplierAddress1 = "Test Street123",
      supplierAddress2 = "Test City456",
      supplierAddress3 = "Test State789",
      supplierCountry = "HEARD ISLAND & MCDONALD ISLANDS"
    )

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
      phone = "9876543210",
      businessActivity = Some(
        BusinessActivityData(
          secondSicCode = "2222",
          thirdSicCode = "3333"
        )
      )
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

  val austriaClaim: ClaimData =
    ClaimData(
      country = "Austria",
      language = None,
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
      phone = "9876543210",
      businessActivity = None
    )
}
