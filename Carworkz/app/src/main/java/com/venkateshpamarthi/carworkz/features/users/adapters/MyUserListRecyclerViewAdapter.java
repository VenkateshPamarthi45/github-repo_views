package com.venkateshpamarthi.carworkz.features.users.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.users.list.UserListFragment.OnListFragmentInteractionListener;
import com.venkateshpamarthi.carworkz.features.users.model.UserManager.User;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyUserListRecyclerViewAdapter extends RecyclerView.Adapter<MyUserListRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MyUserListRecyclerViewA";
    private final List<User> mValues;
    private Context context;
    private final OnListFragmentInteractionListener mListener;

    public MyUserListRecyclerViewAdapter(Context context, List<User> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        this.context = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_userlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Glide.with(context).load(mValues.get(position).imageUrl)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.mIdView);
        holder.mContentView.setText(mValues.get(position).loginName);
        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    Log.d(TAG, "onClick() called with: v = [" + v + "]");
                    mListener.onListFragmentInteraction(holder.mItem);
                }else{
                    Log.i(TAG, "onClick: listener is null");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIdView;
        public final TextView mContentView;
        public final LinearLayout mParentLayout;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mParentLayout = view.findViewById(R.id.parentPanel);
            mIdView = (ImageView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.loginName);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
