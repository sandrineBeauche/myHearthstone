package com.sbm4j.hearthstone.myhearthstone.services.images;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.download.DownloadManager;
import javafx.scene.image.Image;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class CardImageManagerImpl extends AbstractImageManager implements CardImageManager {

    private File bigImagesDir;

    private File smallImagesDir;

    private File tileImagesDir;

    private File thumbsImagesDir;

    private Image alternateCardImage;

    protected HashMap<String, Image> thumbsCache = new HashMap<String, Image>();

    protected HashMap<String, Image> bigsCache = new HashMap<>();

    @Inject
    protected DownloadManager downloadManager;

    @Inject
    protected ConfigManager config;

    protected static final Logger logger = LogManager.getLogger();

    public CardImageManagerImpl(){super();}


    protected File root;

    @Inject
    public void init(){
        this.bigImagesDir = this.config.getBigImagesDir();
        if(!this.bigImagesDir.exists()){
            this.bigImagesDir.mkdirs();
        }
        this.smallImagesDir = this.config.getSmallImagesDir();
        if(!this.smallImagesDir.exists()){
            this.smallImagesDir.mkdirs();
        }
        this.tileImagesDir = this.config.getTileImagesDir();
        if(!this.tileImagesDir.exists()){
            this.tileImagesDir.mkdirs();
        }
        this.thumbsImagesDir = this.config.getThumbsImagesDir();
        if(!this.thumbsImagesDir.exists()){
            this.thumbsImagesDir.mkdirs();
        }

        this.alternateCardImage = this.config.getAlternateCardImage();
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
            File result = this.downloadManager.downloadFile(url, filename);

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

    @Override
    public void downloadCardImages(String cardId){
        if(cardId != null) {
            this.downloadCardImage(cardId, DownloadManager.bigCardImagesUrl, this.bigImagesDir);
            this.downloadCardImage(cardId, DownloadManager.smallCardImagesUrl, this.smallImagesDir);
            this.downloadCardImage(cardId, DownloadManager.tileCardImagesUrl, this.tileImagesDir);
        }
    }

    protected Image imageFilename(File parent, String cardId, boolean alternate) {
        File result =  new File(parent, cardId + ".png");
        if(alternate && !result.exists()){
            return this.alternateCardImage;
        }
        else{
            try {
                return new Image(new FileInputStream(result));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    protected File imageAsFile(File parent, String cardId) {
        File result =  new File(parent, cardId + ".png");
        return result;
    }

    @Override
    public Image getBigCardImage(String cardId, boolean alternate) {
        if(this.bigsCache.containsKey(cardId)){
            return this.bigsCache.get(cardId);
        }
        else{
            Image result = imageFilename(this.bigImagesDir, cardId, alternate);
            if(result != this.alternateCardImage){
                this.bigsCache.put(cardId, result);
            }
            return result;
        }
    }

    @Override
    public Image getSmallCardImage(String cardId, boolean alternate) {
        return imageFilename(this.smallImagesDir, cardId, alternate);
    }

    @Override
    public Image getTileCardImage(String cardId, boolean alternate) {
        return imageFilename(this.tileImagesDir, cardId, alternate);
    }

    @Override
    public Image getThumbnailCardImage(String cardId, boolean alternate) {
        if(this.thumbsCache.containsKey(cardId)){
            return this.thumbsCache.get(cardId);
        }
        else{
            Image result = imageFilename(this.thumbsImagesDir, cardId, alternate);
            if(result != this.alternateCardImage){
                this.thumbsCache.put(cardId, result);
            }
            return result;
        }
    }



    @Override
    public File getBigCardImageAsFile(String cardId) {
        return imageAsFile(this.bigImagesDir, cardId);
    }

    @Override
    public File getSmallCardImageAsFile(String cardId) {
        return imageAsFile(this.smallImagesDir, cardId);
    }

    @Override
    public File getTileCardImageAsFile(String cardId) {
        return imageAsFile(this.tileImagesDir, cardId);
    }

    @Override
    public File getThumbnailCardImageAsFile(String cardId) {
        return imageAsFile(this.thumbsImagesDir, cardId);
    }

    @Override
    public void deleteImagesFromCard(String cardId){
        if(cardId != null) {
            File fBig = this.getBigCardImageAsFile(cardId);
            if (fBig.exists()) {
                fBig.delete();
            }

            File fSmall = this.getSmallCardImageAsFile(cardId);
            if (fSmall.exists()) {
                fSmall.delete();
            }

            File fTile = this.getTileCardImageAsFile(cardId);
            if (fTile.exists()) {
                fTile.delete();
            }

            File fThumb = this.getThumbnailCardImageAsFile(cardId);
            if(fThumb.exists()){
                fThumb.delete();
            }
        }

    }

    @Override
    public void deleteAllCards(){
        this.emptySubDirectory(this.bigImagesDir);
        this.emptySubDirectory(this.smallImagesDir);
        this.emptySubDirectory(this.tileImagesDir);
        this.emptySubDirectory(this.thumbsImagesDir);
    }

    @Override
    public void clearThumbsCache() {
        this.thumbsCache.clear();
    }

    @Override
    public void clearBigsCache() {
        this.bigsCache.clear();
    }

}
