package com.dmcapps.navigationfragment.manager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dmcapps.navigationfragment.R;
import com.dmcapps.navigationfragment.fragments.INavigationFragment;
import com.dmcapps.navigationfragment.helper.ViewUtil;
import com.dmcapps.navigationfragment.manager.micromanagers.ManagerConfig;


/**
 * This Fragment manages the stack of single navigation on fragments.
 * The class allows for easy adding and removing of fragments as the user
 * traverses the screens. A self-contained class that requires no resources
 * in order to function. Each time a new manager is made a separate stack will be created
 * and no overlap will occur in the class.
 */
@SuppressLint("ValidFragment")
public class SingleStackNavigationManagerFragment extends NavigationManagerFragment {
    private static final String TAG = SingleStackNavigationManagerFragment.class.getSimpleName();

    private static final int ACTIONABLE_STACK_SIZE = 1;

    private INavigationFragment mRootFragment;

    public static SingleStackNavigationManagerFragment newInstance(INavigationFragment rootFragment) {
        return new SingleStackNavigationManagerFragment(rootFragment);
    }

    public SingleStackNavigationManagerFragment() {

    }

    public SingleStackNavigationManagerFragment(INavigationFragment rootFragment) {
        mRootFragment = rootFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_stack_navigation_manager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mState.isTablet = view.findViewById(R.id.single_stack_tablet_layout_main_portrait) != null
                || view.findViewById(R.id.single_stack_tablet_layout_main_land) != null;
        mState.isPortrait = view.findViewById(R.id.single_stack_phone_layout_main_portrait) != null
                || view.findViewById(R.id.single_stack_tablet_layout_main_portrait) != null;

        mConfig.minStackSize = ACTIONABLE_STACK_SIZE;
        mConfig.pushContainerId = R.id.single_stack_content;
    }

    @Override
    public void onResume() {
        super.onResume();

        // No Fragments have been added. Attach the root.
        if (mState.fragmentTagStack.size() == 0) {
            pushFragment(getRootFragment());
        }
        // Fragments are in the stack, resume at the top.
        else {
            FragmentManager childFragManager = getRetainedChildFragmentManager();
            FragmentTransaction childFragTrans = childFragManager.beginTransaction();
            childFragTrans.setCustomAnimations(ManagerConfig.NO_ANIMATION, ManagerConfig.NO_ANIMATION);
            childFragTrans.attach(childFragManager.findFragmentByTag(mState.fragmentTagStack.peek()));
            childFragTrans.commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        FragmentManager childFragManager = getRetainedChildFragmentManager();
        FragmentTransaction childFragTrans = childFragManager.beginTransaction();
        childFragTrans.setCustomAnimations(ManagerConfig.NO_ANIMATION, ManagerConfig.NO_ANIMATION);
        childFragTrans.detach(childFragManager.findFragmentByTag(mState.fragmentTagStack.peek()));
        childFragTrans.commit();
    }

    private INavigationFragment getRootFragment() {
        if (mRootFragment == null) {
            throw new RuntimeException("You must create the Manager through newInstance(INavigationFragment) before attaching the Manager to a Fragment Transaction");
        }

        return mRootFragment;
    }
}
