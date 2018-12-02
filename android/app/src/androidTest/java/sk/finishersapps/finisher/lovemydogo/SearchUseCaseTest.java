package sk.finishersapps.finisher.lovemydogo;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import sk.finishersapps.finisher.lovemydogo.acitivities.MainMenuActivity;
import sk.finishersapps.finisher.lovemydogo.acitivities.MapActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SearchUseCaseTest {

    @Rule
    public ActivityTestRule<MainMenuActivity> mainMenuRule
            = new ActivityTestRule<>(MainMenuActivity.class, true, false);
    Context context = null;


    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
    }


    public void findVets(int progressAMount) throws InterruptedException {
        Intent i = new Intent(context, MainMenuActivity.class);
        mainMenuRule.launchActivity(i);
        mainMenuRule.getActivity().processSearchResult(null);
        onView(withId(R.id.mmaVets)).perform(click());
        onView(withId(R.id.mmaDistancePickerLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.mmaDistanceSeekBar)).perform(setProgress(progressAMount));
        onView(withId(R.id.mmaConfirmDistanceButton)).perform(click());
        try {
            while (true) {
                onView(withId(R.id.mmaResultsProgressBar)).check(matches(isDisplayed()));
                Thread.sleep(100);
            }
        } catch (AssertionError ignored) {

        }
    }

    @Test
    @LargeTest
    public void noResultsDistance() throws InterruptedException {
        findVets(0);
        onView(withId(R.id.mmaDistancePickerLayout)).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void unlimitedDistanceTest() throws InterruptedException {
        findVets(100);
        onView(withId(R.id.mmaResultsListLayout)).check(matches(isDisplayed()));
    }

    @Test
    @LargeTest
    public void waysOfTransportShownTest() throws InterruptedException {
        findVets(100);
        onView(withId(R.id.mmaResultsListLayout)).check(matches(isDisplayed()));
        onView(nthChildOf(withId(R.id.mmaResultsListLayout), 0)).perform(click());
        onView(withId(R.id.mmaWayOfTransportBackground)).check(matches(isDisplayed()));
        onView(withId(R.id.mmaCarButton)).perform(click());
    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childPosition + " child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }


    private ViewAction setProgress(final int progress) {
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

}
