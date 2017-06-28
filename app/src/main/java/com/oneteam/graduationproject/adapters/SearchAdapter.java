package com.oneteam.graduationproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.oneteam.graduationproject.R;
import com.oneteam.graduationproject.models.UserModel;

import java.util.ArrayList;

/**
 * Created by Karim Gamal -PC on 6/25/2017.
 */


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private onRecyclerClickListener mOnRecyclerClickListener;
    private ArrayList<UserModel> mArrayList;
    private ArrayList<UserModel> mFilteredList;

    public SearchAdapter(ArrayList<UserModel> arrayList, onRecyclerClickListener onRecyclerClickListener) {
        mArrayList = arrayList;
        mFilteredList = arrayList;

        mOnRecyclerClickListener = onRecyclerClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_for_research_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tv_email.setText(mFilteredList.get(i).getEmailAddress());
        viewHolder.tv_phone.setText(mFilteredList.get(i).getMobileNumber());
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<UserModel> filteredList = new ArrayList<>();

                    for (UserModel userModel : mArrayList) {

                        if (userModel.getFirstName().toLowerCase().contains(charString) ||
                                userModel.getLastName().toLowerCase().contains(charString) ||
                                userModel.getMobileNumber().contains(charString) ||
                                userModel.getEmailAddress().toLowerCase().contains(charString)) {

                            filteredList.add(userModel);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<UserModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_email, tv_phone;

        public ViewHolder(View view) {
            super(view);
            tv_email = (TextView) view.findViewById(R.id.tv_email);
            tv_phone = (TextView) view.findViewById(R.id.tv_phone);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnRecyclerClickListener.onClick(mFilteredList.get(getAdapterPosition()).getEmailAddress());
                }
            });
        }
    }

   public interface onRecyclerClickListener {
        void onClick(String username);
    }
}
