package com.udacity.coyne.bakingapp;
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
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements StepsAdapter.Callbacks, StepsFragment.Callbacks{

    private Recipe recipe;
    private ViewPager viewPager;
    private List<Steps> stepsList;
//    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);

        final int recipeId = getIntent().getIntExtra("recipe_id", 1);

        recipe = RecipesSingleton.get(this).getRecipe(recipeId);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = DetailFragment.newInstance(recipeId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        if(findViewById(R.id.steps_view_pager) != null) {
            stepsList = RecipesSingleton.get(this).getRecipe(recipeId).getSteps();

            viewPager = (ViewPager) findViewById(R.id.steps_view_pager);
            viewPager.setAdapter(new FragmentPagerAdapter(fm) {
                @Override
                public Fragment getItem(int position) {
                    Steps step = stepsList.get(position);
                    Fragment fragment = StepsFragment.newInstance(recipeId, step.getId());
                    return fragment;
                }


                @Override
                public int getCount() {
                    return stepsList.size();
                }
            });

            viewPager.setCurrentItem(stepsList.get(0).getId());
        }
    }



    public static Intent newIntent(Context context, int recipeId){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("recipe_id", recipeId);
        return intent;
    }

    @Override
    public void onStepSelected(Recipe recipe, Steps step) {
        if(findViewById(R.id.steps_view_pager) == null){
            Intent intent = StepsPagerActivity.newIntent(this, recipe.getId(), step.getId());
//            Intent intent = new Intent(context, StepsPagerActivity.class);
//            intent.putExtra("step_id", 0);
//            intent.putExtra("crime_id", recipe.getId());
            startActivity(intent);
        }else{
            viewPager.setCurrentItem(step.getId());
        }

    }
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    @Override
    public void onNextButtonClicked() {
        if(findViewById(R.id.steps_view_pager) != null) {
            int newPage = getItem(1);
            if (newPage < viewPager.getAdapter().getCount()) {
                viewPager.setCurrentItem(newPage);
            }
        }
    }

    @Override
    public void onPrevButtonClicked() {
        if(findViewById(R.id.steps_view_pager) != null) {
            int newPage = getItem(-1);
            if (newPage >= 0) {
                viewPager.setCurrentItem(newPage);
            }
        }
    }
}
