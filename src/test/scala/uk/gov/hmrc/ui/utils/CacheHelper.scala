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

package uk.gov.hmrc.ui.utils

import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{MongoClient, SingleObservableFuture, bsonDocumentToDocument}
import play.api.libs.json._

object CacheHelper extends HttpClient with FileHelper with JsonHelper {

  private lazy val mongoClient: MongoClient =
    MongoClient("mongodb://localhost:27017")

  private lazy val collection =
    mongoClient
      .getDatabase("euvat-filing-frontend")
      .getCollection("user-answers")

  def submitUserAnswers(filingFileName: String, sharedId: String): Unit = {
    val filingJson = getJson(filingFileName)
      .withCreatedAt()
      .withLastUpdated()
      .withId(sharedId)

    awaitResult {
      collection
        .insertOne(bsonDocumentToDocument(BsonDocument(Json.stringify(filingJson))))
        .toFuture()
    }
  }

  def updateUserAnswers(updateFileName: String, sharedId: String): Unit = {
    val updateJson = getJson(updateFileName).as[JsObject]

    val setFields = JsObject(
      updateJson.fields.map { case (key, value) =>
        s"data.$key" -> value
      }
    )

    val updateDoc = BsonDocument(
      Json.stringify(
        Json.obj(
          "$set"         -> setFields,
          "$currentDate" -> Json.obj("lastUpdated" -> true)
        )
      )
    )

    awaitResult {
      collection
        .updateOne(
          equal("_id", sharedId),
          updateDoc
        )
        .toFuture()
    }
  }
}
