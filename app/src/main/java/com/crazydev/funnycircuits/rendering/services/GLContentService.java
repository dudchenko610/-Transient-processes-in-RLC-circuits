package com.crazydev.funnycircuits.rendering.services;

import com.crazydev.funnycircuits.electronic.World;
import com.crazydev.funnycircuits.io.Assets;
import com.crazydev.funnycircuits.math.Vector2D;
import com.crazydev.funnycircuits.math.Vector3D;
import com.crazydev.funnycircuits.rendering.ShaderProgram;
import com.crazydev.funnycircuits.rendering.sprites.Sprite;
import com.crazydev.funnycircuits.rendering.sprites.TexturedSprite;
import com.crazydev.funnycircuits.rendering.VertexBatcher;
import com.crazydev.funnycircuits.rendering.interfaces.IGLContentService;

import java.util.HashMap;

public class GLContentService implements IGLContentService {

    protected HashMap<String, Sprite> numbersSprites = new HashMap<String, Sprite>();
    protected ShaderProgram shaderProgram;
    protected VertexBatcher vertexBatcher;
    protected World electronicWorld;

    protected float [] axesVerts = {-20000, 0, 20000, 0, 0, -20000, 0, 20000};
    protected Vector3D axesColor = new Vector3D(200 / 255.0f, 200 / 255.0f, 200 / 255.0f); // 128
    protected Vector3D gridColor = new Vector3D(230 / 255.0f, 230 / 255.0f, 230 / 255.0f);

    public GLContentService() {
        this.shaderProgram   = ShaderProgram.getInstance();
        this.vertexBatcher   = VertexBatcher.getInstance();
        this.electronicWorld = World.getInstance();
    }

    @Override
    public void drawContent() {
        this.depictDecartGrid();
        vertexBatcher.depictPointsAndLines();
        vertexBatcher.depictSpritesTextured(Assets.digits);

        this.electronicWorld.draw();

    }

