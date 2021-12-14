package com.sbm4j.hearthstone.myhearthstone.services;

import java.io.File;

public abstract class AbstractImageManager {

    protected File root;

    public AbstractImageManager(File root){
        this.root = root;
    }

    public void emptySubDirectory(File folder){
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
