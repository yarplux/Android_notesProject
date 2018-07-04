//package com.shifu.user.twitter_project;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Canvas;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import io.realm.Realm;
//import io.realm.RealmConfiguration;
//
//import static android.app.Activity.RESULT_OK;
//import static com.shifu.user.twitter_project.Messages.FIELD_ID;
//
//public class RealmRVFragment extends Fragment {
//
//    private MainActivity activity;
//    private Realm realm;
//    protected RecyclerView mRecyclerView;
//    protected RealmRVAdapter mAdapter;
//    protected RecyclerView.LayoutManager mLayoutManager;
//    SwipeController swipeController = null;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        activity = (MainActivity)getActivity();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        realm = activity.getRealmDB();
//        mAdapter = activity.getRealmCustomAdapter();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.fr_msg_list, container, false);
//
//
//        mRecyclerView = rootView.findViewById(R.id.recyclerView);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//
//        swipeController = new SwipeController(getActivity(), new SwipeControllerActions() {
//            @Override
//            public void onDelete(final int position) {
//                //Log.d("Deleted:", Long.toString(mAdapter.getItem(position).getID()));
//                new RealmController(getContext()).removeItemById(mAdapter.getItem(position).getDate());
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onEdit(final int position) {
//                Intent intent = new Intent(getActivity(), AddActivity.class);
//                Messages updateRow = realm.where(Messages.class).equalTo(FIELD_ID, mAdapter.getItem(position).getDate()).findFirst();
//                intent.putExtra("text", updateRow.getText());
//                intent.putExtra("position", position);
//                startActivityForResult(intent, 1);
//            }
//        });
//
//
//        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(mRecyclerView);
//
//        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                swipeController.onDraw(c);
//            }
//        });
//        return rootView;
//    }
//
//
//    public SwipeController getSwipeController() {
//        return swipeController;
//    }
//
//    public void addItem(String text) {
//        new RealmController(this.getContext()).addInfo(text);
//
//        // DANGEROUS OPERATION, IF DB TOO MUCH!
//        int number = (int)(long) realm.where(Messages.class).count();
//        mAdapter.notifyItemInserted(number);
//    }
//
//    public void updateItem(int position, String text) {
//        new RealmController(this.getContext()).updateInfo(mAdapter.getItem(position).getDate(), text);
//        mAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case 1:
//                    String text = data.getStringExtra("text");
//                    Integer position = data.getIntExtra("position", -1);
//                    updateItem(position, text);
//                    break;
//            }
//        }
//    }
//
//}