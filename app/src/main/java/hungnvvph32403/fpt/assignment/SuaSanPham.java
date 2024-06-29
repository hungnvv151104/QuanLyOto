package hungnvvph32403.fpt.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import hungnvvph32403.fpt.assignment.Adapter.CarAdapter;
import hungnvvph32403.fpt.assignment.Api.CarsApiService;
import hungnvvph32403.fpt.assignment.Model.CarModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SuaSanPham extends AppCompatActivity {

    TextInputEditText edTen, edNamSX, edHang, edGia;
    Button btn_save,btn_cancel;

    List<CarModel> list = new ArrayList<>();

    private CarsApiService carsApiService;

    private CarAdapter carAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sua_san_pham);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edTen = findViewById(R.id.up_tenxe);
        edNamSX = findViewById(R.id.up_namxe);
        edHang = findViewById(R.id.up_hangxe);
        edGia = findViewById(R.id.up_giaxe);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);

        String ten = getIntent().getStringExtra("ten");
        int namSX = getIntent().getIntExtra("namSX",0);
        String hang = getIntent().getStringExtra("hang");
        int gia = getIntent().getIntExtra("gia",0);

        edTen.setText(ten);
        edNamSX.setText(String.valueOf(namSX));
        edHang.setText(hang);
        edGia.setText(String.valueOf(gia));

        carAdapter = new CarAdapter(getApplicationContext(),list);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten = edTen.getText().toString();
                int nam = Integer.parseInt(edNamSX.getText().toString());
                String hang = edHang.getText().toString();
                int gia = Integer.parseInt(edGia.getText().toString());

                if (ten.isEmpty() || hang.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                CarModel updatedCar = new CarModel(ten, nam, hang, gia);
                suaXe(updatedCar);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuaSanPham.this, ListCarActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private int getCarIndexById(String carId) {
        for (int i = 0; i < list.size(); i++) {
            CarModel car = list.get(i);
            if (car.get_id().equals(carId)) {
                return i;
            }
        }
        return -1;
    }
    private void suaXe(CarModel car) {
        String carId = getIntent().getStringExtra("carId");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.55.101:3000/") // Thay đổi địa chỉ IP và cổng nếu cần
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        carsApiService = retrofit.create(CarsApiService.class);

        Call<CarModel> call = carsApiService.updateCar(carId, car);
        call.enqueue(new Callback<CarModel>() {
            @Override
            public void onResponse(Call<CarModel> call, Response<CarModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Cập nhật thông tin xe thành công", Toast.LENGTH_SHORT).show();
                    CarModel updatedCar = response.body();
                    int carIndex = getCarIndexById(updatedCar.get_id());

                    if (carIndex != -1) {
                        list.set(carIndex, updatedCar);
                        carAdapter.notifyDataSetChanged();
                    }

                    startActivity(new Intent(SuaSanPham.this, ListCarActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi cập nhật thông tin xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CarModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.e("SuaSanPham", "Lỗi kết nối: " + t.getMessage(), t);
                carAdapter.notifyDataSetChanged();
            }
        });
    }

}