package com.dmcapps.navigationfragment.support.v7.manager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dmcapps.navigationfragment.R;
import com.dmcapps.navigationfragment.common.interfaces.Navigation;
import com.dmcapps.navigationfragment.common.helpers.AnimationEndListener;
import com.dmcapps.navigationfragment.support.v7.manager.core.NavigationManagerFragment;
import com.dmcapps.navigationfragment.support.v7.manager.core.micromanagers.lifecycle.MasterDetailLifecycleManager;

/**
 * @deprecated -> Depreciated. You can use two {@link StackNavigationManagerFragment} to perform this the same.
 */
@Deprecated
@SuppressLint("ValidFragment")
public class MasterDetailNavigationManagerFragment extends NavigationManagerFragment {
    private static final String TAG = MasterDetailNavigationManagerFragment.class.getSimpleName();

    // TODO: Should this manager take care of making the home up enabled show?

    // TODO: I think I need callbacks to the activity to tell it that the stack
    // size is small and hence it needs to show the home button for the master view
    // and also to tell the activity that a view has been pushed and hence to now show
    // the home button anymore.

    private String mMasterToggleTitle;
    private int mMasterToggleResId = -1;

    private boolean mManageMasterActionBarToggle = false;

    public static MasterDetailNavigationManagerFragment newInstance(Navigation masterFragment, Navigation detailFragment) {
        return new MasterDetailNavigationManagerFragment(masterFragment, detailFragment);
    }

    public MasterDetailNavigationManagerFragment() {}

    // NOTE I need to pass in the fragments to be used by the constructor.
    // If I serialize them into the bundle then whenever the application is backgrounded
    // or an activity is launched, the application will crash with NotSerializableException
    // if any of the Fragments in the stack have properties that are no Serializable.
    public MasterDetailNavigationManagerFragment(Navigation masterFragment, Navigation detailFragment) {
        mConfig.setMasterFragment(masterFragment);
        mConfig.setDetailFragment(detailFragment);
        mLifecycle = new MasterDetailLifecycleManager();
    }

    public void setManageMasterActionBarToggle(boolean manageToggle) {
        mManageMasterActionBarToggle = manageToggle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(mManageMasterActionBarToggle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_master_detail, menu);
        MenuItem masterToggle = menu.findItem(R.id.menu_master_detail_toggle_master);

        masterToggle.setVisible(isOnRootAndMasterIsToggleable());

        if (mMasterToggleResId > 0) {
            masterToggle.setTitle(mMasterToggleResId);
        }
        else if (mMasterToggleTitle != null && !mMasterToggleTitle.equals("")) {
            masterToggle.setTitle(mMasterToggleTitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_master_detail_toggle_master) {
            toggleMaster();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean shouldMasterToggle() {
        return isOnRootFragment() && isTablet() && isPortrait();
    }

    public void toggleMaster() {
        if (shouldMasterToggle()) {
            final View masterFrame = getView().findViewById(R.id.navigation_manager_container_master);
            if (masterFrame.getVisibility() == View.INVISIBLE) {
                showMaster();
            } else {
                hideMaster();
            }
        }
    }

    // TODO: Better animation handling. This doesn't allow for custom animations.
    public void showMaster() {
        final View masterFrame = getView().findViewById(R.id.navigation_manager_container_master);
        if (shouldMasterToggle() && masterFrame.getVisibility() == View.INVISIBLE) {
            masterFrame.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_from_left);
            masterFrame.startAnimation(anim);
        }
    }

    // TODO: Better animation handling. This doesn't allow for custom animations.
    public void hideMaster() {
        final View masterFrame = getView().findViewById(R.id.navigation_manager_container_master);

        if (shouldMasterToggle() && masterFrame.getVisibility() == View.VISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_to_left);
            anim.setAnimationListener(new AnimationEndListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    masterFrame.setVisibility(View.INVISIBLE);
                }
            });
            masterFrame.startAnimation(anim);
        }
    }

    public boolean isOnRootAndMasterIsToggleable() {
        return isOnRootFragment() && isTablet() && isPortrait();
    }

    public void setMasterToggleTitle(String title) {
        mMasterToggleTitle = title;
        getActivity().invalidateOptionsMenu();
    }

    public void setMasterToggleTitle(int resId) {
        mMasterToggleResId = resId;
        getActivity().invalidateOptionsMenu();
    }
}