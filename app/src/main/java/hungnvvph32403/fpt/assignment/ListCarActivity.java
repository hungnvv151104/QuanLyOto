package hungnvvph32403.fpt.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hungnvvph32403.fpt.assignment.Adapter.CarAdapter;
import hungnvvph32403.fpt.assignment.Api.CarsApiService;
import hungnvvph32403.fpt.assignment.Model.CarModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListCarActivity extends AppCompatActivity {

    ListView lv;
    List<CarModel> list;
    CarAdapter carAdapter;

    FloatingActionButton btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_car);

        lv = findViewById(R.id.rc_car);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.55.101:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CarsApiService carsApiService = retrofit.create(CarsApiService.class);

        Call<List<CarModel>> call = carsApiService.getCars();

        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    Log.d("ListCarActivity", "Response: " + list.size() + " items received");

                    carAdapter = new CarAdapter(getApplicationContext(), list);
                    lv.setAdapter(carAdapter);
                    carAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ListCarActivity", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Log.e("ListCarActivity", "API call failed", t);
            }
        });

        ImageButton btnSortHighToLow = findViewById(R.id.btn_sort_high_to_low);
        ImageButton btnSortLowToHigh = findViewById(R.id.btn_sort_low_to_high);

        btnSortHighToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByPriceHighToLow();
            }
        });

        btnSortLowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByPriceLowToHigh();
            }
        });

        SearchView searchView = findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themXe();
            }
        });
    }

    private void themXe() {
        Intent intent = new Intent(this, ThemSanPham.class);
        startActivity(intent);
        finish();
    }

    private void filter(String query) {
        List<CarModel> filteredList = new ArrayList<>();
        for (CarModel car : list) {
            // Xử lý logic lọc dữ liệu tại đây
            if (car.getTen().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(car);
            }
        }
        // Cập nhật danh sách hiển thị với danh sách đã lọc
        carAdapter.setData(filteredList);
        carAdapter.notifyDataSetChanged();
    }

    private void sortByPriceHighToLow() {
        Collections.sort(list, new Comparator<CarModel>() {
            @Override
            public int compare(CarModel car1, CarModel car2) {
                // Sắp xếp theo giá từ cao đến thấp
                return Float.compare(car2.getGia(), car1.getGia());
            }
        });

        carAdapter.notifyDataSetChanged();
    }

    private void sortByPriceLowToHigh() {
        Collections.sort(list, new Comparator<CarModel>() {
            @Override
            public int compare(CarModel car1, CarModel car2) {
                // Sắp xếp theo giá từ thấp lên cao
                return Float.compare(car1.getGia(), car2.getGia());
            }
        });

        carAdapter.notifyDataSetChanged();
    }
}