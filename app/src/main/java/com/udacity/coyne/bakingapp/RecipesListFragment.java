package com.udacity.coyne.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Patrick Coyne on 7/22/2017.
 */

public class RecipesListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Recipe> recipeList;
    private RecipeAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_recipes_list, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.recipe_list_columns)));

        loadJSON();

        return view;
    }


    private void loadJSON(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://go.udacity.com") //http://go.udacity.com/android-baking-app-json
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ArrayList<Recipe>> call = requestInterface.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                recipeList = response.body();
                updateList(recipeList);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Toast.makeText(getActivity(),"Unable to get information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateList(ArrayList<Recipe> recipeList){
        adapter = new RecipeAdapter(getActivity(), recipeList);
        recyclerView.setAdapter(adapter);
        RecipesSingleton recipes = RecipesSingleton.get(getActivity().getApplicationContext());
        recipes.setRecipeList(recipeList);
    }

}
