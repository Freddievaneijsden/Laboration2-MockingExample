package com.example.payment;

import java.sql.PreparedStatement;

public class DatabaseConnectionMock implements DatabaseConnection {

    @Override
    public PreparedStatement getInstance() {
        return null;
    }
}
