package com.mohit.LMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection 
{
    private static final String url = "jdbc:mysql://localhost:3306/library_db";
    private static final String user = "root";
    private static final String password = "Mohit12@34"; // change if needed

    public static Connection getConnection() throws SQLException 
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } 
        catch (ClassNotFoundException e) 
        {
            System.out.println("JDBC Driver not found: " + e.getMessage());
        }
        
        return DriverManager.getConnection(url, user, password);
    }
}
