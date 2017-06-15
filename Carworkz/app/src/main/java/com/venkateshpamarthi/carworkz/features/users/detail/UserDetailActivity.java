package com.venkateshpamarthi.carworkz.features.users.detail;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.venkateshpamarthi.carworkz.util.Constants;
import com.venkateshpamarthi.carworkz.R;

public class UserDetailActivity extends AppCompatActivity implements UserDetailFragment.OnFragmentInteractionListener {

    private static final String TAG = "UserDetailActivity";
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
        setTitle(mLoginName);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new UserDetailFragment().newInstance(mLoginName);
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
