package com.hey.idbyimage.idbyimage;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class InstructionActivityTest {

    @Rule
    public ActivityTestRule<InstructionActivity> iActivityTest = new ActivityTestRule<InstructionActivity>(InstructionActivity.class);

    private InstructionActivity iActivity = null;

    @Before
    public void setUp() throws Exception {
        iActivity=iActivityTest.getActivity();
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
        iActivity = null;
    }
}