package com.udacity.coyne.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by Patrick Coyne on 7/23/2017.
 */

public class StepsPagerActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private List<Steps> stepsList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.step_fragment);
        setContentView(R.layout.activity_steps_pager);

        final int recipeId = getIntent().getIntExtra("recipe_id", 0);
        int stepId = getIntent().getIntExtra("step_id", 0);

        stepsList = RecipesSingleton.get(this).getRecipe(recipeId).getSteps();

        viewPager = (ViewPager)findViewById(R.id.steps_view_pager);
        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Steps step = stepsList.get(position);
                return StepsFragment.newInstance(recipeId, step.getId());
            }

            @Override
            public int getCount() {
                return stepsList.size();
            }
        });

        viewPager.setCurrentItem(stepId);
    }

    public static Intent newIntent(Context context, int crimeId, int stepId){
        Intent intent = new Intent(context, StepsPagerActivity.class);
        intent.putExtra("recipe_id", crimeId);
        intent.putExtra("step_id", stepId);
        return intent;
    }
}
