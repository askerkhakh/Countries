package com.example.countries;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface RestCountriesService {

    @GET("all")
    Call<List<RestCountry>> listCountries();

}
