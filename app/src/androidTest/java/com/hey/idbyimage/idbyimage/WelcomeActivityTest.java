package com.hey.idbyimage.idbyimage;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        Button btn = wActivity.findViewById(R.id.nextbtn);
        TextView header = wActivity.findViewById(R.id.titleText);
        TextView body = wActivity.findViewById(R.id.bodyText);

        assertNotNull(btn);
        assertNotNull(header);
        assertNotNull(body);
    }

    @Test
    public void testClick(){
        // TODO: 31-Dec-18 - Does not work

    }

    @After
    public void tearDown() throws Exception {
        wActivity=null;
    }
}