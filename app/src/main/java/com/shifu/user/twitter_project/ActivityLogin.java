package com.shifu.user.twitter_project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
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
import android.widget.AutoCompleteTextView;
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
    private AutoCompleteTextView mLoginView;
    private EditText mPasswordView;
    private Button mLeftButton;
    private Button mRightButton;
    private View mProgressView;
    private View mLoginFormView;

    // UI alternative
    private Button mButton;

    private boolean signInState = true;
    private boolean login;

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

                        case "RC.addMsgs":
                            ActivityMain.setRA(new RealmRVAdapter(rc.getBase(Messages.class, "date")));
                            ActivityMain.getRA().notifyDataSetChanged();
                            break;

                        case "RC.changeUser":
                            new FirebaseController(URL_DATABASE, h).loadMsgs();
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
                            mLoginView.setError(getString(R.string.error_login_noexists));
                            mLoginView.requestFocus();
                            break;

                        case "INVALID_PASSWORD":
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                            break;

                        default:
                            Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                return false;
            }
        });

        login = rc.getSize(MessagesAuthor.class) >0;

        mButton = findViewById(R.id.logout_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
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

    }

    private void attemptLogin() {
        String user = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (check(user, password)) {
            showProgress(true);
            new FirebaseController(URL_AUTH, h).login(user, password, !signInState);
        }
    }

    private boolean check(String user, String password) {
        if (mProgressView.getVisibility() == View.VISIBLE) {
            return false;
        }

        mLoginView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(user)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isLoginValid(user)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        return !cancel;
    }

    private boolean isLoginValid(String login) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
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
                setResult(RESULT_OK,  getIntent());
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
