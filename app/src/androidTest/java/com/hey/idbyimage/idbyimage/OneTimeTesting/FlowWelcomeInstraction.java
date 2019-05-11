package com.hey.idbyimage.idbyimage.OneTimeTesting;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.hey.idbyimage.idbyimage.R;
import com.hey.idbyimage.idbyimage.WelcomeActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FlowWelcomeInstraction {

    @Rule
    public ActivityTestRule<WelcomeActivity> mActivityTestRule = new ActivityTestRule<>(WelcomeActivity.class);

    @Test
    public void flowWelcomeInstraction() {
        ViewInteraction appCompatButton = onView(
                allOf(ViewMatchers.withId(R.id.nextbtn), withText("Next"),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.backbtn),
                        withText("Back"),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.instructionText), withText("You are about to rate a set of images. \nIn order to make the identification as accurate as possible we encourage you to rate the images as versatile and accurate as possible. \nOnce you will finish the initial rating you will be asked to choose the most beautiful images from a set of images. \nIf you will choose your top rated images, you will be granted the access to your device. \nNote! there is a safety mechanism to prevent you from getting locked out of your device."),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.startbtn),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));
    }

}
