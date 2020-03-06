package com.crazydev.funnycircuits;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazydev.funnycircuits.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;


public class MainActivity extends BasicActivity {

    private  Drawer.Result  drawerResult;

    private static final String FIRST_TIME_STARTED     = "firsttimestarted";
    private static final String FIRST_TIME_STARTED_KEY = "firsttimestartedkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(this.toolbar)
                .withActionBarDrawerToggle(true)
              /*  .withHeader(R.layout.drawer_header).*/.withOnDrawerListener(new Drawer.OnDrawerListener() {

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        //    MainActivity.this.hideKeyboard();

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        //              MainActivity.this.memoryManager.lookIntoDB();

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        //     MainActivity.this.hideKeyboard();
                    }


                })
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withBadge("99").withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withBadge("6").withIdentifier(2),
                        new SectionDrawerItem().withName(R.string.drawer_item_settings),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_question).setEnabled(false),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_github).withBadge("12+").withIdentifier(1)
                ).build();



        final SharedPreferences sharedPreferences = this.getSharedPreferences(MainActivity.FIRST_TIME_STARTED, MODE_PRIVATE);
        boolean firstTimeRan = sharedPreferences.getBoolean(MainActivity.FIRST_TIME_STARTED_KEY, true);

        if (firstTimeRan) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(MainActivity.FIRST_TIME_STARTED_KEY, false);
            editor.commit();

            // create folder imagesLoooogogs
        }

    }

    @Override
    public void onBackPressed(){
        if(drawerResult.isDrawerOpen()){
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


}
