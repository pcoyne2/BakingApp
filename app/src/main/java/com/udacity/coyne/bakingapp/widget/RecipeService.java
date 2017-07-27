package com.udacity.coyne.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.udacity.coyne.bakingapp.Ingredients;
import com.udacity.coyne.bakingapp.Recipe;
import com.udacity.coyne.bakingapp.RecipesSingleton;

import java.util.List;

/**
 * Created by Patrick Coyne on 7/26/2017.
 */

public class RecipeService extends IntentService {

    public static final String RECIPE_ID = "recipe_id";

    public RecipeService() {
        super("name");
    }

    public static void startActionUpdateWidget(Context context, int recipeId){
        Intent intent = new Intent(context, RecipeService.class);
        intent.putExtra(RECIPE_ID, recipeId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            int id = intent.getIntExtra(RECIPE_ID, 0);
            updateRecipeWidget(id);
        }
    }

    private void updateRecipeWidget(int id) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        List<Recipe> recipeList = RecipesSingleton.get(this).getRecipeList();
        List<Ingredients> ingredientsArrayList = recipeList.get(id-1).getIngredients();//Id starts at 1 list starts 0
        // End up with index out of bounds and wrong recipe
        String ingredientsText = "";
        for(int i=0; i<ingredientsArrayList.size();i++){
            Ingredients ingredient = ingredientsArrayList.get(i);
            ingredientsText = ingredientsText + ingredient.getQuantity() + "" +
                    ingredient.getMeasure() + " "+ ingredient.getIngredient()+ "\n";
        }
        RecipeWidgetProvider.updateRecipeWidget(this, appWidgetManager, ingredientsText, appWidgetIds);
    }
}
