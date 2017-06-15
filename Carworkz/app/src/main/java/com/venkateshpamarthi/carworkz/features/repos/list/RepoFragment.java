package com.venkateshpamarthi.carworkz.features.repos.list;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.repos.adapter.RepoRecyclerViewAdapter;
import com.venkateshpamarthi.carworkz.features.repos.model.RepoManager;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RepoFragment extends Fragment implements RepoManager.onResponseListener {

    public static final String TAG = "RepoFragment";

    private static final String ARG_LOGIN_NAME = "login_name";
    private String mLoginName;
    private RecyclerView recyclerView;
    private OnListFragmentInteractionListener mListener;
    private ProgressDialog mProgressDialog;
    private RepoRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RepoFragment() {
    }

    @SuppressWarnings("unused")
    public static RepoFragment newInstance(String loginName) {
        RepoFragment fragment = new RepoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_NAME, loginName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLoginName = getArguments().getString(ARG_LOGIN_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        RepoManager repoManager = new RepoManager();
        showProgressDialog(getString(R.string.please_wait));
        repoManager.getUserRepoListDetails(mLoginName, this);
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
    public void onSuccess(Object response) {
        final List<RepoManager.Repo> repos = (List<RepoManager.Repo>) response;
        hideProgressDialog();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }else{
                    if (repos != null && repos.size() > 0) {
                        adapter = new RepoRecyclerViewAdapter(getContext(), repos, mListener, mLoginName);
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(RepoManager.Repo item, String loginName);
    }
}
