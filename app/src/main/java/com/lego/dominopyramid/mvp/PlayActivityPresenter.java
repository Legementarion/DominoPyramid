package com.lego.dominopyramid.mvp;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.lego.dominopyramid.R;
import com.lego.dominopyramid.activity.PlayActivity;
import com.lego.dominopyramid.logic.Game;
import com.lego.dominopyramid.utils.Node;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

@InjectViewState
public class PlayActivityPresenter extends MvpPresenter<PlayActivityView> {

    private SharedPreferences sPref;
    private SparseArray<Node> dominoTree = new SparseArray<>();
    private Game game;

    private int firstPick;
    private int secondPick;

    private static final String DOMINO_STATS = "domino_stats";      //for shared preference
    private static final String DOMINO_WINS = "domino_wins";
    private PlayActivity mActivity;
    private List<ImageButton> mDominoArray;

    public void dominoPress(View view) {
        if (firstPick == 0) {
            firstPick = view.getId();
            getViewState().showPick(firstPick);
        } else {
            secondPick = view.getId();
            getViewState().showPick(secondPick);
            if (firstPick != 0 && secondPick != 0) {
                if (firstPick != secondPick) {
                    doPick(firstPick, secondPick);
                }
                new Handler().postDelayed(() -> {
                    firstPick = 0;
                    secondPick = 0;
                    getViewState().cancelPick();
                }, 400);
            }
        }
    }

    public void startGame(PlayActivity activity) {
        increaseStats(activity, false);
        game = new Game();
        for (int i = 0; i < mDominoArray.size(); i++) {
            dominoTree.put(mDominoArray.get(mDominoArray.size() - i - 1).getId(), game.tree[i]);
        }
        aDraw();
    }

    public void stopGame() {
        game = null;
        dominoTree = new SparseArray<>();
        for (int i = 0; i < mDominoArray.size(); i++) {
            mDominoArray.get(i).setEnabled(false);
            mDominoArray.get(i).setVisibility(View.VISIBLE);
            mDominoArray.get(i).setImageResource(mActivity.getResources().getIdentifier("title", "drawable", mActivity.getPackageName()));
        }
    }

    public void showRules(PlayActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.game_rules_title)
                .setMessage(R.string.game_rules)
                .setCancelable(false)
                .setNegativeButton(R.string.ok,
                        (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showStats(PlayActivity activity) {
        sPref = activity.getPreferences(MODE_PRIVATE);
        String stats = sPref.getString(DOMINO_STATS, "");
        String wins = sPref.getString(DOMINO_WINS, "");
        if (stats.equals("") || stats.equals(null)) {
            stats = "0";
            wins = "0";
        }

        String message = activity.getResources().getString(R.string.played) + " " + stats + " "
                + activity.getResources().getString(R.string.games) + "\n"
                + activity.getResources().getString(R.string.wins) + " " + wins + " "
                + activity.getResources().getString(R.string.games);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.game_stats_title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(R.string.ok,
                        (dialog, id) -> dialog.cancel())
                .setPositiveButton(R.string.clear, (dialogInterface, i) -> clearData(activity));
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void clearData(PlayActivity activity) {
        sPref = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(DOMINO_STATS, "0");
        ed.putString(DOMINO_WINS, "0");
        ed.apply();
    }

    public void playedWinGame(PlayActivity activity, boolean win) {
        increaseStats(activity, win);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.win)
                .setMessage(R.string.game_win)
                .setCancelable(false)
                .setPositiveButton(R.string.exit, (dialogInterface, i1) -> activity.finish())
                .setNegativeButton(R.string.more,
                        (dialog, id) -> {
                            stopGame();
                            startGame(activity);
                            dialog.cancel();
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void increaseStats(PlayActivity activity, boolean win) {
        sPref = activity.getPreferences(MODE_PRIVATE);
        String stats = sPref.getString(DOMINO_STATS, "");
        String wins = sPref.getString(DOMINO_WINS, "");
        Log.d("GAME", "increaseStats: " + stats);
        if (stats.equals("") || stats.equals(null)) {
            stats = "0";
            wins = "0";
        }
        Log.d("GAME", "increaseStats: " + stats);
        int i;
        if (win) {
            i = Integer.parseInt(wins);
            wins = Integer.toString(i + 1);
        } else {
            i = Integer.parseInt(stats);
            stats = Integer.toString(i + 1);
        }
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(DOMINO_STATS, stats);
        ed.putString(DOMINO_WINS, wins);
        ed.apply();
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
        for (int i = 0; i < mDominoArray.size(); i++) {
            if (mDominoArray.get(i).getVisibility() == View.INVISIBLE) {
                keyWin++;
                if (keyWin == dominoTree.size() -1){
                    mActivity.playerWin();
                }
            }else {
                return;
            }
        }
    }

    private void aDraw() {
        for (int i = 0; i < mDominoArray.size(); i++) {
            if (dominoTree.keyAt(i) == mDominoArray.get(i).getId()) {
                if (dominoTree.get(mDominoArray.get(i).getId()).isLive()) {
                    if (dominoTree.get(mDominoArray.get(i).getId()).isVisible()) {
                        String buf = "c" + "_" + dominoTree.get(mDominoArray.get(i).getId())
                                .type + "_" + dominoTree.get(mDominoArray.get(i).getId()).value;
                        mDominoArray.get(i).setImageResource(mActivity.getResources().getIdentifier(buf, "drawable", mActivity.getPackageName()));
                        mDominoArray.get(i).setEnabled(true);
                        mDominoArray.get(i).setVisibility(View.VISIBLE);
                    } else {
                        mDominoArray.get(i).setVisibility(View.INVISIBLE);
                        mDominoArray.get(i).setEnabled(false);
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

    public void init(PlayActivity playActivity, List<ImageButton> dominoArray) {
        mActivity = playActivity;
        mDominoArray = dominoArray;
    }
}
