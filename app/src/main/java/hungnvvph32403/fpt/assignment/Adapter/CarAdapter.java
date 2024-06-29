package hungnvvph32403.fpt.assignment.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hungnvvph32403.fpt.assignment.Api.CarsApiService;
import hungnvvph32403.fpt.assignment.Model.CarModel;
import hungnvvph32403.fpt.assignment.R;
import hungnvvph32403.fpt.assignment.SuaSanPham;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarAdapter extends BaseAdapter {
    private Context context;
    private List<CarModel> listCar = new ArrayList<>();
    private Activity activity;

    public CarAdapter(Activity activity, List<CarModel> listCar) {
        this.activity = activity;
        this.listCar = listCar;
    }

    public CarAdapter(Context context, List<CarModel> listCar) {
        this.context = context;
        this.listCar = listCar;
    }

    public void setData(List<CarModel> newData) {
        listCar = newData;
    }

    @Override
    public int getCount() {
        if (listCar == null) {
            return 0;
        }
        return listCar.size();
    }


    @Override
    public Object getItem(int position) {
        return listCar.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.car_list_item, parent, false);

        TextView tvID = rowView.findViewById(R.id.tvIdCar);
        TextView tvName = rowView.findViewById(R.id.tvTenCar);
        TextView tvNam = rowView.findViewById(R.id.tvNamCar);
        TextView tvHang = rowView.findViewById(R.id.tvHangCar);
        TextView tvGia = rowView.findViewById(R.id.tvGiaCar);
        ImageButton btn_delete = rowView.findViewById(R.id.btn_xoa);
        ImageButton btn_edit = rowView.findViewById(R.id.btn_sua);

        String carId = listCar.get(position).get_id();
        String ten = listCar.get(position).getTen();
        int namSX = listCar.get(position).getNamSX();
        String hang = listCar.get(position).getHang();
        int gia = listCar.get(position).getGia();

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SuaSanPham.class);

                intent.putExtra("carId", carId);
                intent.putExtra("ten", ten);
                intent.putExtra("namSX", namSX);
                intent.putExtra("hang", hang);
                intent.putExtra("gia", gia);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.55.101:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                CarsApiService carsApiService = retrofit.create(CarsApiService.class);

                String carId = listCar.get(position).get_id();
                Call<List<CarModel>> call = carsApiService.deleteCar(carId);

                call.enqueue(new Callback<List<CarModel>>() {
                    @Override
                    public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                        if (response.isSuccessful()) {
                            listCar.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Xóa xe thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa xe không ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CarModel>> call, Throwable t) {
                        listCar.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Xóa xe thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        CarModel car = listCar.get(position);
        tvID.setText(String.valueOf(car.get_id()));
        tvName.setText(car.getTen());
        tvNam.setText(String.valueOf(car.getNamSX()));
        tvHang.setText(car.getHang());
        tvGia.setText(String.valueOf(car.getGia()));

        return rowView;
    }
}