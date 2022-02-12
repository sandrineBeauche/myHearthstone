package com.sbm4j.hearthstone.myhearthstone.views;

import com.sbm4j.hearthstone.myhearthstone.viewmodel.CardDetailsTooltipViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class CardDetailsTooltipView implements FxmlView<CardDetailsTooltipViewModel>, Initializable {

    @FXML
    protected ImageView CardImage;

    @FXML
    protected ImageView SetLogoImage;

    @InjectViewModel
    protected CardDetailsTooltipViewModel viewModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.CardImage.imageProperty().bindBidirectional(this.viewModel.getCardImageProperty());
        this.SetLogoImage.imageProperty().bindBidirectional(this.viewModel.getCardSetLogoImageProperty());
    }


}
