package robolectric;


import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.Rule;

import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;

import ca.nika.it.gear5.ReviewFragment;

@RunWith(RobolectricTestRunner.class)
public class RobolectricTest {

    @Rule
    public FragmentTestRule<?, ReviewFragment> fragmentTestRule =
            FragmentTestRule.create(ReviewFragment.class);
}
