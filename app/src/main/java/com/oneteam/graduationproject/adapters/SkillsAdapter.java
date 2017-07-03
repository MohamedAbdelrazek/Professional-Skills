package com.oneteam.graduationproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneteam.graduationproject.R;
import com.oneteam.graduationproject.models.SkillModel;

import java.util.ArrayList;


public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {
    private onRecyclerLongClickListener mOnRecyclerLongClickListener;
    private ArrayList<SkillModel> mArrayList;

    public SkillsAdapter(ArrayList<SkillModel> arrayList, onRecyclerLongClickListener listener) {
        mArrayList = arrayList;
        mOnRecyclerLongClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_rate_skills, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.skill_name.setText(mArrayList.get(position).getSkillName());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView skill_name;


        public ViewHolder(View view) {
            super(view);
            skill_name = (TextView) view.findViewById(R.id.skill_txt_view);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnRecyclerLongClickListener.onClick(mArrayList.get(getAdapterPosition()).getSkillId());
                    return false;
                }
            });
        }
    }

    public interface onRecyclerLongClickListener {
        void onClick(int skillid);
    }
}

