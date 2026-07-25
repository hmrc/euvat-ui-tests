/*
 * Copyright 2024 HM Revenue & Customs
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

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}

import java.sql.{Connection, DriverManager, Statement}

trait DatabaseHelper extends BeforeAndAfterEach with BeforeAndAfterAll { self: Suite =>

  // Oracle Database Configuration
  private val oracleUrl      = "jdbc:oracle:thin:@//localhost:1521/XE"
  private val oracleUsername = "sys as sysdba"
  private val oraclePassword = "oracle"

//  Deletes data created for VRN 999900001 in Oracle DB tables REFUND_APPLICATION
  def deleteOracleTableData(): Unit = {
    var connection: Connection = null
    var statement: Statement   = null

    try {
      connection = DriverManager.getConnection(oracleUrl, oracleUsername, oraclePassword)
      connection.setAutoCommit(false)
      statement = connection.createStatement()

      // Delete from the table REFUND_APPLICATION
      val deleteQuery =
        """
        DELETE FROM EUVAT_FILE_DATA.REFUND_APPLICATION
        WHERE APPLICANT_VAT_REG_NUMBER = 999900001
      """
      val rowsDeleted = statement.executeUpdate(deleteQuery)
      println(s"********** DELETED $rowsDeleted ROWS FROM MONTHLY_RETURN. **********")

      // Commit the transaction
      connection.commit()
      println("********** DATA DELETION IN RDS CANDE COMPLETED SUCCESSFULLY. **********")
    } catch {
      case e: Exception =>
        e.printStackTrace()
        if (connection != null) {
          println("********** ROLLING BACK TRANSACTION DUE TO AN ERROR. **********")
          connection.rollback()
        }
    } finally {
      if (statement != null) statement.close()
      if (connection != null) connection.close()
    }
  }

  def cleanupDatabaseIfNotStub(): Unit = {
    val isStubEnvironment = Env.USE_STUB
    if (!isStubEnvironment) {
      println("********** RUNNING ORACLE DATABASE CLEANUP AS THIS IS NOT A STUB ENVIRONMENT. **********")
      deleteOracleTableData()
    } else {
      println("********** SKIPPING ORACLE DATABASE CLEANUP AS THIS IS A STUB ENVIRONMENT. **********")
    }
  }

}
