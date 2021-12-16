package com.sbm4j.hearthstone.myhearthstone.services;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class CardImageManager extends AbstractImageManager{

    private static String bigImagesDirectoryName = "bigCards";

    private static String smallImagesDirectoryName = "smallCards";

    private static String tileImagesDirectoryName = "tilesCards";

    private static String thumbnailsDirectoryName = "thumbsCards";

    private File bigImagesDir;

    private File smallImagesDir;

    private File tileImagesDir;

    private File thumbsImagesDir;

    private File alternateCardImage;

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
        this.thumbsImagesDir = new File(root, thumbnailsDirectoryName);
        if(!this.thumbsImagesDir.exists()){
            this.thumbsImagesDir.mkdir();
        }

        this.alternateCardImage = new File(this.getClass().getResource("backCardClassic.jpg").getFile());

        this.downloadManager = new DownloadImageManager();
    }


    protected void generateCardThumbnail(File image){
        int width = 170;
        int height = (int) (width * 1.5);
        File out = new File(this.thumbsImagesDir, image.getName());
        try {
            logger.info("Generating thumbnail for the image " + image.getAbsolutePath());
            Thumbnails.of(image).size(width, height).toFile(out);
        }
        catch(IOException ex){
            logger.error("Error when generating thumbnail for the image " + image.getName());
        }
    }


    protected void downloadCardImage(String cardId, String serverUri, File localDirectory){
        String url = serverUri + cardId + ".png";
        String filename = localDirectory.toString() + File.separator + cardId + ".png";
        try {
            File result = this.downloadManager.downloadImageFile(url, filename);

            if(serverUri.contains("512x")){
                this.generateCardThumbnail(result);
            }
        }
        catch (IOException e) {
            logger.error("Error when downloading " + url, e);
            if(serverUri.contains("frFR")){
                logger.info("Trying to download enUS version");
                String server = serverUri.replace("frFR", "enUS");
                this.downloadCardImage(cardId, server, localDirectory);
            }
        }
    }

    public void downloadCardImages(String cardId){
        if(cardId != null) {
            this.downloadCardImage(cardId, DownloadImageManager.bigCardImagesUrl, this.bigImagesDir);
            this.downloadCardImage(cardId, DownloadImageManager.smallCardImagesUrl, this.smallImagesDir);
            this.downloadCardImage(cardId, DownloadImageManager.tileCardImagesUrl, this.tileImagesDir);
        }
    }

    protected File imageFilename(File parent, String cardId, boolean alternate){
        File result =  new File(parent, cardId + ".png");
        if(alternate && !result.exists()){
            return this.alternateCardImage;
        }
        else{
            return result;
        }
    }

    public File getBigCardImage(String cardId, boolean alternate){
        return imageFilename(this.bigImagesDir, cardId, alternate);
    }

    public File getSmallCardImage(String cardId, boolean alternate){
        return imageFilename(this.smallImagesDir, cardId, alternate);
    }

    public File getTileCardImage(String cardId, boolean alternate){
        return imageFilename(this.tileImagesDir, cardId, alternate);
    }

    public File getThumbnailCardImage(String cardId, boolean alternate){
        return imageFilename(this.thumbsImagesDir, cardId, alternate);
    }

    public void deleteImagesFromCard(String cardId){
        if(cardId != null) {
            File fBig = getBigCardImage(cardId, false);
            if (fBig.exists()) {
                fBig.delete();
            }

            File fSmall = getSmallCardImage(cardId, false);
            if (fSmall.exists()) {
                fSmall.delete();
            }

            File fTile = getTileCardImage(cardId, false);
            if (fTile.exists()) {
                fTile.delete();
            }

            File fThumb = getThumbnailCardImage(cardId, false);
            if(fThumb.exists()){
                fThumb.delete();
            }
        }

    }

    public void deleteAllCards(){
        this.emptySubDirectory(this.bigImagesDir);
        this.emptySubDirectory(this.smallImagesDir);
        this.emptySubDirectory(this.tileImagesDir);
        this.emptySubDirectory(this.thumbsImagesDir);
    }

}
