package com.shifu.user.twitter_project;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RealmRVAdapter extends RealmRecyclerViewAdapter<Messages, RealmRVAdapter.ViewHolder> {

    private final static String date_format = "HH:mm:ss dd.MM.yyyy";
    private static String username;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView text, date, author;
        public Messages data;

        ViewHolder(View v) {
            super(v);
            text = v.findViewById(R.id.msg_text);
            date = v.findViewById(R.id.msg_date);
            author = v.findViewById(R.id.msg_author);
        }
    }

    RealmRVAdapter(OrderedRealmCollection<Messages> data) {
        super(data, true);
        setHasStableIds(true);
        username = ActivityMain.getRC().getItem(MessagesAuthor.class, null, null).getUsername();
        Log.d("RA", "Set username:"+username);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fr_msg, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, int position) {
        final Messages obj = getItem(position);
        viewHolder.data = obj;
        viewHolder.text.setText(obj.getText());
        viewHolder.date.setText(new SimpleDateFormat(date_format, Locale.US).format(new Date(obj.getDate())));
        if (obj.getRetwitted() == null || obj.getRetwitted().equals("")) {
            viewHolder.author.setText(username);
        } //else {
//            viewHolder.author.setText(FragmentList.activity.getResources()
//                            .getString(R.string.retwitUid, ""));
//        }
        }

    @Override
    public long getItemId(int index) {
        return getItem(index).getDate();
    }

}
