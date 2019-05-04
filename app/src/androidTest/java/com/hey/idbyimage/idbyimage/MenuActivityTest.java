package com.hey.idbyimage.idbyimage;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MenuActivityTest {
    @Rule
    public ActivityTestRule<MenuActivity> mActivityTest = new ActivityTestRule<MenuActivity>(MenuActivity.class);

    private MenuActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity=mActivityTest.getActivity();
    }

    @Test
    public void testLayout(){
        Button tryBtn = mActivity.findViewById(R.id.trybtn);
        Button activate = mActivity.findViewById(R.id.activatebtn);
        Button removePer = mActivity.findViewById(R.id.removePerm);
        Button settings = mActivity.findViewById(R.id.settingsbtn);
        assertNotNull(tryBtn);
        assertNotNull(removePer);
        assertNotNull(activate);
        assertNotNull(settings);
    }



    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}