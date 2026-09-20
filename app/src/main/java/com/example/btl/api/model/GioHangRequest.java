package com.example.btl.api.model;

import java.util.ArrayList;
import java.util.List;

public class GioHangRequest {
    private Integer maNhanVien;
    private String ghiChu;
    private List<ChiTietGioHangRequest> chiTiet = new ArrayList<>();

    public GioHangRequest() {
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

    public List<ChiTietGioHangRequest> getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(List<ChiTietGioHangRequest> chiTiet) {
        this.chiTiet = chiTiet;
    }
}
