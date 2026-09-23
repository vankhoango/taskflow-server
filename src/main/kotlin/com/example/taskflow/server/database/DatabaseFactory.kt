package com.example.taskflow.server.database

import java.sql.Connection
import java.sql.DriverManager

object DatabaseFactory {

    private const val DATABASE_URL =
        "jdbc:sqlite:taskflow.db"

    fun connect(): Connection {
        return DriverManager.getConnection(DATABASE_URL)
    }

    fun initialize() {
        connect().use { connection ->
            connection.createStatement().use { statement ->
                statement.execute("""
                    CREATE TABLE IF NOT EXISTS projects (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }
    }
}