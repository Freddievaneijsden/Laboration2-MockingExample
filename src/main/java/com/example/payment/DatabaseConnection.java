package com.example.payment;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DatabaseConnection {

    void executeUpdate(String s) throws SQLException;
}
