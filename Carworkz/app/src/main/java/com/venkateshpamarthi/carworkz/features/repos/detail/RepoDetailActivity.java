package com.venkateshpamarthi.carworkz.features.repos.detail;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.users.detail.UserDetailFragment;
import com.venkateshpamarthi.carworkz.util.Constants;

public class RepoDetailActivity extends AppCompatActivity implements RepoDetailFragment.OnFragmentInteractionListener {

    private static final String TAG = "RepoDetailActivity";
    private String mLoginName;
    private String mRepoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getExtras().containsKey(Constants.Extras.KEY_LOGIN_NAME)){
            mLoginName = getIntent().getExtras().getString(Constants.Extras.KEY_LOGIN_NAME);
            mRepoName = getIntent().getExtras().getString(Constants.Extras.KEY_REPO_NAME);
        }
        setTitle(mLoginName + " " + mRepoName);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new RepoDetailFragment().newInstance(mLoginName, mRepoName);
        fragmentManager.beginTransaction().replace(R.id.activity_container, fragment, UserDetailFragment.TAG).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "onFragmentInteraction() called with: uri = [" + uri + "]");
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
