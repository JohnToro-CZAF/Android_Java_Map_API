package com.johntoro.myapplication.remotes;
import com.johntoro.myapplication.models.MyPlace;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GoogleApiService {
    @GET
    Call<MyPlace> getNearByPlaces(@Url String url);

    @GET
    Call<MyPlace> getMyNearByPlaces(@Url String url);
}
