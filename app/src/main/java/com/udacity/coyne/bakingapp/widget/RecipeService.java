package com.udacity.coyne.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.udacity.coyne.bakingapp.Ingredients;
import com.udacity.coyne.bakingapp.RecipesSingleton;

import java.util.List;

/**
 * Created by Patrick Coyne on 7/26/2017.
 */

public class RecipeService extends IntentService {

    public RecipeService() {
        super("name");
    }

    public static void startActionUpdateWidget(Context context){
        Intent intent = new Intent(context, RecipeService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            updateRecipeWidget();
        }
    }

    private void updateRecipeWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        List<Ingredients> ingredientsArrayList = RecipesSingleton.get(this).getRecipeList().get(0).getIngredients();
        String ingredientsText = "";
        for(int i=0; i<ingredientsArrayList.size();i++){
            Ingredients ingredient = ingredientsArrayList.get(i);
            ingredientsText = ingredientsText + ingredient.getQuantity() + "" +
                    ingredient.getMeasure() + " "+ ingredient.getIngredient()+ "\n";
        }
        RecipeWidgetProvider.updateRecipeWidget(this, appWidgetManager, ingredientsText, appWidgetIds);
    }
}
