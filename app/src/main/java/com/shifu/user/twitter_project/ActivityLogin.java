package com.shifu.user.twitter_project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.shifu.user.twitter_project.ActivityMain.URL_AUTH;
import static com.shifu.user.twitter_project.ActivityMain.URL_DATABASE;

public class ActivityLogin extends AppCompatActivity {


    private Handler h;
    private Integer counter = 0;

    // UI references.
    private EditText mLoginView;
    private EditText mPasswordView;
    private Button mLeftButton;
    private Button mRightButton;
    private View mProgressView;
    private View mLoginFormView;

    // UI alternative

    private EditText mNewLoginView;
    private EditText mNewPassView;

    private boolean signInState = true;
    private boolean login;

    private int waitUpdate = 0;

    private static RealmController rc = ActivityMain.getRC();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        h = new Handler(new Handler.Callback() {
            String TAG = "H.Login";
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Log.d(TAG, "Event type:"+Integer.toString(msg.what));
                if (msg.what == 1) {
                    Log.d(TAG, "Event:"+msg.obj);
                    switch((String) msg.obj) {
                        case "RC.clear":
                            counter++;
                            if (counter == 3) {
                                counter = 0;
                                clearState();
                                mLeftButton.setText(R.string.action_sign_in);
                                mRightButton.setText(R.string.action_sign_up_prepare);
                                signInState=true;

                                login = false;
                                showProgress(false);
                            }
                            break;

                        case "FC.updatePass":
                            waitUpdate--;
                            break;

                        case "RC.changeUserName":
                            Log.d(TAG, "Username now:"+ActivityMain.getRC().getItem(MessagesAuthor.class, null, null).getUsername());
                            waitUpdate--;
                        case "RC.addMsgs":
                            ActivityMain.setRA(new RealmRVAdapter(rc.getBase(Messages.class, "date")));
                            ActivityMain.getRA().notifyDataSetChanged();
                            break;

                        case "RC.changeUser":
                            new FirebaseController(URL_DATABASE, h).loadMsgs(new Auth(rc.getItem(MessagesAuthor.class, null, null)));
                            break;

                        default:
                            break;
                    }
                } else if (msg.what == 2) {
                    rc.changeUser((Auth) msg.obj, h);
                    login = true;
                    showProgress(false);
                } else if (msg.what == 3) {
                    new FirebaseController(URL_DATABASE, h).pushUser((Auth)msg.obj);
                } else if (msg.what == 0){
                    Log.d(TAG, "Event:"+msg.obj);
                    showProgress(false);
                    switch ((String) msg.obj) {
                        case "EMAIL_EXISTS":
                            mLoginView.setError(getString(R.string.error_login_exists));
                            mLoginView.requestFocus();
                            break;

                        case "EMAIL_NOT_FOUND":
                            mLoginView.setError(getString(R.string.error_login_no_exists));
                            mLoginView.requestFocus();
                            break;

                        case "INVALID_PASSWORD":
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                            break;
                        case "CREDENTIAL_TOO_OLD_LOGIN_AGAIN":
                            Toast.makeText(getApplicationContext(), getString(R.string.error_old_credentials), Toast.LENGTH_SHORT).show();
                        default:
                            //Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else if (msg.what == 7) {
                    Log.d(TAG, "Base now:"+ActivityMain.getRC().getSize(MessagesAuthor.class));
                    Log.d(TAG, "Username now:"+ActivityMain.getRC().getItem(MessagesAuthor.class, MessagesAuthor.FIELD_ID, msg.obj).getUsername());
                }

                if (waitUpdate == 0) showProgress(false);
                return false;
            }
        });

        login = rc.getSize(MessagesAuthor.class) >0;

        Button mButtonLogout = findViewById(R.id.logout_button);
        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        mNewLoginView = findViewById(R.id.new_name);
        mNewPassView = findViewById(R.id.new_password);

