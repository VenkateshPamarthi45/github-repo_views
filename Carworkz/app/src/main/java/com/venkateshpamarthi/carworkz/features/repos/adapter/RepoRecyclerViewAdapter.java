package com.venkateshpamarthi.carworkz.features.repos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.venkateshpamarthi.carworkz.R;
import com.venkateshpamarthi.carworkz.features.repos.list.RepoFragment.OnListFragmentInteractionListener;
import com.venkateshpamarthi.carworkz.features.repos.model.RepoManager;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RepoManager.Repo} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RepoRecyclerViewAdapter extends RecyclerView.Adapter<RepoRecyclerViewAdapter.ViewHolder> {

    private final List<RepoManager.Repo> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    private String mLoginName;

    public RepoRecyclerViewAdapter(Context context, List<RepoManager.Repo> items, OnListFragmentInteractionListener listener, String mLoginName) {
        this.context = context;
        mValues = items;
        mListener = listener;
        this.mLoginName = mLoginName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_repo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).name);
        holder.mContentView.setText(mValues.get(position).description);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, mLoginName);
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
        public final TextView mIdView;
        public final TextView mContentView;
        public RepoManager.Repo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
