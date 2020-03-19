package com.crazydev.funnycircuits;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.crazydev.funnycircuits.electronic.Wire;
import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.electronic.elements.Capacitor;
import com.crazydev.funnycircuits.electronic.elements.DCSource;
import com.crazydev.funnycircuits.electronic.elements.Inductor;
import com.crazydev.funnycircuits.electronic.elements.Resistor;
import com.crazydev.funnycircuits.electronic.interfaces.ILoggable;
import com.crazydev.funnycircuits.events.OnWireSelectedListener;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;
import com.crazydev.funnycircuits.rendering.ShaderProgram;
import com.crazydev.funnycircuits.views.NumberPickerView;

public class SchemaActivity extends AppCompatActivity implements
        OnWireSelectedListener, NumberPickerView.OnPickerValueChanged, View.OnClickListener {

    public static final String PARCELABLE_KEY_WIRE_MODE               = "wire_ley";
    public static final String PARCELABLE_KEY_VIEWPOINT_TRANSLATION_X = "viewpoint_translation_ley_x";
    public static final String PARCELABLE_KEY_VIEWPOINT_TRANSLATION_Y = "viewpoint_translation_ley_y";
    public static final String PARCELABLE_KEY_VIEWPOINT_ZOOM          = "viewpoint_zoom_ley";

    public static final int REQUEST_CODE_ADD_ELEMENT_ACTIVITY = 0;

    protected Toolbar         toolbar;
    protected OpenGLRenderer  openGLRenderer;
    protected ShaderProgram   shaderProgram = ShaderProgram.getInstance();;

    protected World           electronicWorld;
    protected LayoutInflater  inflater;

    protected boolean isWireMode = false;

    private EditText editText;
    private Button button;
    private ImageView deleteWireButton;

    private NumberPickerView numberPickerView;
    private CheckBox cbCurrent, cbVoltage, cbCharge, cbLinkage;

    private Wire selectedWire;

    private UIThreadSynchronizer uiThreadSynchronizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_schema);

        this.toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.uiThreadSynchronizer = new UIThreadSynchronizer(this);
        this.openGLRenderer  = (OpenGLRenderer) findViewById(R.id.open_gl_renderer);
        this.openGLRenderer.getTouchEventService().setOnWireSelectedListener(this);

     //   this.registerForContextMenu(this.openGLRenderer);

        this.inflater      = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.numberPickerView = (NumberPickerView) findViewById(R.id.numberPickerView);
        this.numberPickerView.setOnPickerValueChanged(this);

        this.cbCurrent = (CheckBox) findViewById(R.id.cb_current);
        this.cbVoltage = (CheckBox) findViewById(R.id.cb_voltage);
        this.cbCharge  = (CheckBox) findViewById(R.id.cb_charge);
        this.cbLinkage = (CheckBox) findViewById(R.id.cb_linkage);

        this.cbCurrent.setOnClickListener(this);
        this.cbVoltage.setOnClickListener(this);
        this.cbCharge.setOnClickListener(this);
        this.cbLinkage.setOnClickListener(this);

    //    this.numberPickerView.setValue(0.0001);
        this.adjustNumberPickerView();

        this.openGLRenderer.setUIThreadSynchronizer(this.uiThreadSynchronizer);
        this.electronicWorld = World.getInstance();

        this.editText = (EditText) findViewById(R.id.branch_number);
        this.button   = (Button) findViewById(R.id.ok);
        this.deleteWireButton = (ImageView) findViewById(R.id.delete_wire);

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String line = editText.getText().toString();
                String arr[] = line.split("l");

                int m = Integer.parseInt(arr[0]);
                int n = Integer.parseInt(arr[1]);


             //   openGLRenderer.getElectronicWorld().underlineBranch(m, n);
                electronicWorld.underlineBranch(m, n);

            }
        });


    }

    private void adjustNumberPickerView() {
        int display_mode = getResources().getConfiguration().orientation;

        LinearLayout.LayoutParams itemParams = (LinearLayout.LayoutParams) this.numberPickerView.getLayoutParams();

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;


        switch(display_mode) {
            case Configuration.ORIENTATION_PORTRAIT:
                itemParams.width = itemParams.height = (int)(screenWidth  / 2.3);
            //    itemParams.height = (int)(screenHeight / 4.2);

                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                itemParams.width = itemParams.height  = (int)(screenWidth  / 4.2);
             //   itemParams.height = (int)(screenHeight / 2.0);

                break;

            default:
                // pizdets
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        this.isWireMode = inState.getBoolean(SchemaActivity.PARCELABLE_KEY_WIRE_MODE);

        CheckBox imb = (CheckBox) findViewById(R.id.pencil);

        imb.setChecked(this.isWireMode);
      /*  if (this.isWireMode){
            imb.setBackground(getResources().getDrawable(R.drawable.pencilbr));
        } else {
            imb.setBackground(getResources().getDrawable(R.drawable.pencil));
        }*/

        this.openGLRenderer.getTouchEventService().setWireMode(this.isWireMode);
        Vector2D pos = new Vector2D(inState.getFloat(SchemaActivity.PARCELABLE_KEY_VIEWPOINT_TRANSLATION_X),
                                    inState.getFloat(SchemaActivity.PARCELABLE_KEY_VIEWPOINT_TRANSLATION_Y));

   //     Log.d("logggg", "x = " + pos.x + " y = " + pos.y);

        shaderProgram.setViewPort(pos);
        shaderProgram.setZoom(inState.getFloat(SchemaActivity.PARCELABLE_KEY_VIEWPOINT_ZOOM));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SchemaActivity.PARCELABLE_KEY_WIRE_MODE, this.isWireMode);

        Vector2D pos = shaderProgram.getViewportPosition();

        outState.putFloat(SchemaActivity.PARCELABLE_KEY_VIEWPOINT_TRANSLATION_X, pos.x);
        outState.putFloat(SchemaActivity.PARCELABLE_KEY_VIEWPOINT_TRANSLATION_Y, pos.y);
        outState.putFloat(SchemaActivity.PARCELABLE_KEY_VIEWPOINT_ZOOM, shaderProgram.getZoom());

    }

    public void onWireCreatingMode(View v) {

        CheckBox imb = (CheckBox) v;
        this.isWireMode = !this.isWireMode;
        imb.setChecked(this.isWireMode);

        this.openGLRenderer.getTouchEventService().setWireMode(this.isWireMode);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        super.onContextMenuClosed(menu);
        this.openGLRenderer.getTouchEventService().onContextMenuClosed();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        Log.d("translating", "itemmm");

        switch(item.getItemId()) {
            case R.id.action_delete:
                this.openGLRenderer.deleteSelectedWires();
                break;
        }

        return super.onContextItemSelected(item);

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


    // FROM LAYOUT

    public void onDeleteWire(View v) {
        this.openGLRenderer.deleteSelectedWires();
        this.uiThreadSynchronizer.freeUpScreen();
    }

    public void onStartCalculating(View v) {



        ApplicationMode.setSimulatingMode();
    }

    public void onStopCalculating(View v) {
        ApplicationMode.setEditingMode();
    }

    public void onAddElement(View v) {
        Intent intent = new Intent(SchemaActivity.this, AddElementActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_ELEMENT_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch(requestCode) {
            case REQUEST_CODE_ADD_ELEMENT_ACTIVITY:

                String type = data.getStringExtra("wireType");
                int orientation = data.getIntExtra("orientation", -1);

                Vector2D pos = shaderProgram.getViewportPosition();

          //    Log.d("actRes", "type = " + type + " orientation = "  + orientation);

                if (type.equals("DC")) {
                    this.electronicWorld.createElement(new Vector2D(Math.round(pos.x), Math.round(pos.y)), Wire.WireType.DC_SOURCE, orientation);
                } else if (type.equals("Capacitor")) {
                    this.electronicWorld.createElement(new Vector2D(Math.round(pos.x), Math.round(pos.y)), Wire.WireType.CAPACITOR, orientation);
                } else if (type.equals("Resistor")) {
                    this.electronicWorld.createElement(new Vector2D(Math.round(pos.x), Math.round(pos.y)), Wire.WireType.RESISTOR, orientation);
                } else if (type.equals("Inductor")) {
                    this.electronicWorld.createElement(new Vector2D(Math.round(pos.x), Math.round(pos.y)), Wire.WireType.INDUCTOR, orientation);
                }

                break;
        }
    }

    @Override
    public void onWireSelect(Wire wire) {
        this.uiThreadSynchronizer.wireWasSelected(wire);
    }

    @Override
    public void onDeselect() {
        this.uiThreadSynchronizer.freeUpScreen();
    }

    @Override
    public void onValueChanged(double value) {

        if (this.selectedWire == null) {
            return;
        }

        switch(this.selectedWire.type) {
            case INDUCTOR:
                ((Inductor) this.selectedWire).inductance = value;
                break;
            case CAPACITOR:
                ((Capacitor) this.selectedWire).capacity = value;
                break;
            case DC_SOURCE:
                ((DCSource) this.selectedWire).voltage = value;
                break;
            case RESISTOR:
                ((Resistor) this.selectedWire).resistance = value;
                break;
        }


    }


    @Override
    public void onClick(View view) {

        if (selectedWire instanceof ILoggable) {
            ILoggable loggable = (ILoggable) selectedWire;

        //    Log.d("HEREE", " __HERE 2__");

            switch (view.getId()) {
                case R.id.cb_current:
                    loggable.setCurrentLogState(this.cbCurrent.isChecked());
                    break;
                case R.id.cb_voltage:
                    loggable.setVoltageLogState(this.cbVoltage.isChecked());
                    break;
                case R.id.cb_charge:
                    loggable.setChargeLogState(this.cbCharge.isChecked());
                    break;
                case R.id.cb_linkage:
                    loggable.setLinkageLogState(this.cbLinkage.isChecked());
                    break;
            }

        }


    }

    public static class UIThreadSynchronizer extends Handler {

        private SchemaActivity schemaActivity;

        public UIThreadSynchronizer(SchemaActivity schemaActivity) {
            this.schemaActivity = schemaActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    schemaActivity.deleteWireButton.setImageResource(R.drawable.basketbr);
                    schemaActivity.deleteWireButton.setEnabled(false);
                    break;
                case 1:
                    schemaActivity.deleteWireButton.setImageResource(R.drawable.basket);
                    schemaActivity.deleteWireButton.setEnabled(true);
                    break;

                    // 0 on wire select
                case 2:
                    int visibility = View.VISIBLE;
                    Wire wire      = (Wire) msg.obj;

                    schemaActivity.cbCurrent.setChecked(false);
                    schemaActivity.cbCharge.setChecked(false);
                    schemaActivity.cbVoltage.setChecked(false);
                    schemaActivity.cbLinkage.setChecked(false);

                    if (wire instanceof ILoggable) {
                        ILoggable loggable = (ILoggable) wire;

                        Log.d("HEREE", " __HERE 1__");

                        schemaActivity.cbCurrent.setChecked(loggable.getCurrentLogState());
                        schemaActivity.cbCharge.setChecked(loggable.getChargeLogState());
                        schemaActivity.cbVoltage.setChecked(loggable.getVoltageLogState());
                        schemaActivity.cbLinkage.setChecked(loggable.getLinkageLogState());
                    }

                    switch (wire.type) {
                        case INDUCTOR:
                            schemaActivity.numberPickerView.setValue(((Inductor) wire).inductance);
                            schemaActivity.cbCurrent.setVisibility(View.VISIBLE);
                            schemaActivity.cbVoltage.setVisibility(View.VISIBLE);
                            schemaActivity.cbLinkage.setVisibility(View.VISIBLE);

                            break;
                        case RESISTOR:
                            schemaActivity.numberPickerView.setValue(((Resistor) wire).resistance);
                            schemaActivity.cbCurrent.setVisibility(View.VISIBLE);
                            schemaActivity.cbVoltage.setVisibility(View.VISIBLE);

                            break;

                        case CAPACITOR:
                            schemaActivity.numberPickerView.setValue(((Capacitor) wire).capacity);
                            schemaActivity.cbCurrent.setVisibility(View.VISIBLE);
                            schemaActivity.cbVoltage.setVisibility(View.VISIBLE);
                            schemaActivity.cbCharge.setVisibility(View.VISIBLE);

                            break;
                        case DC_SOURCE:
                            schemaActivity.numberPickerView.setValue(((DCSource) wire).voltage);
                            break;
                        case WIRE:
                            visibility = View.GONE;
                            schemaActivity.selectedWire = null;
                    }

                    schemaActivity.numberPickerView.setVisibility(visibility);

                    break;
                    // free up screen
                case 3:
                    schemaActivity.numberPickerView.setVisibility(View.GONE);

                    schemaActivity.cbCurrent.setChecked(false);
                    schemaActivity.cbCharge.setChecked(false);
                    schemaActivity.cbVoltage.setChecked(false);
                    schemaActivity.cbLinkage.setChecked(false);

                    schemaActivity.cbCurrent.setVisibility(View.GONE);
                    schemaActivity.cbCharge.setVisibility(View.GONE);
                    schemaActivity.cbVoltage.setVisibility(View.GONE);
                    schemaActivity.cbLinkage.setVisibility(View.GONE);

                    break;
            }
        }

        public void setDeleteButtonState(boolean state) {
            Message message = new Message();
            message.what = state ? 1 : 0;
            this.sendMessage(message);
        }

        public void enableStartButton(boolean state) {

        }

        public void enableStopButton(boolean state) {

        }

        public void wireWasSelected(Wire wire) {
            Message message = new Message();
            message.what = 2;
            message.obj = wire;

            schemaActivity.selectedWire = wire;
            this.sendMessage(message);
        }

        public void freeUpScreen() {
            schemaActivity.selectedWire = null;
            this.sendEmptyMessage(3);
        }


    }

}
