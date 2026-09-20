package com.example.coffee;

public class GioHang {
    private int maDoUong;
    private String tenDoUong;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private String hinhAnh;
    private String ghiChu;

    public GioHang(int maDoUong, String tenDoUong, int soLuong, double donGia,
                   double thanhTien, String hinhAnh, String ghiChu) {
        this.maDoUong = maDoUong;
        this.tenDoUong = tenDoUong;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.hinhAnh = hinhAnh;
        this.ghiChu = ghiChu;
    }

    public int getMaDoUong() {
        return maDoUong;
    }

    public String getTenDoUong() {
        return tenDoUong;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
