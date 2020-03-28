package com.crazydev.funnycircuits.io;

import android.content.Context;

import com.crazydev.funnycircuits.rendering.Font;
import com.crazydev.funnycircuits.rendering.Texture;
import com.crazydev.funnycircuits.rendering.TextureRegion;


public class Assets {

    public static Texture elements;
    public static TextureRegion wireRegion;
    public static TextureRegion wireSelectionRegion;
    public static TextureRegion nodeRegion;
    public static TextureRegion nodeSelectionRegion;

    public static Texture wire_selection;

    public static Texture digits;
    public static TextureRegion digitsRegion_m_b;
    public static TextureRegion digitsRegion_0_b;
    public static TextureRegion digitsRegion_1_b;
    public static TextureRegion digitsRegion_2_b;
    public static TextureRegion digitsRegion_3_b;
    public static TextureRegion digitsRegion_4_b;
    public static TextureRegion digitsRegion_5_b;
    public static TextureRegion digitsRegion_6_b;
    public static TextureRegion digitsRegion_7_b;
    public static TextureRegion digitsRegion_8_b;
    public static TextureRegion digitsRegion_9_b;
    public static TextureRegion digitsRegion_p_b;

    public static Texture UIsTexture;
    public static Font BLACK_FONT;

    public static void load(Context context) {
        IOManager ioManager = new IOManager(context);

        UIsTexture = new Texture(ioManager, "black_font.png");
        BLACK_FONT = new Font(UIsTexture);

        elements            = new Texture(ioManager, "elements.png");
        wireRegion          = new TextureRegion(elements, 0  , 177, 222, 151);
        wireSelectionRegion = new TextureRegion(elements,0, 0, 222, 151);
        nodeRegion          = new TextureRegion(elements,24, 344, 174, 174);
        nodeSelectionRegion = new TextureRegion(elements,24, 533, 174, 174);

        wire_selection = new Texture(ioManager, "wire_selection.png");
    //    wireSelectionRegion = new TextureRegion(wire_selection,0, 0, 80, 20);

        digits = new Texture(ioManager, "digits.png");
        digitsRegion_m_b = new TextureRegion(digits, 503, 105, 92, 14);
        digitsRegion_0_b = new TextureRegion(digits, 8  , 0, 105, 169);
        digitsRegion_1_b = new TextureRegion(digits, 130, 0, 105, 169);
        digitsRegion_2_b = new TextureRegion(digits, 248, 0, 105, 169);
        digitsRegion_3_b = new TextureRegion(digits, 367, 0, 105, 169);
        digitsRegion_4_b = new TextureRegion(digits, 485, 0, 113, 169);
        digitsRegion_5_b = new TextureRegion(digits, 608, 0, 105, 169);
        digitsRegion_6_b = new TextureRegion(digits, 732, 0, 105, 169);
        digitsRegion_7_b = new TextureRegion(digits, 852, 0, 105, 169);
        digitsRegion_8_b = new TextureRegion(digits, 972, 0, 105, 169);
        digitsRegion_9_b = new TextureRegion(digits, 1091, 0, 105, 169);
        digitsRegion_p_b = new TextureRegion(digits, 1204, 0, 125, 163);

    }
}
