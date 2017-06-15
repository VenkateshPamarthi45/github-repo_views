package com.venkateshpamarthi.carworkz.features.users.model;

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
import com.venkateshpamarthi.carworkz.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserManager {
    private static final String TAG = "UserManager";
    public static final List<User> ITEMS = new ArrayList<User>();
    private static int lastId = 0;

    public interface onResponseListener {
        void onSuccess(Object response);
        void onFailure(int statusCode);
    }

    public void getUserListDetails(Context context, final onResponseListener listener) {
        Log.d(TAG, "getUserListDetails() called with: context = [" + context + "], last id = [" + lastId + "], listener = [" + listener + "]");
        String url = Constants.Urls.BASE_URL + "?since="+ lastId;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                        parseResponse(response, listener,0);
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

    public void getUserDetails(Context context, String username, final onResponseListener listener) {
        Log.d(TAG, "getUserDetails() called with: context = [" + context + "], username = [" + username + "], listener = [" + listener + "]");
        String url = Constants.Urls.BASE_URL + "/"+ username;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                        parseDetailResponse(response,listener);
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

    private void parseDetailResponse(final JSONObject response, final onResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject userResponseObject = response;
                    String id = userResponseObject.getString("id");
                    String imageUrl = userResponseObject.getString("avatar_url");
                    String name = userResponseObject.getString("login");
                    String bio = userResponseObject.getString("bio");
                    String following = userResponseObject.getString("following");
                    String followers = userResponseObject.getString("followers");
                    String public_repos = userResponseObject.getString("public_repos");
                    String name1 = userResponseObject.getString("name");
                    User user = new User(id, name, imageUrl);
                    user.bio = bio;
                    user.followers = followers;
                    user.following = following;
                    user.public_repos = public_repos;
                    user.name = name1;
                    listener.onSuccess(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getSearchUserListDetails(Context context,String username, final onResponseListener listener) {
        Log.d(TAG, "getUserListDetails() called with: context = [" + context + "], last id = [" + lastId + "], listener = [" + listener + "]");

        String url = Constants.Urls.SEARCH_URL + "?q="+ username.replaceAll(" ", "%20");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                        try {
                            JSONArray itemsArray = response.getJSONArray("items");
                            parseResponse(itemsArray, listener, 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
    private void parseResponse(final JSONArray response, final onResponseListener listener, final int type) {
        Log.d(TAG, "parseResponse() called with: response = [" + response + "], listener = [" + listener + "]");
        if(response != null && type == 0){
            try {
                JSONObject jsonObject = response.getJSONObject(response.length() - 1);
                lastId = jsonObject.getInt("id");
                Log.i(TAG, "parseResponse: last id " + lastId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<User> users = new ArrayList<User>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject userResponseObject = response.getJSONObject(i);
                        String id = userResponseObject.getString("id");
                        String imageUrl = userResponseObject.getString("avatar_url");
                        String name = userResponseObject.getString("login");
                        User user = new User(id, name, imageUrl);
                        if (type == 0) {
                            ITEMS.add(user);
                        } else {
                            users.add(user);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (type == 0) {
                    listener.onSuccess(null);
                } else {
                    listener.onSuccess(users);
                }
            }
        }).start();
    }

    /**
     * A user item representing a piece of loginName.
     */
    public static class User {
        public final String id;
        public final String loginName;
        public final String imageUrl;
        public String following;
        public String followers;
        public String public_repos;
        public String bio;
        public String name;

        public User(String id, String loginName, String imageUrl) {
            this.id = id;
            this.loginName = loginName;
            this.imageUrl = imageUrl;
        }

        @Override
        public String toString() {
            return loginName;
        }
    }
}
