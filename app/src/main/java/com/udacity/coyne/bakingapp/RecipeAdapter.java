package com.udacity.coyne.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Patrick Coyne on 7/22/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    private ArrayList<Recipe> mRecipes;
    private Context context;
    private Callbacks mCallback;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        mRecipes = recipes;
        mCallback = (Callbacks) context;
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
        if(!mRecipes.get(position).getImage().isEmpty()){
            Picasso.with(context).load(mRecipes.get(position).getImage()).into(holder.recipe_image);
        }else{
//            holder.recipe_image.setImageResource(R.drawable.example_appwidget_preview);
            holder.recipe_image.setVisibility(View.GONE);
        }
        holder.bind(mRecipes.get(position));
    }

    public interface Callbacks{
        void onRecipeSelected(Recipe recipe);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView recipe_name, serving_size, ingredients;
        private ImageView recipe_image;
        private Recipe recipe;
        public ViewHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);
            recipe_name = (TextView)view.findViewById(R.id.recipe_name);
            ingredients = (TextView)view.findViewById(R.id.ingredients);
            serving_size = (TextView)view.findViewById(R.id.servings);
            recipe_image = (ImageView)view.findViewById(R.id.recipe_image);

        }

        public void bind(Recipe recipe1){
            recipe = recipe1;
        }

        @Override
        public void onClick(View view) {
//            Intent intent = DetailActivity.newIntent(context, recipe.getId());
//            context.startActivity(intent);
            mCallback.onRecipeSelected(recipe);
//            context.startActivity(new Intent());
        }
    }
}
