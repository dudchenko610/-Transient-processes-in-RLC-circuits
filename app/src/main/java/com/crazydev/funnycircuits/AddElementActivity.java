package com.crazydev.funnycircuits;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;

public class AddElementActivity extends AppCompatActivity implements View.OnClickListener {

    private ScrollView scrollView;
    protected Toolbar toolbar;
    protected TextView toolbarHeader;
    protected GridLayout     gridLayout;
    protected LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_element);

        this.toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.inflater      = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.toolbarHeader = (TextView)   findViewById(R.id.toolbar_title);
        this.gridLayout    = (GridLayout) findViewById(R.id.grid_layout);

        this.toolbarHeader.setText("Add element");

        this.scrollView   = (ScrollView) findViewById(R.id.scroll_view);


        this.addElementToList("DC", R.drawable.dc1);        // 0
        this.addElementToList("Capacitor", R.drawable.c1);  // 1
        this.addElementToList("Resistor", R.drawable.r1);   // 2
        this.addElementToList("Inductor", R.drawable.i1);   // 3
    }

    protected void addElementToList(String elementName, int elementImage) {

        int display_mode = getResources().getConfiguration().orientation;
        LinearLayout linlay = (LinearLayout) inflater.inflate(R.layout.gridelementitem_addelement, null);
        GridLayout.LayoutParams itemParams = new GridLayout.LayoutParams();

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        TextView fileName = (TextView) linlay.findViewById(R.id.file_name);
        fileName.setText(elementName);

        ImageView image = (ImageView) linlay.findViewById(R.id.image);
        image.setImageResource(elementImage);

        switch(display_mode) {
            case Configuration.ORIENTATION_PORTRAIT:
                itemParams.width  = (int)(screenWidth  / 3.0);
                itemParams.height = (int)(screenHeight / 8.2);
                this.gridLayout.setColumnCount(3);
                fileName.setTextSize(TypedValue.COMPLEX_UNIT_PT, 6);
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                itemParams.width  = (int)(screenWidth  / 5.0);
                itemParams.height = (int)(screenHeight / 4.0);
                this.gridLayout.setColumnCount(5);
                fileName.setTextSize(TypedValue.COMPLEX_UNIT_PT, 3);
                break;

            default:
                // pizdets
        }

        linlay.setLayoutParams(itemParams);
        linlay.setOnClickListener(this);

        this.gridLayout.addView(linlay);

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

    public void onClickStuff(View v) {

    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent();

        LinearLayout linearLayout = (LinearLayout) v;
        TextView     tvName       = (TextView) linearLayout.findViewById(R.id.file_name);

        final String name = tvName.getText().toString();

        View view = null;
        AlertDialog.Builder builder;

        ImageView o1;
        ImageView o2;
        ImageView o3;
        ImageView o4;

        switch (name) {
            case "DC":

                view = getLayoutInflater().inflate(R.layout.choose_orientation_dialog_dc, null);

                o1 = view.findViewById(R.id.image1);
                o2 = view.findViewById(R.id.image2);
                o3 = view.findViewById(R.id.image3);
                o4 = view.findViewById(R.id.image4);

                o1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 0);
                    }
                });

                o2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 1);
                    }
                });

                o3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 2);
                    }
                });

                o4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 3);
                    }
                });

                break;
            case "Capacitor":

                view = getLayoutInflater().inflate(R.layout.choose_orientation_dialog_c, null);

                o1 = view.findViewById(R.id.image1);
                o2 = view.findViewById(R.id.image2);

                o1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 0);
                    }
                });

                o2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 2);
                    }
                });

                break;

            case "Resistor":

                view = getLayoutInflater().inflate(R.layout.choose_orientation_dialog_r, null);

                o1 = view.findViewById(R.id.image1);
                o2 = view.findViewById(R.id.image2);

                o1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 0);
                    }
                });

                o2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 2);
                    }
                });

                break;

            case "Inductor":

                view = getLayoutInflater().inflate(R.layout.choose_orientation_dialog_i, null);

                o1 = view.findViewById(R.id.image1);
                o2 = view.findViewById(R.id.image2);
                o3 = view.findViewById(R.id.image3);
                o4 = view.findViewById(R.id.image4);

                o1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 0);
                    }
                });

                o2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 1);
                    }
                });

                o3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 2);
                    }
                });

                o4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnResult(intent, name, 3);
                    }
                });

                break;
        }

        builder = new AlertDialog.Builder(this);
        builder.setView(view).show(); // E/WindowManager: android.view.WindowLeaked: Activity com.crazydev.funnycircuits.AddElementActivity has leaked window co


    //    Log.d("actRes", "type = "  + tvName.getText().toString() + " orientation = "  + 0);
    }

    private void returnResult(Intent intent, String name, int orientation) {
        intent.putExtra("wireType", name);
        intent.putExtra("orientation", orientation);
        setResult(RESULT_OK, intent);
        finish();
    }
}
