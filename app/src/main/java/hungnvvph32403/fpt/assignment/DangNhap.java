package hungnvvph32403.fpt.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.database.FirebaseDatabase;

public class DangNhap extends AppCompatActivity {
    Button btnLogin,btnCancel;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user ;
    EditText edtUserName , edtPassWord ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        anhXa();


        btnLogin = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtUserName.setText("");
                edtPassWord.setText("");
                Toast.makeText(DangNhap.this, "Hủy đăng nhập", Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.tvDangky).setOnClickListener(v -> {
            startActivity(new Intent(DangNhap.this,DangKy.class));
        });


        btnLogin.setOnClickListener(v -> {

          if (edtUserName.getText().toString().trim().isEmpty()||
                  edtPassWord.getText().toString().trim().isEmpty()){
              Toast.makeText(this, "Bạn nhập thiếu", Toast.LENGTH_SHORT).show();
          }else {
              String email = edtUserName.getText().toString().trim();
              String pass = edtPassWord.getText().toString().trim();


                user = auth.getCurrentUser();
              auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()){
                          Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(DangNhap.this , ListCarActivity.class));
                          finish();
                      }else {
                          Toast.makeText(DangNhap.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }
        });
    }



    private void anhXa (){
        edtUserName = findViewById(R.id.edt_user);
        edtPassWord = findViewById(R.id.edt_pass);
        edtUserName.setSingleLine(true);
        edtPassWord.setSingleLine(true);
    }

}