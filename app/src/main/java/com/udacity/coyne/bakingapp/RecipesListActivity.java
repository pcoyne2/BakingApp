package com.udacity.coyne.bakingapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesListActivity extends AppCompatActivity implements RecipeAdapter.Callbacks{

//    private RecyclerView recyclerView;
//    private ArrayList<Recipe> recipeList;
//    private RecipeAdapter adapter;


//    @Override
//    protected Fragment createFragment() {
//        return new RecipesListFragment();
//    }
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = new RecipesListFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
//                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

//        initViews();
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
//        if(findViewById(R.id.detail_fragment_container) == null){
            Intent intent = DetailActivity.newIntent(this, recipe.getId());
            startActivity(intent);
//        }else{
//            Fragment detail = DetailFragment.newInstance(recipe.getId());
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.detail_fragment_container, detail)
//                    .commit();
//        }
    }

//    private void initViews(){
//        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//        loadJSON();
//    }

//    private void loadJSON(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://go.udacity.com") //http://go.udacity.com/android-baking-app-json
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
//        Call<ArrayList<Recipe>> call = requestInterface.getRecipes();
//        call.enqueue(new Callback<ArrayList<Recipe>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
//                recipeList = response.body();
//                adapter = new RecipeAdapter(recipeList);
//                recyclerView.setAdapter(adapter);
//                RecipesSingleton recipes = RecipesSingleton.get(getApplicationContext());
//                recipes.setRecipeList(recipeList);
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
//
//            }
//        });
//    }
}
