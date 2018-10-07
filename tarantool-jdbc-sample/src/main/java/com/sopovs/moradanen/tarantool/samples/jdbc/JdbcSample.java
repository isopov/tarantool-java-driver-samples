package com.sopovs.moradanen.tarantool.samples.jdbc;

import java.sql.*;

public class JdbcSample {

    public static void main(String[] args) throws SQLException {
        //IN most real applications you should use some jdbc connection pool like Hikari
        try (Connection con = DriverManager.getConnection("jdbc:tarantool://localhost");
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE MESSAGES (ID INTEGER PRIMARY KEY, VALUE VARCHAR(100))");

            try (PreparedStatement pst = con.prepareStatement("INSERT INTO MESSAGES VALUES(?,?)")) {
                pst.setInt(1, 1);
                pst.setString(2, "Hello World!");
                pst.executeUpdate();
            }

            try (ResultSet res = st.executeQuery("SELECT * FROM MESSAGES")) {
                while (res.next()) {
                    System.out.println(res.getString("VALUE"));
                }
            }

            st.executeUpdate("DROP TABLE MESSAGES");
        }
    }
}
