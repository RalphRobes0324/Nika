package espresso;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import ca.nika.it.gear5.LoginSetup.LoginFragment;
import ca.nika.it.gear5.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class EspressoTest {

    @Rule
    public FragmentTestRule<?, LoginFragment> fragmentTestRule =
            FragmentTestRule.create(LoginFragment.class);

    @Test
    public void validLogin()
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }

    @Test
    public void invalidLogin()
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("fakeAccount"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("fakeAccount"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }

    @Test
    public void validUserInvalidPassword()
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("badPassword"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }

    @Test
    public void invalidUserValidPassword()
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("fakeAccount"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }

    @Test
    public void capitalizationCheckUsername()
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("Admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }

    @Test
    public void capitalizationCheckPassword()
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("Admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }
}
