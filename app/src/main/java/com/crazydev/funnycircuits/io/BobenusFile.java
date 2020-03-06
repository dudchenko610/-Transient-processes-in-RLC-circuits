package com.crazydev.funnycircuits.io;

import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;

public class BobenusFile extends File {

    public BobenusFile(String pathname) {
        super(pathname);
    }


    @Override
    public boolean delete() {

        if (this.isDirectory()) {
            String[] fileNames = this.list(new FilenameFilter() {

                @Override
                public boolean accept(File current, String name) {
                    return true;
                }

            });

            BobenusFile file;

            for (String fileName : fileNames) {

                file = new BobenusFile(this.getAbsolutePath() + "/" + fileName);

           //   Log.d("taggg", "BobenusFile " + this.getAbsolutePath() + " : " + file.getName());
           //   Log.d("taggg", "BobenusFile fileName = " + fileName) ;

                file.delete();
            }
        }

        return super.delete();
    }
}
