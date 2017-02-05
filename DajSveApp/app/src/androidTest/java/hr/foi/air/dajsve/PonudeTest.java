package hr.foi.air.dajsve;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import hr.foi.air.dajsve.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

/**
 * Created by Helena on 31.1.2017..
 */

@RunWith(AndroidJUnit4.class)
public class PonudeTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    //test za taba s novim ponudama
    @Test
    public void NovePonude(){
        onView(withText("Nove ponude")).check(ViewAssertions.matches(isDisplayed()));
    }
    //test za prikaz taba sa svim ponudama
    @Test
    public void SvePonude(){
        onView(withText("Sve ponude")).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
    }

    //test za klik na ponudu
    @Test
    public void KlikNaPonudu(){
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ponuda_name)).check(ViewAssertions.matches(isDisplayed()));
    }

    //metoda za otvaranje drawera
    private static ViewAction actionOpenDrawer() {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "open drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).openDrawer(GravityCompat.START);
            }
        };

    }
}
