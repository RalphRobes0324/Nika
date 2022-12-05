// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package robolectric;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.robolectric.annotation.Implements;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static android.os.Build.VERSION_CODES.S;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

import android.content.pm.ActivityInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.robolectric.android.controller.ActivityController;

import ca.nika.it.gear5.LoginSetup.LoginActivity;
import ca.nika.it.gear5.MainActivity;
import ca.nika.it.gear5.R;
import ca.nika.it.gear5.SignInFile.GoogleSignInActivity;
import ca.nika.it.gear5.SplashActivity;

@RunWith(RobolectricTestRunner.class)
@Implements(value = org.robolectric.shadows.ShadowBackdropFrameRenderer.class, minSdk = S, isInAndroidSdk = false)
public class RobolectricTest {

    //verify image view is displayed
    @Test
    public void imageViewCheck() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();
            ImageView checker = (ImageView) activity.findViewById(R.id.nikaImageView);
            assertNotNull(activity.getString(R.string.image_not_shown), checker);
        }
    }

    //verify that landscape works for main
    @Test
    public void checkLandscapeMain() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity mainActivity = controller.get();
            mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    //verify that landscape works for login
    @Test
    public void checkLandscapeLogin() {
        try (ActivityController<LoginActivity> controller = Robolectric.buildActivity(LoginActivity.class)) {
            controller.setup();
            LoginActivity loginActivity = controller.get();
            loginActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    //verify frame layout exist
    @Test
    public void frameLayoutCheck() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();
            FrameLayout checker = (FrameLayout) activity.findViewById(R.id.nikaFrameLayout);
            assertNotNull(activity.getString(R.string.image_not_shown), checker);
        }
    }

    //verify bottom nav exist
    @Test
    public void bottomNavCheck() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();
            BottomNavigationView checker = (BottomNavigationView) activity.findViewById(R.id.nikaBottomNavigationView);
            assertNotNull(activity.getString(R.string.image_not_shown), checker);
        }
    }

    //verify bottom nav item play exist
    @Test
    public void bottomNavPlayCheck() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();
            BottomNavigationItemView checker = (BottomNavigationItemView) activity.findViewById(R.id.play);
            assertNotNull(activity.getString(R.string.image_not_shown), checker);
        }
    }

    //verify bottom nav item score exist
    @Test
    public void bottomNavScoreCheck() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();
            BottomNavigationItemView checker = (BottomNavigationItemView) activity.findViewById(R.id.score);
            assertNotNull(activity.getString(R.string.image_not_shown), checker);
        }
    }

    //verify bottom nav item profile exist
    @Test
    public void bottomNavProfileCheck() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();
            BottomNavigationItemView checker = (BottomNavigationItemView) activity.findViewById(R.id.profile);
            assertNotNull(activity.getString(R.string.image_not_shown), checker);
        }
    }

    //verify bottom nav item profile exist (test should fail)
    @Test
    public void bottomNavInvalidCheck() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();
            BottomNavigationItemView checker = (BottomNavigationItemView) activity.findViewById(R.id.submit_review);
            assertNotNull(activity.getString(R.string.image_not_shown), checker);
        }
    }
}
