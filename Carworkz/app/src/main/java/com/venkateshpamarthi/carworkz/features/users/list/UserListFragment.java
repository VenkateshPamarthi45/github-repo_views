package com.venkateshpamarthi.carworkz.features.users.list;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.venkateshpamarthi.carworkz.features.users.adapters.MyUserListRecyclerViewAdapter;
import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.users.model.UserManager;
import com.venkateshpamarthi.carworkz.features.users.model.UserManager.User;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class UserListFragment extends Fragment implements UserManager.onResponseListener {

    public static final String TAG = "UserListFragment";
    private OnListFragmentInteractionListener mListener;
    private boolean isLoadingMoreItems = false;
    private int mPageId = 1;
    private MyUserListRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog mProgressDialog;
    private ProgressBar loadMoreProgressBar;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UserListFragment newInstance() {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userlist_list, container, false);

        // Set the adapter
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            loadMoreProgressBar = view.findViewById(R.id.load_more_progress_bar);
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setIndeterminate(true);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    Log.d(TAG, "onScrolled() called with: firstVisible Count = " + firstVisibleItemPosition + " visible count = [" + visibleItemCount + "], total Item count = [" + totalItemCount + "]");
                    if (!isLoadingMoreItems) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0) {
                            Log.d(TAG, "onScrolled() api call firstVisible Count = " + firstVisibleItemPosition + " visible count = [" + visibleItemCount + "], total Item count = [" + totalItemCount + "]");
                            mPageId++;
                            isLoadingMoreItems = true;
                            apiCall();
                        }
                    }
                }
            });
            apiCall();
        return view;
    }

    private void apiCall() {
        UserManager userManager = new UserManager();
        if (mPageId == 1) {
            showProgressDialog(getString(R.string.please_wait));
        }else{
            loadMoreProgressBar.setVisibility(View.VISIBLE);
        }
        userManager.getUserListDetails(getContext(), this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    @UiThread
    public void onSuccess(Object response) {
        Log.d(TAG, "onSuccess() called with: response = [" + response + "]");
        isLoadingMoreItems = false;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                loadMoreProgressBar.setVisibility(View.GONE);
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }else{
                    adapter = new MyUserListRecyclerViewAdapter(getContext(), UserManager.ITEMS, mListener);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onFailure(int statusCode) {
        isLoadingMoreItems = false;
        hideProgressDialog();
        Log.d(TAG, "onFailure() called with: statusCode = [" + statusCode + "]");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and loginName
        void onListFragmentInteraction(User item);
    }
}
