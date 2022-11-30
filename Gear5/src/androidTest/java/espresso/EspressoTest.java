// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560

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
    public void validLogin() //username and password are in server and match
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }

    @Test
    public void invalidLogin() //username and password are non-exisitent in server
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("fakeAccount"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("fakeAccount"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }

    @Test
    public void validUserInvalidPassword() //admin is a verified user but the password is incorrect
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("badPassword"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }

    @Test
    public void invalidUserWrongAccountPassword() //fakeaccount is a non-existent account and the password is for a different account
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("fakeAccount"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }

    @Test
    public void capitalizationCheckUsername() //username capitalization is wrong
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("Admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }

    @Test
    public void capitalizationCheckPassword() //password capitalization is wrong
    {
        onView(withId(R.id.nika_edittext_username_loginFrag))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_edittext_pwd_loginFrag))
                .perform(typeText("Admin"), closeSoftKeyboard());
        onView(withId(R.id.nika_btn_login_login)).perform(click());
    }
}
