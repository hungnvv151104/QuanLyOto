    package hungnvvph32403.fpt.assignment;

    import android.content.Intent;
    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.google.android.material.textfield.TextInputEditText;

    import java.util.List;

    import hungnvvph32403.fpt.assignment.Api.CarsApiService;
    import hungnvvph32403.fpt.assignment.Model.CarModel;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;
    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;

    public class ThemSanPham extends AppCompatActivity {

        TextInputEditText addTen, addNamSX, addHang, addGia;
        Button btn_add,btn_cancel;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_them_san_pham);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            addTen = findViewById(R.id.add_tenxe);
            addNamSX = findViewById(R.id.add_namxe);
            addHang = findViewById(R.id.add_hangxe);
            addGia = findViewById(R.id.add_giaxe);
            btn_add = findViewById(R.id.btn_add);
            btn_cancel = findViewById(R.id.btn_cancel);

            btn_add.setOnClickListener(view -> addSanPham());
            btn_cancel.setOnClickListener(view -> huyAdd());
        }

        private void addSanPham(){
            String ten = addTen.getText().toString();
            int nam = Integer.parseInt(addNamSX.getText().toString());
            String hang = addHang.getText().toString();
            int gia = Integer.parseInt(addGia.getText().toString());

            if(ten.isEmpty() || hang.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            CarModel newCar = new CarModel(ten,nam, hang, gia);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.55.101:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            CarsApiService carsApiService = retrofit.create(CarsApiService.class);

            Call<List<CarModel>> call = carsApiService.addCar(newCar);

            call.enqueue(new Callback<List<CarModel>>() {
                @Override
                public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>>response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ThemSanPham.this, "Sản phẩm đã được thêm thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ThemSanPham.this, ListCarActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ThemSanPham.this, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<CarModel>> call, Throwable t) {
                    Toast.makeText(ThemSanPham.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ThemSanPham.this, ListCarActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }

        private void huyAdd(){
            Toast.makeText(this, "Hủy thêm sản phẩm!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ThemSanPham.this,ListCarActivity.class);
            startActivity(intent);
            finish();

        }

    }