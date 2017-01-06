package com.dmcapps.navigationfragment.v17;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;

import com.dmcapps.navigationfragment.common.core.ActionBarManager;
import com.dmcapps.navigationfragment.common.core.NavigationManager;
import com.dmcapps.navigationfragment.common.core.NavigationTransaction;
import com.dmcapps.navigationfragment.common.helpers.utils.ObjectUtils;
import com.dmcapps.navigationfragment.common.interfaces.Navigation;
import com.dmcapps.navigationfragment.common.interfaces.NavigationManagerContainer;

import java.util.UUID;

/**
 * This is the NavigationFragment that all classes in the stack must implement
 * The extension of this class in the child fragments allows access to the presenting
 * and dismissing of existing {@link Fragment} in the stack. The class will also generate
 * and maintain a constant TAG for the class allowing the navigation manager to
 * effectively store and present the Fragments as needed.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class NavigationFragment extends Fragment implements Navigation {

    private final String TAG = UUID.randomUUID().toString();
    private String mTitle;
    // Need to store the bundle myself as you can't change the bundle after the fragment has been presented.
    private Bundle mNavBundle;

    public NavigationFragment() { }

    @Override
    public String getNavTag() {
        return TAG;
    }

    @Override
    public void setNavBundle(Bundle navBundle) {
        mNavBundle = navBundle;
    }

    @Override
    public Bundle getNavBundle() {
        return mNavBundle;
    }

    /**
     * Get the {@link NavigationManagerFragment} of the Fragment in the stack. This method will crash with
     * a RuntimeException if no Parent fragment is a {@link NavigationManagerFragment}.
     *
     * @return
     *      The Parent Fragment in the stack of fragments that is the Navigation Manager of the Fragment.
     */
    @Override
    public NavigationManager getNavigationManager() {
        Fragment parent = this;

        // Loop until we find a parent that is a NavigationFragmentManager or there are no parents left to check.
        do {
            parent = parent.getParentFragment();

            NavigationManagerContainer container = ObjectUtils.as(NavigationManagerFragment.class, parent);
            if (container != null) {
                NavigationManager navigationManager = container.getNavigationManager();
                navigationManager.beginTransaction();
                return navigationManager;
            }
        } while(parent != null);

        throw new RuntimeException("No parent NavigationManagerFragment found. In order to use the Navigation Manager Fragment you must have a parent in your Fragment Manager.");
    }

    /**
     * Present a fragment on the {@link NavigationManager}
     * Uses default animation set on the {@link NavigationManager}
     *
     * @param
     *      navFragment -> The {@link Fragment} to present.
     */
    @Override
    public void presentFragment(Navigation navFragment) {
        getNavigationManager().presentFragment(navFragment);
    }

    /**
     * Push a new Fragment onto the stack and presenting it to the screen
     * Uses default animation set on the {@link NavigationManager}
     * Sends a Bundle with the Fragment that can be retrieved using {@link Navigation#getNavBundle()}
     *
     * @param
     *      navFragment -> The {@link Fragment} to show. It must be a {@link Fragment} that implements {@link Navigation}
     * @param
     *      navBundle -> Bundle to add to the presenting of the {@link Fragment}.
     *
     * @deprecated
     *      This function is being replaced with the {@link NavigationManager#setNavBundle(Bundle)} method call.
     *      In order to add a bundle you should call
     *      <code>
     *          getNavigationManager()
     *              .setNavBundle(navBundle)
     *              .presentFragment(navFragment);
     *      </code>
     *      Allowing for more parameters to be passed in with the call.
     *      To be removed in 2.1.0.
     */
    @Deprecated
    public void presentFragment(Navigation navFragment, Bundle navBundle) {
        getNavigationManager().presentFragment(navFragment, navBundle);
    }

    /**
     * Dimiss the current fragment off the top of the stack and dismiss it.
     * Uses default animation of slide in from left and slide out to right animation.
     */
    @Override
    public void dismissFragment() {
        getNavigationManager().dismissFragment();
    }

    /**
     * Pop the current fragment off the top of the stack and dismiss it.
     * Uses default animation of slide in from left and slide out to right animation.
     *
     * @param
     *      navBundle -> The navigation bundle to add to the fragment after the pop occurs
     */
    @Override
    public void dismissFragment(Bundle navBundle) {
        getNavigationManager().dismissFragment(navBundle);
    }

    /**
     * Dismiss all fragments to the given index in the stack (With 0 being the root fragment)
     */
    @Override
    public void dismissToIndex(int index) {
        getNavigationManager().clearNavigationStackToIndex(index);
    }

    /**
     * Dismiss all the fragments on the Navigation Manager stack until the root fragment using the default slide in and out.
     */
    @Override
    public void dismissToRoot() {
        getNavigationManager().clearNavigationStackToRoot();
    }

    /**
     * Dismiss all the fragments on the Navigation Manager stack including the root fragment using the default slide in and out.
     * Present a fragment on the Navigation Manager using the default slide in and out.
     *
     * @param
     *      navFragment -> The Fragment to present.
     */
    @Override
    public void replaceRootFragment(Navigation navFragment) {
        getNavigationManager().replaceRootFragment(navFragment);
    }

    /**
     * A method for setting the title of the action bar. (Saves you from having to call getActivity().setTitle())
     *
     * @param
     *      resId -> Resource Id of the title you would like to set.
     */
    @Override
    public void setTitle(int resId) {
        setTitle(getString(resId));
    }

    /**
     * A method for setting the title of the action bar. (Saves you from having to call getActivity().setTitle())
     *
     * @param
     *      title -> String of the title you would like to set.
     */
    @Override
    public void setTitle(String title) {
        mTitle = title;
        ActionBarManager.setTitle(getActivity(), mTitle);
    }

    /**
     * A method for retrieving the currently set title for the NavigationFragment
     *
     * @return
     *      The current title of the NavigationFragment
     */
    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public boolean isPortrait() {
        return getNavigationManager().isPortrait();
    }

    @Override
    public boolean isTablet() {
        return getNavigationManager().isTablet();
    }
}