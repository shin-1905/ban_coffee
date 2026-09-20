package com.example.btl.api.model;

import java.util.List;

public class GioHangResponse {
    private int maGioHang;
    private Integer maNhanVien;
    private String ghiChu;
    private double tongTien;
    private List<ChiTietGioHangResponse> chiTiet;

    public GioHangResponse() {
    }

    public int getMaGioHang() {
        return maGioHang;
    }

    public void setMaGioHang(int maGioHang) {
        this.maGioHang = maGioHang;
    }

    public Integer getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(Integer maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public List<ChiTietGioHangResponse> getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(List<ChiTietGioHangResponse> chiTiet) {
        this.chiTiet = chiTiet;
    }
}
