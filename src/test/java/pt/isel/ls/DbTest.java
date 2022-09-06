package pt.isel.ls;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DbTest {
    String url = "jdbc:postgresql://localhost/postgres?user=postgres&password=li42d-g10&ssl=false";
    Connection myCon;

    @Test
    public void testConnection() throws SQLException {
        myCon = DriverManager.getConnection(url);
        assertNotNull(myCon);
    }

    @Test
    public void testSelect() throws SQLException {
        try {
            myCon = DriverManager.getConnection(url);

            String[] arr = {"Alice", "Bob"};

            String bol = "SELECT name FROM students\n"
                    +
                    "WHERE course = ?;";
            PreparedStatement ps = myCon.prepareStatement(bol);
            ps.setInt(1, 1);
            ResultSet myRes = ps.executeQuery();
            int i = 0;
            while (myRes.next()) {
                assertEquals(arr[i], myRes.getString("name"));
                i++;

            }
            myRes.close();
            ps.close();
        } finally {
            if (myCon != null) {
                myCon.close();
            }
        }
    }

    @Test
    public void testInsert() throws SQLException {
        try {
            myCon = DriverManager.getConnection(url);
            myCon.setAutoCommit(false);


            String bol = "insert into students(course, number, name) values (?, ?, ?)";

            PreparedStatement ps = myCon.prepareStatement(bol);
            ps.setInt(1, 1);
            ps.setInt(2, 12349);
            ps.setString(3, "Carlos");
            int x = ps.executeUpdate();
            assertEquals(1, x);
            ResultSet keys = ps.getGeneratedKeys();
            while (keys.next()) {
                assertEquals("Carlos",keys.getString("name"));
            }
            ps.close();
            keys.close();

        } finally {
            if (myCon != null) {
                myCon.rollback();
                myCon.close();
            }
        }
    }

    @Test
    public void testDelete() throws SQLException {
        try {
            myCon = DriverManager.getConnection(url);
            myCon.setAutoCommit(false);
            String bol = "delete from students where name = ?";
            PreparedStatement ps = myCon.prepareStatement(bol);
            ps.setString(1, "Bob");
            int x = ps.executeUpdate();
            assertEquals(1, x);
            ps.close();
        } finally {
            if (myCon != null) {
                myCon.rollback();
                myCon.close();
            }
        }
    }

    @Test
    public void testUpdate() throws SQLException {
        try {
            myCon = DriverManager.getConnection(url);
            myCon.setAutoCommit(false);
            String bol = "UPDATE students set name=? where number = ?";
            PreparedStatement ps = myCon.prepareStatement(bol);
            ps.setString(1, "Catarina");
            ps.setInt(2, 12345);
            int x = ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            assertEquals(1, x);
            while (keys.next()) {
                assertEquals("Catarina", keys.getString(1));
            }
            keys.close();
            ps.close();
        } finally {
            if (myCon != null) {
                myCon.rollback();
                myCon.close();
            }
        }
    }
}