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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageManagerImpl implements ImageManager{

    @Inject
    protected ConfigManager configManager;

    protected static final Logger logger = LogManager.getLogger();




    @Override
    public Image getCardSetIcon(String code) throws FileNotFoundException {
        String name = this.configManager.getImageDirectory().getAbsolutePath()
                + File.separator + "extensions" + File.separator + "icons"
                + File.separator + code + ".png";
        File f = new File(name);
        if(f.exists()){
            return new Image(new FileInputStream(f));
        }
        else{
            logger.warn("Image file not found: " + name);
            return null;
        }
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


}
