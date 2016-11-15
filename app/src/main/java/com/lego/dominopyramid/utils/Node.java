package com.lego.dominopyramid.utils;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String id;
    private boolean live;
    private boolean visible;
    public int value;
    public Types type;

    public List<Node> children = new ArrayList<>();
    public List<Node> parent = new ArrayList<>();

    public Node(Node mother, Node father){
        parent.add(mother);
        parent.add(father);
        setLive(false);
        setVisible(true);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Node> getChildren() {
        if (!children.isEmpty()) {
            if (children.get(0) == null && children.get(1) == null) {
                return null;
            } else {
                return children;
            }
        }else {
            return null;
        }
    }

    public List<Node> getParent() {
        if (!parent.isEmpty()) {
            if (parent.get(0) == null && parent.get(1) == null) {
                return null;
            } else {
                return parent;
            }
        }else {
            return null;
        }
    }

    public boolean isLive() {
        if ((getParent() == null) || (getChildren() == null)){
            setLive(true);
        }
        return live;
    }

    private void setLive(boolean live) {
        this.live = live;
    }

    @Override
    public String toString() {
        return "type = " + type + " | value = " + value;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
