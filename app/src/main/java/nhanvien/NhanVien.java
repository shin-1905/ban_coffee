package nhanvien;

public class NhanVien {
    private final String maNV, hoTen, gioiTinh, ngaySinh, soDienThoai, diaChi;

    public NhanVien(String maNV, String hoTen, String gioiTinh,
                    String ngaySinh, String soDienThoai, String diaChi) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }
}
