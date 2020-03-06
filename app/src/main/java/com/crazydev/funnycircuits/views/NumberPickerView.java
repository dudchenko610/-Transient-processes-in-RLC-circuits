package com.crazydev.funnycircuits.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.crazydev.funnycircuits.math.Vector2D;

import java.util.Random;

public class NumberPickerView extends View implements View.OnTouchListener{

    public interface OnPickerValueChanged {
        void onValueChanged(double value);
    }

    private OnPickerValueChanged onPickerValueChanged;

    private float cx;
    private float cy;
    private float rad_1;
    private float rad_2;
    private float rad_3;
    private float rad_4;
    private float rad_5;

    private float r_centr; // радиусы наших окружностей

    private Paint p_cursor_area_color    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_external_area_color  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_internal_area_color  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_iexternal_area_color = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_center_area_color    = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint p_text_area_color      = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path path_cursor = new Path();

    private int pixelTextValue = 0;

    private Paint p_handl = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float deg_col = -50; // углы поворота
    private float lm; // отступы и выступы линий

    private int size;

    private double value = 1;

    public NumberPickerView(Context context) {
        this(context, null);
    }

    public NumberPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        p_cursor_area_color.setStyle(Paint.Style.STROKE);
        p_external_area_color.setStyle(Paint.Style.STROKE);
        p_internal_area_color.setStyle(Paint.Style.STROKE);
        p_iexternal_area_color.setStyle(Paint.Style.STROKE);
        p_center_area_color.setStyle(Paint.Style.FILL_AND_STROKE);
        p_text_area_color.setStyle(Paint.Style.STROKE);
//        p_text_area_color.setARGB(255,6,0,167);

        p_handl.setStyle(Paint.Style.FILL_AND_STROKE);
        p_handl.setStrokeWidth(8);
        p_handl.setARGB(255,39,174,96);

        p_text_area_color.setARGB(255,39,174,96);

        p_handl.setStrokeCap(Paint.Cap.ROUND);

        setOnTouchListener(this);

