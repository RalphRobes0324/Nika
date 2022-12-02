package robolectric;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.robolectric.annotation.Implements;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static android.os.Build.VERSION_CODES.S;
import static junit.framework.TestCase.assertNotNull;

import android.content.pm.ActivityInfo;
import android.widget.ImageView;

import org.robolectric.android.controller.ActivityController;
import ca.nika.it.gear5.MainActivity;
import ca.nika.it.gear5.R;

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

    //verify that landscape works
    @Test
    public void checkLandscape() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}
