package com.crazydev.funnycircuits.io;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class IOManager {

    private AssetManager assetManager;

    public IOManager (Context context) {
        this.assetManager = context.getAssets();
    }

    public InputStream readAsset(String assetFileName) throws IOException {
        return this.assetManager.open(assetFileName);
    }

    public static boolean createDir(String folder) {
        File f1 = new File(folder);

        if (!f1.exists()) {
            f1.mkdirs();
            return true;
        }

        return false;
    }

}
