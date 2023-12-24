package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.model.BattleAccount;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.services.download.DownloadManager;
import com.sbm4j.hearthstone.myhearthstone.services.imports.ImportCollectionAction;
import com.sbm4j.hearthstone.myhearthstone.views.ParamCommand;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenuConnexionViewModel implements ViewModel, Initializable {

    protected Logger logger = LogManager.getLogger();

    @Inject
    protected DBFacade dbFacade;

    @Inject
    protected DBManager dbManager;

    @Inject
    protected Injector injector;

    public static final String COLLECTION_UPDATED = "COLLECTION_UPDATED";

    @Inject
    protected DownloadManager downloadManager;

    @Inject
    private NotificationCenter notificationCenter;


    /* battleAccount property */
    private final ObjectProperty<BattleAccount> battleAccount = new SimpleObjectProperty<>();
    public ObjectProperty<BattleAccount> getBattleAccountProperty(){return this.battleAccount;}
    public BattleAccount getBattleAccount(){return this.battleAccount.get();}
    public void setBattleAccount(BattleAccount account){
        this.battleAccount.set(account);
        if(account == null){
            this.connectedProperty.set(false);
            this.disconnectedProperty.set(true);
            setMenuConnexionText("Se connecter");
        }
        else{
            this.connectedProperty.set(true);
            this.disconnectedProperty.set(false);
            setMenuConnexionText(account.getBattleTag());
        }
    }

    private final BooleanProperty connectedProperty = new SimpleBooleanProperty();
    public BooleanProperty getConnectedProperty(){return this.connectedProperty;}

    private final BooleanProperty disconnectedProperty = new SimpleBooleanProperty();
    public BooleanProperty getDisconnectedProperty(){return this.disconnectedProperty;}



    /* menu text */
    private final StringProperty menuConnexionText = new SimpleStringProperty();
    public StringProperty getMenuConnexionTextProperty(){ return this.menuConnexionText;}
    public String getMenuConnexionText(){ return this.menuConnexionText.get();}
    public void setMenuConnexionText(String value){ this.menuConnexionText.set(value);}

    /* battleAccounts property */
    private final ObjectProperty<ObservableList<BattleAccount>> availableBattleAccounts = new SimpleObjectProperty<>();
    public ObjectProperty<ObservableList<BattleAccount>> availableBattleAccountsProperty(){ return this.availableBattleAccounts; }
    public ObservableList<BattleAccount> getAvailableBattleAccounts(){ return this.availableBattleAccounts.get();}
    public void setAvailableBattleAccounts(ObservableList<BattleAccount> value){ this.availableBattleAccounts.set(value);}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<BattleAccount> accounts = this.dbFacade.getAccounts();
        this.availableBattleAccounts.set(FXCollections.observableArrayList(accounts));

        BattleAccount connectedAccount = this.dbFacade.getConnectedAccount();
        this.setBattleAccount(connectedAccount);
    }



    protected ParamCommand addBattleAccountCommand = new ParamCommand("Créer un deck", () -> new Action(){
        @Override
        protected void action() throws Exception {
            BattleAccount account = (BattleAccount) addBattleAccountCommand.getParameters().get("account");

            String battleString = account.getBattleTag() + "(" + account.getEmail() + ")";
            logger.info("Add a new account for " + battleString);
            Boolean added = dbFacade.addBattleAccount(account);
            if(added) {
                availableBattleAccounts.get().add(account);
                addBattleAccountCommand.setNotificationMessage("Le compte battle.net " + battleString + " ajouté avec succès");
            }
            else{
                addBattleAccountCommand.setNotificationMessage("Erreur lors de l'ajout du compte " + battleString);
                throw new Exception();
            }
        }
    });
    public ParamCommand getAddBattleAccountCommand(){ return this.addBattleAccountCommand; }


    protected void updateConnectedAccount(BattleAccount account, Boolean connected){
        account.setConnected(connected);
        Session session = dbManager.getSession();
        session.beginTransaction();
        session.update(account);
        session.getTransaction().commit();
        dbManager.closeSession();
    }


    protected ParamCommand connectToBattleAccount = new ParamCommand("Connecter au compte", () -> new Action(){

        @Override
        protected void action() throws Exception {
            BattleAccount account = (BattleAccount) connectToBattleAccount.getParameters().get("account");
            Boolean result = downloadManager.connectToHSReplay(account);
            if(result){
                updateConnectedAccount(account, true);
                setBattleAccount(account);
                connectToBattleAccount.setNotificationMessage("Connexion établie avec le compte " + account.getBattleTag());
            }
            else{
                connectToBattleAccount.setNotificationMessage("Echec de la connexion au compte " + account.getBattleTag());
            }
        }
    });
    public ParamCommand getConnectToBattleAccount(){return this.connectToBattleAccount;}


    protected ParamCommand disconnectFromBattleAccount = new ParamCommand("Se déconnecter", () -> new Action(){

        @Override
        protected void action() throws Exception {
            BattleAccount account = dbFacade.getConnectedAccount();
            downloadManager.disconnectFromHSreplay();
            updateConnectedAccount(account, false);
            setBattleAccount(null);
            disconnectFromBattleAccount.setNotificationMessage("Déconnecté avec succès");
        }
    });

    public ParamCommand getDisconnectFromBattleAccount(){return this.disconnectFromBattleAccount;}

    protected ParamCommand updateCollectionCommand = new ParamCommand(
            "Mettre à jour",
            () -> (Action) this.injector.getInstance(ImportCollectionAction.class), true);
    public ParamCommand getUpdateCollectionCommand() { return this.updateCollectionCommand;}

    public void notifyCollectionUpdated(){
        this.notificationCenter.publish(MenuConnexionViewModel.COLLECTION_UPDATED);
    }
}
