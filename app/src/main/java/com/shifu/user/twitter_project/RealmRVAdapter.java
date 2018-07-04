//package com.shifu.user.twitter_project;
//
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import io.realm.OrderedRealmCollection;
//import io.realm.RealmRecyclerViewAdapter;
//
//public class RealmRVAdapter extends RealmRecyclerViewAdapter<Messages, RealmRVAdapter.ViewHolder> {
//
//    private String date_format = "HH:mm:ss dd.MM.yyyy";
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        private  TextView text, date;
//        public Messages data;
//
//        public ViewHolder(View v) {
//            super(v);
//
//            text = (TextView) v.findViewById(R.id.msg_text);
//            date = (TextView) v.findViewById(R.id.msg_date);
//
//        }
//    }
//
//    public RealmRVAdapter(OrderedRealmCollection<Messages> data) {
//        super(data, true);
//        setHasStableIds(true);
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View v = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.fr_msg, viewGroup, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, int position) {
//        final Messages obj = getItem(position);
//        viewHolder.data = obj;
//        viewHolder.text.setText(obj.getText());
//        viewHolder.date.setText(new SimpleDateFormat(date_format, Locale.US).format(new Date(obj.getDate())));
//    }
//
//    public void setData(OrderedRealmCollection<Messages> data) {
//        updateData(data);
//    }
//
//    @Override
//    public long getItemId(int index) {
//        return getItem(index).getDate();
//    }
//
//}
