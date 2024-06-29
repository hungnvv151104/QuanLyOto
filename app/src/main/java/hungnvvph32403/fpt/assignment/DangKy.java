package hungnvvph32403.fpt.assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DangKy extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("NguoiDung");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    //Người dùng
    FirebaseUser user;

    EditText edtUser, edtPass, edtCheckPass;
    Button btnDangKi, btnCancelDK;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        anhXa();


        btnDangKi.setOnClickListener(v -> {
            progressDialog.show();
            if (edtUser.getText().toString().isEmpty() ||
                    edtPass.getText().toString().isEmpty() ||
                    edtCheckPass.getText().toString().isEmpty()
            ) {
                progressDialog.dismiss();

                Toast.makeText(this, "Giữ liệu còn thiếu", Toast.LENGTH_SHORT).show();
            } else if (!edtPass.getText().toString().equalsIgnoreCase(edtCheckPass.getText().toString())) {
                progressDialog.dismiss();

                Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            } else {
                String email, password;
                email = edtUser.getText().toString();
                password = edtPass.getText().toString();
                auth.createUserWithEmailAndPassword(email, password).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(DangKy.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                    user = auth.getCurrentUser();
                                    reference.child(user.getUid()).child("taiKhoan").setValue(email);

                                    //Thong tin
                                    reference.child(user.getUid()).child("ten").setValue(null);
                                    reference.child(user.getUid()).child("gioiTinh").setValue(null);
                                    reference.child(user.getUid()).child("ngaySinh").setValue(null);
                                    reference.child(user.getUid()).child("soDienThoai").setValue(null);
                                    reference.child(user.getUid()).child("diaChi").setValue(null);
                                    startActivity(new Intent(DangKy.this, ListCarActivity.class));

                                    finish();

                                } else {
                                    Toast.makeText(DangKy.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

            }


        });

        btnCancelDK.setOnClickListener(v -> {
            onBackPressed();
        });


    }


    private void anhXa() {
        progressDialog = new ProgressDialog(this);
        edtUser = findViewById(R.id.edt_userDK);
        edtPass = findViewById(R.id.edt_passDK);
        edtCheckPass = findViewById(R.id.edt_checkpassDK);
        btnDangKi = findViewById(R.id.btnsigninDK);
        btnCancelDK = findViewById(R.id.btnCancelDK);

        edtPass.setSingleLine(true);
        edtUser.setSingleLine(true);
        edtCheckPass.setSingleLine(true);
    }
}