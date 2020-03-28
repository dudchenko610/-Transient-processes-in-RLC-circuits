package com.crazydev.funnycircuits.rendering.services.simulating;

import com.crazydev.funnycircuits.math.Rectangle;
import com.crazydev.funnycircuits.rendering.sprites.ColoredSprite;

public class Item {

    public ColoredSprite itemColSpr;
    public ColoredSprite slider;

    public Rectangle rectangle;
    public float xDItem;
    public boolean visible = false;

    public Item() {
        this.rectangle = new Rectangle();
    }

}
