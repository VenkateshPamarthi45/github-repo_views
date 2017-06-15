package com.venkateshpamarthi.carworkz.features.users.list;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.venkateshpamarthi.carworkz.util.Constants;
import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.users.detail.UserDetailActivity;
import com.venkateshpamarthi.carworkz.features.users.model.UserManager;
import com.venkateshpamarthi.carworkz.features.users.search.SearchActivity;

public class UserListActivity extends AppCompatActivity implements UserListFragment.OnListFragmentInteractionListener {

    private static final String TAG = "UserListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new UserListFragment().newInstance();
        fragmentManager.beginTransaction().replace(R.id.activity_container, fragment, UserListFragment.TAG).commit();
    }

    @Override
    public void onListFragmentInteraction(UserManager.User item) {
        Log.d(TAG, "onListFragmentInteraction() called with: item = [" + item + "]");
        Intent intent = new Intent(UserListActivity.this, UserDetailActivity.class);
        intent.putExtra(Constants.Extras.KEY_LOGIN_NAME, item.loginName);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit() called with: query = [" + query + "]");
                Intent intent = new Intent(UserListActivity.this, SearchActivity.class);
                intent.putExtra(Constants.Extras.KEY_SEARCH_QUERY, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
}
