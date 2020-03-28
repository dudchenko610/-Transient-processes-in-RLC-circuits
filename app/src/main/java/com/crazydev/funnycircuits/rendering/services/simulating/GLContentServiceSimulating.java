package com.crazydev.funnycircuits.rendering.services.simulating;

import android.util.Log;

import com.crazydev.funnycircuits.electronic.math.DoubleArrayStructure;
import com.crazydev.funnycircuits.io.Assets;
import com.crazydev.funnycircuits.math.OverlapTester;
import com.crazydev.funnycircuits.math.Rectangle;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.sprites.ColoredSprite;
import com.crazydev.funnycircuits.rendering.services.GLContentService;
import com.crazydev.funnycircuits.rendering.sprites.ItemColoredSprite;

import java.util.ArrayList;

public class GLContentServiceSimulating extends GLContentService {

    protected Rectangle handleRectangle;
    private ColoredSprite handleColoredSprite;
    private float yDHandle = 0;


    protected Rectangle workingAreaRectangle;
    private ColoredSprite workingColoredSprite;
    private float yDWorkingArea;


    private ColoredSprite sliderColoredSprite;

    protected ArrayList<Item> items;

    private DoubleArrayStructure doubleArrayStructure;
    private Vector3D color = new Vector3D(1, 0, 0);
    private Vector3D colorGrid = new Vector3D(89.0f / 255.0f, 91.0f / 255.0f, 93.0f / 255.0f);
    private Vector3D colorAxe = new Vector3D(43.0f / 255.0f, 43.0f / 255.0f, 43.0f / 255.0f);

    public GLContentServiceSimulating() {
        super();

        this.handleRectangle = new Rectangle();
        this.workingAreaRectangle = new Rectangle();

        this.items = new ArrayList<Item>();


        this.doubleArrayStructure = new DoubleArrayStructure();

        double dx = 1e-2;
        for (int i = 0; i < 350; i ++) {
            this.doubleArrayStructure.add(Math.cos(i * dx * 4) * dx * i);
        }
    }

    @Override
    public void initialize() {
        super.initialize();

        this.handleColoredSprite = new ColoredSprite(vertexBatcher,
                new Vector3D(43.0f / 255.0f, 43.0f / 255.0f, 43.0f / 255.0f), 1.0f, new Vector2D(0, 0), 0, 0);

        this.workingColoredSprite = new ColoredSprite(vertexBatcher,
                new Vector3D(60.0f / 255.0f, 63.0f / 255.0f, 65.0f / 255.0f), 0.6f, new Vector2D(0, 0), 0, 0);

        this.sliderColoredSprite = new ColoredSprite(vertexBatcher,
                new Vector3D(78.0f / 255.0f, 78.0f / 255.0f, 78.0f / 255.0f), 0.9f, new Vector2D(0, 0), 0, 0);

        float w = shaderProgram.ACTUAL_WIDTH;
        float h = shaderProgram.ACTUAL_HEIGHT;

        float height = h / 14;

        float x_c = shaderProgram.left   + (shaderProgram.right - shaderProgram.left  ) / 2;
        float y_c = shaderProgram.bottom + (shaderProgram.top   - shaderProgram.bottom) / 2;

        this.yDHandle = (- h / 2) / h;

        float y_offset = h / 80;
        float heightItem = h / 4;

        float y_b = y_c + this.yDHandle * h - y_offset - height * 0.5f - heightItem * 0.5f;

        ColoredSprite itemColoredSprite;
        ColoredSprite slider;

        Item item;
        for (int i = 0; i < 5; i ++) {
            itemColoredSprite = new ColoredSprite(vertexBatcher,
                    new Vector3D(49.0f / 255.0f, 51.0f / 255.0f, 53.0f / 255.0f), 0.8f, new Vector2D(0, 0), 0, 0);
            itemColoredSprite.setPosition(x_c, y_b - i * y_offset - i * heightItem, w, heightItem);

            slider = new ColoredSprite(vertexBatcher,
                    new Vector3D(89.0f / 255.0f, 91.0f / 255.0f, 93.0f / 255.0f), 0.9f, new Vector2D(0, 0), 0, 0);

            item = new Item();
            item.itemColSpr = itemColoredSprite;
            item.slider = slider;

            this.items.add(item);
        }

        this.recalculateSizes();

    }

