package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardDetail;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardDetailsTooltipViewModel implements ViewModel, Initializable {

    /* cardImage property */
    private ObjectProperty<Image> cardImage = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getCardImageProperty(){return this.cardImage;}
    public Image getCardImage(){return this.cardImage.get();}
    public void setCardImage(Image value){this.cardImage.set(value);}

    /* cardSetLogoImage property */
    private ObjectProperty<Image> cardSetLogoImage = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getCardSetLogoImageProperty(){return this.cardSetLogoImage;}
    public Image getCardSetLogoImage(){return this.cardSetLogoImage.get();}
    public void setCardSetLogoImage(Image value){this.cardSetLogoImage.set(value);}

    @Inject
    protected CardImageManager cardImageManager;

    @Inject
    protected ImageManager imageManager;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showCardDetails(CardDetail cardDetail){
        Image cardImg = cardImageManager.getBigCardImage(cardDetail.getId(), false);
        this.setCardImage(cardImg);
        try {
            Image setImg = imageManager.getCardSetLogo(cardDetail.getCardSet().getCode());
            this.setCardSetLogoImage(setImg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
