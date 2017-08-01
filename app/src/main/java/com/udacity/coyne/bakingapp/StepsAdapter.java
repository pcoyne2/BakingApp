package com.udacity.coyne.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick Coyne on 7/23/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>{
    private List<Steps> stepsList;
    private Context context;
    private Recipe recipe;
    private Callbacks callbacks;

    public StepsAdapter(List<Steps> stepsList, Context context, Recipe recipe) {
        this.stepsList = stepsList;
        this.context = context;
        this.recipe = recipe;
        callbacks = (Callbacks)context;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.shortDescription.setText(stepsList.get(position).getShortDescription());
        holder.bind(stepsList.get(position), recipe);
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public interface Callbacks{
        void onStepSelected(Recipe recipe, Steps step);
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView shortDescription;
        Steps step;
        Recipe recipe;
        public StepsViewHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);
            shortDescription = (TextView)view.findViewById(R.id.short_description);

        }

        public void bind(Steps step, Recipe recipe){
            this.step = step;
            this.recipe = recipe;
        }

        @Override
        public void onClick(View view) {
//            Intent intent = StepsPagerActivity.newIntent(context, recipe.getId(), step.getId());
//            Intent intent = new Intent(context, StepsPagerActivity.class);
//            intent.putExtra("step_id", 0);
//            intent.putExtra("crime_id", recipe.getId());
//            context.startActivity(intent);
            callbacks.onStepSelected(recipe, step);
//            context.startActivity(new Intent());
        }
    }
}
