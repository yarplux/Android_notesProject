package com.shifu.user.twitter_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private RealmRVFragment realmRVFragment;
    private RealmRVAdapter mAdapter;
    private Realm realm;
    private Context context;

    public Realm getRealmDB() {return realm; }
    public RealmRVAdapter getRealmCustomAdapter() {return mAdapter;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        new RealmController(context).Clear();

        mAdapter =  new RealmRVAdapter(realm.where(Messages.class).findAll().sort("date"));

        context = this;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://vedmak.wikia.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JsonApi jsonApi = retrofit.create(JsonApi.class);
        jsonApi.loadTitles("Popular").enqueue(new Callback<JsonList>() {

            @Override
            public void onResponse(Call<JsonList> call, Response<JsonList> response) {
                if (response.isSuccessful()) {
                    //Log.d("REST Response:", response.body().toString());
                    new RealmController(context).addInfo(response.body());
                    mAdapter.notifyDataSetChanged();

                    realmRVFragment = new RealmRVFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, realmRVFragment, "START")
                            .commit();
                } else {
                    Log.e("REST error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<JsonList> call, Throwable t) {
                t.printStackTrace();
            }
        });

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
