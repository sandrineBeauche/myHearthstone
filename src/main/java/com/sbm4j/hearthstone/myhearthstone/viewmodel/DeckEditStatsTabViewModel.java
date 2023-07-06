package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;


import java.util.HashMap;
import java.util.List;

public class DeckEditStatsTabViewModel extends AbstractDeckEditTabViewModel{

    /* curveManaData property */
    private ObjectProperty<ObservableList<XYChart.Data<String, Number>>> curveManaData = new SimpleObjectProperty<ObservableList<XYChart.Data<String, Number>>>();
    public ObjectProperty<ObservableList<XYChart.Data<String, Number>>> getCurveManaDataProperty(){return this.curveManaData;}
    public ObservableList<XYChart.Data<String, Number>> getCurveManaData(){return this.curveManaData.get();}
    public void setCurveManaData(ObservableList<XYChart.Data<String, Number>> value){this.curveManaData.set(value);}

    /* statsTagsList property */
    private ObjectProperty<ObservableList<TagStat>> statsTagsList = new SimpleObjectProperty<ObservableList<TagStat>>();
    public ObjectProperty<ObservableList<TagStat>> getStatsTagsListProperty(){return this.statsTagsList;}
    public ObservableList<TagStat> getStatsTagsList(){return this.statsTagsList.get();}
    public void setStatsTagsList(ObservableList<TagStat> value){this.statsTagsList.set(value);}

    public List<String> getTagList(){
        return this.getStatsTagsList().stream().map(t -> t.getTag()).toList();
    }


    protected DBFacade dbFacade;

    protected HashMap<Integer, TreeNode<TypeTagStat>> sunburstChartNodes = new HashMap<>();

    protected TreeNode<TypeTagStat> sunburstChartTree;
    public TreeNode<TypeTagStat> getSunburstChartTree() {
        this.sunburstChartTree.lock();
        return this.sunburstChartTree;
    }

    public DeckEditStatsTabViewModel(DeckEditViewModel mainViewModel) {
        super(mainViewModel);

        this.dbFacade = mainViewModel.getDbFacade();

        this.statsTagsList.set(FXCollections.observableArrayList());

        this.setCurveManaData(FXCollections.observableArrayList());
        for (int i = 0; i < 7; i++) {
            XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(Integer.toString(i), 0);
            this.getCurveManaData().add(data);
        }
        this.getCurveManaData().add(new XYChart.Data<String, Number>("7+", 0));

        TypeTagStat rootData = new TypeTagStat("root", 0, 0, null);
        this.sunburstChartTree = new TreeNode<>(rootData);
        this.sunburstChartNodes.put(0, this.sunburstChartTree);
    }



    public void refresh(){
        if(!this.refreshed) {
            this.refreshed = true;
            this.computeFirstSunburstStats();
            this.computeFirstCurveManaData();
            this.computeFirstTagStatsListData();

        }
    }

    protected void computeFirstCurveManaData(){
        Integer[] curveData = this.dbFacade.getManaCurveStats(this.currentDeck);

        for (int i = 0; i < curveData.length; i++) {
            this.getCurveManaData().get(i).setYValue(curveData[i]);
        }
        this.refreshToRedraw();
    }


    protected void computeFirstTagStatsListData(){
        this.getStatsTagsList().clear();
        List<TagStat> stats = this.dbFacade.getTagsStats(this.currentDeck);
        this.getStatsTagsList().addAll(stats);
    }

    protected void computeFirstSunburstStats(){
        this.sunburstChartTree.lock();
        this.sunburstChartTree.removeAllNodes();

        List<TypeTagStat> stats = this.dbFacade.getTypeTagsStats(this.currentDeck);
        for(TypeTagStat currentStat: stats){
            int parentGroup = currentStat.getParentGroup();
            TreeNode<TypeTagStat> parent = this.sunburstChartNodes.get(parentGroup);
            TreeNode<TypeTagStat> newNode = new TreeNode<>(currentStat, parent);
            this.sunburstChartNodes.put(currentStat.getGroup(), newNode);
        }
        this.sunburstChartTree.unlock();
    }


    public void refreshToRedraw(){
        List<XYChart.Data<String, Number>> data = this.getCurveManaData().stream().toList();
        this.getCurveManaData().clear();
        this.getCurveManaData().addAll(data);

        this.sunburstChartTree.unlock();
    }


    public void update(DeckCardListItem item, int nbDelta){
        this.updateManaCurve(item, nbDelta);
        this.updateTagsStats(item, nbDelta);
        this.updateTypeTagsStats(item, nbDelta);
    }

    protected void updateManaCurve(DeckCardListItem item, int nbDelta){
        int index = item.getMana();
        if(index > 7){
            index = 7;
        }

        XYChart.Data<String, Number> data = this.getCurveManaData().get(index);
        int newValue = data.getYValue().intValue() + nbDelta;
        if(newValue < 0){
            newValue = 0;
        }
        data.setYValue(newValue);
    }

    protected void updateTagsStats(DeckCardListItem item, int nbDelta){
        String tagsString = item.getTags();
        if(tagsString != null && tagsString != ""){
            String [] tags = tagsString.split(",");
            for(String currentTag: tags){
                TagStat stat = this.getStatsTagsList().stream().filter(t -> t.getTag().equals(currentTag)).findAny().orElse(null);
                if(stat == null){
                    TagStat newTagStat = new TagStat(currentTag, nbDelta);
                    this.getStatsTagsList().add(newTagStat);
                }
                else{
                    int newValue = (int) stat.getValue() + nbDelta;
                    if(newValue > 0){
                        stat.setValue(newValue);
                    }
                    else{
                        this.getStatsTagsList().remove(stat);
                    }
                }
            }
        }
    }

    protected void updateTypeTagsStats(DeckCardListItem item, int nbDelta){
        String typeTagsString = item.getTypeTags();
        if(typeTagsString != ""){
            this.sunburstChartTree.lock();
            List<CardTag> tags = this.dbFacade.getTypeTagsfromCard(item.getDbfId());
            for(CardTag currentTag: tags){
                TreeNode<TypeTagStat> stat = this.sunburstChartNodes.get(currentTag.getExclusiveGroup());
                if(stat == null){
                    String color = currentTag.getColor();
                    int group = currentTag.getExclusiveGroup();
                    TypeTagStat newTypeTag = new TypeTagStat(currentTag.getName(), nbDelta, group, color);
                    int parentGroup = newTypeTag.getParentGroup();
                    TreeNode<TypeTagStat> parent = this.sunburstChartNodes.get(parentGroup);
                    TreeNode<TypeTagStat> newNode = new TreeNode<>(newTypeTag);
                    parent.addNode(newNode);
                    this.sunburstChartNodes.put(currentTag.getExclusiveGroup(), newNode);
                }
                else{
                    TypeTagStat s = stat.getItem();
                    int newValue = (int) s.getValue() + nbDelta;
                    if(newValue > 0){
                        s.setValue(newValue);
                    }
                    else{
                        stat.getParent().removeNode(stat);
                        this.sunburstChartNodes.remove(currentTag.getExclusiveGroup());
                    }
                }
            }
        }
    }
}
