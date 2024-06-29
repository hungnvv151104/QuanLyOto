package hungnvvph32403.fpt.assignment.Api;

import java.util.List;

import hungnvvph32403.fpt.assignment.Model.CarModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CarsApiService {
    @GET("/")
    Call<List<CarModel>> getCars();
    @POST("/cars")
    Call<List<CarModel>> addCar(@Body CarModel car);
    @DELETE("cars/{carId}")
    Call<List<CarModel>> deleteCar(@Path("carId") String carId);
    @PUT("cars/{carId}")
    Call<CarModel> updateCar(@Path("carId") String carId, @Body CarModel car);
}
