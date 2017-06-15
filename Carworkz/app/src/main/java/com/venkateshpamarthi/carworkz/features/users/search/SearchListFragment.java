package com.venkateshpamarthi.carworkz.features.users.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.venkateshpamarthi.carworkz.features.users.adapters.MyUserListRecyclerViewAdapter;
import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.users.list.UserListFragment;
import com.venkateshpamarthi.carworkz.features.users.model.UserManager;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SearchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchListFragment extends Fragment implements UserManager.onResponseListener {

    public static final String TAG = "SearchListFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SEARCH_NAME = "param1";

    private String mSearchedName;

    private UserListFragment.OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private MyUserListRecyclerViewAdapter adapter;
    private ProgressDialog mProgressDialog;

    public SearchListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param searchedName Parameter 1.
     * @return A new instance of fragment SearchListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchListFragment newInstance(String searchedName) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_NAME, searchedName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchedName = getArguments().getString(ARG_SEARCH_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_userlist_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        apiCall();
        return view;
    }

    private void apiCall() {
        UserManager userManager = new UserManager();
        showProgressDialog(getString(R.string.please_wait));
        userManager.getSearchUserListDetails(getContext(), mSearchedName, this);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserListFragment.OnListFragmentInteractionListener) {
            mListener = (UserListFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSuccess(final Object response) {
        final List<UserManager.User> users = (List<UserManager.User>) response;
        hideProgressDialog();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }else{
                    if (users != null && users.size() > 0) {
                        adapter = new MyUserListRecyclerViewAdapter(getContext(), users, (UserListFragment.OnListFragmentInteractionListener) mListener);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        });
    }

    @Override
    public void onFailure(int statusCode) {
        hideProgressDialog();
    }
}