        Button mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptUpdate();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginView = findViewById(R.id.login);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLeftButton = findViewById(R.id.left_login_button);
        mLeftButton.setText(R.string.action_sign_in);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signInState) {
                    attemptLogin();
                } else {
                    changeState();
                }
            }
        });

        mRightButton = findViewById(R.id.right_login_button);
        mRightButton.setText(R.string.action_sign_up_prepare);
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signInState) {
                    changeState();
                } else {
                    attemptLogin();
                }
            }
        });

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        mLoginFormView = (login) ? findViewById(R.id.logout_form) : findViewById(R.id.login_form);
        mLoginFormView.setVisibility(View.VISIBLE);
        mProgressView = findViewById(R.id.login_progress);



    }

    private void logout() {
        showProgress(true);
        rc.clear(MessagesAuthor.class, h);
        rc.clear(MessagesUsers.class, h);
        rc.clear(Messages.class, h);

    }

    private void changeState() {
        showProgress(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
            }
        }, 300);
        clearState();

        mLeftButton.setText((signInState)?R.string.action_back:R.string.action_sign_in);
        mRightButton.setText((signInState)?R.string.action_sign_up:R.string.action_sign_up_prepare);
        signInState = !signInState;
    }

    private void clearState() {
        mLoginView.setText("");
        mLoginView.setError(null);

        mPasswordView.setText("");
        mPasswordView.setError(null);

        mNewLoginView.setError(null);
        mNewLoginView.setText("");

        mNewPassView.setError(null);
        mNewPassView.setText("");
    }

    private void attemptLogin() {
            if (mProgressView.getVisibility() == View.VISIBLE) return;

            mLoginView.setError(null);
            mPasswordView.setError(null);

            boolean cancel = false;
            View focusView = null;

            if (!TextUtils.isEmpty(mPasswordView.getText().toString()) && !isPasswordValid(mPasswordView)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            if (TextUtils.isEmpty(mLoginView.getText().toString())) {
                mLoginView.setError(getString(R.string.error_field_required));
                focusView = mLoginView;
                cancel = true;
            } else if (!isLoginValid(mLoginView)) {
                mLoginView.setError(getString(R.string.error_invalid_login));
                focusView = mLoginView;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {
                showProgress(true);
                new FirebaseController(URL_AUTH, h).login(
                        mLoginView.getText().toString(),
                        mPasswordView.getText().toString(),
                        !signInState);
            }
    }

    private void attemptUpdate(){

        if (!TextUtils.isEmpty(mNewPassView.getText().toString()) && !isPasswordValid(mNewPassView)) {
            showProgress(true);
            waitUpdate++;
            String password = mNewPassView.getText().toString();
            String idToken = ActivityMain.getRC().getItem(MessagesAuthor.class, null, null).getIdToken();
            new FirebaseController(ActivityMain.URL_AUTH, h).updatePass(password,idToken);
        } else if (!TextUtils.isEmpty(mNewPassView.getText().toString())){
            mNewPassView.setError(getString(R.string.error_invalid_password));
            mNewPassView.requestFocus();
        }

        if (!isLoginValid(mNewLoginView)) {
            mNewLoginView.setError(getString(R.string.error_invalid_login));
            mNewLoginView.requestFocus();
        } else {
             showProgress(true);
             waitUpdate++;
             String username = mNewLoginView.getText().toString();
             String idToken = ActivityMain.getRC().getItem(MessagesAuthor.class, null, null).getIdToken();
             String refresh = ActivityMain.getRC().getItem(MessagesAuthor.class, null, null).getRefreshToken();
             String uid = ActivityMain.getRC().getItem(MessagesAuthor.class, null, null).getUid();
             new FirebaseController(ActivityMain.URL_AUTH, h).updateName(new Auth(username, uid, idToken, refresh));
        }
    }


    private boolean isLoginValid(EditText loginView) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(EditText passView) {
        //TODO: Replace this with your own logic
        return passView.getText().toString().length() > 6;
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView = (login) ? findViewById(R.id.logout_form) : findViewById(R.id.login_form);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                clearState();
                setResult(RESULT_OK,  getIntent());
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        clearState();
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
