package com.venkateshpamarthi.carworkz.features.repos.list;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.repos.detail.RepoDetailActivity;
import com.venkateshpamarthi.carworkz.features.repos.model.RepoManager;
import com.venkateshpamarthi.carworkz.features.users.list.UserListActivity;
import com.venkateshpamarthi.carworkz.features.users.search.SearchActivity;
import com.venkateshpamarthi.carworkz.util.Constants;

public class ExploreReposListActivity extends AppCompatActivity implements RepoFragment.OnListFragmentInteractionListener {

    private static final String TAG = "ExploreReposListActivit";

    private String mLoginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getExtras().containsKey(Constants.Extras.KEY_LOGIN_NAME)){
            mLoginName = getIntent().getExtras().getString(Constants.Extras.KEY_LOGIN_NAME);
            Log.i(TAG, "onCreate: login loginName " + mLoginName);
        }
        setTitle(mLoginName + " Repos");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new RepoFragment().newInstance(mLoginName);
        fragmentManager.beginTransaction().replace(R.id.activity_container, fragment, RepoFragment.TAG).commit();
    }

    @Override
    public void onListFragmentInteraction(RepoManager.Repo item, String mLoginName) {
        Log.d(TAG, "onListFragmentInteraction() called with: item = [" + item + "]");
        Intent intent = new Intent(ExploreReposListActivity.this, RepoDetailActivity.class);
        intent.putExtra(Constants.Extras.KEY_LOGIN_NAME, mLoginName);
        intent.putExtra(Constants.Extras.KEY_REPO_NAME, item.name);
        startActivity(intent);
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
}
