package com.example.payment;

public class DatabaseConnectionMock implements DatabaseConnection {

    private boolean updateCalled = false;
    private String lastQuery;

    @Override
    public void executeUpdate(String query) {
        this.updateCalled = true;
        this.lastQuery = query;
    }

    public boolean wasUpdateCalled() {
        return updateCalled;
    }

    public String getLastQuery() {
        return lastQuery;
    }
}
