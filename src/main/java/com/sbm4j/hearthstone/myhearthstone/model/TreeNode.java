package com.sbm4j.hearthstone.myhearthstone.model;

import com.sbm4j.hearthstone.myhearthstone.views.sunburstChart.TreeNodeEvent;
import com.sbm4j.hearthstone.myhearthstone.views.sunburstChart.TreeNodeEventListener;
import com.sbm4j.hearthstone.myhearthstone.views.sunburstChart.TreeNodeEventType;
import eu.hansolo.fx.charts.data.ChartItem;
import eu.hansolo.fx.charts.data.Item;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TreeNode<T extends ChartItem> {
    protected final TreeNodeEvent<T> PARENT_CHANGED   = new TreeNodeEvent<T>(this, TreeNodeEventType.PARENT_CHANGED);
    protected final TreeNodeEvent<T> CHILDREN_CHANGED = new TreeNodeEvent<T>(this, TreeNodeEventType.CHILDREN_CHANGED);
    protected final TreeNodeEvent<T> VALUE_CHANGED = new TreeNodeEvent<T>(this, TreeNodeEventType.VALUE_CHANGED);
    protected final TreeNodeEvent<T> TREE_CHANGED = new TreeNodeEvent<T>(this, TreeNodeEventType.TREE_CHANGED);
    protected       T                           item;
    protected       TreeNode<T>                 parent;
    protected       TreeNode<T>                 treeRoot;
    protected       ArrayList<TreeNode<T>>      descendants;
    protected       int                         depth;
    protected       boolean                     locked = false;
    protected       boolean                     dirty = false;
    protected final ObservableList<TreeNode<T>> children;
    private       List<TreeNodeEventListener<T>> listeners;


    // ******************** Constructors **************************************
    public TreeNode(final T ITEM) {
        this(ITEM, null);
    }
    public TreeNode(final T ITEM, final TreeNode<T> PARENT) {
        item      = ITEM;
        parent    = PARENT;
        children  = FXCollections.observableArrayList();
        if(parent != null){
            parent.addNode(this);
        }
        listeners = new ArrayList<>();
        this.init();
        this.initListener();
    }


    // ******************** Methods *******************************************
    protected void init() {
        // Add this node to parents children
        if (null != parent) {
            this.depth = parent.depth + 1;
            this.treeRoot = parent.treeRoot;
        }
        else{
            this.treeRoot = this;
            this.depth = 0;
            this.descendants = new ArrayList<>();
        }
        for(TreeNode<T> currentChild: this.children){
            currentChild.init();
        }
    }

    protected void initListener(){
        children.addListener((ListChangeListener<TreeNode<T>>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) { c.getRemoved().forEach(
                        TreeNode::removeAllTreeNodeEventListeners); }
            }
            treeRoot.fireTreeNodeEvent(CHILDREN_CHANGED);
        });

        this.item.valueProperty().addListener((observableValue, number, t1) -> {
                treeRoot.fireTreeNodeEvent(VALUE_CHANGED);
            }
        );
    }

    public boolean isRoot() { return null == parent; }
    public boolean isLeaf() { return (null == children || children.isEmpty()); }
    public boolean hasParent() { return null != parent; }
    public TreeNode<T> getParent() { return parent; }
    public TreeNode<T> getTreeRoot() { return this.treeRoot; }


    public T getItem() { return item; }
    public void setItem(final T ITEM) { item = ITEM; }

    public List<TreeNode<T>> getChildren() { return children; }

    public void addNode(final TreeNode<T> NODE) {
        if (children.contains(NODE)) { return; }
        NODE.parent = this;
        children.add(NODE);
        NODE.init();

        if(treeRoot.descendants == null){
            treeRoot.descendants = new ArrayList<TreeNode<T>>();
        }
        if(NODE.descendants != null){
            treeRoot.descendants.addAll(NODE.descendants);
        }
        treeRoot.descendants.add(NODE);
    }
    public void removeNode(final TreeNode<T> NODE) {
        children.remove(NODE);
        treeRoot.descendants.remove(NODE);
        if(NODE.descendants == null) {
            NODE.descendants = new ArrayList<>();
        }
        NODE.descendants.addAll(NODE.stream().toList());
        treeRoot.descendants.removeAll(NODE.descendants);
        NODE.parent = null;
        NODE.init();
    }

    public void addNodes(final TreeNode<T>... NODES) { addNodes(Arrays.asList(NODES)); }
    public void addNodes(final List<TreeNode<T>> NODES) { NODES.forEach(this::addNode); }

    public void removeNodes(final TreeNode<T>... NODES) { removeNodes(Arrays.asList(NODES)); }
    public void removeNodes(final List<TreeNode<T>> NODES) { NODES.forEach(this::removeNode); }

    public void removeAllNodes() {
        ArrayList<TreeNode<T>> nodes = new ArrayList<>();
        nodes.addAll(this.children);
        for(TreeNode<T> currentChild: nodes){
            this.removeNode(currentChild);
        }
    }

    public Stream<TreeNode<T>> stream() {
        if (isLeaf()) {
            return Stream.of(this);
        } else {
            return getChildren().stream()
                    .map(TreeNode::stream)
                    .reduce(Stream.of(this), Stream::concat);
        }
    }



    public List<TreeNode<T>> getAll() {
        return treeRoot.descendants;
    }
    public List<T> getAllData() {
        return this.treeRoot.descendants.stream()
                .map(TreeNode<T>::getItem).collect(Collectors.toList());
    }

    public int getNoOfNodes() {
        return this.treeRoot.descendants.size() + 1;
    }
    public int getNoOfLeafNodes() {
        return (int) this.treeRoot.descendants.stream().filter(TreeNode::isLeaf)
            .map(TreeNode::getItem).count();
    }

    public boolean contains(final TreeNode<T> NODE) {
        return this.treeRoot.descendants.stream()
                .anyMatch(n -> n.equals(NODE));
    }
    public boolean containsData(final T ITEM) {
        return this.treeRoot.descendants.stream()
                .anyMatch(n -> n.item.equals(ITEM));
    }

    public int getDepth() {
        return this.depth;
    }


    public int getMaxLevel() {
        return treeRoot.descendants.stream()
                .map(TreeNode<T>::getDepth)
                .max(Comparator.naturalOrder()).orElse(0);
    }

    public List<TreeNode<T>> getSiblings() { return null == getParent() ? new ArrayList<>() : getParent().getChildren(); }


    public HashMap<Integer, ArrayList<TreeNode<T>>> getLevelMap(){
        if(this.isRoot()){
            HashMap<Integer, ArrayList<TreeNode<T>>> result = new HashMap<>();
            result.put(0, new ArrayList<TreeNode<T>>());
            result.get(0).add(this);
            for(TreeNode<T> current: this.descendants){
                if(!result.containsKey(current.depth)){
                    result.put(current.depth, new ArrayList<>());
                }
                result.get(current.depth).add(current);
            }
            return result;
        }
        else{
            return this.treeRoot.getLevelMap();
        }
    }


    // ******************** Event handling ************************************
    public void setOnTreeNodeEvent(final TreeNodeEventListener<T> LISTENER) { addTreeNodeEventListener(LISTENER); }
    public void addTreeNodeEventListener(final TreeNodeEventListener<T> LISTENER) { if (!listeners.contains(LISTENER)) listeners.add(LISTENER); }
    public void removeTreeNodeEventListener(final TreeNodeEventListener<T> LISTENER) { listeners.remove(LISTENER); }
    public void removeAllTreeNodeEventListeners() { listeners.clear(); }

    public void fireTreeNodeEvent(final TreeNodeEvent<T> EVENT) {
        if(this.locked){
            this.dirty = true;
        }
        else {
            for (TreeNodeEventListener<T> listener : listeners) {
                listener.onTreeNodeEvent(EVENT);
            }
        }
    }

    public void lock(){
        if(!this.locked) {
            this.locked = true;
            this.dirty = false;
        }
    }

    public void unlock(){
        if(this.locked) {
            this.locked = false;
            if (this.dirty) {
                this.fireTreeNodeEvent(TREE_CHANGED);
            }
        }
    }
}
