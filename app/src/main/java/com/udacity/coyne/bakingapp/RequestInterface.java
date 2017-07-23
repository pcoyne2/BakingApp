package com.udacity.coyne.bakingapp;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("/android-baking-app-json")
    Call<ArrayList<Recipe>> getRecipes();
}
