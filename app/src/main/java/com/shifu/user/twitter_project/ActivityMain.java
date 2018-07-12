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
import android.view.View;
import android.widget.Button;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ActivityMain extends AppCompatActivity {

    final static int LOGIN = 3;
    final static int AUTHOR = 2;
    final static int ADD = 0;
    final static int EDIT = 1;

    final static int MESSAGES = 10;
    final static int USERS = 11;

    private final String URL_AUTH = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/";

    private FragmentList msgFragment, userFragment;
    RealmRVAdapter mAdapter;
    RealmRVAdapterUsers uAdapter;
    private Realm realm;

    private FirebaseController firebaseController;
    private ActivityMain activity = this;

    private boolean loginState;
    private String username;
    private Handler h = new Handler();

    private Button itemProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, ActivityMsg.class);
//        intent.putExtra("requestCode", AUTHOR);
//        startActivityForResult(intent, AUTHOR);

//        firebaseController = new FirebaseController(getApplicationContext(), mAdapter);
//        firebaseController.getToken();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        username = "test";
        loginState = false;

        Log.d("LOGIN BASE NOTES:", Long.toString(realm.where(MessagesAuthor.class).count()));

        mAdapter =  new RealmRVAdapter(realm.where(Messages.class).findAll().sort("date"));
        uAdapter =  new RealmRVAdapterUsers(realm.where(MessagesUsers.class).findAll().sort("username"));

        RealmController rc = new RealmController(getApplicationContext(), h);
        rc.changeUser(username);


        Bundle bundle = new Bundle();
        bundle.putInt("state", MESSAGES);
        msgFragment = new FragmentList();
        msgFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, msgFragment, "START")
                .commit();

        firebaseController = new FirebaseController(getApplicationContext(), mAdapter);
        firebaseController.loadMessages(username);

        //realm = Realm.getDefaultInstance();

//        h = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(android.os.Message msg) {
//                Log.d("Handler", Integer.toString(msg.what));
//                    if (msg.what == 1) {
//                        uAdapter.notifyDataSetChanged();
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("state", USERS);
//                        userFragment = new FragmentList();
//                        userFragment.setArguments(bundle);
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.container, userFragment, "USERS")
//                                .addToBackStack(null)
//                                .commit();
//                }
//                return false;
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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
                intent.putExtra("login", loginState);
                intent.putExtra("URL", URL_AUTH);
                intent.putExtra("requestCode", LOGIN);
                startActivityForResult(intent, LOGIN);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD:
                    String text = data.getStringExtra("text");
                    firebaseController.pushMessage(text, new Date().getTime(), username);
                    break;

//                case AUTHOR:
//                    username = data.getStringExtra("text");
//                    break;

                case LOGIN:
                    loginState = data.getBooleanExtra("login", true);
                    Log.d("GET Login", Boolean.toString(loginState));

//                    username = data.getStringExtra("username");
//                    Log.d("Loaded username", username);
//
//                    mAdapter =  new RealmRVAdapter(realm.where(Messages.class).findAll().sort("date"));
//                    uAdapter =  new RealmRVAdapterUsers(realm.where(MessagesUsers.class).findAll().sort("username"));
//
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("state", MESSAGES);
//                    msgFragment = new FragmentList();
//                    msgFragment.setArguments(bundle);
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .add(R.id.container, msgFragment, "START")
//                            .commit();
//
//                    firebaseController = new FirebaseController(getApplicationContext(), mAdapter);
//                    firebaseController.loadMessages(username);

                    break;
            }
        }
    }


    public Realm getRealmDB() {
        return realm;
    }

    public FirebaseController getFirebaseController() {
        return firebaseController;
    }

}
