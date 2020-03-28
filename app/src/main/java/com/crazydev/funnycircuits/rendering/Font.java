package com.crazydev.funnycircuits.rendering;

import com.crazydev.funnycircuits.io.Assets;

public class Font {

    public final TextureRegion[] glyphsStandart;
    private VertexBatcher vertexBatcher;
    private Texture texture;
    private ShaderProgram shaderProgram;

    public Font(Texture texture) {

        this.vertexBatcher = VertexBatcher.getInstance();
        this.shaderProgram = ShaderProgram.getInstance();

        int glyphsAmount = 155;
        int englishAlphabetAmount = 26;
        int russianAlphabetAmount = 33;

        int heightUCE = 44;

        glyphsStandart = new TextureRegion[glyphsAmount];

        this.texture = texture;

        glyphsStandart[0]   = new TextureRegion(texture, 12,  16,  21, 22);   // A
        glyphsStandart[1]   = new TextureRegion(texture, 33,  16,  17, 22);   // B
        glyphsStandart[2]   = new TextureRegion(texture, 54,  16,  17, 22);   // C
        glyphsStandart[3]   = new TextureRegion(texture, 72,  16,  20, 22);   // D
        glyphsStandart[4]   = new TextureRegion(texture, 94,  16,  16, 22);   // E
        glyphsStandart[5]   = new TextureRegion(texture, 112, 16,  15, 22);   // F
        glyphsStandart[6]   = new TextureRegion(texture, 130, 16,  19, 22);   // G
        glyphsStandart[7]   = new TextureRegion(texture, 151, 16,  21, 22);   // H
        glyphsStandart[8]   = new TextureRegion(texture, 174, 16,  8,  22);   // I
        glyphsStandart[9]   = new TextureRegion(texture, 184, 16,  8,  27);   // J
        glyphsStandart[10]  = new TextureRegion(texture, 194, 16,  18, 22);   // K
        glyphsStandart[11]  = new TextureRegion(texture, 213, 16,  16, 22);   // L
        glyphsStandart[12]  = new TextureRegion(texture, 230, 16,  25, 22);   // M
        glyphsStandart[13]  = new TextureRegion(texture, 257, 16,  21, 22);   // N
        glyphsStandart[14]  = new TextureRegion(texture, 281, 16,  19, 22);   // O
        glyphsStandart[15]  = new TextureRegion(texture, 303, 16,  16, 22);   // P
        glyphsStandart[16]  = new TextureRegion(texture, 322, 16,  21, 28);   // Q
        glyphsStandart[17]  = new TextureRegion(texture, 344, 16,  18, 22);   // R
        glyphsStandart[18]  = new TextureRegion(texture, 364, 16,  13, 22);   // S
        glyphsStandart[19]  = new TextureRegion(texture, 379, 16,  18, 22);   // T
        glyphsStandart[20]  = new TextureRegion(texture, 399, 16,  20, 22);   // U
        glyphsStandart[21]  = new TextureRegion(texture, 420, 16,  22, 22);   // V
        glyphsStandart[22]  = new TextureRegion(texture, 442, 16,  28, 22);   // W
        glyphsStandart[23]  = new TextureRegion(texture, 471, 16,  20, 22);   // X
        glyphsStandart[24]  = new TextureRegion(texture, 492, 16,  20, 22);   // Y
        glyphsStandart[25]  = new TextureRegion(texture, 513, 16,  16, 22);   // Z
        // TOTALLY 26 WORDS
        glyphsStandart[26]  = new TextureRegion(texture, 13,  66,  14, 16);   // a
        glyphsStandart[27]  = new TextureRegion(texture, 27,  58,  15, 24);   // b
        glyphsStandart[28]  = new TextureRegion(texture, 44,  66,  12, 16);   // c
        glyphsStandart[29]  = new TextureRegion(texture, 58,  58,  15, 24);   // d
        glyphsStandart[30]  = new TextureRegion(texture, 75,  66,  13, 16);   // e
        glyphsStandart[31]  = new TextureRegion(texture, 90,  58,  11, 24);   // f
        glyphsStandart[32]  = new TextureRegion(texture, 101, 65,  16, 23);   // g -
        glyphsStandart[33]  = new TextureRegion(texture, 117, 58,  17, 24);   // h
        glyphsStandart[34]  = new TextureRegion(texture, 135, 59,  8,  23);   // i
        glyphsStandart[35]  = new TextureRegion(texture, 145, 59,  6,  30);   // j -
        glyphsStandart[36]  = new TextureRegion(texture, 153, 58,  16, 24);   // k
        glyphsStandart[37]  = new TextureRegion(texture, 169, 58,  8,  24);   // l
        glyphsStandart[38]  = new TextureRegion(texture, 179, 66,  25, 16);   // m
        glyphsStandart[39]  = new TextureRegion(texture, 206, 66,  17, 16);   // n
        glyphsStandart[40]  = new TextureRegion(texture, 225, 66,  14, 16);   // o
        glyphsStandart[41]  = new TextureRegion(texture, 241, 66,  15, 22);   // p -
        glyphsStandart[42]  = new TextureRegion(texture, 258, 66,  15, 22);   // q -
        glyphsStandart[43]  = new TextureRegion(texture, 275, 66,  11, 16);   // r
        glyphsStandart[44]  = new TextureRegion(texture, 287, 66,  11, 16);   // s
        glyphsStandart[45]  = new TextureRegion(texture, 300, 62,  11, 20);   // t
        glyphsStandart[46]  = new TextureRegion(texture, 311, 66,  16, 16);   // u
        glyphsStandart[47]  = new TextureRegion(texture, 328, 66,  16, 16);   // v
        glyphsStandart[48]  = new TextureRegion(texture, 344, 66,  22, 16);   // w
        glyphsStandart[49]  = new TextureRegion(texture, 366, 66,  16, 16);   // x
        glyphsStandart[50]  = new TextureRegion(texture, 382, 66,  16, 23);   // y -
        glyphsStandart[51]  = new TextureRegion(texture, 399, 66,  12, 16);   // z
        // totally 26 words
        glyphsStandart[52]  = new TextureRegion(texture, 413, 66,  8,  23);   // white_space
        glyphsStandart[53]  = new TextureRegion(texture, 160, 104, 14, 22);   // 0
        glyphsStandart[54]  = new TextureRegion(texture, 15,  104, 10, 22);   // 1
        glyphsStandart[55]  = new TextureRegion(texture, 29,  104, 13, 22);   // 2
        glyphsStandart[56]  = new TextureRegion(texture, 45,  104, 14, 22);   // 3
        glyphsStandart[57]  = new TextureRegion(texture, 61,  104, 16, 22);   // 4
        glyphsStandart[58]  = new TextureRegion(texture, 79,  102, 13, 24);   // 5
        glyphsStandart[59]  = new TextureRegion(texture, 95,  103, 14, 23);   // 6
        glyphsStandart[60]  = new TextureRegion(texture, 112, 104, 13, 22);   // 7
        glyphsStandart[61]  = new TextureRegion(texture, 128, 104, 14, 22);   // 8
        glyphsStandart[62]  = new TextureRegion(texture, 144, 104, 14, 23);   // 9

        glyphsStandart[63]  = new TextureRegion(texture, 368, 148, 6, 22);   // .

    }

