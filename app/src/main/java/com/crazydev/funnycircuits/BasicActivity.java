package com.crazydev.funnycircuits;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;

import com.crazydev.funnycircuits.io.BobenusFile;
import com.crazydev.funnycircuits.io.IOManager;
import com.crazydev.funnycircuits.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FilenameFilter;

public abstract class BasicActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public static final String INTENT_KEY = "FolderName";

    protected Toolbar        toolbar;
    protected TextView       toolbarHeader;
    protected LayoutInflater inflater;
    protected GridLayout     gridLayout;

    protected String folderName = "";

    private String[] items = {"Rename", "Delete"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.inflater      = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.toolbarHeader = (TextView)   findViewById(R.id.toolbar_title);
        this.gridLayout    = (GridLayout) findViewById(R.id.grid_layout);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

             Intent intent = new Intent(BasicActivity.this, SchemaActivity.class);
             startActivityForResult(intent, Constants.REQUEST_CODE_SCHEMA_ACTIVITY);

            }
        });

        findViewById(R.id.create_folder).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                BasicActivity.this.showCreateFolderDialog();
            }

        });

        this.outputFolderAndFiles();

    }

    protected void showCreateFolderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Type file name");
        View customLayout = getLayoutInflater().inflate(R.layout.create_folder_dialog, null);
        builder.setView(customLayout);

        final EditText editText = customLayout.findViewById(R.id.editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String folderName = editText.getText().toString();
                if (IOManager.createDir(BasicActivity.this.getFilesDir().toString() + BasicActivity.this.folderName + "/" + folderName)) {
                    BasicActivity.this.addFolderToMenu(folderName);
                }

                BasicActivity.this.hideKeyboard(editText);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                BasicActivity.this.hideKeyboard(editText);
            }

        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

        this.showKeyboard();
    }

    protected void outputFolderAndFiles() {

        this.gridLayout.removeAllViews();

        File file = new File(this.getFilesDir().toString() + this.folderName);

        String[] directories = file.list(new FilenameFilter() {

            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }

        });

        for (String directory : directories) {
            File folder = new File(directory);
            this.addFolderToMenu(folder.getName());
        }

        //    Log.d("taggg", Arrays.toString(directories) + " dfgdf");
    }

    protected void addFolderToMenu(String folder) {

        int display_mode = getResources().getConfiguration().orientation;
        LinearLayout linlay = (LinearLayout) inflater.inflate(R.layout.gridmenuitem_main, null);
        GridLayout.LayoutParams itemParams = new GridLayout.LayoutParams();

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        TextView fileName = (TextView) linlay.findViewById(R.id.file_name);
        fileName.setText(folder);

        switch(display_mode) {
            case Configuration.ORIENTATION_PORTRAIT:
                itemParams.width  = (int)(screenWidth  / 3.0);
                itemParams.height = (int)(screenHeight / 4.2);
                this.gridLayout.setColumnCount(3);
                fileName.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                itemParams.width  = (int)(screenWidth  / 5.0);
                itemParams.height = (int)(screenHeight / 2.0);
                this.gridLayout.setColumnCount(5);
                fileName.setTextSize(TypedValue.COMPLEX_UNIT_PT, 5);
                break;

            default:
                // pizdets
        }

        linlay.setLayoutParams(itemParams);



        linlay.setOnClickListener(this);
        linlay.setOnLongClickListener(this);

        this.gridLayout.addView(linlay);
    }

    protected void showFolderSettings(final String fullFolderPath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        break;
                    case 1:

                        File file = new BobenusFile(fullFolderPath);
                        file.delete();
                        BasicActivity.this.outputFolderAndFiles();

                        break;

                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void showKeyboard() {
        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    protected void hideKeyboard() {

        View v  = this.getCurrentFocus();

        if (v != null) {
            IBinder binder = v.getWindowToken();
            if (binder != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binder, 0);

            }
        }
    }

    protected void hideKeyboard(View v) {

        if (v != null) {
            IBinder binder = v.getWindowToken();
            if (binder != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binder, 0);

            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof LinearLayout) {
            LinearLayout linlay = (LinearLayout) view;

            TextView tvFileName = linlay.findViewById(R.id.file_name);
            String fileName = tvFileName.getText().toString();

      //      Log.d("taggg", "IT IS! onclick " + fileName);

            Intent intent = new Intent(getBaseContext(), FolderActivity.class);
            intent.putExtra(BasicActivity.INTENT_KEY, this.folderName + "/" + fileName);
            startActivity(intent);

        }

    }

    @Override
    public boolean onLongClick(View view) {
        if (view instanceof LinearLayout) {

            LinearLayout linlay = (LinearLayout) view;

            TextView tvFileName = linlay.findViewById(R.id.file_name);
            String fileName     = tvFileName.getText().toString();
            String fullFilePath = this.getFilesDir().toString() + this.folderName + "/" + fileName;
            File file           = new File(fullFilePath);

            if (file.isDirectory()) {
                this.showFolderSettings(fullFilePath);

            } else {

            }

            Log.d("taggg", "IT IS! onlongclick");

        }

        return false;
    }

}
