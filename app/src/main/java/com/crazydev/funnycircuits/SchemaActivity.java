package com.crazydev.funnycircuits;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.crazydev.funnycircuits.events.OnWireSelected;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.rendering.OpenGLRenderer;
import com.crazydev.funnycircuits.rendering.ShaderProgram;
import com.crazydev.funnycircuits.views.NumberPickerView;

public class SchemaActivity extends AppCompatActivity implements
        OnWireSelected, NumberPickerView.OnPickerValueChanged {

    public static final String PARCELABLE_KEY_WIRE_MODE               = "wire_ley";
    public static final String PARCELABLE_KEY_VIEWPOINT_TRANSLATION_X = "viewpoint_translation_ley_x";
    public static final String PARCELABLE_KEY_VIEWPOINT_TRANSLATION_Y = "viewpoint_translation_ley_y";
    public static final String PARCELABLE_KEY_VIEWPOINT_ZOOM          = "viewpoint_zoom_ley";

    public static final int REQUEST_CODE_ADD_ELEMENT_ACTIVITY = 0;

    protected Toolbar                 toolbar;
    protected OpenGLRenderer   openGLRenderer;
    protected World           electronicWorld;
    protected LayoutInflater inflater;

    protected boolean isWireMode = false;

    private EditText editText;
    private Button button;
    private ImageView deleteWireButton;

    private NumberPickerView numberPickerView;

    private Handler handler;
    private Wire selectedWire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_schema);

        this.toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.openGLRenderer  = (OpenGLRenderer) findViewById(R.id.open_gl_renderer);
        this.openGLRenderer.setOnWireSelected(this);

     //   this.registerForContextMenu(this.openGLRenderer);

        this.inflater      = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.numberPickerView = (NumberPickerView) findViewById(R.id.numberPickerView);
        this.numberPickerView.setOnPickerValueChanged(this);

    //    this.numberPickerView.setValue(0.0001);
        this.adjustNumberPickerView();

        Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    SchemaActivity.this.deleteWireButton.setImageResource(R.drawable.basket);
                } else {
                    SchemaActivity.this.deleteWireButton.setImageResource(R.drawable.basketbr);
                }

            }

        };

        this.openGLRenderer.setDeleteWireButton(handler);
        this.electronicWorld = this.openGLRenderer.getElectronicWorld();

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
                openGLRenderer.getElectronicWorld().underlineBranch(m, n);

            }
        });

        this.handler = new Handler() {

            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case 0:

                        int visibility = View.VISIBLE;
                        Wire wire      = (Wire) msg.obj;

                        switch (wire.type) {
                            case INDUCTOR:
                                SchemaActivity.this.numberPickerView.setValue(((Inductor) wire).inductance);
                                break;
                            case RESISTOR:
                                SchemaActivity.this.numberPickerView.setValue(((Resistor) wire).resistance);
                                break;
                            case CAPACITOR:
                                SchemaActivity.this.numberPickerView.setValue(((Capacitor) wire).capacity);
                                break;
                            case DC_SOURCE:
                                SchemaActivity.this.numberPickerView.setValue(((DCSource) wire).voltage);
                                break;
                            case WIRE:
                                visibility = View.GONE;
                                SchemaActivity.this.selectedWire = null;
                        }

                        SchemaActivity.this.numberPickerView.setVisibility(visibility);
                        break;
                    case 1:

                        SchemaActivity.this.numberPickerView.setVisibility(View.GONE);
                        break;
                }

            }
        };

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

        this.openGLRenderer.setWireMode(this.isWireMode);
        ShaderProgram shaderProgram = this.openGLRenderer.getShaderProgram();
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

        ShaderProgram shaderProgram = this.openGLRenderer.getShaderProgram();
        Vector2D pos = shaderProgram.getViewportPosition();

        outState.putFloat(SchemaActivity.PARCELABLE_KEY_VIEWPOINT_TRANSLATION_X, pos.x);
        outState.putFloat(SchemaActivity.PARCELABLE_KEY_VIEWPOINT_TRANSLATION_Y, pos.y);
        outState.putFloat(SchemaActivity.PARCELABLE_KEY_VIEWPOINT_ZOOM, shaderProgram.getZoom());

    }

    public void onWireCreatingMode(View v) {
        CheckBox imb = (CheckBox) v;
        this.isWireMode = !this.isWireMode;

        imb.setChecked(this.isWireMode);

     /*   if (this.isWireMode){
            imb.setBackground(getResources().getDrawable(R.drawable.pencilbr));
        } else {
            imb.setBackground(getResources().getDrawable(R.drawable.pencil));
        }*/

        this.openGLRenderer.setWireMode(this.isWireMode);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onContextMenuClosed(Menu menu) {
        super.onContextMenuClosed(menu);
        this.openGLRenderer.onContextMenuClosed();

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

    public void onDeleteWire(View v) {
        this.openGLRenderer.deleteSelectedWires();
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

                ShaderProgram shaderProgram = this.openGLRenderer.getShaderProgram();
                Vector2D pos = shaderProgram.getViewportPosition();

          //    Log.d("actRes", "type = " + type + " orientation = "  + orientation);

                if (type.equals("DC")) {
                    openGLRenderer.getElectronicWorld().createElement(new Vector2D(Math.round(pos.x), Math.round(pos.y)), Wire.WireType.DC_SOURCE, orientation);
                } else if (type.equals("Capacitor")) {
                    openGLRenderer.getElectronicWorld().createElement(new Vector2D(Math.round(pos.x), Math.round(pos.y)), Wire.WireType.CAPACITOR, orientation);
                } else if (type.equals("Resistor")) {
                    openGLRenderer.getElectronicWorld().createElement(new Vector2D(Math.round(pos.x), Math.round(pos.y)), Wire.WireType.RESISTOR, orientation);
                } else if (type.equals("Inductor")) {
                    openGLRenderer.getElectronicWorld().createElement(new Vector2D(Math.round(pos.x), Math.round(pos.y)), Wire.WireType.INDUCTOR, orientation);
                }

                break;
        }
    }

    @Override
    public void onSelect(Wire wire) {
        Message message = new Message();
        message.what = 0;
        message.obj = wire;

        this.selectedWire = wire;
        this.handler.sendMessage(message);
    }

    @Override
    public void onDeselect() {

        this.selectedWire = null;
        this.handler.sendEmptyMessage(1);

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


}
