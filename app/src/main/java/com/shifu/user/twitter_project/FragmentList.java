package com.shifu.user.twitter_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;

import static android.app.Activity.RESULT_OK;
import static com.shifu.user.twitter_project.ActivityMain.MESSAGES;
import static com.shifu.user.twitter_project.ActivityMain.USERS;
import static com.shifu.user.twitter_project.Messages.FIELD_ID;
import static com.shifu.user.twitter_project.ActivityMain.EDIT;

public class FragmentList extends Fragment {

    public static ActivityMain activity;
    private Realm realm;
    protected RecyclerView mRecyclerView;
    protected RealmRVAdapter mAdapter;
    protected RealmRVAdapterUsers uAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    FirebaseController firebaseController = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ActivityMain)getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = activity.getRealmDB();
        firebaseController = activity.getFirebaseController();
        mAdapter=activity.mAdapter;
        uAdapter=activity.uAdapter;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fr_msg_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        setAdapter();
        return rootView;
    }

    public void setAdapter(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int state = bundle.getInt("state", 0);

            if (state == MESSAGES) {
                mRecyclerView.setAdapter(mAdapter);
                final SwipeController swipeController = new SwipeController(getActivity(), new SwipeControllerActions() {
                    @Override
                    public void onDelete(final int position) {
                        firebaseController.deleteMessage(position);
                    }

                    @Override
                    public void onEdit(final int position) {
                        Intent intent = new Intent(getActivity(), ActivityMsg.class);
                        Messages updateRow = realm.where(Messages.class).equalTo(FIELD_ID, mAdapter.getItem(position).getID()).findFirst();
                        intent.putExtra("text", updateRow.getText());
                        intent.putExtra("position", position);
                        intent.putExtra("requestCode", EDIT);
                        startActivityForResult(intent, EDIT);
                    }
                });


                ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
                itemTouchhelper.attachToRecyclerView(mRecyclerView);

                mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                        swipeController.onDraw(c);
                    }
                });
            } else if (state == USERS) {
                Log.d("Users in local DB:", realm.where(MessagesUsers.class).findAll().toString());
                mRecyclerView.setAdapter(uAdapter);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case EDIT:
                    String text = data.getStringExtra("text");
                    Integer position = data.getIntExtra("position", -1);
                    firebaseController.updateMessage(position, text);
                    break;
            }
        }
    }

}
