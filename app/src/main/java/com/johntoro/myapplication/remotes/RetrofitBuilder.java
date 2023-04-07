/**

 A class for building Retrofit instances for Google Maps API service calls.
 */
package com.johntoro.myapplication.remotes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/";

    /**
     * Builds a Retrofit instance if one does not exist already and returns it.
     * @return Retrofit instance
     */
    public static Retrofit builder() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}