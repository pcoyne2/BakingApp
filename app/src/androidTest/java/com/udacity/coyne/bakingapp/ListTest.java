package com.udacity.coyne.bakingapp;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by Patrick Coyne on 7/26/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ListTest {

    @Rule
    public ActivityTestRule<RecipesListActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipesListActivity.class);

    @Test
    public void clickItem_OpensActivity(){
        onData(anything()).inAdapterView(withId(R.id.card_recycler_view)).atPosition(1).perform(click());

        onView(withId(R.id.ingredients_text_view)).check(matches(withText("hello")));
    }
}
