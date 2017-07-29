package com.udacity.coyne.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

/**
 * Created by Patrick Coyne on 7/23/2017.
 */

public class StepsPagerActivity extends AppCompatActivity implements StepsFragment.Callbacks{

    private ViewPager viewPager;
    private List<Steps> stepsList;
    Fragment fragment;

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
        viewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Steps step = stepsList.get(position);
                fragment = StepsFragment.newInstance(recipeId, step.getId());
                return fragment;
            }


            @Override
            public int getCount() {
                return stepsList.size();
            }
        });

        viewPager.setCurrentItem(stepId);

//        back = (ImageButton)findViewById(R.id.back_button);
//        forward = (ImageButton)findViewById(R.id.forward_button);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int newPage = getItem(-1);
//                if(newPage >= 0) {
//                    viewPager.setCurrentItem(newPage);
//                }
//            }
//        });
//        forward.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int newPage = getItem(1);
//                if(newPage < viewPager.getAdapter().getCount()) {
//                    viewPager.setCurrentItem(newPage);
//                }
//            }
//        });

    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    public static Intent newIntent(Context context, int crimeId, int stepId){
        Intent intent = new Intent(context, StepsPagerActivity.class);
        intent.putExtra("recipe_id", crimeId);
        intent.putExtra("step_id", stepId);
        return intent;
    }

    @Override
    public void onNextButtonClicked() {
        int newPage = getItem(1);
        if(newPage < viewPager.getAdapter().getCount()) {
            viewPager.setCurrentItem(newPage);
        }
    }

    @Override
    public void onPrevButtonClicked() {
        int newPage = getItem(-1);
        if(newPage >= 0) {
            viewPager.setCurrentItem(newPage);
        }
    }
}
