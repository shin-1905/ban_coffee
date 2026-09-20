package com.example.btl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coffee.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

public class login extends AppCompatActivity {
    TextInputEditText txtpass, txtusername;
    Button btndangnhap;
    SQlite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_layout);
        txtpass = findViewById(R.id.txtpass);
        txtusername = findViewById(R.id.txtusername);
        btndangnhap = findViewById(R.id.btnlogin);
        db = new SQlite(this);
        dangnhap();
    }

    private void dangnhap() {
        btndangnhap.setOnClickListener(v ->{
            String tk = txtusername.getText().toString();
            String mk = txtpass.getText().toString();
            if (tk.isEmpty() || mk.isEmpty()){
                Toast.makeText(this, "Vui long nhap tai khoan mat khau", Toast.LENGTH_SHORT).show();
                return;
            }
            int role = db.checkLoginRole(tk, mk);
            if(role == 0){
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
            else if(role == 1){
                startActivity(new Intent(this, trangchu.class));
                Toast.makeText(this, "Ban la quan ly", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(role == 2){
                startActivity(new Intent(this, MainActivity.class));
                Toast.makeText(this, "Ban la nhan vien ", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }
}