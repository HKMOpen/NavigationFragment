package com.dmcapps.navigationfragmentexample.v17.TransitionExample;

import android.os.Bundle;

import com.dmcapps.navigationfragment.v17.NavigationFragment;
import com.dmcapps.navigationfragmentexample.R;
import com.dmcapps.navigationfragmentexample.v17.NavigationFragments.SmallImageFragment;
import com.dmcapps.navigationfragmentexample.v17.SingleStackSuperActivity;

public class TransitionExampleActivity extends SingleStackSuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_example);
    }

    @Override
    protected NavigationFragment rootFragment() {
        return SmallImageFragment.newInstance();
    }

    @Override
    protected int getContainerId() {
        return R.id.frag_container;
    }
}
