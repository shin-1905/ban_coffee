package com.example.btl.api;

import com.example.btl.api.model.Ban;
import com.example.btl.api.model.GioHangRequest;
import com.example.btl.api.model.GioHangResponse;
import com.example.btl.api.model.Mon;
import com.example.btl.api.model.NhanVien;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("api/mon")
    Call<List<Mon>> getDanhSachMon();

    @POST("api/mon")
    Call<Mon> themMon(@Body Mon mon);

    @PUT("api/mon/{id}")
    Call<Mon> suaMon(
            @Path("id") int id,
            @Body Mon mon
    );

    @DELETE("api/mon/{id}")
    Call<Void> xoaMon(
            @Path("id") int id
    );

    @GET("api/nhanvien")
    Call<List<NhanVien>> getDanhSachNhanVien();

    @GET("api/nhanvien/{id}")
    Call<NhanVien> getNhanVienTheoId(@Path("id") int id);

    @POST("api/nhanvien")
    Call<NhanVien> themNhanVien(@Body NhanVien nhanVien);

    @PUT("api/nhanvien/{id}")
    Call<NhanVien> suaNhanVien(
            @Path("id") int id,
            @Body NhanVien nhanVien
    );

    @DELETE("api/nhanvien/{id}")
    Call<Void> xoaNhanVien(@Path("id") int id);

    @GET("api/ban")
    Call<List<Ban>> getDanhSachBan();

    @GET("api/ban/{id}")
    Call<Ban> getBanTheoId(@Path("id") int id);

    @POST("api/ban")
    Call<Ban> themBan(@Body Ban ban);

    @PUT("api/ban/{id}")
    Call<Ban> suaBan(
            @Path("id") int id,
            @Body Ban ban
    );

    @DELETE("api/ban/{id}")
    Call<Void> xoaBan(@Path("id") int id);

    @GET("api/giohang")
    Call<List<GioHangResponse>> getDanhSachGioHang();

    @GET("api/giohang/{id}")
    Call<GioHangResponse> getGioHangTheoId(@Path("id") int id);

    @POST("api/giohang")
    Call<GioHangResponse> themGioHang(@Body GioHangRequest request);

    @PUT("api/giohang/{id}")
    Call<GioHangResponse> suaGioHang(
            @Path("id") int id,
            @Body GioHangRequest request
    );

    @DELETE("api/giohang/{id}")
    Call<Void> xoaGioHang(@Path("id") int id);
}
