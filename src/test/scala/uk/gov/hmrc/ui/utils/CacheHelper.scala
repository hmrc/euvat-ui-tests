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

package uk.gov.hmrc.ui.utils

import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.{SingleObservableFuture, bsonDocumentToDocument}
import play.api.libs.json.Json

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global

object CacheHelper extends HttpClient with FileHelper with JsonHelper with MongoHelper {

  def submitClaimDetailsAnswers(manageFileName: String, filingFileName: String): Unit = {
    val sharedId = UUID.randomUUID().toString

    val manageJson = getJson(manageFileName)
      .withCreatedAt()
      .withLastUpdated()
      .withId(sharedId)

    val filingJson = getJson(filingFileName)
      .withCreatedAt()
      .withLastUpdated()
      .withId(sharedId)

    awaitResult {
      for {
        _ <- mongoClient
               .getDatabase("euvat-management-frontend")
               .getCollection("user-answers")
               .insertOne(bsonDocumentToDocument(BsonDocument(Json.stringify(manageJson))))
               .toFuture()

        _ <- mongoClient
               .getDatabase("euvat-filing-frontend")
               .getCollection("user-answers")
               .insertOne(bsonDocumentToDocument(BsonDocument(Json.stringify(filingJson))))
               .toFuture()
      } yield ()
    }
  }

  def submitPurchaseDetailsAnswers(fileName: String): Unit = {
    val json = getJson(fileName).withCreatedAt().withLastUpdated().withId()
    awaitResult {
      mongoClient
        .getDatabase("euvat-filing-frontend")
        .getCollection("user-answers")
        .insertOne(bsonDocumentToDocument(BsonDocument(Json.stringify(json))))
        .toFuture()
    }
  }
}