    public void drawTextCenteredUpper(String text, float x, float y, float xComp, float yComp) {
        float ratio = (shaderProgram.ACTUAL_HEIGHT / shaderProgram.ACTUAL_START_HEIGHT);
        xComp = xComp * ratio;
        yComp = yComp * ratio;

        float textLength = Assets.BLACK_FONT.stringLength(text, xComp);

        float x_c = x - textLength / 2;
        float y_c = y - (Assets.BLACK_FONT.glyphsStandart[0].height) * yComp;

        this.drawText(text, x_c, y_c, xComp, yComp);
    }

    public void drawTextCentered(String text, float x, float y, float xComp, float yComp) {
        float ratio = (shaderProgram.ACTUAL_HEIGHT / shaderProgram.ACTUAL_START_HEIGHT);
        xComp = xComp * ratio;
        yComp = yComp * ratio;

        float textLength = Assets.BLACK_FONT.stringLength(text, xComp);

        float x_c = x - textLength / 2;
        float y_c = y - (Assets.BLACK_FONT.glyphsStandart[0].height / 2) * yComp;

        this.drawText(text, x_c, y_c, xComp, yComp);
    }

    // it is like draw TextureSprite
    public void drawText(String text, float x, float y, float xComp, float yComp) {

        float visible = 1;

        for (int i = 0; i < text.length(); i ++) {
            char c = text.charAt(i);


            if (c >= 97 && c <= 122) {
                int index = c - 71;

                if (index == 32 || index == 41 || index == 42) {
                    vertexBatcher.drawGlyphSprite(x, y - 6 * yComp, glyphsStandart[index].width * xComp, glyphsStandart[index].height * yComp, glyphsStandart[index], texture, visible);
                    x += glyphsStandart[index].width * xComp;
                    continue;
                }

                if (index == 35 || index == 50) {
                    vertexBatcher.drawGlyphSprite(x, y - 7 * yComp, glyphsStandart[index].width * xComp, glyphsStandart[index].height * yComp, glyphsStandart[index], texture, visible);
                    x += glyphsStandart[index].width * xComp;
                    continue;
                }

                vertexBatcher.drawGlyphSprite(x, y, glyphsStandart[index].width * xComp, glyphsStandart[index].height * yComp, glyphsStandart[index], texture, visible);
                x += glyphsStandart[index].width * xComp;
                continue;
            }

            if (c >= 65 && c <= 90) {
                int index = c - 65;

                if (index == 16) {
                    vertexBatcher.drawGlyphSprite(x, y - 6 * yComp, glyphsStandart[index].width * xComp, glyphsStandart[index].height * yComp, glyphsStandart[index], texture, visible);
                    x += glyphsStandart[index].width * xComp;
                    continue;
                }

                if (index == 9) {
                    vertexBatcher.drawGlyphSprite(x, y - 5 * yComp, glyphsStandart[index].width * xComp, glyphsStandart[index].height * yComp, glyphsStandart[index], texture, visible);
                    x += glyphsStandart[index].width * xComp;
                    continue;
                }

                vertexBatcher.drawGlyphSprite(x, y, glyphsStandart[index].width * xComp, glyphsStandart[index].height * yComp, glyphsStandart[index], texture, visible);

                x += glyphsStandart[index].width * xComp;
            }

            if (c > 47 && c < 58) {
                int index = c + 5;


                if (index == 62) {
                    vertexBatcher.drawGlyphSprite(x, y - 1 * yComp, glyphsStandart[index].width * xComp, glyphsStandart[index].height * yComp, glyphsStandart[index], texture, visible);
                    x += glyphsStandart[index].width * xComp;
                    continue;
                }

                vertexBatcher.drawGlyphSprite(x, y, glyphsStandart[index].width * xComp, glyphsStandart[index].height * yComp, glyphsStandart[index], texture, visible);
                x += glyphsStandart[index].width * xComp;

                continue;
            }

            if (c == 32) {
                vertexBatcher.drawGlyphSprite(x, y, glyphsStandart[52].width * xComp, glyphsStandart[52].height * yComp, glyphsStandart[52], texture, visible);
                x += glyphsStandart[52].width * xComp;
                continue;
            }

            if (c == '.') {
                vertexBatcher.drawGlyphSprite(x, y, glyphsStandart[63].width * xComp, glyphsStandart[63].height * yComp, glyphsStandart[63], texture, visible);
                x += glyphsStandart[63].width * xComp;
                continue;
            }

        }
    }

    public float stringLength(String text, float xComp) {

        float x = 0;

        for (int i = 0; i < text.length(); i ++) {
            int c = text.charAt(i);

            //		Log.d("newtag", "" + c);

            if (c >= 97 && c <= 122) {
                int index = c - 71;
                x += glyphsStandart[index].width * xComp;
            }

            if (c > 47 && c < 58) {
                int index = c + 5;
                x += glyphsStandart[index].width * xComp;
            }

            if (c == 32) {
                x += glyphsStandart[52].width * xComp;
            }

            if (c >= 65 && c <= 90) {
                int index = c - 65;

                x += glyphsStandart[index].width * xComp;
            }



        }

        return x;
    }


}
