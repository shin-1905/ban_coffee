package com.example.coffeemanager.data;

public class Invoice {
    private final int id;
    private final int tableId;
    private final String date;
    private final double total;
    private final String status;

    public Invoice(int id, int tableId, String date, double total, String status) {
        this.id = id;
        this.tableId = tableId;
        this.date = date;
        this.total = total;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getTableId() {
        return tableId;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }
}
