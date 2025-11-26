package com.example.khorshed_2207013_gpa_calculator_builder;

import com.example.khorshed_2207013_gpa_calculator_builder.DatabaseManager;
import com.example.khorshed_2207013_gpa_calculator_builder.Course;
import com.example.khorshed_2207013_gpa_calculator_builder.Student;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    public int insertStudent(Student student, List<Course> courses) throws SQLException {
        String insertStudentSql = """
                INSERT INTO students (student_name, student_id, semester, total_credits, cgpa, target_credits)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection conn = DatabaseManager.getConnection();
        conn.setAutoCommit(false);

        try (PreparedStatement pstmt = conn.prepareStatement(insertStudentSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, student.getStudentName());
            pstmt.setString(2, student.getStudentId());
            pstmt.setString(3, student.getSemester());
            pstmt.setDouble(4, student.getTotalCredits());
            pstmt.setDouble(5, student.getCgpa());
            pstmt.setInt(6, student.getTargetCredits());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int studentRecordId = keys.getInt(1);
                    insertCourses(studentRecordId, courses, conn);
                    conn.commit();
                    return studentRecordId;
                }
            }
            throw new SQLException("Failed to retrieve generated student ID");
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private void insertCourses(int studentRecordId, List<Course> courses, Connection conn) throws SQLException {
        String insertCourseSql = """
                INSERT INTO courses (student_record_id, course_name, course_code, course_credit, teacher_one, teacher_two, grade, grade_point)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(insertCourseSql)) {
            for (Course course : courses) {
                pstmt.setInt(1, studentRecordId);
                pstmt.setString(2, course.getCourseName());
                pstmt.setString(3, course.getCourseCode());
                pstmt.setDouble(4, course.getCourseCredit());
                pstmt.setString(5, course.getTeacherOne());
                pstmt.setString(6, course.getTeacherTwo());
                pstmt.setString(7, course.getGrade());
                pstmt.setDouble(8, course.getGradePoint());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT * FROM students ORDER BY created_at DESC";
        List<Student> students = new ArrayList<>();

        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        }
        return students;
    }

    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        }
        return null;
    }

    public List<Course> getCoursesByStudentId(int studentRecordId) throws SQLException {
        String sql = "SELECT * FROM courses WHERE student_record_id = ?";
        List<Course> courses = new ArrayList<>();

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, studentRecordId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(new Course(
                            rs.getString("course_name"),
                            rs.getString("course_code"),
                            rs.getDouble("course_credit"),
                            rs.getString("teacher_one"),
                            rs.getString("teacher_two"),
                            rs.getString("grade"),
                            rs.getDouble("grade_point")
                    ));
                }
            }
        }
        return courses;
    }

    public void updateStudent(Student student) throws SQLException {
        String sql = """
                UPDATE students 
                SET student_name = ?, semester = ?, total_credits = ?, cgpa = ?, target_credits = ?
                WHERE id = ?
                """;

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, student.getStudentName());
            pstmt.setString(2, student.getSemester());
            pstmt.setDouble(3, student.getTotalCredits());
            pstmt.setDouble(4, student.getCgpa());
            pstmt.setInt(5, student.getTargetCredits());
            pstmt.setInt(6, student.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : LocalDateTime.now();

        return new Student(
                rs.getInt("id"),
                rs.getString("student_name"),
                rs.getString("student_id"),
                rs.getString("semester"),
                rs.getDouble("total_credits"),
                rs.getDouble("cgpa"),
                rs.getInt("target_credits"),
                createdAt
        );
    }
}
