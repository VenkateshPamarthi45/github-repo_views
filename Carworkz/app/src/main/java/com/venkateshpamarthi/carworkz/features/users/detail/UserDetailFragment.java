package com.venkateshpamarthi.carworkz.features.users.detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.repos.list.ExploreReposListActivity;
import com.venkateshpamarthi.carworkz.features.users.model.UserManager;
import com.venkateshpamarthi.carworkz.util.Constants;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDetailFragment extends Fragment implements UserManager.onResponseListener{

    public static final String TAG = "UserDetailFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LOGIN_NAME = "login_name";
    private String mLoginName;
    private TextView userName;
    private TextView userBio;

    private OnFragmentInteractionListener mListener;
    ImageView profileImageView;
    private ProgressDialog mProgressDialog;
    private TextView publicRepoTextView;
    private TextView followingTextView;
    private TextView followersTextView;
    private Button mExploreReposButton;

    public UserDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param loginName Parameter 1.
     * @return A new instance of fragment UserDetailFragment.
     */
    public static UserDetailFragment newInstance(String loginName) {
        UserDetailFragment fragment = new UserDetailFragment();
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

    public void showProgressDialog(String message) {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);

        mExploreReposButton = view.findViewById(R.id.explore_repos_button);
        profileImageView = view.findViewById(R.id.profile_pic);
        userName = view.findViewById(R.id.user_name);
        userBio = view.findViewById(R.id.user_bio);
        publicRepoTextView = view.findViewById(R.id.public_repos);
        followingTextView = view.findViewById(R.id.following);
        followersTextView = view.findViewById(R.id.followers);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);

        UserManager userManager = new UserManager();
        showProgressDialog(getString(R.string.please_wait));
        userManager.getUserDetails(getContext(), mLoginName, this);
        mExploreReposButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExploreReposListActivity.class);
                intent.putExtra(Constants.Extras.KEY_LOGIN_NAME, mLoginName);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        hideProgressDialog();
        final UserManager.User user = (UserManager.User) response;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getContext()).load(user.imageUrl)
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(profileImageView);
                userName.setText(user.loginName);
                userBio.setText(user.bio);
                followersTextView.setText(getString(R.string.followers) +"\n" + user.followers);
                followingTextView.setText(getString(R.string.following) +"\n" + user.following);
                publicRepoTextView.setText(getString(R.string.Repos) +"\n" + user.public_repos);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and loginName
        void onFragmentInteraction(Uri uri);
    }
}
