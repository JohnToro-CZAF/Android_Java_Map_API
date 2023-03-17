package com.johntoro.myapplication.remotes;
import com.johntoro.myapplication.models.NearByResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GoogleApiService {
    @GET
    Call<NearByResponse> getNearByPlaces(@Url String url);

    @GET
    Call<NearByResponse> getMyNearByPlaces(@Url String url);
}
