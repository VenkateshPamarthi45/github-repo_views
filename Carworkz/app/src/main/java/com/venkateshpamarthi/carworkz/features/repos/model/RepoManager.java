package com.venkateshpamarthi.carworkz.features.repos.model;

import android.content.Context;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.venkateshpamarthi.carworkz.AppController;
import com.venkateshpamarthi.carworkz.features.repos.detail.RepoDetailFragment;
import com.venkateshpamarthi.carworkz.features.users.model.UserManager;
import com.venkateshpamarthi.carworkz.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class RepoManager {

    private static final String TAG = "RepoManager";



    public interface onResponseListener {
        void onSuccess(Object response);
        void onFailure(int statusCode);
    }

    public void getUserRepoListDetails(String userName, final onResponseListener listener) {
        Log.d(TAG, "getUserRepoListDetails() called with: userName = [" + userName + "], listener = [" + listener + "]");
        String url = Constants.Urls.BASE_URL + "/"+ userName + "/repos";
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                        parseResponse(response, listener);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error.networkResponse != null) {
                    listener.onFailure(error.networkResponse.statusCode);
                }

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void getRepoDetails(String mLoginName, String mRepoName, final onResponseListener listener) {
        Log.d(TAG, "getRepoDetails() called with: mLoginName = [" + mLoginName + "], mRepoName = [" + mRepoName + "], listener = [" + listener + "]");
        String url = Constants.Urls.REPO_URL + "/"+ mLoginName + "/"+mRepoName;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                        parseDetailResponse(response, listener);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error.networkResponse != null) {
                    listener.onFailure(error.networkResponse.statusCode);
                }

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void createIssue(String mLoginName, String mRepoName, String issueName, String issueDesc, final onResponseListener listener) {
        Log.d(TAG, "createIssue() called with: mLoginName = [" + mLoginName + "], mRepoName = [" + mRepoName + "], issueName = [" + issueName + "], issueDesc = [" + issueDesc + "], listener = [" + listener + "]");
        String url = Constants.Urls.REPO_URL + "/"+ mLoginName + "/"+mRepoName + "/issues";

        JSONObject networkParams = new JSONObject();
        try {
            networkParams.put(Constants.NetworkParams.TITLE, issueName);
            networkParams.put(Constants.NetworkParams.BODY, issueDesc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, networkParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                        listener.onSuccess(null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error.networkResponse != null) {
                    listener.onFailure(error.networkResponse.statusCode);
                }

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @WorkerThread
    private void parseResponse(final JSONArray response, final onResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Repo> repos = new ArrayList<Repo>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject repoObject = response.getJSONObject(i);
                        String id  = repoObject.getString("id");
                        String name = repoObject.getString("name");
                        String desc = repoObject.getString("description");
                        Repo repo = new Repo(id, name, desc);
                        repos.add(repo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onSuccess(repos);
            }
        }).start();

    }

    @WorkerThread
    private void parseDetailResponse(final JSONObject response, final onResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject repoObject = response;
                    String id  = repoObject.getString("id");
                    String name = repoObject.getString("name");
                    String desc = repoObject.getString("description");
                    Repo repo = new Repo(id, name, desc);
                    String openIssues = repoObject.getString("open_issues");
                    String watchers = repoObject.getString("watchers");
                    String subscribers_count = repoObject.getString("subscribers_count");
                    repo.openIssues = openIssues;
                    repo.watchers = watchers;
                    repo.subscribers_count = subscribers_count;
                    listener.onSuccess(repo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * A Repo item representing a piece of content.
     */
    public static class Repo {
        public final String id;
        public final String name;
        public final String description;
        public String openIssues;
        public String watchers;
        public String subscribers_count;

        public Repo(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}
