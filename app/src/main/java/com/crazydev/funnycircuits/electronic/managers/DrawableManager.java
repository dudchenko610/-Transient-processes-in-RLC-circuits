package com.crazydev.funnycircuits.electronic.managers;

import com.crazydev.funnycircuits.electronic.interfaces.IDrawableManager;
import com.crazydev.funnycircuits.rendering.sprites.Sprite;

import java.util.ArrayList;

public abstract class DrawableManager implements IDrawableManager {

    protected ArrayList<Sprite> sprites;
    private boolean isSelected = false;

    public DrawableManager() {
        this.sprites = new ArrayList<>();
    }

    @Override
    public void drawSprites() {
        int half_size = this.sprites.size() / 2;
        int offset = this.isSelected ? half_size : 0;

        for (int i = offset; i < half_size + offset; i ++) {
            this.sprites.get(i).draw();
        }
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

}
