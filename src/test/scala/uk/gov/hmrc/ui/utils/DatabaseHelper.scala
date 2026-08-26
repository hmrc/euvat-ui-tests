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

      val deletePurchaseQuery =
        """
          DELETE FROM EUVAT_FILE_DATA.PURCHASE
          WHERE APPLICATION_ID IN (
            SELECT APPLICATION_ID
            FROM EUVAT_FILE_DATA.REFUND_APPLICATION
            WHERE APPLICANT_VAT_REG_NUMBER = 999900001
              AND REFUNDING_COUNTRY_CODE IN ('EE', 'DE', 'FR')
          )
        """

      val purchaseRowsDeleted = statement.executeUpdate(deletePurchaseQuery)
      println(s"******************** DELETED $purchaseRowsDeleted ROWS FROM PURCHASE. ********************")

      val deleteRefundApplicationQuery =
        """
          DELETE FROM EUVAT_FILE_DATA.REFUND_APPLICATION
          WHERE APPLICANT_VAT_REG_NUMBER = 999900001
            AND REFUNDING_COUNTRY_CODE IN ('EE', 'DE', 'FR')
        """

      val refundRowsDeleted = statement.executeUpdate(deleteRefundApplicationQuery)
      println(s"******************** DELETED $refundRowsDeleted ROWS FROM REFUND_APPLICATION. ********************")

      connection.commit()
      println("******************** DATA DELETION COMPLETED SUCCESSFULLY. ********************")
    } catch {
      case e: Exception =>
        e.printStackTrace()
        if (connection != null) {
          println("******************** ROLLING BACK TRANSACTION DUE TO AN ERROR. ********************")
          connection.rollback()
        }
    } finally {
      if (statement != null) statement.close()
      if (connection != null) connection.close()
    }
  }

  def insertDuplicatePurchaseRecordTID(): Unit = {
    var connection: Connection = null
    var selectStmt: Statement  = null
    var insertStmt: Statement  = null
    var rs: java.sql.ResultSet = null

    try {
      connection = DriverManager.getConnection(oracleUrl, oracleUsername, oraclePassword)
      connection.setAutoCommit(false)

      selectStmt = connection.createStatement()

      val selectApplicationIdQuery =
        """
        SELECT APPLICATION_ID
        FROM EUVAT_FILE_DATA.REFUND_APPLICATION
        WHERE APPLICANT_VAT_REG_NUMBER = 999900001
          AND REFUNDING_COUNTRY_CODE = 'DE'
        ORDER BY APPLICATION_ID DESC
      """

      rs = selectStmt.executeQuery(selectApplicationIdQuery)

      if (rs.next()) {
        val applicationId = rs.getLong("APPLICATION_ID")

        insertStmt = connection.createStatement()

        val insertPurchaseQuery =
          s"""
           INSERT INTO EUVAT_FILE_DATA.PURCHASE (
             APPLICATION_ID,
             ITEM_NUMBER,
             GOODS_DESCRIPTION_CATEGORY,
             INVOICE_NUMBER,
             SUPPLIER_TAX_IDENTIFIER
           )
           VALUES (
             $applicationId,
             2,
             '10',
             'INV-1',
             'TID-1'
           )
         """

        val rowsInserted = insertStmt.executeUpdate(insertPurchaseQuery)
        println(
          s"******************** INSERTED $rowsInserted DUPLICATE PURCHASE ROW FOR APPLICATION_ID=$applicationId WITH ITEM_NUMBER=2 ********************"
        )
      } else {
        throw new RuntimeException("No refund application found for VRN 999900001 and country DE")
      }

      connection.commit()
    } catch {
      case e: Exception =>
        e.printStackTrace()
        if (connection != null) connection.rollback()
    } finally {
      if (rs != null) rs.close()
      if (selectStmt != null) selectStmt.close()
      if (insertStmt != null) insertStmt.close()
      if (connection != null) connection.close()
    }
  }

  def insertDuplicatePurchaseRecordVRN(): Unit = {
    var connection: Connection = null
    var selectStmt: Statement  = null
    var insertStmt: Statement  = null
    var rs: java.sql.ResultSet = null

    try {
      connection = DriverManager.getConnection(oracleUrl, oracleUsername, oraclePassword)
      connection.setAutoCommit(false)

      selectStmt = connection.createStatement()

      val selectApplicationIdQuery =
        """
          SELECT APPLICATION_ID
          FROM EUVAT_FILE_DATA.REFUND_APPLICATION
          WHERE APPLICANT_VAT_REG_NUMBER = 999900001
            AND REFUNDING_COUNTRY_CODE = 'EE'
          ORDER BY APPLICATION_ID DESC
        """

      rs = selectStmt.executeQuery(selectApplicationIdQuery)

      if (rs.next()) {
        val applicationId = rs.getLong("APPLICATION_ID")

        insertStmt = connection.createStatement()

        val insertPurchaseQuery =
          s"""
             INSERT INTO EUVAT_FILE_DATA.PURCHASE (
               APPLICATION_ID,
               ITEM_NUMBER,
               GOODS_DESCRIPTION_CATEGORY,
               INVOICE_NUMBER,
               GOODS_DESCRIPTION_SUBCATEGORY,
               SUPPLIER_VAT_REG_NUMBER
             )
             VALUES (
               $applicationId,
               2,
               '7',
               'DUP',
               '7.1.1',
               'EE0000000111'
             )
           """

        val rowsInserted = insertStmt.executeUpdate(insertPurchaseQuery)
        println(
          s"******************** INSERTED $rowsInserted DUPLICATE PURCHASE ROW FOR APPLICATION_ID=$applicationId WITH ITEM_NUMBER=2 ********************"
        )
      } else {
        throw new RuntimeException("No refund application found for VRN 999900001 and country EE")
      }

      connection.commit()
    } catch {
      case e: Exception =>
        e.printStackTrace()
        if (connection != null) connection.rollback()
    } finally {
      if (rs != null) rs.close()
      if (selectStmt != null) selectStmt.close()
      if (insertStmt != null) insertStmt.close()
      if (connection != null) connection.close()
    }
  }

  def cleanupDatabaseIfNotStub(): Unit = {
    val isStubEnvironment = Env.USE_STUB
    if (!isStubEnvironment) {
      println(
        "******************** RUNNING ORACLE DATABASE CLEANUP AS THIS IS NOT A STUB ENVIRONMENT. ********************"
      )
      deleteOracleTableData()
    } else {
      println(
        "******************** SKIPPING ORACLE DATABASE CLEANUP AS THIS IS A STUB ENVIRONMENT. ********************"
      )
    }
  }

}