    @Override
    public void initialize() {
        Sprite sprite;

        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_m_b, new Vector2D(1, 1), 2, 4);
        numbersSprites.put("-", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_0_b, new Vector2D(1, 1), 2, 4);
        numbersSprites.put("0", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_1_b, new Vector2D(3, 1), 2, 4);
        numbersSprites.put("1", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_2_b, new Vector2D(5, 1), 2, 4);
        numbersSprites.put("2", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_3_b, new Vector2D(7, 1), 2, 4);
        numbersSprites.put("3", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_4_b, new Vector2D(9, 1), 2, 4);
        numbersSprites.put("4", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_5_b, new Vector2D(11, 1), 2, 4);
        numbersSprites.put("5", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_6_b, new Vector2D(13, 1), 2, 4);
        numbersSprites.put("6", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_7_b, new Vector2D(15, 1), 2, 4);
        numbersSprites.put("7", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_8_b, new Vector2D(17, 1), 2, 4);
        numbersSprites.put("8", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_9_b, new Vector2D(19, 1), 2, 4);
        numbersSprites.put("9", sprite);
        sprite = new TexturedSprite(vertexBatcher, Assets.digitsRegion_p_b, new Vector2D(21, 1), 2, 4);
        numbersSprites.put("p", sprite);
    }

    protected void depictDecartGrid() {

        float w = shaderProgram.ACTUAL_WIDTH;
        float h = shaderProgram.ACTUAL_HEIGHT;

        float x_c = shaderProgram.left   + (shaderProgram.right - shaderProgram.left  ) / 2;
        float y_c = shaderProgram.bottom + (shaderProgram.top   - shaderProgram.bottom) / 2;

        float x_left  = x_c - w / 2.0f;
        float x_right = x_c + w / 2.0f;

        float y_bottom = y_c - h / 2.0f;
        float y_top    = y_c + h / 2.0f;

        int x_l = ((int) x_left)  + (x_left < 0  ? 0 : 1);
        int x_r = ((int) x_right) + (x_right < 0 ? 1 : 0);

        int y_b = ((int) y_bottom) + (y_bottom < 0  ? 0 : 1);
        int y_t = ((int) y_top)    + (y_top < 0 ? 1 : 0);

        int gridLen = 0;
        for (int i = x_l; i <= x_r; i ++) {

            vertexBatcher.addLine(i, y_bottom, i, y_top, gridColor, 1.0f);
        }

        for (int i = y_b; i <= y_t; i ++) {
            vertexBatcher.addLine(x_left, i, x_right, i, gridColor, 1.0f);

        }

        vertexBatcher.addLine(axesVerts[0], axesVerts[1], axesVerts[2], axesVerts[3], axesColor, 1.0f);
        vertexBatcher.addLine(axesVerts[4], axesVerts[5], axesVerts[6], axesVerts[7], axesColor, 1.0f);

        float r_x = shaderProgram.ACTUAL_WIDTH  / 70;
        float r_y = shaderProgram.ACTUAL_HEIGHT / 70;

        if (r_x > r_y) {
            r_y *= 1.8f;
            r_x = 0.7f * r_y;
        } else {
            r_y = r_x / 0.7f;
        }


        int prec = -100000;

        for (int i = y_b - 1; i <= y_t + 1; i ++) {

            if (i == 0) {
                continue;
            }

            int j = Math.abs(i);
            double piParrams = (j / Math.PI) - ((int) (j / Math.PI)) ;
            int p_num = (int) (i / Math.PI);
            String p_label = p_num + "p";

            String num = String.valueOf(i);
            float x_offset = 0;

            if (x_right - num.length() * r_x - r_x < 0) {
                x_offset = x_right - num.length() * r_x ;
            }

            if (x_left > 0) {
                x_offset = x_left + r_x;
            }

            if (x_left < 0 && x_right - num.length() * r_x - r_x > 0) {
                x_offset = r_x;
            }

            float n_p_offset = 0;
            float p_offset   = -1.0f * p_label.length() * r_x - r_x;

            if (x_left + p_label.length() * r_x + r_x > 0) {
                n_p_offset = x_left + 1.1f *r_x + r_x + num.length() * r_x;

                p_offset = n_p_offset;

            } else {
                p_offset += x_offset;

            }


            if (shaderProgram.getZoom() > 3) {
                if (i % 10 == 0) {
                    depictNumber(num, x_offset, i + r_y / 1.2f, r_x, r_y);
                }

                continue;
            }

            if (shaderProgram.getZoom() > 2) {
                if (i % 5 == 0) {
                    depictNumber(num, x_offset, i + r_y / 1.2f, r_x, r_y);

                }

                continue;
            }

            if (shaderProgram.getZoom() > 1) {
                if (i % 2 == 0) {
                    depictNumber(num, x_offset, i + r_y / 1.2f, r_x, r_y);

                }

                continue;
            } else {

                depictNumber(num, x_offset, i + r_y / 1.2f, r_x, r_y);
            }


        }

        prec = -10000;

        for (int i = x_l - 1; i <= x_r + 1; i ++) {

            if (i == 0) {
                continue;
            }

            String num = String.valueOf(i);

            int j = Math.abs(i);
            double piParrams = (j / Math.PI) -  ((int) (j / Math.PI)) ;
            int p_num = (int) (i / Math.PI);
            String p_label = p_num + "p";

            float y_offset = 0;
            float r_y_2 = r_y / 1.2f;

            if (y_top - num.length() * r_y_2 - r_y_2 < 0) {
                y_offset = y_top -  r_y_2 ;
            }

            if (y_bottom > 0) {
                y_offset = y_bottom + r_y_2;
            }

            if (y_bottom < 0 && y_top - r_y_2 - r_y_2 > 0) {
                y_offset = r_y_2;
            }


            float n_p_offset = 0;
            float p_offset   = y_offset;

            if (y_bottom + r_y > 0) {
                n_p_offset = y_bottom +  4*r_y;

                p_offset = n_p_offset;

            } else {
                p_offset = y_offset;
            }

            if (shaderProgram.getZoom() > 3) {
                if (i % 10 == 0) {
                    depictNumber(num, i + r_x / 1f, y_offset, r_x, r_y);
                }


                continue;
            }

            if (shaderProgram.getZoom() > 2) {
                if (i % 5 == 0) {
                    depictNumber(num, i + r_x / 1f, y_offset, r_x, r_y);
                }


                continue;
            }

            if (shaderProgram.getZoom() > 1) {
                if (i % 2 == 0) {
                    depictNumber(num, i + r_x / 1f, y_offset, r_x, r_y);
                }


                continue;
            }

            depictNumber(num, i + r_x / 1f, y_offset, r_x, r_y);

        }

        depictNumber("0", r_x, r_y, r_x, r_y);

    }

    protected void depictNumber(String num, float x, float y, float w, float h) {

        float offset = 0;
        Sprite sprite;
        for (int i = 0; i < num.length(); i++) {

            char c = num.charAt(i);

            sprite = numbersSprites.get(String.valueOf(c));

            if (num.charAt(i) == '-') {
                sprite.setPosition(x + offset - w / 2.2f, y, w / 1.8f, h / 12);
                offset += w / 2.5f;
            } else {
                sprite.setPosition(x + offset, y, w, h);
                offset += w;
            }

            sprite.draw();
        }
    }
}
