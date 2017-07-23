package com.udacity.coyne.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Patrick Coyne on 7/22/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    private ArrayList<Recipe> mRecipes;
    private Context context;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        mRecipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.recipe_name.setText(mRecipes.get(position).getName());
        holder.ingredients.setText("Ingredients: "+mRecipes.get(position).getIngredients().size());
        holder.serving_size.setText("Servings: "+mRecipes.get(position).getServings());
        holder.bind(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView recipe_name, serving_size, ingredients;
        private Recipe recipe;
        public ViewHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);
            recipe_name = (TextView)view.findViewById(R.id.recipe_name);
            ingredients = (TextView)view.findViewById(R.id.ingredients);
            serving_size = (TextView)view.findViewById(R.id.servings);

        }

        public void bind(Recipe recipe1){
            recipe = recipe1;
        }

        @Override
        public void onClick(View view) {
            Intent intent = StepsPagerActivity.newIntent(context, recipe.getId(), 0);
//            Intent intent = new Intent(context, StepsPagerActivity.class);
//            intent.putExtra("step_id", 0);
//            intent.putExtra("crime_id", recipe.getId());
            context.startActivity(intent);
//            context.startActivity(new Intent());
        }
    }
}
