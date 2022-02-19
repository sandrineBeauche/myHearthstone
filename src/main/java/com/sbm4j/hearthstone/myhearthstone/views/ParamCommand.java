package com.sbm4j.hearthstone.myhearthstone.views;

import com.sbm4j.hearthstone.myhearthstone.utils.NotificationsUtil;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.value.ObservableValue;

import java.util.HashMap;
import java.util.function.Supplier;

public class ParamCommand extends DelegateCommand {

    protected HashMap<String, Object> params = new HashMap<>();

    protected String commandTitle;

    protected String notificationMessage;

    public ParamCommand(String commandTitlte, Supplier<Action> actionSupplier) {
        super(actionSupplier);
        this.commandTitle = commandTitlte;
    }

    public ParamCommand(String commandTitle, Supplier<Action> actionSupplier, boolean inBackground) {
        super(actionSupplier, inBackground);
        this.commandTitle = commandTitle;
    }

    public ParamCommand(String commandTitle, Supplier<Action> actionSupplier, ObservableValue<Boolean> executableObservable) {
        super(actionSupplier, executableObservable);
        this.commandTitle = commandTitle;
    }

    public ParamCommand(Supplier<Action> actionSupplier, ObservableValue<Boolean> executableObservable, boolean inBackground) {
        super(actionSupplier, executableObservable, inBackground);
    }

    public void putParameter(String parameterName, Object value){
        this.params.put(parameterName, value);
    }

    public HashMap<String, Object> getParameters(){
        return this.params;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getCommandTitle() {
        return commandTitle;
    }

    public void setCommandTitle(String commandTitle) {
        this.commandTitle = commandTitle;
    }

    @Override
    protected void succeeded() {
        NotificationsUtil.showInfoNotification(this.getCommandTitle(),
                "Opération effectuée avec succès",
                this.getNotificationMessage());
    }

    @Override
    protected void failed() {
        NotificationsUtil.showErrorNotification(this.getCommandTitle(),
                "Oups! Il y a des erreurs",
                this.getNotificationMessage() + " Veuillez consulter les logs pour plus d'informations");
    }
}
