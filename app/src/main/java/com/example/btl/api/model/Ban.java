package com.example.btl.api.model;

public class Ban {
    private int maBan;
    private String tenBan;

    public Ban() {
    }

    public Ban(int maBan, String tenBan) {
        this.maBan = maBan;
        this.tenBan = tenBan;
    }

    public int getMaBan() {
        return maBan;
    }

    public void setMaBan(int maBan) {
        this.maBan = maBan;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }
}
