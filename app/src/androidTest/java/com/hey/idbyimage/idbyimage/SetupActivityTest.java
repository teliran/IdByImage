package com.hey.idbyimage.idbyimage;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetupActivityTest {
    @Rule
    public ActivityTestRule<SetupActivity> sActivityTest = new ActivityTestRule<SetupActivity>(SetupActivity.class);

    private SetupActivity sActivity = null;


    @Before
    public void setUp() throws Exception {
        sActivity = sActivityTest.getActivity();
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
        sActivity = null;
    }
}