        int MY_DIP_VALUE = 13; //5dp
        this.pixelTextValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MY_DIP_VALUE, getResources().getDisplayMetrics());

        p_text_area_color.setTextSize(this.pixelTextValue);
    //    p_text_area_color.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mWidth = measure(widthMeasureSpec);
        int mHeight = measure(heightMeasureSpec);
        size = Math.min(mWidth, mHeight);
        setMeasuredDimension(size, size);

        calculateSizes(); //  метод для расчетов всяких наших размеров

    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMoge = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMoge == MeasureSpec.UNSPECIFIED) result = 200;
        else result = specSize;
        return result;
    }

    private void calculateSizes() {

        float radius    = this.size * 0.5f;
        float rad_third = radius / 3;
        float rad_50    = radius / 40;

        float rad_70    = radius / 70;
        float rad_23_23 = (radius - rad_third) * 0.6f;

        this.cx = this.size * 0.5f;
        this.cy = this.cx;

        this.rad_2 = radius - rad_70 / 2;
        this.rad_3 = radius - rad_70 / 2 - rad_third;
        this.rad_4 = rad_23_23 - rad_70 / 2;
        this.rad_5 = radius - rad_third - rad_third * 0.5f;

        lm    = rad_third / 2.6f;

        rad_1 = radius - rad_third / 2;

        r_centr = rad_23_23;


        p_cursor_area_color.setARGB(10,39,174,96);
        p_cursor_area_color.setStrokeWidth(rad_third);

        p_external_area_color.setARGB(45,39,174,96); // 97 66 91
        p_external_area_color.setStrokeWidth(rad_70);

        p_internal_area_color.setARGB(45,39,174,96);
        p_internal_area_color.setStrokeWidth(rad_70);

        p_center_area_color.setARGB(10,39,174,96);
   //     p_center_area_color.setStrokeWidth(r_centr);

        p_iexternal_area_color.setARGB(45,39,174,96);
        p_iexternal_area_color.setStrokeWidth(rad_70);


    /*    float rr =  radius - rad_third;

        path_cursor.moveTo(cx + rad_1 - lm, cy);
        path_cursor.lineTo(cx + rad_1 + lm, cy + lm * 1f);
        path_cursor.lineTo(cx + rad_1 + lm, cy - lm * 1f);
        path_cursor.lineTo(cx + rad_1 - lm, cy);*/

    }

    private float xL = +0.642787f;
    private float yL = -0.766044f;

    private double oldDeg = 0;
    private double degreeCounter = 0;

    private String valueToDepict = "1.0";

    protected float getAngle(float x, float y) {

        Vector2D vN = new Vector2D(x, y);
        vN.normalize();

        Vector2D vL = new Vector2D(xL, yL);

        Vector2D aL = vL.copy().turnCounterClockWise();

        float mn = Vector2D.dotProduct(aL, vN);


        this.xL = vN.x;
        this.yL = vN.y;


        float deg = 0;
        if (x != 0) {
            deg = y / x;
        }

        deg = (float) Math.toDegrees(Math.atan(deg));

      //  Log.d("rozr", "deg = " + deg);

        if (x < 0) {
            deg += 180;
        } else if (x > 0 && y < 0) {
            deg += 360;
        }

        deg = ((int) (deg / 1)) * 1;

        double degRad     = Math.toRadians(deg + 40);
        double oldRadians = Math.toRadians(this.oldDeg);

        Vector2D signleN = new Vector2D(1, 0);
        signleN.rotate((float) degRad);

        Vector2D signleO = new Vector2D(1, 0);
        signleO.rotate((float) oldRadians);

        float cos = Vector2D.dotProduct(signleN, signleO);

        if (cos > 1.0f) {
            cos = 1.0f;
        }

        if (cos < -1.0f) {
            cos = -1.0f;
        }

        float ang = (float) Math.acos(cos);
        double degrees = Math.toDegrees(ang);

        if (Float.isNaN(ang)) {
            Log.d("rozr", "isNan = " + ang);
            Log.d("rozr", "cos = " + cos);
        }

        degrees = Math.round(degrees);

        this.degreeCounter += mn < 0 ? -degrees : degrees;

        this.value = Math.pow(10, degreeCounter / 360);

     //   Log.d("rozr", "degreeC = " + degreeCounter);
      //  Log.d("rozr", "deg_col = " + this.deg_col);

        String s = String.valueOf(this.value);
        this.setNumberLabel(s);


        this.oldDeg = deg + 40;

        return deg;
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        drawTextCentred(c, p_text_area_color, valueToDepict, cx, cy);
        drawCircles(c);
        drawStaticLabels(c);

        drawLines(c);

    }

    private void drawCircles(Canvas c) {
        c.drawCircle(cx, cy, rad_1, p_cursor_area_color);
        c.drawCircle(cx, cy, rad_2, p_external_area_color);
        c.drawCircle(cx, cy, rad_3, p_internal_area_color);
        c.drawCircle(cx, cy, rad_4, p_iexternal_area_color);

        c.drawCircle(cx, cy, r_centr, p_center_area_color);

    }

    private void drawLines(Canvas c) {
        float d = deg_col;


        c.rotate(d, cx, cy);

        c.drawPath(path_cursor, p_handl);

        c.drawLine(cx + rad_1 - lm, cy, cx + rad_1 + lm, cy, p_handl);

        c.rotate(-d, cx, cy);

    }

    private void drawStaticLabels(Canvas c) {

        int deg = 0;
        for (int i = 9; i >= 1; i --) {
            c.rotate(deg, cx, cy);
            c.translate(0, -this.rad_5);

            c.rotate(-deg, cx, cy);

         //   c.drawText( i + "", cx - dx, cy - dy, p_text_area_color);

            drawTextCentred(c, p_text_area_color, "" + i, cx, cy);

            c.rotate(+deg, cx, cy );

            c.translate(0, +this.rad_5);
            c.rotate(-deg, cx, cy);

            deg -= 40;
        }

    }

    private final Rect textBounds = new Rect(); //don't new this up in a draw method

    public void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy){
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }

    public void setValue(double value) {
        double degree = Math.log10(value);

        this.degreeCounter = Math.round(degree * 360);
        this.oldDeg        = this.degreeCounter;

    //    Log.d("rozr", "degree = " + degree);
     //   Log.d("rozr", "degreeCounter = " + degreeCounter);

        int d = (int) degree;
        degree = degree - d;

        this.deg_col = (int) Math.round(degree * 360) - 40;
        this.setNumberLabel(String.valueOf(value));

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:
                float x = event.getX() - cx;
                float y = event.getY() - cy;

                this.deg_col = getAngle(x, y);

                break;

            case MotionEvent.ACTION_UP:
                if (this.onPickerValueChanged != null) {
                    this.onPickerValueChanged.onValueChanged(this.getValue());
                }

                break;

        }

        invalidate();
        return true;

    }

    public void setOnPickerValueChanged(OnPickerValueChanged onPickerValueChanged) {
        this.onPickerValueChanged = onPickerValueChanged;
    }

    public double getValue() {

        char last = this.valueToDepict.charAt(this.valueToDepict.length() - 1);

        double value = -1;


        if (Character.isDigit(last)) {

            value = Double.parseDouble(this.valueToDepict);

        } else {
            double mn = Double.parseDouble(this.valueToDepict.substring(0, this.valueToDepict.length() - 1));

            switch(last) {
                case 'a':
                    value = mn * Math.pow(10, -18);
                    break;
                case 'f':
                    value = mn * Math.pow(10, -15);
                    break;
                case 'p':
                    value = mn * Math.pow(10, -12);
                    break;
                case 'n':
                    value = mn * Math.pow(10, -9);
                    break;
                case 'u':
                    value = mn * Math.pow(10, -6);
                    break;
                case 'm':
                    value = mn * Math.pow(10, -3);
                    break;
                case 'k':
                    value = mn * Math.pow(10, 3);
                    break;
                case 'M':
                    value = mn * Math.pow(10, 6);
                    break;
                case 'G':
                    value = mn * Math.pow(10, 9);
                    break;
                case 'T':
                    value = mn * Math.pow(10, 12);
                    break;
                case 'P':
                    value = mn * Math.pow(10, 15);
                    break;
                case 'E':
                    value = mn * Math.pow(10, 18);
                    break;
                default:
                    // pizdets
            }
        }

        return value;
    }

    private void setNumberLabel(String s) {

        if (s.contains("E") || s.contains("e")) {

            String arr2[] = s.split("E");
            int power = Integer.parseInt(arr2[1]);

            //   Log.d("rozr", "power = " + power);

            if (power < 0) {

                if (power < -18) {

                    return ;
                }

                power = Math.abs(power);

                if (power % 3 == 1) {
                    this.valueToDepict = String.valueOf(s.charAt(0)) + String.valueOf(s.charAt(2)) + "0";
                } else if (power % 3 == 0) {
                    this.valueToDepict = String.valueOf(s.charAt(0)) + "." + String.valueOf(s.charAt(2));
                } else if (power % 2 == 0 || power % 3 == 2) {
                    this.valueToDepict = String.valueOf(s.charAt(0)) + String.valueOf(s.charAt(2));
                }  else {
                    // I need to think about it
                }

                int b = (power - 1) / 3;

                switch(b) {
                    case 0:
                        this.valueToDepict += "m";
                        break;
                    case 1:
                        this.valueToDepict += "u";
                        break;
                    case 2:
                        this.valueToDepict += "n";
                        break;
                    case 3:
                        this.valueToDepict += "p";
                        break;
                    case 4:
                        this.valueToDepict += "f";
                        break;
                    case 5:
                        this.valueToDepict += "a";
                        break;
                }

                //   this.valueToDepict = String.valueOf(this.value);

                return ;
            }

            long l = (long) value;

            s = l + ".0";
        }

        s = s.replace(".", "l");

        String arr[] = s.split("l");
        int len = arr[0].length();


        if (len == 1) {
            if (arr[0].equals("0")) {

                String r = arr[1];

                if (!(r.contains("E") || r.contains("e"))) {
                    int nulls = 0;

                    for (int i = 0; i < r.length(); i++) {
                        char c = r.charAt(i);
                        if (c != '0') {
                            nulls = i;
                            break;
                        }
                    }

                    r = r.substring(nulls);
                    r += "000000000";

                    nulls++;

                    //    Log.d("rozr", "r = " + r);

                    if (nulls % 3 == 1) {
                        this.valueToDepict = String.valueOf(r.charAt(0)) + String.valueOf(r.charAt(1)) + "0";
                    } else if (nulls % 3 == 0) {
                        this.valueToDepict = String.valueOf(r.charAt(0)) + "." + String.valueOf(r.charAt(1));
                    } else if (nulls % 2 == 0 || nulls % 3 == 2) {
                        this.valueToDepict = String.valueOf(r.charAt(0)) + String.valueOf(r.charAt(1));
                    }  else {
                        // I need to think about it
                    }

                    int b = (nulls - 1) / 3;

                    switch(b) {
                        case 0:
                            this.valueToDepict += "m";
                            break;
                        case 1:
                            this.valueToDepict += "u";
                            break;
                        case 2:
                            this.valueToDepict += "n";
                            break;
                    }

                    return;

                }


                //   this.valueToDepict = String.valueOf(this.value);
            } else {
                this.valueToDepict = arr[0] + "." + String.valueOf(arr[1].charAt(0));
            }

        } else if (len > 0) {
            int b = (len - 1) / 3;

            if (len % 3 == 1) {
                this.valueToDepict = String.valueOf(arr[0].charAt(0)) + "." + String.valueOf(arr[0].charAt(1));
            } else if (len % 3 == 0) {
                this.valueToDepict = String.valueOf(arr[0].charAt(0)) + String.valueOf(arr[0].charAt(1)) + "0";
            } else if (len % 2 == 0 || len % 3 == 2) {
                this.valueToDepict = String.valueOf(arr[0].charAt(0)) + String.valueOf(arr[0].charAt(1));
            }  else {
                // I need to think about it
            }

            switch(b) {
                case 1:
                    this.valueToDepict += "k";
                    break;
                case 2:
                    this.valueToDepict += "M";
                    break;
                case 3:
                    this.valueToDepict += "G";
                    break;
                case 4:
                    this.valueToDepict += "T";
                    break;
                case 5:
                    this.valueToDepict += "P";
                    break;
                case 6:
                    this.valueToDepict += "E";
                    break;
            }

        }
    }


}
