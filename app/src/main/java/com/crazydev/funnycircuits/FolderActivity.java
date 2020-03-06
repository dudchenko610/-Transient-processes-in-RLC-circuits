package com.crazydev.funnycircuits;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.crazydev.funnycircuits.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FolderActivity extends BasicActivity {

    private TextView toolbarHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.folderName = getIntent().getStringExtra(BasicActivity.INTENT_KEY);
        super.onCreate(savedInstanceState);

      //  Log.d("taggg", "oncreate folderactivty " + this.folderName);

        String arr [] = this.folderName.split("/");

        this.toolbarHeader = (TextView) findViewById(R.id.toolbar_title);

        if (arr.length != 0) {
            this.toolbarHeader.setText(arr[arr.length - 1]);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
