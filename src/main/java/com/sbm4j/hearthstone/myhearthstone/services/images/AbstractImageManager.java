package com.sbm4j.hearthstone.myhearthstone.services.images;

import java.io.File;

public abstract class AbstractImageManager {


    protected void emptySubDirectory(File folder){
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    emptySubDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

}
