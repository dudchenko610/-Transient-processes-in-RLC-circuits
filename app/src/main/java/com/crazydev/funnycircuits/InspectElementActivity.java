package com.crazydev.funnycircuits;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class InspectElementActivity extends AppCompatActivity {

    private EditText labelEdit;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_element);

        this.labelEdit = (EditText) findViewById(R.id.label_edit);
        Intent intent = this.getIntent();
        this.labelEdit.setText(intent.getStringExtra("label"));

        this.okButton = (Button) findViewById(R.id.ok);



        this.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent intent = new Intent();
                intent.putExtra("label", labelEdit.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void hideKeyboard() {

        View v  = this.getCurrentFocus();

        if (v != null) {
            IBinder binder = v.getWindowToken();
            if (binder != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binder, 0);
            }
        }
    }

}
