package com.sbm4j.hearthstone.myhearthstone.model;

import eu.hansolo.fx.charts.data.ChartItem;
import javafx.scene.paint.Color;

public class TypeTagStat extends ChartItem {

    protected int group;

    protected Color color;

    public TypeTagStat(String name, long value, int group, String color){
        super(name, value);
        this.group = group;
        if(color == null){
            this.setFill(Color.YELLOWGREEN);
        }
        else{
            this.setFill(Color.valueOf(color));
        }
    }


    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getParentGroup(){
        if(this.group % 100 == 0){
            return 0;
        }
        else {
            return Integer.parseInt(Integer.toString(this.group).substring(0, 1)) * 100;
        }
    }

    @Override
    public Color getFill() {
        return this.color;
    }

    @Override
    public void setFill(Color FILL) {
        this.color = FILL;
    }
}
