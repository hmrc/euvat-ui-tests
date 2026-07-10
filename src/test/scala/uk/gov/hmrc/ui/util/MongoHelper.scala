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

package uk.gov.hmrc.ui.util

import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, SingleObservableFuture}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import scala.concurrent.Await

trait MongoHelper extends BeforeAndAfterEach with BeforeAndAfterAll { self: Suite =>

  // MongoDB Configuration
  private val mongoUri           = "mongodb://localhost:27017"
  private val mongoDatabaseName1 = "euvat-management-frontend"
  private val mongoDatabaseName2 = "euvat-filing-frontend"

  def dropMongoCollections(): Unit = {
    val mongoClient: MongoClient = MongoClient(mongoUri)

    val database1: MongoDatabase = mongoClient.getDatabase(mongoDatabaseName1)
    val database2: MongoDatabase = mongoClient.getDatabase(mongoDatabaseName2)

    val collection1: MongoCollection[Document] = database1.getCollection("user-answers")
    val collection2: MongoCollection[Document] = database2.getCollection("user-answers")

    try {
      Await.result(collection1.drop().toFuture(), 10.seconds)
      println(
        s"******************** MONGODB COLLECTION 'user-answers' IN DATABASE '$mongoDatabaseName1' DROPPED SUCCESSFULLY. ********************"
      )

      Await.result(collection2.drop().toFuture(), 10.seconds)
      println(
        s"******************** MONGODB COLLECTION 'user-answers' IN DATABASE '$mongoDatabaseName2' DROPPED SUCCESSFULLY. ********************"
      )
    } catch {
      case e: Exception =>
        println(s"********** FAILED TO DROP MONGODB COLLECTION 'user-answers': ${e.getMessage} **********")
    } finally mongoClient.close()
  }

}
