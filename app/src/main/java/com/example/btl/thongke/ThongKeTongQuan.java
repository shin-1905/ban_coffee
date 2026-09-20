package com.example.btl.thongke;

public class ThongKeTongQuan {
    private final int soHoaDon;
    private final double tongDoanhThu;
    private final double trungBinhHoaDon;

    public ThongKeTongQuan(int soHoaDon, double tongDoanhThu, double trungBinhHoaDon) {
        this.soHoaDon = soHoaDon;
        this.tongDoanhThu = tongDoanhThu;
        this.trungBinhHoaDon = trungBinhHoaDon;
    }

    public int getSoHoaDon() {
        return soHoaDon;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public double getTrungBinhHoaDon() {
        return trungBinhHoaDon;
    }
}