    private float height;
    private float y_offset;
    private float heightItem;

    private float handle_y_center;
    private float handle_y_bottom;
    private float slider_offset;

    @Override
    public void drawContent() {
        super.drawContent();

        vertexBatcher.clearVerticesBufferTexture();
        vertexBatcher.clearVerticesBufferColor_Markers();

        float w = shaderProgram.ACTUAL_WIDTH;
        float h = shaderProgram.ACTUAL_HEIGHT;

        height = h / 14;
        y_offset = h / 200;
        heightItem = h / 2.5f;
        slider_offset = h / 80;

        float dx = w / 300;
        float side_offset = slider_offset * 1.2f;
        float text_bottom_offset = h / 300;


        float x_c = shaderProgram.left   + (shaderProgram.right - shaderProgram.left  ) / 2;
        float y_c = shaderProgram.bottom + (shaderProgram.top   - shaderProgram.bottom) / 2;
        float y_bottom_screen = shaderProgram.bottom;

        handle_y_center = y_c + this.yDHandle * h;
        handle_y_bottom = handle_y_center - height * 0.5f;


        float x_u = handle_y_bottom + this.yDWorkingArea * h;
        float hWorkingAreaSize = this.items.size() * (y_offset + heightItem);

        float x_l = x_u - hWorkingAreaSize;

        float workingAHeight = handle_y_bottom - y_bottom_screen; // workingAreaRectangle.corners[3].y - workingAreaRectangle.corners[0].y;


        if (x_l > y_bottom_screen) {
            this.yDWorkingArea = (y_bottom_screen - handle_y_bottom + hWorkingAreaSize) / h;
        }

        if (hWorkingAreaSize <= workingAHeight) {
            this.yDWorkingArea = 0;
        }


        this.workingAreaRectangle.setRectangle(x_c, (handle_y_bottom + y_bottom_screen) * 0.5f, w, workingAHeight);
        this.workingColoredSprite.setPosition(this.workingAreaRectangle);

        this.workingColoredSprite.draw();


        float y_b = handle_y_bottom - heightItem * 0.5f + this.yDWorkingArea * h;

        ColoredSprite itemColoredSprite;
        Item item;

        float y_center_item;


        for (int i = 0; i < this.items.size(); i ++) {
            item = this.items.get(i);
            itemColoredSprite = item.itemColSpr;
            item.visible = false;

            y_center_item = y_b - i * y_offset - i * heightItem;


            item.rectangle.setRectangle(x_c, y_center_item, w, heightItem);

            float y_before = item.rectangle.corners[3].y;

            if (OverlapTester.getOverlap(this.workingAreaRectangle, item.rectangle) != null) {
                item.visible = true;
                itemColoredSprite.setPosition(item.rectangle);
                itemColoredSprite.draw();

                int ww = this.doubleArrayStructure.getTotalSize();
                float width = (this.doubleArrayStructure.getTotalSize() + 20) * dx;
                float start = shaderProgram.left;
                float x_pos = start + item.xDItem * w;
                float x_end = x_pos + width;

                float workingWidth = shaderProgram.right - shaderProgram.left;

               /* if (x_end < shaderProgram.right) {
                    itemColoredSprite.xDItem = (shaderProgram.right - width - start) / w;
                }*/

                int offfset = (int) ((shaderProgram.left - x_pos) / dx);



                for (int j = 3 + offfset / 10; j < offfset / 10 + 32; j ++) {
                    vertexBatcher.addLine(x_pos + j * dx * 10, item.rectangle.corners[0].y + 2 * dx * 10,
                                          x_pos + j * dx * 10, item.rectangle.corners[3].y,
                                                colorGrid, 0.3f);

                    if ((j - 2) % 5 == 0) {
                        String label = (j - 2) / 10 + "";

                        if ((j - 2) % 10 == 0) {
                            label += " ms";
                        } else {
                            label += ".5 ms";
                        }

                        Assets.BLACK_FONT.drawTextCenteredUpper(label, x_pos + j * dx * 10,
                                item.rectangle.corners[0].y + 2 * dx * 10 - text_bottom_offset,
                                0.008f, 0.008f);
                    }


                }


                vertexBatcher.addLine(item.rectangle.corners[0].x + 2 * dx * 10, item.rectangle.corners[0].y + 1 * dx * 10,
                        item.rectangle.corners[0].x + 2 * dx * 10, item.rectangle.corners[3].y,
                        colorAxe, 0.9f);

                int verticalAmount = (int) ((item.rectangle.corners[3].y - item.rectangle.corners[0].y) / (dx * 10));
                for (int j = 2; j < verticalAmount + 1; j ++) {
                     vertexBatcher.addLine(item.rectangle.corners[0].x + 2 * dx * 10, item.rectangle.corners[0].y + j * dx * 10,
                             item.rectangle.corners[2].x, item.rectangle.corners[0].y + j * dx * 10,
                             colorGrid, 0.3f);

                }

                int vv = (((int) (heightItem / (dx * 10))) - 1) / 2;

                if (item.rectangle.corners[0].y +  (2 + vv) * dx * 10 <= handle_y_bottom) {
                    vertexBatcher.addLine(item.rectangle.corners[0].x + 1 * dx * 10, item.rectangle.corners[0].y +  (2 + vv) * dx * 10,
                            item.rectangle.corners[2].x, item.rectangle.corners[0].y + (2 + vv) * dx * 10,
                            colorAxe, 0.9f);
                }



              //  Log.d("offfset", offfset + "");

                float center_y_graph_line = item.rectangle.corners[0].y + (2 + vv) * dx * 10;
                float hh = (vv - 1) * 10 * dx;
                float absoluteMaxInverse = (float) (1 /  this.doubleArrayStructure.getAbsoluteMax());

                int mm = ww > offfset + 300 ? offfset + 300 : ww;

                if (y_before == item.rectangle.corners[3].y) {


                    for (int j = offfset + 1; j < mm; j ++) {
                        vertexBatcher.addLine( x_pos + dx * (j - 1) + ((2 * dx * 10)), (float) (center_y_graph_line + this.doubleArrayStructure.get(j - 1) * absoluteMaxInverse * hh),
                                x_pos + dx * j  +((2 * dx * 10)), (float) (center_y_graph_line + this.doubleArrayStructure.get(j) * absoluteMaxInverse * hh), this.color, 1.0f);

                    }
                } else {

                    for (int j = offfset + 1; j < mm; j ++) {

                        float y_value = (float) (center_y_graph_line + this.doubleArrayStructure.get(j) * absoluteMaxInverse * hh);

                        if (y_value - handle_y_bottom > height * 0.5f) {
                            continue;
                        }

                        vertexBatcher.addLine( x_pos + dx * (j - 1) + ((2 * dx * 10)), (float) (center_y_graph_line + this.doubleArrayStructure.get(j - 1) * absoluteMaxInverse * hh),
                                x_pos + dx * j + ((2 * dx * 10)), (float) (center_y_graph_line + this.doubleArrayStructure.get(j) * absoluteMaxInverse * hh), this.color, 1.0f);

                    }

                }



                width += 0;

                float sliderWidth = workingWidth * workingWidth / width;
                if (sliderWidth < workingWidth) {

                    float x_posSlider   = shaderProgram.left + sliderWidth * 0.5f + slider_offset * 0.5f;
                    float x_bottomValue = (shaderProgram.right - width - start) / w;

                    x_posSlider += (item.xDItem / x_bottomValue) * (workingWidth - sliderWidth - slider_offset);

                    item.slider.setPosition(x_posSlider, y_center_item - heightItem * 0.5f + w / 80, sliderWidth, w / 80);
                    item.slider.draw();

                }

            }

        }

        float sliderHeight = (workingAHeight * workingAHeight / hWorkingAreaSize);

        if (sliderHeight < workingAHeight) {
            float y_posSlider   = handle_y_bottom - sliderHeight * 0.5f - slider_offset * 0.5f;
            float y_bottomValue = (y_bottom_screen - handle_y_bottom + hWorkingAreaSize) / h;

            y_posSlider -= (this.yDWorkingArea / y_bottomValue) * (workingAHeight - sliderHeight - slider_offset);

            this.sliderColoredSprite.setPosition(shaderProgram.right - w / 80, y_posSlider, w / 80, sliderHeight);
            this.sliderColoredSprite.draw();
        }




    //    this.vertexBatcher.depictSpritesTextured(Assets.UIsTexture);
      //  this.vertexBatcher.clearVerticesBufferTexture();


        this.vertexBatcher.depictSpritesColored();
        this.vertexBatcher.clearVerticesBufferColor();

        this.vertexBatcher.depictPointsAndLines();
        this.vertexBatcher.clearVerticesBufferColor_Markers();


        vertexBatcher.depictSpritesTextured(Assets.UIsTexture);
        vertexBatcher.clearVerticesBufferTexture();

        ///
        this.handleRectangle.setRectangle(x_c, handle_y_center, w, height);
        this.handleColoredSprite.setPosition(this.handleRectangle);
        this.handleColoredSprite.draw();
        ///

        this.vertexBatcher.depictSpritesColored();
        this.vertexBatcher.clearVerticesBufferColor();


    }

