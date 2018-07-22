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

import com.shifu.user.twitter_project.realm.MessagesAuthor;

import java.util.HashMap;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity {

    private static final Map<String, String> Errors=new HashMap <>();
    static
    {
        Errors.put("EMAIL_EXISTS", "Такой пользователь уже существует!");
        Errors.put("EMAIL_NOT_FOUND", "Пользователь не существует!");
        Errors.put("INVALID_PASSWORD", "Пароль некорректен!");
        Errors.put("CREDENTIAL_TOO_OLD_LOGIN_AGAIN", "Чтобы изменить параметры вашего профелия, Вам нужно выйти и войти заново");
    }


    private Handler h;

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

    private RealmController rc = ActivityMain.getRC();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        h = new Handler(new Handler.Callback() {
            String TAG = "H.Login";
            @Override
            public boolean handleMessage(android.os.Message msg) {
                Log.d(TAG, "Event type:"+Integer.toString(msg.what));
                Log.d(TAG, "Event:"+msg.obj);
                if (msg.what == 1) {
                    switch((String) msg.obj) {
                        case "RC.clear":
                            clearState();
                            mLeftButton.setText(R.string.action_sign_in);
                            mRightButton.setText(R.string.action_sign_up_prepare);
                            signInState=true;
                            login = false;
                            showProgress(false);
                            break;

                        case "RC.changeToken":
                            //TODO почему-то не очеищается поле с паролем

                        case "RC.changeUserName":
                            clearState();
                            showProgress(false);
                            break;

                        default:
                            break;
                    }
                } else if (msg.what == 2) {
                    login = true;
                    showProgress(false);
                } else if (msg.what == 0){
                    msg.what = 0;
                    showProgress(false);
                    String str = Errors.get((String)msg.obj);
                    if (str != null) {
                        Log.d(TAG, "error note:"+str);
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    }
                }
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
                if (mNewLoginView.getText().length() > 0) attemptUser();
                if (mNewPassView.getText().length() > 0) attemptPass();
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
        rc.clear(h);
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

            String pass = mPasswordView.getText().toString();
            String name = mLoginView.getText().toString();

            boolean cancel = false;
            View focusView = null;

            if (!TextUtils.isEmpty(pass) && !isPasswordValid(mPasswordView)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            if (TextUtils.isEmpty(name)) {
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
                FirebaseController.login(name, pass, !signInState, h);
            }
    }

    private void attemptPass(){
        if (isPasswordValid(mNewPassView)) {
            //TODO доделать оформление ожидания, если будет время
            //showProgress(true);

            MessagesAuthor user = rc.getItem(MessagesAuthor.class, null, null);
            Bundle obj = new Bundle();
            obj.putString("password", mNewPassView.getText().toString());
            obj.putString("idToken", user.getIdToken());
            FirebaseController.updatePass(obj, h);
        } else {
            mNewPassView.setError(getString(R.string.error_invalid_password));
            mNewPassView.requestFocus();
        }
    }

    private void attemptUser() {
        if (isLoginValid(mNewLoginView)) {
            //TODO доделать оформление ожидания, если будет время
            //showProgress(true);

            MessagesAuthor user = rc.getItem(MessagesAuthor.class, null, null);
            Bundle obj = new Bundle();
            obj.putString("username", mNewLoginView.getText().toString());
            obj.putString("idToken", user.getIdToken());

            FirebaseController.updateName(obj, h);
        } else {
            mNewLoginView.setError(getString(R.string.error_invalid_login));
            mNewLoginView.requestFocus();
        }

    }
    private boolean isLoginValid(EditText loginView) {
        //TODO: validate login
        return true;
    }

    private boolean isPasswordValid(EditText passView) {
        //TODO: validate password
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
