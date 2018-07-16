package com.shifu.user.twitter_project;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;
import static com.shifu.user.twitter_project.ActivityMain.EDIT;

public class FragmentList extends Fragment {

    private static RealmRVAdapter ra;
    private static RealmController rc;

    private Handler h;

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rc = ActivityMain.getRC();

        h = new Handler(new Handler.Callback() {
            String TAG = "H.List";
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Log.d(TAG, "Event type:"+Integer.toString(msg.what));
                if (msg.what <= 1) {
                    Log.d(TAG, "Event:"+msg.obj);
                    //Toast.makeText(getContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fr_msg_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ra = ActivityMain.getRA();
        mRecyclerView.setAdapter(ra);
        final SwipeController swipeController = new SwipeController(getActivity(), new SwipeControllerActions() {
            @Override
            public void onDelete(final int position) {
                rc.removeItemById(Messages.class, ra.getItem(position).getID(), h);
            }

            @Override
            public void onEdit(final int position) {
                Intent intent = new Intent(getActivity(), ActivityMsg.class);
                Messages updateRow = rc.getItem(Messages.class, Messages.FIELD_ID, ra.getItem(position).getID());
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

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case EDIT:
                    String text = data.getStringExtra("text");
                    Integer position = data.getIntExtra("position", -1);
                    rc.changeMsg(ra.getItem(position).getID(), text, h);
                    break;
            }
        }
    }

}
