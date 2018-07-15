package com.shifu.user.twitter_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Date;

public class ActivityMain extends AppCompatActivity {

    final static int ADD = 0;
    final static int EDIT = 1;
    final static int LOGIN = 2;

    final static String URL_AUTH = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/";
    final static String URL_REFRESH = "https://securetoken.googleapis.com/v1/";
    final static String URL_DATABASE = "https://shifu-ad6cd.firebaseio.com/";

    private static RealmRVAdapter mAdapter;
    private static RealmController rc;
    private static Menu menuMain;

    private FragmentList msgFragment;
    private Handler h = new Handler();

    static RealmController getRC() {
        return rc;
    }

    static RealmRVAdapter getRA() {
        return  mAdapter;
    }

    static void setRA(RealmRVAdapter ra) {
        mAdapter = ra;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Main", "Created");

        h = new Handler(new Handler.Callback() {
            String TAG = "H.Main";

            @Override
            public boolean handleMessage(android.os.Message msg) {
                Log.d(TAG, "Event type:"+Integer.toString(msg.what));
                if (msg.what == 1) {
                    Log.d(TAG, "Event:"+msg.obj);
                    switch((String) msg.obj) {
                        case "RC.addMsgs":
                            mAdapter.notifyDataSetChanged();
                            break;

                        default:
                            break;
                    }
                } else if (msg.what == 4) {
                    mAdapter.notifyDataSetChanged();
                    new FirebaseController(URL_DATABASE, h).pushMsg((String) msg.obj);
                } else if (msg.what == 0) {
                    Log.d(TAG, "Event:"+msg.obj);
                    //Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        rc = new RealmController(getApplicationContext());
        mAdapter =  new RealmRVAdapter(rc.getBase(Messages.class, "date"));

        if(rc.getSize(MessagesAuthor.class) >0) {
            Log.d("LOGIN STATE:", rc.getItem(MessagesAuthor.class, null, null).getUsername());
            msgFragment = new FragmentList();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, msgFragment, "START")
                    .commit();

            new FirebaseController(URL_DATABASE, h).loadMsgs(new Auth(rc.getItem(MessagesAuthor.class, null, null)));

        } else {
            Log.d("LOGIN STATE:", "false");
            rc.clear(Messages.class, h);
            rc.clear(MessagesUsers.class, h);
            rc.clear(MessagesAuthor.class, h);
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

//            case R.id.menu_authors:
//                firebaseController.loadUsers(username, h);
//                return true;

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

//                case AUTHOR:
//                    username = data.getStringExtra("text");
//                    break;

                case LOGIN:
                    // TODO нужен ли?
//                    mAdapter =  new RealmRVAdapter(rc.getBase(Messages.class, "date"));
//                    mAdapter.notifyDataSetChanged();
                    menuMain.findItem(R.id.menu_add).setVisible(rc.getSize(MessagesAuthor.class) > 0);
                    break;

            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Main", "Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Main", "Destroed");
        rc.destroy();
        rc=null;
    }

}