    void moveHandle(float yD) {

        float h = shaderProgram.ACTUAL_HEIGHT;

        float next_yDHandle = this.yDHandle + yD / h;
        float curr_height = next_yDHandle * h;


        if (curr_height < - h / 2) {
            this.yDHandle = (- h / 2) / h;
        } else if (curr_height > h * 0.5f) {
            this.yDHandle =  (h * 0.5f) / h;
        } else {
            this.yDHandle = next_yDHandle;
        }

    }

    void moveWorkingArea(float yD) {
        float h = shaderProgram.ACTUAL_HEIGHT;

        float y_c = shaderProgram.bottom + (shaderProgram.top   - shaderProgram.bottom) / 2;
        float y_bottom = shaderProgram.bottom;

        height = h / 14;

        float next_yDHandle = this.yDWorkingArea + yD / h;
        float curr_height = next_yDHandle * h;

        y_offset = h / 80;
        heightItem = h / 2.5f;

        handle_y_bottom = y_c + this.yDHandle * h - height * 0.5f;
        float x_u = handle_y_bottom + next_yDHandle * h;
        float hWorkingAreaSize = this.items.size() * (y_offset + heightItem);

        float x_l = x_u - hWorkingAreaSize;

        float workingAHeight = handle_y_bottom - y_bottom;

        if (hWorkingAreaSize <= workingAHeight) {
            this.yDWorkingArea = 0;
            return;
        }

        if (curr_height < 0) {
            this.yDWorkingArea = 0; // * 1/h
        } else if (x_l > y_bottom) {
            this.yDWorkingArea = (y_bottom - handle_y_bottom + hWorkingAreaSize) / h;

        } else {
            this.yDWorkingArea = next_yDHandle;
        }



    }

    void moveItem(Item item, float xD) {
        float w = shaderProgram.ACTUAL_WIDTH;

        float dx = w / 300;

        float nextXDItem = item.xDItem + xD / w;
        float next_width = nextXDItem * w;

        float width = (this.doubleArrayStructure.getTotalSize() + 20) * dx;
        float start = shaderProgram.left;
        float x_pos = start + next_width;
        float x_end = x_pos + width;

        if (width < shaderProgram.right - shaderProgram.left || next_width > 0) {
            item.xDItem = 0;
        } else if (x_end < shaderProgram.right) {
            item.xDItem = (shaderProgram.right - width - start) / w;
        } else {
            item.xDItem = nextXDItem;
        }

    }

    void recalculateSizes() {

    }
}
