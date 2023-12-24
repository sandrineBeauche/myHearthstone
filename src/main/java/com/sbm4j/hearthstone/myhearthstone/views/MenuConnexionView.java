package com.sbm4j.hearthstone.myhearthstone.views;

import com.sbm4j.hearthstone.myhearthstone.model.BattleAccount;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.MenuConnexionViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuConnexionView implements FxmlView<MenuConnexionViewModel>, Initializable {

    @FXML
    protected MenuButton mnuButtonConnection;

    @FXML
    protected MenuItem mnuItemAddAccount;

    @FXML
    protected MenuItem mnuItemUpdateCollection;


    @FXML
    protected MenuItem mnuItemDisconnect;

    @InjectViewModel
    protected MenuConnexionViewModel viewModel;

    protected Logger logger = LogManager.getLogger();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.viewModel.initialize(location, resources);

        mnuButtonConnection.textProperty().bindBidirectional(viewModel.getMenuConnexionTextProperty());

        mnuItemAddAccount.visibleProperty().bindBidirectional(viewModel.getDisconnectedProperty());
        mnuItemDisconnect.visibleProperty().bindBidirectional(viewModel.getConnectedProperty());
        mnuItemUpdateCollection.visibleProperty().bindBidirectional(viewModel.getConnectedProperty());

        for(BattleAccount current: this.viewModel.getAvailableBattleAccounts()){
            addMenuItemBattleAccount(current);
        }

        this.viewModel.getAvailableBattleAccounts().addListener((ListChangeListener<BattleAccount>) c -> {
            BattleAccount newAccount = c.getAddedSubList().get(0);
            addMenuItemBattleAccount(newAccount);
        });
    }

    protected void addMenuItemBattleAccount(BattleAccount account){
        MenuItem newMenuItem = new MenuItem(account.getBattleTag());
        newMenuItem.visibleProperty().bindBidirectional(viewModel.getDisconnectedProperty());
        newMenuItem.setOnAction(event -> connectToBattleAccount(account));
        mnuButtonConnection.getItems().add(0, newMenuItem);
    }

    public void addBattleAccount(){
        Optional<BattleAccount> params = Dialogs.newBattleAccountDialog();
        params.ifPresent(account -> {
            try {
                ParamCommand command = viewModel.getAddBattleAccountCommand();
                command.putParameter("account", account);
                command.execute();
            }
            catch(Exception ex){
                logger.error(ex);
            }
        });
    }

    protected void connectToBattleAccount(BattleAccount account){
        ParamCommand command = viewModel.getConnectToBattleAccount();
        command.putParameter("account", account);
        command.execute();
    }

    public void disconnectFromBattleAccount(){
        ParamCommand command = viewModel.getDisconnectFromBattleAccount();
        command.execute();
    }

    public void updateCollection(){
        ParamCommand command = this.viewModel.getUpdateCollectionCommand();
        Dialogs.showProgressDialog(command, "Mettre à jour la collection");
        this.viewModel.notifyCollectionUpdated();
    }
}
