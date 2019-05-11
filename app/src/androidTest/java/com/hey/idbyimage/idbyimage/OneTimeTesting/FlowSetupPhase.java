package com.hey.idbyimage.idbyimage.OneTimeTesting;

// This Test runs only on a device that did not filled the user data yet

        import android.support.test.espresso.UiController;
        import android.support.test.espresso.ViewAction;
        import android.support.test.espresso.ViewInteraction;
        import android.support.test.espresso.action.ReplaceTextAction;
        import android.support.test.espresso.matcher.ViewMatchers;
        import android.support.test.filters.LargeTest;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;
        import android.view.View;
        import android.widget.SeekBar;

        import com.hey.idbyimage.idbyimage.InstructionActivity;
        import com.hey.idbyimage.idbyimage.R;
        import com.hey.idbyimage.idbyimage.ShuffleAlgorithm;
        import com.hey.idbyimage.idbyimage.Utils.BadRatingDistributionException;

        import org.hamcrest.Matcher;
        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import java.util.HashMap;

        import static android.support.test.espresso.Espresso.onView;
        import static android.support.test.espresso.action.ViewActions.click;
        import static android.support.test.espresso.action.ViewActions.replaceText;
        import static android.support.test.espresso.assertion.ViewAssertions.matches;
        import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
        import static android.support.test.espresso.matcher.ViewMatchers.withId;
        import static android.support.test.espresso.matcher.ViewMatchers.withText;
        import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FlowSetupPhase {

    @Rule
    public ActivityTestRule<InstructionActivity> mActivityTestRule = new ActivityTestRule<>(InstructionActivity.class);

    @Test
    public void FlowSetupPhase() {
        TestInstractionToUserDate();
        TestUserDataToPinLock();
        TestPinLockToSetup();

    }

    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    private void TestPinLockToSetup() {
        ValidateSetupLayout();
        HashMap<String,Integer> ratings = GenerateRatings(50);
        for(int i=0;i<25;i++){
            ViewInteraction rating1 = onView(
                    allOf(ViewMatchers.withId(R.id.rating1),
                            isDisplayed()));
            ViewInteraction rating2 = onView(
                    allOf(withId(R.id.rating2),
                            isDisplayed()));
            rating1.perform(setProgress(ratings.get("p"+(i+1))));
            rating2.perform(setProgress(ratings.get("p"+(i+2))));
            ViewInteraction nextbtn = onView(
                    allOf(withId(R.id.nextbtn),
                            isDisplayed()));
            nextbtn.perform(click());
        }
        try{
            ShuffleAlgorithm algo = new ShuffleAlgorithm(ratings);
            algo.shuffle(4,5);
            ViewInteraction trybtn = onView(
                    allOf(withId(R.id.trybtn),
                            isDisplayed()));
            ViewInteraction activatebtn = onView(
                    allOf(withId(R.id.activatebtn),
                            isDisplayed()));
            ViewInteraction removePerm = onView(
                    allOf(withId(R.id.removePerm),
                            isDisplayed()));
            ViewInteraction settingsbtn = onView(
                    allOf(withId(R.id.settingsbtn),
                            isDisplayed()));
            trybtn.check(matches(isDisplayed()));
            activatebtn.check(matches(isDisplayed()));
            removePerm.check(matches(isDisplayed()));
            settingsbtn.check(matches(isDisplayed()));

        }catch (BadRatingDistributionException e){
            ValidateSetupLayout();
        }




    }

    private HashMap<String,Integer> GenerateRatings(int num) {
        HashMap<String,Integer> toReturn = new HashMap<String, Integer>();
        for(int i=0;i<num;i++){
            toReturn.put("p"+(i+1),(int)(Math.random()*10));
        }
        return toReturn;
    }

    private void ValidateSetupLayout() {
        ViewInteraction helpBtn = onView(
                allOf(withId(R.id.helpBtn),
                        isDisplayed()));
        ViewInteraction imagePlace1 = onView(
                allOf(withId(R.id.imagePlace1),
                        isDisplayed()));
        ViewInteraction imagePlace2 = onView(
                allOf(withId(R.id.imagePlace2),
                        isDisplayed()));
        ViewInteraction rating1 = onView(
                allOf(withId(R.id.rating1),
                        isDisplayed()));
        ViewInteraction rating2 = onView(
                allOf(withId(R.id.rating2),
                        isDisplayed()));
        ViewInteraction progressIndicator = onView(
                allOf(withId(R.id.progressIndicator),
                        isDisplayed()));
        ViewInteraction nextbtn = onView(
                allOf(withId(R.id.nextbtn),
                        withText("Next"),
                        isDisplayed()));
        ViewInteraction backbtn = onView(
                allOf(withId(R.id.backbtn),
                        withText("Back"),
                        isDisplayed()));
        ViewInteraction rating1progress = onView(
                allOf(withId(R.id.rating1progress),
                        isDisplayed()));
        ViewInteraction rating2progress = onView(
                allOf(withId(R.id.rating2progress),
                        isDisplayed()));
        rating1progress.check(matches(isDisplayed()));
        rating2progress.check(matches(isDisplayed()));
        backbtn.check(matches(isDisplayed()));
        nextbtn.check(matches(isDisplayed()));
        progressIndicator.check(matches(isDisplayed()));
        rating1.check(matches(isDisplayed()));
        rating2.check(matches(isDisplayed()));
        imagePlace1.check(matches(isDisplayed()));
        imagePlace2.check(matches(isDisplayed()));
        helpBtn.check(matches(isDisplayed()));

    }

    private void TestUserDataToPinLock() {
        ViewInteraction textPin = onView(
                allOf(withId(R.id.textPin),
                        withText("Please set your PIN code and click \"Next\""),
                        isDisplayed()));
        ViewInteraction titlePin = onView(
                allOf(withId(R.id.titlePin), withText("PIN"),
                        isDisplayed()));
        ViewInteraction pin_layout = onView(
                allOf(withId(R.id.pin_layout),
                        isDisplayed()));
        ViewInteraction first_pin = onView(
                allOf(withId(R.id.first_pin),
                        isDisplayed()));
        ViewInteraction second_pin = onView(
                allOf(withId(R.id.second_pin),
                        isDisplayed()));
        ViewInteraction third_pin = onView(
                allOf(withId(R.id.pin_layout),
                        isDisplayed()));
        ViewInteraction fourth_pin = onView(
                allOf(withId(R.id.pin_layout),
                        isDisplayed()));
        ViewInteraction submitBtn = onView(
                allOf(withId(R.id.submitBtn),
                        isDisplayed()));
        ViewInteraction backBtn = onView(
                allOf(withId(R.id.backBtn),
                        isDisplayed()));

        ValidatePinLayout(textPin, titlePin, pin_layout, first_pin, second_pin, third_pin, fourth_pin, submitBtn, backBtn);

        CheckIncorrectPassword();
        CheckCorrectPassword();

    }



    private void ValidatePinLayout(ViewInteraction textPin, ViewInteraction titlePin, ViewInteraction pin_layout, ViewInteraction first_pin, ViewInteraction second_pin, ViewInteraction third_pin, ViewInteraction fourth_pin, ViewInteraction submitBtn, ViewInteraction backBtn) {
        textPin.check(matches(isDisplayed()));

        titlePin.check(matches(isDisplayed()));

        pin_layout.check(matches(isDisplayed()));

        first_pin.check(matches(isDisplayed()));

        second_pin.check(matches(isDisplayed()));

        third_pin.check(matches(isDisplayed()));

        fourth_pin.check(matches(isDisplayed()));

        submitBtn.check(matches(isDisplayed()));

        backBtn.check(matches(isDisplayed()));
    }


    private void CheckIncorrectPassword() {
        ViewInteraction submitBtn = onView(
                allOf(withId(R.id.submitBtn),
                        isDisplayed()));
        ViewInteraction first_pin = onView(
                allOf(withId(R.id.first_pin),
                        isDisplayed()));
        first_pin.perform(replaceText("1"));
        ViewInteraction second_pin = onView(
                allOf(withId(R.id.second_pin),
                        isDisplayed()));
        second_pin.perform(replaceText("2"));
        ViewInteraction third_pin = onView(
                allOf(withId(R.id.third_pin),
                        isDisplayed()));
        third_pin.perform(replaceText("3"));
        ViewInteraction fourth_pin = onView(
                allOf(withId(R.id.fourth_pin),
                        isDisplayed()));
        fourth_pin.perform(replaceText("4"));
        submitBtn.perform(click());
        first_pin = onView(
                allOf(withId(R.id.first_pin),
                        isDisplayed()));
        first_pin.perform(replaceText("2"));
        second_pin = onView(
                allOf(withId(R.id.second_pin),
                        isDisplayed()));
        second_pin.perform(replaceText("2"));
        third_pin = onView(
                allOf(withId(R.id.third_pin),
                        isDisplayed()));
        third_pin.perform(replaceText("3"));
        fourth_pin = onView(
                allOf(withId(R.id.fourth_pin),
                        isDisplayed()));
        fourth_pin.perform(replaceText("4"));
        submitBtn = onView(
                allOf(withId(R.id.submitBtn),
                        isDisplayed()));
        ViewInteraction backBtn = onView(
                allOf(withId(R.id.backBtn),
                        isDisplayed()));


        submitBtn.perform(click());
        backBtn.perform(click());
    }

    private void CheckCorrectPassword() {
        ViewInteraction submitBtn = onView(
                allOf(withId(R.id.submitBtn),
                        isDisplayed()));
        ViewInteraction first_pin = onView(
                allOf(withId(R.id.first_pin),
                        isDisplayed()));
        first_pin.perform(replaceText("1"));
        ViewInteraction second_pin = onView(
                allOf(withId(R.id.second_pin),
                        isDisplayed()));
        second_pin.perform(replaceText("2"));
        ViewInteraction third_pin = onView(
                allOf(withId(R.id.third_pin),
                        isDisplayed()));
        third_pin.perform(replaceText("3"));
        ViewInteraction fourth_pin = onView(
                allOf(withId(R.id.fourth_pin),
                        isDisplayed()));
        fourth_pin.perform(replaceText("4"));
        submitBtn.perform(click());
        first_pin = onView(
                allOf(withId(R.id.first_pin),
                        isDisplayed()));
        first_pin.perform(replaceText("1"));
        second_pin = onView(
                allOf(withId(R.id.second_pin),
                        isDisplayed()));
        second_pin.perform(replaceText("2"));
        third_pin = onView(
                allOf(withId(R.id.third_pin),
                        isDisplayed()));
        third_pin.perform(replaceText("3"));
        fourth_pin = onView(
                allOf(withId(R.id.fourth_pin),
                        isDisplayed()));
        fourth_pin.perform(replaceText("4"));
        submitBtn = onView(
                allOf(withId(R.id.submitBtn),
                        isDisplayed()));


        submitBtn.perform(click());
    }

    private void TestInstractionToUserDate() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.startbtn), withText("Start"),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction ageLabel = onView(
                allOf(withId(R.id.ageLable),
                        withText("Enter Age:"),
                        isDisplayed()));
        ageLabel.check(matches(isDisplayed()));

        ViewInteraction genderLabel = onView(
                allOf(withId(R.id.gender), withText("Select Gender:"),
                        isDisplayed()));
        genderLabel.check(matches(isDisplayed()));

        ViewInteraction radioGroup = onView(
                allOf(withId(R.id.radioGroup),
                        isDisplayed()));
        radioGroup.check(matches(isDisplayed()));

        ViewInteraction male = onView(
                allOf(withId(R.id.male),
                        withText("Male"),
                        isDisplayed()));
        male.check(matches(isDisplayed()));

        ViewInteraction female = onView(
                allOf(withId(R.id.female),
                        withText("Female"),
                        isDisplayed()));
        female.check(matches(isDisplayed()));

        ViewInteraction ageInput = onView(
                allOf(withId(R.id.ageInput),
                        isDisplayed()));
        ageInput.check(matches(isDisplayed()));

        int numGen = (int)(Math.random()* 95)+5;

        ageInput.perform(replaceText(""+numGen));

        ViewInteraction nextBtn = onView(
                allOf(withId(R.id.nextBtn), withText("Next"),
                        isDisplayed()));
        nextBtn.perform(click());
    }

}
