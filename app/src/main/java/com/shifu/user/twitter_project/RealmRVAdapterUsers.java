package com.shifu.user.twitter_project;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RealmRVAdapterUsers extends RealmRecyclerViewAdapter<MessagesUsers, RealmRVAdapterUsers.ViewHolder> {

    private final String date_format = "HH:mm:ss dd.MM.yyyy";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView name;
        public MessagesUsers data;

        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.author_text);
        }
    }

    public RealmRVAdapterUsers(OrderedRealmCollection<MessagesUsers> data) {
        super(data, true);
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fr_author, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final MessagesUsers obj = getItem(position);
        viewHolder.data = obj;
        viewHolder.name.setText(obj.getUsername());
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

}
