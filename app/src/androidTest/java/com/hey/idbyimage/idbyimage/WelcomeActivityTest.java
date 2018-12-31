package com.hey.idbyimage.idbyimage;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class WelcomeActivityTest {

    @Rule
    public ActivityTestRule<WelcomeActivity> wActivityTest = new ActivityTestRule<WelcomeActivity>(WelcomeActivity.class);

    private WelcomeActivity wActivity = null;


    @Before
    public void setUp() throws Exception {
        wActivity = wActivityTest.getActivity();
    }

    @Test
    public void testLayout(){
        // TODO: 31-Dec-18
    }

    @Test
    public void testClick(){
        // TODO: 31-Dec-18
    }

    @After
    public void tearDown() throws Exception {
        wActivity=null;
    }
}