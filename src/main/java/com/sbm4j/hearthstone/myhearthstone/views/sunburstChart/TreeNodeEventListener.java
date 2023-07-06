package com.sbm4j.hearthstone.myhearthstone.views.sunburstChart;

import eu.hansolo.fx.charts.data.ChartItem;

@FunctionalInterface
public interface TreeNodeEventListener <T extends ChartItem> {
    void onTreeNodeEvent(final TreeNodeEvent<T> EVENT);
}
