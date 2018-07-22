package com.shifu.user.twitter_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.shifu.user.twitter_project.realm.Messages;
import com.shifu.user.twitter_project.realm.MessagesAuthor;

import java.util.Date;

public class ActivityMain extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MyMark", "activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        h = new Handler(new Handler.Callback() {
            String TAG = "H.Main";

            @Override
            public boolean handleMessage(android.os.Message msg) {
                Log.d(TAG, "Event type:"+Integer.toString(msg.what));
                Log.d(TAG, "Event:"+msg.obj);
                if (msg.what == 1) {
                    switch ((String)msg.obj) {
                        case "LOADED": case "RC.clear":
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.container, new FragmentList(), "START")
                                    .commit();
                            break;
                    }
                }
                return false;
            }
        });

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        rc = new RealmController(getApplicationContext());
        ra =  new RealmRVAdapter(rc.getBase(Messages.class, "date"));

        final String TAG = "LOADED";

        if(rc.getSize(MessagesAuthor.class) >0) {

            Log.d(TAG,
                    "State: " + rc.getItem(MessagesAuthor.class, null, null).getUsername()
                    + " Msgs in cashe: " + rc.getSize(Messages.class)
                    + " Msgs in adapter: " + ra.getItemCount());

            MessagesAuthor user = rc.getItem(MessagesAuthor.class, null, null);
            Bundle obj = new Bundle();
            obj.putString("uid", user.getUid());
            obj.putString("username", user.getUsername());
            obj.putString("idToken", user.getIdToken());
            obj.putString("refreshToken", user.getRefreshToken());

            FirebaseController.loadMsgs(obj,h);
            h.sendMessage(Message.obtain(h, 1, TAG));
        } else {
            Log.d(TAG, "State: logout");
            rc.clear(h);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menuMain = menu;
        menuMain.findItem(R.id.menu_add).setVisible(rc.getSize(MessagesAuthor.class) > 0);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.menu_add:
                intent = new Intent(this, ActivityMsg.class);
                intent.putExtra("requestCode", ADD);
                startActivityForResult(intent, ADD);
                return true;

            case R.id.menu_profile:
                intent = new Intent(this, ActivityLogin.class);
                intent.putExtra("requestCode", LOGIN);
                startActivityForResult(intent, LOGIN);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD:
                    String text = data.getStringExtra("text");
                    rc.addMsg(text, new Date().getTime(), h);
                    break;

                case LOGIN:
                    menuMain.findItem(R.id.menu_add).setVisible(rc.getSize(MessagesAuthor.class) > 0);
                    break;

            }
        }
    }
}
