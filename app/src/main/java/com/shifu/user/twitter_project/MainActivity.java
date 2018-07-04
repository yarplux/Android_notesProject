package com.shifu.user.twitter_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private RealmRVFragment realmRVFragment;
    private RealmRVAdapter mAdapter;
    private Realm realm;

    public Realm getRealmDB() {return realm; }
    public RealmRVAdapter getRealmCustomAdapter() {return mAdapter;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);

        //realm = Realm.getDefaultInstance();
        //new RealmController(context).Clear();

        mAdapter =  new RealmRVAdapter(realm.where(Messages.class).findAll().sort("date"));

        //Log.d("Loaded:", realm.where(Messages.class).findAll().sort("date").toString());
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        realmRVFragment = new RealmRVFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, realmRVFragment, "START")
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add:
                Intent intent = new Intent(this, AddActivity.class);
                startActivityForResult(intent, 0);
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
                case 0:
                    String text = data.getStringExtra("text");
                    realmRVFragment.addItem(text);
                    break;
            }
        }
    }

}
