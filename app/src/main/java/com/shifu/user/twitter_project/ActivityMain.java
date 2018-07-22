package com.shifu.user.twitter_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.shifu.user.twitter_project.realm.Messages;
import com.shifu.user.twitter_project.realm.MessagesAuthor;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ActivityMain extends AppCompatActivity {

    /**
     * Old Fields
     */
    final static int ADD = 0;
    final static int EDIT = 1;
    final static int LOGIN = 2;

    private static RealmRVAdapter ra;
    private static RealmController rc;
    private static Menu menuMain;

    private Handler h = new Handler();

    static RealmController getRC() {
        return rc;
    }

    static RealmRVAdapter getRA() {
        return ra;
    }

    static void setRA(RealmRVAdapter ra) {
        ActivityMain.ra = ra;
    }

    /**
     *  New Fields
     */
    private final static int LIMIT = 50;
    private RVAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private Subscription mySubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MyMark", "activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        h = new Handler(new Handler.Callback() {
//            String TAG = "H.Main";
//
//            @Override
//            public boolean handleMessage(android.os.Message msg) {
//                Log.d(TAG, "Event type:"+Integer.toString(msg.what));
//                Log.d(TAG, "Event:"+msg.obj);
//                if (msg.what == 1) {
//                    switch ((String)msg.obj) {
//                        case "LOADED": case "RC.clear":
//                            getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .add(R.id.container, new FragmentList(), "START")
//                                    .commit();
//                            break;
//                    }
//                }
//                return false;
//            }
//        });

//        Toolbar myToolbar = findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

//        rc = new RealmController(getApplicationContext());

        init(this, findViewById(android.R.id.content), savedInstanceState);

//        final String TAG = "LOADED";

//        if(rc.getSize(MessagesAuthor.class) >0) {
//
//            Log.d(TAG,
//                    "State: " + rc.getItem(MessagesAuthor.class, null, null).getUsername()
//                    + " Msgs in cashe: " + rc.getSize(Messages.class)
//                    + " Msgs in adapter: " + ra.getItemCount());
//
//            MessagesAuthor user = rc.getItem(MessagesAuthor.class, null, null);
//            Bundle obj = new Bundle();
//            obj.putString("uid", user.getUid());
//            obj.putString("username", user.getUsername());
//            obj.putString("idToken", user.getIdToken());
//            obj.putString("refreshToken", user.getRefreshToken());
//
//            FirebaseController.loadMsgs(obj,h);
//            h.sendMessage(Message.obtain(h, 1, TAG));
//        } else {
//            Log.d(TAG, "State: logout");
//            rc.clear(h);
//        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        menuMain = menu;
//        menuMain.findItem(R.id.menu_add).setVisible(rc.getSize(MessagesAuthor.class) > 0);
//        return true;
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        Intent intent;
//
//        switch (item.getItemId()) {
//            case R.id.menu_add:
//                intent = new Intent(this, ActivityMsg.class);
//                intent.putExtra("requestCode", ADD);
//                startActivityForResult(intent, ADD);
//                return true;
//
//            case R.id.menu_profile:
//                intent = new Intent(this, ActivityLogin.class);
//                intent.putExtra("requestCode", LOGIN);
//                startActivityForResult(intent, LOGIN);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case ADD:
//                    String text = data.getStringExtra("text");
//                    rc.addMsg(text, new Date().getTime(), h);
//                    break;
//
//                case LOGIN:
//                    menuMain.findItem(R.id.menu_add).setVisible(rc.getSize(MessagesAuthor.class) > 0);
//                    break;
//
//            }
//        }
//    }

private void init(Context context, View view, Bundle savedInstanceState) {
    recyclerView = view.findViewById(R.id.recyclerView);

    GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(context, 1);
    recyclerViewLayoutManager.supportsPredictiveItemAnimations();

    if (savedInstanceState == null) {
        recyclerViewAdapter = new RVAdapter();
        recyclerViewAdapter.setHasStableIds(true);
    }
    recyclerView.setSaveEnabled(true);

    recyclerView.setLayoutManager(recyclerViewLayoutManager);
    recyclerView.setAdapter(recyclerViewAdapter);

    if (recyclerViewAdapter.isAllItemsLoaded()) {
        return;
    }

    // RecyclerView pagination
    RxTool<List<Item>> rxTool = new RxTool <>(recyclerView, offset -> EmulateResponseManager.getInstance(this).getEmulateResponse(offset, LIMIT), LIMIT);

    mySubscription = rxTool
            .getPagingObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<Item>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(List<Item> items) {
                    recyclerViewAdapter.addNewItems(items);
                    recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.getItemCount() - items.size());
                }
            });
}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mySubscription != null && !mySubscription.isUnsubscribed()) {
            mySubscription.unsubscribe();
        }

        // for memory leak prevention (RecycleView is not unsubscibed from adapter DataObserver)
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        super.onDestroy();
    }

}
