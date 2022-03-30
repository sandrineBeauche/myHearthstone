package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardDetail;
import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.cardClasses.CardClassImageLoader;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.mapping.ModelWrapper;
import de.saxsys.mvvmfx.utils.mapping.accessorfunctions.ObjectPropertyAccessor;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardDetailsViewModel implements ViewModel, Initializable {

    @Inject
    protected CardImageManager cardImageManager;

    @Inject
    protected ImageManager imageManager;

    protected ModelWrapper<CardDetail> modelWrapper = new ModelWrapper<>();

    protected StringProperty cardNameProperty = modelWrapper.field(CardDetail::getName, CardDetail::setName);
    public StringProperty getCardNameProperty(){return this.cardNameProperty;}

    protected BooleanProperty eliteProperty = modelWrapper.field(CardDetail::getElite, CardDetail::setElite);
    public BooleanProperty getEliteProperty(){ return this.eliteProperty;}

    /* standard property */
    private BooleanProperty standard = new SimpleBooleanProperty();
    public BooleanProperty getStandardProperty(){return this.standard;}
    public Boolean getStandard(){return this.standard.get();}
    public void setStandard(Boolean value){this.standard.set(value);}

    /* cardImage property */
    private ObjectProperty<Image> cardImage = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getCardImageProperty(){return this.cardImage;}
    public Image getCardImage(){return this.cardImage.get();}
    public void setCardImage(Image value){this.cardImage.set(value);}

    /* extensionImage property */
    private ObjectProperty<Image> extensionImage = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getExtensionImageProperty(){return this.extensionImage;}
    public Image getExtensionImage(){return this.extensionImage.get();}
    public void setExtensionImage(Image value){this.extensionImage.set(value);}

    /* cardClassImage property */
    private ObjectProperty<Image> cardClassImage = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getCardClassImageProperty(){return this.cardClassImage;}
    public Image getCardClassImage(){return this.cardClassImage.get();}
    public void setCardClassImage(Image value){this.cardClassImage.set(value);}
    
    /* infos property */
    private ObjectProperty<ObservableList<CardDetailData>> infos = new SimpleObjectProperty<ObservableList<CardDetailData>>();
    public ObjectProperty<ObservableList<CardDetailData>> getInfosProperty(){return this.infos;}
    public ObservableList<CardDetailData> getInfos(){return this.infos.get();}
    public void setInfos(ObservableList<CardDetailData> value){this.infos.set(value);}

    /* availableTags property */
    private ObjectProperty<ObservableList<CardTag>> availableTags = new SimpleObjectProperty<ObservableList<CardTag>>();
    public ObjectProperty<ObservableList<CardTag>> getAvailableTagsProperty(){return this.availableTags;}
    public ObservableList<CardTag> getAvailableTags(){return this.availableTags.get();}
    public void setAvailableTags(ObservableList<CardTag> value){this.availableTags.set(value);}

    /* associatedTags property */
    private ObjectProperty<ObservableList<CardTag>> associatedTags = new SimpleObjectProperty<ObservableList<CardTag>>();
    public ObjectProperty<ObservableList<CardTag>> getAssociatedTagsProperty(){return this.associatedTags;}
    public ObservableList<CardTag> getAssociatedTags(){return this.associatedTags.get();}
    public void setAssociatedTags(ObservableList<CardTag> value){this.associatedTags.set(value);}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setInfos(FXCollections.observableArrayList());
        this.setAvailableTags(FXCollections.observableArrayList());
        this.setAssociatedTags(FXCollections.observableArrayList());
    }

    public void showCard(CardDetail card) throws FileNotFoundException {
        this.modelWrapper.set(card);
        this.setCardImage(this.cardImageManager.getBigCardImage(card.getId(), false));
        this.setExtensionImage(this.imageManager.getCardSetLogo(card.getCardSet().getCode()));
        this.setCardClassImage(CardClassImageLoader.getImage(card.getCardClass().get(0).getCode()));
        this.setStandard(card.getCardSet().isStandard());

        this.getInfos().clear();
        this.getInfos().add(new CardDetailData("Artiste", card.getArtist()));
        this.getInfos().add(new CardDetailData("Flavor", card.getFlavor()));

        String text = card.getText();
        if(text != null && text != "") {
            this.getInfos().add(new CardDetailData("Texte", text));
        }


    }

    public class CardDetailData {

        protected String dataName;

        protected String dataValue;

        public CardDetailData(String dataName, String dataValue) {
            this.dataName = dataName;
            this.dataValue = dataValue;
        }

        public String getDataName() {
            return dataName;
        }

        public void setDataName(String dataName) {
            this.dataName = dataName;
        }

        public String getDataValue() {
            return dataValue;
        }

        public void setDataValue(String dataValue) {
            this.dataValue = dataValue;
        }
    }
}
