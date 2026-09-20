package com.example.btl.api.model;

public class ChiTietGioHangRequest {
    private int maMon;
    private int soLuong;

    public ChiTietGioHangRequest() {
    }

    public ChiTietGioHangRequest(int maMon, int soLuong) {
        this.maMon = maMon;
        this.soLuong = soLuong;
    }

    public int getMaMon() {
        return maMon;
    }

    public void setMaMon(int maMon) {
        this.maMon = maMon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
