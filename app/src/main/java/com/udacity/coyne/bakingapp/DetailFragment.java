package com.udacity.coyne.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.coyne.bakingapp.widget.RecipeService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick Coyne on 7/23/2017.
 */


/*
Copyright (c) 2016 Amanda Hill and thoughtbot, inc.

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/
public class DetailFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";

    private TextView ingredients;
    private RecyclerView stepsRv;
    private StepsAdapter stepsAdapter;
    private Recipe recipe;

    public static DetailFragment newInstance(int recipesId){
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipesId);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {//Saveinstance for when backing out of steps pager
        super.onCreate(savedInstanceState);
        int recipeId = getArguments().getInt(ARG_RECIPE_ID, 1);
                //.getInt(ARG_RECIPE_ID);

        recipe = RecipesSingleton.get(getActivity()).getRecipe(recipeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail, container, false);

        ingredients = (TextView)view.findViewById(R.id.ingredients_text_view);

        List<Ingredients> ingredientsArrayList = recipe.getIngredients();
        String ingredientsText = "";
        for(int i=0; i<ingredientsArrayList.size();i++){
            Ingredients ingredient = ingredientsArrayList.get(i);
            ingredientsText = ingredientsText + ingredient.getQuantity() + "" +
                    ingredient.getMeasure() + " "+ ingredient.getIngredient()+ "\n";
        }
        ingredients.setText(ingredientsText);
        RecipeService.startActionUpdateWidget(getActivity(), recipe.getId());

        stepsRv = (RecyclerView)view.findViewById(R.id.steps_recycler_view);
        stepsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepsAdapter = new StepsAdapter(recipe.getSteps(), getActivity(), recipe);
        stepsRv.setAdapter(stepsAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_RECIPE_ID, recipe.getId());
    }

}

