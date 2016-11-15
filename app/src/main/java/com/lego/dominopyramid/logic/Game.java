package com.lego.dominopyramid.logic;

import android.util.Log;

import com.lego.dominopyramid.utils.Node;
import com.lego.dominopyramid.utils.Types;

import java.util.Random;

public class Game {
    private static final int DECK_SIZE = 28;
    int[] value;
    Types[] types;

    public Game() {
        initGameDeck(DECK_SIZE);
    }

    public Node[] tree;

    private void initGameDeck(int deckSize) {
        tree = new Node[deckSize];
        value = new int[deckSize];
        types = new Types[deckSize];

        for (int i = 0, j = 6, type = 0; i < deckSize; j--, i++) {
            value[i] = j;
            types[i] = Types.getTypeByID(type);
            if (j == type) {
                type++;
                j = 6;
            }
        }
        value[deckSize - 1] = 6;

        shuffle();

        for (int i = 0, res = 13, row = 7; i < deckSize; i++) {
            if (i < 7) {
                tree[i] = new Node(null, null);
                tree[i].setId("" + i);
                tree[i].value = value[i];
                tree[i].type = types[i];
            } else {
                if (i == res) {
                    row--;
                    res += (row - 1);
                }
                tree[i] = new Node(tree[i - row], tree[i - (row - 1)]);
                tree[i].setId("" + i);
                tree[i].value = value[i];
                tree[i].type = types[i];
                tree[i - row].children.add(tree[i]);
                tree[i - (row - 1)].children.add(tree[i]);

            }
        }
    }

    private void shuffle() {
        Random rnd = new Random();
        for (int i = 0; i < value.length - 1; i++) {
            int index = rnd.nextInt(value.length);
            int buf = value[index];
            Types temp = types[index];
            value[index] = value[i];
            types[index] = types[i];
            value[i] = buf;
            types[i] = temp;
        }
    }

    public void printTree() {
        for (int i = 0; i < tree.length; i++) {
            Log.d("PRINT TREE", tree[i].getId());
            if (tree[i].getParent() != null) {
                for (int j = 0; j < tree[i].getParent().size(); j++)
                    Log.d("PRINT TREE", "Parent -" + tree[i].getParent().get(j).getId());

            }
            if (tree[i].getChildren() != null) {
                for (int j = 0; j < tree[i].getChildren().size(); j++)
                    Log.d("PRINT TREE", "Children -" + tree[i].getChildren().get(j).getId());
            }
            Log.d("PRINT TREE", "---------------------");
        }
    }
}
