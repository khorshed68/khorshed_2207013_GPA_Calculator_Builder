package com.example.khorshed_2207013_gpa_calculator_builder;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {
    private static final String DB_NAME = "gpa_calculator.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;
    private static Connection connection;

    private DatabaseManager() {
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            initializeSchema();
        }
        return connection;
    }

    private static void initializeSchema() {
        String createStudentsTable = """
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_name TEXT NOT NULL,
                    student_id TEXT UNIQUE NOT NULL,
                    semester TEXT,
                    total_credits REAL NOT NULL,
                    cgpa REAL NOT NULL,
                    target_credits INTEGER NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

        String createCoursesTable = """
                CREATE TABLE IF NOT EXISTS courses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_record_id INTEGER NOT NULL,
                    course_name TEXT NOT NULL,
                    course_code TEXT NOT NULL,
                    course_credit REAL NOT NULL,
                    teacher_one TEXT,
                    teacher_two TEXT,
                    grade TEXT NOT NULL,
                    grade_point REAL NOT NULL,
                    FOREIGN KEY (student_record_id) REFERENCES students(id) ON DELETE CASCADE
                )
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createStudentsTable);
            stmt.execute(createCoursesTable);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }

    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    public static boolean databaseExists() {
        return new File(DB_NAME).exists();
    }
}
