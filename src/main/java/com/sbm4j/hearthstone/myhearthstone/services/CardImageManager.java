package com.sbm4j.hearthstone.myhearthstone.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class CardImageManager extends AbstractImageManager{

    private static String bigImagesDirectoryName = "bigCards";

    private static String smallImagesDirectoryName = "smallCards";

    private static String tileImagesDirectoryName = "tilesCards";

    private File bigImagesDir;

    private File smallImagesDir;

    private File tileImagesDir;

    protected DownloadImageManager downloadManager;

    protected static final Logger logger = LogManager.getLogger();

    public CardImageManager(File root){
        super(root);
        this.bigImagesDir = new File(root, bigImagesDirectoryName);
        if(!this.bigImagesDir.exists()){
            this.bigImagesDir.mkdir();
        }
        this.smallImagesDir = new File(root, smallImagesDirectoryName);
        if(!this.smallImagesDir.exists()){
            this.smallImagesDir.mkdir();
        }
        this.tileImagesDir = new File(root, tileImagesDirectoryName);
        if(!this.tileImagesDir.exists()){
            this.tileImagesDir.mkdir();
        }
        this.downloadManager = new DownloadImageManager();
    }


    protected void downloadCardImage(String cardId, String serverUri, File localDirectory){
        String url = serverUri + cardId + ".png";
        String filename = localDirectory.toString() + File.separator + cardId + ".png";
        try {
            this.downloadManager.downloadImageFile(url, filename);
        } catch (IOException e) {
            logger.error("Error when downloading " + url, e);
        }
    }

    public void downloadCardImages(String cardId){
        this.downloadCardImage(cardId, DownloadImageManager.bigCardImagesUrl, this.bigImagesDir);
        this.downloadCardImage(cardId, DownloadImageManager.smallCardImagesUrl, this.smallImagesDir);
        this.downloadCardImage(cardId, DownloadImageManager.tileCardImagesUrl, this.tileImagesDir);
    }

    protected File imageFilename(File parent, String cardId){
        return new File(parent, cardId + ".png");
    }

    public File getBigCardImage(String cardId){
        return imageFilename(this.bigImagesDir, cardId);
    }

    public File getSmallCardImage(String cardId){
        return imageFilename(this.smallImagesDir, cardId);
    }

    public File getTileCardImage(String cardId){
        return imageFilename(this.tileImagesDir, cardId);
    }

    public void deleteImagesFromCard(String cardId){
        File fBig = getBigCardImage(cardId);
        if(fBig.exists()){
            fBig.delete();
        }

        File fSmall = getSmallCardImage(cardId);
        if(fSmall.exists()){
            fSmall.delete();
        }

        File fTile = getTileCardImage(cardId);
        if(fTile.exists()){
            fTile.delete();
        }
    }

    public void deleteAllCards(){
        this.emptySubDirectory(this.bigImagesDir);
        this.emptySubDirectory(this.smallImagesDir);
        this.emptySubDirectory(this.tileImagesDir);
    }

}
