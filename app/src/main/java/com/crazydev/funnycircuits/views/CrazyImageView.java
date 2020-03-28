package com.crazydev.funnycircuits.views;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

import androidx.annotation.Nullable;

import com.crazydev.funnycircuits.R;

public class CrazyImageView extends AppCompatImageView {

    private int drawableEnabled;
    private int drawableDisabled;

    public CrazyImageView(Context context) {
        super(context);
    }

    public CrazyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void setDrawables(int drawableEnabled, int drawableDisabled) {
        this.drawableEnabled = drawableEnabled;
        this.drawableDisabled = drawableDisabled;
    }

    @Override
    public void setEnabled(boolean b) {

        if (drawableEnabled != 0 && drawableDisabled != 0) {
            if (b) {
                this.setImageResource(this.drawableEnabled);
            } else {
                this.setImageResource(this.drawableDisabled);
            }
        }

        super.setEnabled(b);

    }

}
