package com.example.coffee;

public class DoUong {
    private int maDoUong;
    private String tenDoUong;
    private double donGia;
    private String moTa;
    private String hinhAnh;

    public DoUong(int maDoUong, String tenDoUong, double donGia, String moTa, String hinhAnh) {
        this.maDoUong = maDoUong;
        this.tenDoUong = tenDoUong;
        this.donGia = donGia;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }

    public int getMaDoUong() {
        return maDoUong;
    }

    public String getTenDoUong() {
        return tenDoUong;
    }

    public double getDonGia() {
        return donGia;
    }

    public String getMoTa() {
        return moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }
}
