package com.lego.dominopyramid.logic;

import android.util.SparseArray;
import android.view.View;

import com.lego.dominopyramid.activity.PlayActivity;
import com.lego.dominopyramid.utils.Node;

import java.util.List;

public class Core {
    private static Core instance;
    private PlayActivity activity;
    private SparseArray<Node> dominoTree = new SparseArray<>();
    private Game game;

    public static Core getInstance(PlayActivity activity) {
        return instance == null ? (instance = new Core(activity)) : instance;
    }

    private Core(PlayActivity activity) {
        this.activity = activity;
    }

    public void startGame() {
        game = new Game();
        for (int i = 0; i < activity.mDominoArray.length; i++) {
            dominoTree.put(activity.mDominoArray[activity.mDominoArray.length - i - 1].getId(), game.tree[i]);
        }
        aDraw();
    }

    public void stopGame() {
        game = null;
        dominoTree = new SparseArray<>();
        for (int i = 0; i < activity.mDominoArray.length; i++) {
            StringBuilder buf = new StringBuilder("title");
            activity.mDominoArray[i].setEnabled(false);
            activity.mDominoArray[i].setVisibility(View.VISIBLE);
            activity.mDominoArray[i].setImageResource(activity.getResources().getIdentifier(buf.toString(), "drawable", activity.getPackageName()));
        }

    }

    public void doPick(int value, int value1) {
        int resultValue = dominoTree.get(value).value +
                dominoTree.get(value).type.getIDByType() +
                dominoTree.get(value1).type.getIDByType() +
                dominoTree.get(value1).value;
        if (resultValue == 12) {
            dominoTree.get(value).setVisible(false);
            dominoTree.get(value1).setVisible(false);
            removeRelation(dominoTree.get(value));
            removeRelation(dominoTree.get(value1));
            aDraw();
        }
        checkWin();
    }

    private void checkWin() {
        int keyWin = 0;
        for (int i = 0; i < activity.mDominoArray.length; i++) {
            if (activity.mDominoArray[i].getVisibility() == View.INVISIBLE) {
                keyWin++;
                if (keyWin == dominoTree.size() -1){
                    activity.playerWin();
                }
            }else {
                return;
            }
        }
    }

    private void aDraw() {
        for (int i = 0; i < activity.mDominoArray.length; i++) {
            if (dominoTree.keyAt(i) == activity.mDominoArray[i].getId()) {
                if (dominoTree.get(activity.mDominoArray[i].getId()).isLive()) {
                    if (dominoTree.get(activity.mDominoArray[i].getId()).isVisible()) {
                        StringBuilder buf = new StringBuilder("c" + "_" + dominoTree.get(activity.mDominoArray[i].getId())
                                .type + "_" + dominoTree.get(activity.mDominoArray[i].getId()).value);
                        activity.mDominoArray[i].setImageResource(activity.getResources().getIdentifier(buf.toString(), "drawable", activity.getPackageName()));
                        activity.mDominoArray[i].setEnabled(true);
                        activity.mDominoArray[i].setVisibility(View.VISIBLE);
                    } else {
                        activity.mDominoArray[i].setVisibility(View.INVISIBLE);
                        activity.mDominoArray[i].setEnabled(false);
                    }
                }
            }
        }
    }

    private void removeRelation(Node root) {
        List<Node> tempList;
        //parent
        tempList = root.getParent();
        if (tempList != null) {
            for (int i = 0; i < tempList.size(); i++) {
                for (int j = 0; j < dominoTree.size(); j++) {
                    int key = dominoTree.keyAt(j);
                    if (dominoTree.get(key) == (tempList.get(i))) {
                        dominoTree.get(key).getChildren().remove(root);
                    }
                }
            }
        }
        //children
        tempList = root.getChildren();
        if (tempList != null) {
            for (int i = 0; i < tempList.size(); i++) {
                for (int j = 0; j < dominoTree.size(); j++) {
                    int key = dominoTree.keyAt(j);
                    if (dominoTree.get(key) == (tempList.get(i))) {
                        dominoTree.get(key).getParent().remove(root);
                    }
                }
            }
        }
    }
}
