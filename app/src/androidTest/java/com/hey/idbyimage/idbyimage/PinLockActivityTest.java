package com.hey.idbyimage.idbyimage;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class PinLockActivityTest {
    @Rule
    public ActivityTestRule<PinLockActivity> pActivityTest = new ActivityTestRule<PinLockActivity>(PinLockActivity.class);

    private PinLockActivity pActivity = null;

    private TextView[] pin=null;
    private Button next,back;
    @Before
    public void setUp() throws Exception {
        pActivity=pActivityTest.getActivity();
        pin=new TextView[4];
        pin[0]=pActivity.findViewById(R.id.first_pin);
        pin[1]=pActivity.findViewById(R.id.second_pin);
        pin[2]=pActivity.findViewById(R.id.third_pin);
        pin[3]=pActivity.findViewById(R.id.fourth_pin);
        next=pActivity.findViewById(R.id.nextBtn);
        back=pActivity.findViewById(R.id.backBtn);
    }

    @Test
    public void testLayout(){
        assertNotNull(next);
        assertNotNull(back);
        assertNotNull(pin[0]);
        assertNotNull(pin[1]);
        assertNotNull(pin[2]);
        assertNotNull(pin[3]);
    }

    @Test
    public void testBadPin(){
        //Check not full password
        try {
            pActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<3;i++)
                        pin[i].setText(""+(i+5));
                    pActivityTest.getActivity().checkPin();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertNull(pActivity.getPin());
        assertTrue(pActivity.isFirstScreen());

        //Check not the same password
        try {
            pActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<4;i++)
                        pin[i].setText(""+(i+5));
                    pActivityTest.getActivity().onClick(next);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertFalse(pActivity.isFirstScreen());

        try {
            pActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<4;i++)
                        pin[i].setText(""+(i+4));
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertEquals(pActivity.getDefined(),"5678");
        assertFalse(pActivity.validatePin());
    }

    @Test
    public void testGoodPin(){
        //Check not the same password
        try {
            pActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<4;i++)
                        pin[i].setText(""+(i+5));
                    pActivityTest.getActivity().onClick(next);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertFalse(pActivity.isFirstScreen());

        try {
            pActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<4;i++)
                        pin[i].setText(""+(i+5));
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertEquals(pActivity.getDefined(),"5678");
        assertTrue(pActivity.validatePin());
    }


    @After
    public void tearDown() throws Exception {
        pActivity=null;
    }
}