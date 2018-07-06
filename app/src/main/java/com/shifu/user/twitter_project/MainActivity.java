package com.shifu.user.twitter_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

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

//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference myRef = database.getReference("items");
//
//        // Read from the database
//        //
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                new RealmController(context).addInfo(dataSnapshot);
//                mAdapter.setData(realm.where(Messages.class).findAll().sort("date"));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Log.w("FbDB", "Failed to read value.", error.toException());
//            }
//        });

        //Log.d("Loaded:", realm.where(Messages.class).findAll().sort("date").toString());
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        realmRVFragment = new RealmRVFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, realmRVFragment, "START")
                .commit();

        context = this;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://shifu-ad6cd.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JsonApi jsonApi = retrofit.create(JsonApi.class);
        jsonApi.loadMessages("messages.json").enqueue(new Callback<List<JsonItem>>() {

            @Override
            public void onResponse(Call<List<JsonItem>> call, Response<List<JsonItem>> response) {
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
            public void onFailure(Call<List<JsonItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });

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
