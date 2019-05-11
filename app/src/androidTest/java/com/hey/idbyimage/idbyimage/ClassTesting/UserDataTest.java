package com.hey.idbyimage.idbyimage.ClassTesting;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hey.idbyimage.idbyimage.R;
import com.hey.idbyimage.idbyimage.UserData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDataTest {

    @Rule
    public ActivityTestRule<UserData> iActivityTest = new ActivityTestRule<UserData>(UserData.class);

    private UserData userDataActivity = null;
    private Button back;
    private Button next;
    private TextView ageLable;
    private TextView genderLable;
    private EditText ageInput;
    private RadioGroup radioGroup;
    private RadioButton male;
    private RadioButton female;

    @Before
    public void setUp() throws Exception {
        userDataActivity = iActivityTest.getActivity();
        back = userDataActivity.findViewById(R.id.backBtn);
        next = userDataActivity.findViewById(R.id.nextBtn);
        ageLable = userDataActivity.findViewById(R.id.ageLable);
        genderLable = userDataActivity.findViewById(R.id.gender);
        ageInput = userDataActivity.findViewById(R.id.ageInput);
        radioGroup = userDataActivity.findViewById(R.id.radioGroup);
        male = userDataActivity.findViewById(R.id.male);
        female = userDataActivity.findViewById(R.id.female);

    }

    @Test
    public void testLayout(){
        assertNotNull(back);
        assertNotNull(next);
        assertNotNull(ageLable);
        assertNotNull(genderLable);
        assertNotNull(ageInput);
        assertNotNull(radioGroup);
        assertNotNull(male);
        assertNotNull(female);
    }

    @Test
    public void testContent(){
        assertEquals(back.getText(),"Back");
        assertEquals(next.getText(),"Next");
        assertEquals(ageLable.getText(),"Enter Age:");
        assertEquals(genderLable.getText(),"Select Gender:");
        assertTrue(male.isChecked() || female.isChecked());
        assertEquals(male.getText(),"Male");
        assertEquals(female.getText(),"Female");
    }


    @After
    public void tearDown() throws Exception {
    }
}