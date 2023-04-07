/**

 Interface for Google Places API.
 Defines methods for getting nearby places and user's nearby places.
 */
package com.johntoro.myapplication.remotes;
import com.johntoro.myapplication.models.NearByResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GoogleApiService {
    /**
     * Sends a GET request to the Google Places API and returns a Call object for the response.
     *
     * @param url - The URL for the request.
     * @return A Call object for the response containing a NearByResponse object.
     */
    @GET
    Call<NearByResponse> getNearByPlaces(@Url String url);

    /**
     * Sends a GET request to the Google Places API for user's nearby places and returns a Call object for the response.
     *
     * @param url - The URL for the request.
     * @return A Call object for the response containing a NearByResponse object.
     */
    @GET
    Call<NearByResponse> getMyNearByPlaces(@Url String url);

}
