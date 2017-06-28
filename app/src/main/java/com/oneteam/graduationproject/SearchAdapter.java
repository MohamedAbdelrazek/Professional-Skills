package com.oneteam.graduationproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Karim Gamal -PC on 6/25/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {

    private ArrayList<UserModel> mArrayList;
    private ArrayList<UserModel> mFilteredList;

    public SearchAdapter(ArrayList<UserModel> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_row, viewGroup, false);
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
        }
    }
}
