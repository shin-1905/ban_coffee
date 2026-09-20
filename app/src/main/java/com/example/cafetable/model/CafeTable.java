package com.example.cafetable.model;

public class CafeTable {
    private int id;
    private String name;
    private int seats;
    private String status;
    private String note;

    public CafeTable() {
    }

    public CafeTable(int id, String name, int seats, String status, String note) {
        this.id = id;
        this.name = name;
        this.seats = seats;
        this.status = status;
        this.note = note;
    }

    public CafeTable(String name, int seats, String status, String note) {
        this.name = name;
        this.seats = seats;
        this.status = status;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
