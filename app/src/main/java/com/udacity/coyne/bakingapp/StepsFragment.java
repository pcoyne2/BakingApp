package com.udacity.coyne.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by Patrick Coyne on 7/23/2017.
 */

public class StepsFragment extends Fragment {
    private static final String ARG_STEPS_ID = "step_id";
    private static final String ARG_RECIPE_ID = "recipe_id";

    private Steps step;
    private Recipe recipe;

    private TextView textView;

    public static StepsFragment newInstance(int recipesId, int stepsId){
        Bundle args = new Bundle();
//        args.putSerializable(ARG_RECIPE_ID, recipesId);
//        args.putSerializable(ARG_STEPS_ID, stepsId);
        args.putInt(ARG_STEPS_ID, stepsId);
        args.putInt(ARG_RECIPE_ID, recipesId);

        StepsFragment fragment = new StepsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int recipeId = getArguments().getInt(ARG_RECIPE_ID);
        int stepId = getArguments().getInt(ARG_STEPS_ID);

        recipe = RecipesSingleton.get(getActivity()).getRecipe(recipeId);
        step = recipe.getSteps().get(stepId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_fragment, container, false);
        textView = view.findViewById(R.id.step_text);
        textView.setText(step.getDescription()+" "+step.getThumbnailURL() + " "+step.getVideoURL());

        return view;

    }
}
