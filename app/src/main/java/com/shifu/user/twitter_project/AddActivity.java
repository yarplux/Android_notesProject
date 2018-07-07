package com.shifu.user.twitter_project;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    Button savebutton;
    Button canselbutton;
    EditText text;
    Intent intent;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

//        final View view = getWindow().getDecorView();
//        final WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
//
//        lp.gravity = Gravity.CENTER;
//        getWindowManager().updateViewLayout(view, lp);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Creates the layout for the window and the look of it
//        requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        // Params for the window.
//        // You can easily set the alpha and the dim behind the window from here
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.alpha = 1.0f;    // lower than one makes it more transparent
//        params.dimAmount = 1f;  // set it higher if you want to dim behind the window
//        getWindow().setAttributes(params);

        // Gets the display size so that you can set the window to a percent of that
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        // You could also easily used an integer value from the shared preferences to set the percent
        if (height > width) {
            getWindow().setLayout((int) (width * .9), (int) (height * .7));
        } else {
            getWindow().setLayout((int) (width * .7), (int) (height * .8));
        }

        setContentView(R.layout.activity_add);

        savebutton = findViewById(R.id.button_save);
        canselbutton = findViewById(R.id.button_cansel);
        text = findViewById(R.id.add_text);

        intent = getIntent();
        if (intent.getStringExtra("text") != null) {
            text.setText(intent.getStringExtra("text"));
            savebutton.setText(getResources().getString(R.string.save));
        }

        canselbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (text.getText().length() > 0) {
                    intent.putExtra("text", text.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
    }
}
