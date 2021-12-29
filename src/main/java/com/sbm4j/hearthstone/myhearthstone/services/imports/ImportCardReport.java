package com.sbm4j.hearthstone.myhearthstone.services.imports;

import java.util.ArrayList;
import java.util.HashMap;

public class ImportCardReport {

    protected int nbCreated = 0;

    protected int nbUpdated = 0;

    protected int nbUpToDate = 0;

    protected HashMap<Integer, ArrayList<String>> errors = new HashMap<Integer, ArrayList<String>>();

    protected ArrayList<String> globalErrors = new ArrayList<String>();

    public int getNbCreated() {
        return nbCreated;
    }

    public void setNbCreated(int nbCreated) {
        this.nbCreated = nbCreated;
    }

    public int getNbUpdated() {
        return nbUpdated;
    }

    public void setNbUpdated(int nbUpdated) {
        this.nbUpdated = nbUpdated;
    }

    public int getNbUpToDate() {
        return nbUpToDate;
    }

    public void setNbUpToDate(int nbUpToDate) {
        this.nbUpToDate = nbUpToDate;
    }

    public HashMap<Integer, ArrayList<String>> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<Integer, ArrayList<String>> errors) {
        this.errors = errors;
    }

    public ArrayList<String> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(ArrayList<String> globalErrors) {
        this.globalErrors = globalErrors;
    }

    public void incrCreated(){
        this.nbCreated = this.nbCreated + 1;
    }

    public void incrUpdated(){
        this.nbUpdated = this.nbUpdated + 1;
    }

    public void incrUpToDate(){
        this.nbUpToDate = this.nbUpToDate + 1;
    }

    public void addError(String message){
        this.globalErrors.add(message);
    }

    public void addError(int dbfid, String message){
        if(!this.errors.containsKey(dbfid)){
            this.errors.put(dbfid, new ArrayList<String>());
        }
        this.errors.get(dbfid).add(message);
    }
}
