package com.shifu.user.notes_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import static com.shifu.user.notes_project.ActivityMain.ADD;
import static com.shifu.user.notes_project.ActivityMain.EDIT;

public class ActivityMsg extends AppCompatActivity {

    Button saveButton;
    Button canselButton;
    EditText text;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        saveButton = findViewById(R.id.button_save);
        canselButton = findViewById(R.id.button_cansel);
        text = findViewById(R.id.add_text);

        intent = getIntent();
        switch (intent.getIntExtra("requestCode", ADD)) {
            case ADD:
                saveButton.setText(R.string.action_add);
                break;
            case EDIT:
                text.setText(intent.getStringExtra("text"));
                saveButton.setText(R.string.action_save);
                break;
        }

        canselButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().length() > 0) {
                    intent.putExtra("text", text.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    text.setError(getString(R.string.error_msg_empty));
                    text.requestFocus();
                }
            }
        });
    }
}
