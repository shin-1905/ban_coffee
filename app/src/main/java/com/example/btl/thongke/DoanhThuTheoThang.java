package com.example.btl.thongke;

public class DoanhThuTheoThang {
    private final int thang;
    private final int soHoaDon;
    private final double doanhThu;

    public DoanhThuTheoThang(int thang, int soHoaDon, double doanhThu) {
        this.thang = thang;
        this.soHoaDon = soHoaDon;
        this.doanhThu = doanhThu;
    }

    public int getThang() {
        return thang;
    }

    public int getSoHoaDon() {
        return soHoaDon;
    }

    public double getDoanhThu() {
        return doanhThu;
    }
}
