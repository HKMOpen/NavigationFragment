package com.dmcapps.navigationfragment.common.helpers.fragmentmanagerwrapper;

import com.dmcapps.navigationfragment.common.helpers.fragmenttransactionwrapper.FragmentTransactionWrapper;

/**
 * Created by dcarmo on 2016-11-19.
 */

public interface FragmentManagerWrapper {

    FragmentTransactionWrapper beginTransactionWrapped();

    Object findFragmentByTag(String tag);

    void popBackStack();

    void popBackStack(String name, int flags);

    void popBackStack(int id, int flags);

    void popBackStackImmediate();

    void popBackStackImmediate(String name, int flags);

    void popBackStackImmediate(int id, int flags);

    boolean executePendingTransactions();

}