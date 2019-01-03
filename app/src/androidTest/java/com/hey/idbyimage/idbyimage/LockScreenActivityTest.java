package com.hey.idbyimage.idbyimage;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.locks.Lock;

import static org.junit.Assert.*;

public class LockScreenActivityTest {
    @Rule
    public ActivityTestRule<LockScreenActivity> lActivityTest = new ActivityTestRule<LockScreenActivity>(LockScreenActivity.class);

    private LockScreenActivity lActivity = null;


    @Before
    public void setUp() throws Exception {
        lActivity=lActivityTest.getActivity();
    }

    @Test
    public void testLayout(){
        TextView title = lActivity.findViewById(R.id.titleText);
        Button submit = lActivity.findViewById(R.id.submit);
        //Check images presented



        assertNotNull(title);
        assertNotNull(submit);
    }

    @Test
    public void testSubmit(){

    }


    @After
    public void tearDown() throws Exception {
        lActivity=null;
    }

}