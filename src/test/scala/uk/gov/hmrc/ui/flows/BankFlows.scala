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

import uk.gov.hmrc.ui.pages.bank.*
import uk.gov.hmrc.ui.pages.claim.MakeEuvatClaim
import uk.gov.hmrc.ui.utils.*

trait BankFlows extends FlowLogging {

  def startBankDetailsFlow(): Unit = {
    logStep("I start bank details")
    MakeEuvatClaim.clickLinkByText("Bank details")
    BeforeYouStartBank.verifyPageTitle(BeforeYouStartBank.pageTitle)
    BeforeYouStartBank.continue()
  }

  def addBankDetails(data: BankDetailsData): Unit = {
    logStep("I add bank details")
    AccountHolderType.verifyPageTitle(AccountHolderType.pageTitle)
    AccountHolderType.selectAccountHolderType(data.accountHolderType.value)

    BankDetailsForm.verifyPageTitle(BankDetailsForm.pageTitle)
    BankDetailsForm.submitBankDetailsForm(
      data.accountName,
      data.bic,
      data.iban,
      data.currency
    )

    CheckYourBankDetails.verifyPageTitle(CheckYourBankDetails.pageTitle)
  }

  def changeBankDetailsAfterSave(data: BankDetailsData): Unit = {
    logStep("I change bank details")
    MakeEuvatClaim.clickLinkByText("Bank details")
    CheckYourBankDetails.verifyPageTitle(CheckYourBankDetails.pageTitle)
    CheckYourBankDetails.clickChangeBankDetailsLink()

    AccountHolderType.verifyPageTitle(AccountHolderType.pageTitle)
    AccountHolderType.selectAccountHolderType(data.accountHolderType.value)

    BankDetailsForm.verifyPageTitle(BankDetailsForm.pageTitle)
    BankDetailsForm.submitBankDetailsForm(
      data.accountName,
      data.bic,
      data.iban,
      data.currency
    )

    CheckYourBankDetails.verifyPageTitle(CheckYourBankDetails.pageTitle)
  }

  def saveBankDetails(): Unit = {
    logStep("I save bank details")
    CheckYourBankDetails.saveAndContinue()
    MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
  }
}
