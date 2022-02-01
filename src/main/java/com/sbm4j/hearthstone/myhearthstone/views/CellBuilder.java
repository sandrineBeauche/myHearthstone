package com.sbm4j.hearthstone.myhearthstone.views;

import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import javafx.geometry.Pos;
import javafx.scene.control.cell.TextFieldTableCell;

public class CellBuilder {

    public static <S, T> TextFieldTableCell<S, T> buildStringCell(){
        TextFieldTableCell<S, T> cell = new TextFieldTableCell<S, T>();
        cell.setAlignment(Pos.BASELINE_LEFT);
        cell.setWrapText(true);
        return cell;
    }

    public static <S, T> TextFieldTableCell<S, T> buildIntegerCell(){
        TextFieldTableCell<S, T> cell = new TextFieldTableCell<S, T>();
        cell.setAlignment(Pos.BASELINE_CENTER);
        cell.setWrapText(true);
        return cell;
    }

}
