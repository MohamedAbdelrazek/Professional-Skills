package com.oneteam.graduationproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneteam.graduationproject.R;
import com.oneteam.graduationproject.models.QuestionModel;

import java.util.ArrayList;

/**
 * Created by Mohamed AbdelraZek on 6/28/2017.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private ArrayList<QuestionModel> mData;
    private onRecyclerClickListener mOnRecyclerClickListener;

    public PostsAdapter(ArrayList<QuestionModel> data, onRecyclerClickListener onRecyclerClickListener) {
        mOnRecyclerClickListener = onRecyclerClickListener;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_for_home_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.username.setText(mData.get(position).getAuthorName());
        holder.content.setText(mData.get(position).getContent());
        holder.title.setText(mData.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        if (mData.size() > 0) {
            return mData.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content, title, username;

        public ViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.user_name);
            content = (TextView) view.findViewById(R.id.content);
            title = (TextView) view.findViewById(R.id.title);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnRecyclerClickListener.onClick(mData.get(getAdapterPosition()));

                }
            });
        }
    }

    public interface onRecyclerClickListener {
        void onClick(QuestionModel questionModel);
    }
}
