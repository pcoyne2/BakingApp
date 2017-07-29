package com.udacity.coyne.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipesListActivityTest2 {

    @Rule
    public ActivityTestRule<RecipesListActivity> mActivityTestRule = new ActivityTestRule<>(RecipesListActivity.class);

    @Test
    public void recipesListActivityTest2() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.card_recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.ingredients_text_view), withText("2.0CUP Graham Cracker crumbs\n6.0TBLSP unsalted butter, melted\n0.5CUP granulated sugar\n1.5TSP salt\n5.0TBLSP vanilla\n1.0K Nutella or other chocolate-hazelnut spread\n500.0G Mascapone Cheese(room temperature)\n1.0CUP heavy cream(cold)\n4.0OZ cream cheese(softened)\n"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("2.0CUP Graham Cracker crumbs\n6.0TBLSP unsalted butter, melted\n0.5CUP granulated sugar\n1.5TSP salt\n5.0TBLSP vanilla\n1.0K Nutella or other chocolate-hazelnut spread\n500.0G Mascapone Cheese(room temperature)\n1.0CUP heavy cream(cold)\n4.0OZ cream cheese(softened)\n")));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.steps_recycler_view), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.card_recycler_view), isDisplayed()));
        recyclerView3.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.ingredients_text_view), withText("350.0G Bittersweet chocolate (60-70% cacao)\n226.0G unsalted butter\n300.0G granulated sugar\n100.0G light brown sugar\n5.0UNIT large eggs\n1.0TBLSP vanilla extract\n140.0G all purpose flour\n40.0G cocoa powder\n1.5TSP salt\n350.0G semisweet chocolate chips\n"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("350.0G Bittersweet chocolate (60-70% cacao)\n226.0G unsalted butter\n300.0G granulated sugar\n100.0G light brown sugar\n5.0UNIT large eggs\n1.0TBLSP vanilla extract\n140.0G all purpose flour\n40.0G cocoa powder\n1.5TSP salt\n350.0G semisweet chocolate chips\n")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
