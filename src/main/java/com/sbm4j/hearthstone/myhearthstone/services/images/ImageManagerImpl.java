package com.sbm4j.hearthstone.myhearthstone.services.images;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.cardClasses.CardClassImageLoader;
import com.sbm4j.hearthstone.myhearthstone.services.images.mana.ManaImageLoader;
import com.sbm4j.hearthstone.myhearthstone.services.images.rarity.RarityImageLoader;
import com.sbm4j.hearthstone.myhearthstone.views.ManaOption;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class ImageManagerImpl implements ImageManager{

    @Inject
    protected ConfigManager configManager;

    protected static final Logger logger = LogManager.getLogger();


    protected HashMap<String, Image> cardSetIcon = new HashMap<String, Image>();

    protected HashMap<String, Image> cardSetLogo = new HashMap<String, Image>();

    protected HashMap<String, Image> heroIcon = new HashMap<String, Image>();

    protected HashMap<String, Image> heroPortrait = new HashMap<String, Image>();

    protected HashMap<String, Image> resourceImage = new HashMap<>();


    protected Image getImage(String code, HashMap<String,Image> cache, String directoryName) throws FileNotFoundException {
        if(cache.containsKey(code)){
            return cache.get(code);
        }
        else {
            String name = this.configManager.getImageDirectory().getAbsolutePath()
                    + File.separator + directoryName + File.separator + code + ".png";
            File f = new File(name);
            if (f.exists()) {
                Image result = new Image(new FileInputStream(f));
                cache.put(code, result);
                return result;
            } else {
                logger.warn("Image file not found: " + name);
                cache.put(code, null);
                return null;
            }
        }
    }



    @Override
    public Image getCardSetIcon(String code) throws FileNotFoundException {
        return getImage(code, cardSetIcon, "extensions" + File.separator + "icons");
    }

    @Override
    public Image getCardSetLogo(String code) throws FileNotFoundException {
        return getImage(code, cardSetLogo, "extensions" + File.separator + "logos");
    }


    @Override
    public Image getHeroIcon(String code) throws FileNotFoundException {
        return getImage(code, heroIcon, "heros" + File.separator + "icons");
    }

    @Override
    public Image getHeroPortrait(String code) throws FileNotFoundException {
        return getImage(code, heroPortrait, "heros" + File.separator + "big");
    }


    @Override
    public Image getIconImage(Class<?> clazz, String code) throws FileNotFoundException {
        if(clazz == CardClass.class){
            return CardClassImageLoader.getImage(code);
        }
        if(clazz == CardSet.class){
            return getCardSetIcon(code);
        }
        if(clazz == Rarity.class){
            return RarityImageLoader.getImage(code);
        }
        if(clazz == ManaOption.class){
            return ManaImageLoader.getImage(code);
        }
        return null;
    }

    @Override
    public ImageView getImageViewFromResource(Class ownerClazz, String resourceName){
        Image img = null;
        if(this.resourceImage.containsKey(resourceName)){
            img = this.resourceImage.get(resourceName);
        }
        else{
            InputStream in = ownerClazz.getResourceAsStream(resourceName);
            if(in != null) {
                img = new Image(in);
                this.resourceImage.put(resourceName, img);
            }
            else{
                logger.error("Image resource not found: " + resourceName);
                return null;
            }
        }
        return new ImageView(img);
    }
}
