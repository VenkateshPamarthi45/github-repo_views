package com.venkateshpamarthi.carworkz.features.repos.detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.repos.list.ExploreReposListActivity;
import com.venkateshpamarthi.carworkz.features.repos.model.RepoManager;
import com.venkateshpamarthi.carworkz.util.Constants;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RepoDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RepoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepoDetailFragment extends Fragment implements RepoManager.onResponseListener{

    public static final String TAG = "RepoDetailFragment";

    private static final String ARG_LOGIN_NAME = "login";
    private static final String ARG_REPO_NAME = "repo";
    private String mLoginName;
    private TextView repoNameTextView;
    private TextView descriptionTextView;

    private OnFragmentInteractionListener mListener;
    private ImageView profileImageView;
    private ProgressDialog mProgressDialog;
    private TextView subscribersTextView;
    private TextView watchersTextView;
    private TextView openIssuesTextView;
    private Button mCreateIssueButton;
    private String mRepoName;

    public RepoDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param loginName Parameter 1.
     * @return A new instance of fragment UserDetailFragment.
     */
    public static RepoDetailFragment newInstance(String loginName, String repoName) {
        RepoDetailFragment fragment = new RepoDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_NAME, loginName);
        args.putString(ARG_REPO_NAME, repoName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLoginName = getArguments().getString(ARG_LOGIN_NAME);
            mRepoName = getArguments().getString(ARG_REPO_NAME);
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

        mCreateIssueButton = view.findViewById(R.id.explore_repos_button);
        profileImageView = view.findViewById(R.id.profile_pic);
        repoNameTextView = view.findViewById(R.id.user_name);
        descriptionTextView = view.findViewById(R.id.user_bio);
        subscribersTextView = view.findViewById(R.id.public_repos);
        watchersTextView = view.findViewById(R.id.following);
        openIssuesTextView = view.findViewById(R.id.followers);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);

        profileImageView.setVisibility(View.GONE);
        RepoManager repoManager = new RepoManager();
        showProgressDialog(getString(R.string.please_wait));
        repoManager.getRepoDetails(mLoginName, mRepoName, this);
        mCreateIssueButton.setText(R.string.create_issue);
        mCreateIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createIssueDialog();
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
        final RepoManager.Repo repo = (RepoManager.Repo) response;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                repoNameTextView.setText(repo.name);
                descriptionTextView.setText(repo.description);
                openIssuesTextView.setText(getString(R.string.open_issues) +"\n" + repo.openIssues);
                watchersTextView.setText(getString(R.string.watchers) +"\n" + repo.watchers);
                subscribersTextView.setText(getString(R.string.subscribers) +"\n" + repo.subscribers_count);
            }
        });

    }

    private void createIssueDialog() {

        final AppCompatDialog dialog = new AppCompatDialog(getContext());
        dialog.setContentView(R.layout.issue_dialog);
        // Set dialog title
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window != null ? window.getAttributes() : null);

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setTitle("Create Issue");

        final TextInputEditText nameEditText = (TextInputEditText) dialog.findViewById(R.id.dialog_name_edit_text);
        final TextInputEditText descriptionEditText = (TextInputEditText) dialog.findViewById(R.id.dialog_body_edit_text);
        Button doneButton = (Button) dialog.findViewById(R.id.dialog_name_done_button);
        dialog.show();
        // if decline button is clicked, close the custom dialog
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                String issueName = nameEditText.getText().toString();
                String issueDesc = descriptionEditText.getText().toString();
                if (!TextUtils.isEmpty(issueName) && !TextUtils.isEmpty(issueDesc)) {
                    RepoManager repoManager = new RepoManager();
                    repoManager.createIssue(mLoginName, mRepoName, issueName, issueDesc, new RepoManager.onResponseListener(){
                        @Override
                        public void onSuccess(Object response) {
                            Log.d(TAG, "onSuccess() called with: response = [" + response + "]");
                            Toast.makeText(getContext(), "Create issue successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode) {
                            Log.d(TAG, "onFailure() called with: statusCode = [" + statusCode + "]");
                            Toast.makeText(getContext(), "Sorry, Something went wrong!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Please fill details", Toast.LENGTH_SHORT).show();
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
