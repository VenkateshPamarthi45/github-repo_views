package com.venkateshpamarthi.carworkz.features.users.search;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.venkateshpamarthi.carworkz.util.Constants;
import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.users.detail.UserDetailActivity;
import com.venkateshpamarthi.carworkz.features.users.list.UserListFragment;
import com.venkateshpamarthi.carworkz.features.users.model.UserManager;

public class SearchActivity extends AppCompatActivity implements UserListFragment.OnListFragmentInteractionListener {

    private static final String TAG = "SearchActivity";
    private String mSearchedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getExtras().containsKey(Constants.Extras.KEY_SEARCH_QUERY)){
            mSearchedName = getIntent().getExtras().getString(Constants.Extras.KEY_SEARCH_QUERY);
            Log.i(TAG, "onCreate: searched loginName " + mSearchedName);
        }
        setTitle(mSearchedName);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new SearchListFragment().newInstance(mSearchedName);
        fragmentManager.beginTransaction().replace(R.id.activity_container, fragment, SearchListFragment.TAG).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onListFragmentInteraction(UserManager.User item) {
        Log.d(TAG, "onListFragmentInteraction() called with: item = [" + item + "]");
        Intent intent = new Intent(SearchActivity.this, UserDetailActivity.class);
        intent.putExtra(Constants.Extras.KEY_LOGIN_NAME, item.loginName);
        startActivity(intent);
    }
}
