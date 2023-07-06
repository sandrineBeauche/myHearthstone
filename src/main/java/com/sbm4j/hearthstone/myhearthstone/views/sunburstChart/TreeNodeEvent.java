package com.sbm4j.hearthstone.myhearthstone.views.sunburstChart;

import com.sbm4j.hearthstone.myhearthstone.model.TreeNode;
import eu.hansolo.fx.charts.data.ChartItem;



public record TreeNodeEvent<T extends ChartItem>(TreeNode<T> Source,
                                                      TreeNodeEventType Type) {
}

