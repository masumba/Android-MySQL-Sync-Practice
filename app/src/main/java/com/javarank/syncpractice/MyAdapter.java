package com.javarank.syncpractice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anik on 3/10/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PersonViewHolder> {

    private List<Person> personList;

    public MyAdapter() {
    }

    @Override
    public MyAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.PersonViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.fullNameTextView.setText(person.getFirstName()+" "+ person.getLastName());

        if( person.getSyncStatus() == DBContract.SYNC_STATUS_OK ) {
            holder.syncStatusImageView.setImageResource(R.drawable.check);
        } else {
            holder.syncStatusImageView.setImageResource(R.drawable.autorenew);
        }
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
        notifyDataSetChanged();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.full_name_text_view)
        TextView fullNameTextView;
        @BindView(R.id.sync_status_image_view)
        ImageView syncStatusImageView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            //ButterKnife.bind(itemView);
            fullNameTextView = (TextView) itemView.findViewById(R.id.full_name_text_view);
            syncStatusImageView = (ImageView) itemView.findViewById(R.id.sync_status_image_view);
        }
    }

}
