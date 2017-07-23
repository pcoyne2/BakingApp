package com.udacity.coyne.bakingapp;

import android.content.Context;

import java.util.List;

/**
 * Created by Patrick Coyne on 7/22/2017.
 */

public class RecipesSingleton {
    private static RecipesSingleton recipesSingleton;

    private Context context;
    private List<Recipe> recipeList;

    public static RecipesSingleton get(Context context){
        if(recipesSingleton == null){
            recipesSingleton = new RecipesSingleton(context);
        }
        return recipesSingleton;
    }

    public RecipesSingleton(Context context) {
        this.context = context;
    }

    public void addRecipe(Recipe recipe){
        recipeList.add(recipe);
    }

    public Recipe getRecipe(int id){
        for(Recipe recipe : recipeList){
            if(recipe.getId() == id){
                return recipe;
            }
        }
        return null;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }
}
