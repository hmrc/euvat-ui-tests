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

import uk.gov.hmrc.ui.pages.claim.MakeEuvatClaim
import uk.gov.hmrc.ui.pages.supportingDocuments.*
import uk.gov.hmrc.ui.utils.FlowLogging

trait SupportingDocumentsFlows extends FlowLogging {

  def startSupportingDocumentsFlow(): Unit = {
    logStep("I start Supporting documents")
    MakeEuvatClaim.clickLinkByText("Supporting documents")
    BeforeYouStartSupportingDocuments.verifyPageTitle(BeforeYouStartSupportingDocuments.pageTitle)
    BeforeYouStartSupportingDocuments.continue()
  }

  def addSupportingDocuments(data: SupportingDocumentsData): Unit = {
    logStep("I add supporting documents")
    AnySupportingDocuments.verifyPageTitle(AnySupportingDocuments.pageTitle)
    if (data.hasSupportingDocuments) {
      AnySupportingDocuments.continueAsYes()
      UploadFile.verifyPageTitle(UploadFile.pageTitle)
      logStep("User selects a file to upload")
      UploadFile.selectFile("filesForUpload/Test1MbPDF.pdf")
      UploadFile.continue()
      YourFile.verifyPageTitle(YourFile.pageTitle)
      YourFile.inputAdditionalInfo("Test input additional information")
      YourFile.continueAfterFileUploaded()

//    This needs to uncomment after integration of RA6.2 & RA6.3 screens is in place
//      logStep("I view uploaded supporting documents")
//      UploadedFiles.verifyPageTitle(UploadedFiles.pageTitle)
//      UploadedFiles.clickUploadFileCTA()
//      UploadFile.verifyPageTitle(UploadFile.pageTitle)
//      logStep("User selects a file to upload")
//      UploadFile.selectFile("filesForUpload/Test1MbPDF.pdf")
//      UploadFile.continue()
//      YourFile.verifyPageTitle(YourFile.pageTitle)
//      YourFile.continueAfterFileUploaded()
//      UploadedFiles.verifyPageTitle(UploadedFiles.pageTitle)
//      UploadedFiles.continue()
//      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
    } else {
      AnySupportingDocuments.continueAsNo()
      MakeEuvatClaim.verifyPageTitle(MakeEuvatClaim.pageTitle)
    }
  }

}
