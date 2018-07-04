//package com.shifu.user.twitter_project;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//
//public class AddActivity extends AppCompatActivity {
//
//    private Integer position;
//    Button savebutton;
//    EditText text;
//    Intent intent;
//
//
//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
////
////        if (getResources().getBoolean(R.bool.is_tablet) && mOpenAsSmallWindow) {
////            final View view = getWindow().getDecorView();
////            final WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
////
////            lp.gravity = Gravity.CENTER;
////
////            lp.width = mActivityWindowWidth;
////            lp.height = mActivityWindowHeight;
////            getWindowManager().updateViewLayout(view, lp);
////        }
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add);
//
//        savebutton = (Button) findViewById(R.id.button_save);
//        text = (EditText) findViewById(R.id.add_text);
//
//        intent = getIntent();
//        if (intent.getStringExtra("text") != null) {
//            text.setText(intent.getStringExtra("text"));
//            savebutton.setText(getResources().getString(R.string.msg_save));
//        }
//
//
//        savebutton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (text.getText().length() > 0) {
//                    intent.putExtra("text", text.getText().toString());
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
//                else {
//                    setResult(RESULT_CANCELED);
//                    finish();
//                }
//            }
//        });
//    }
//}
