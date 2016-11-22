package com.lego.dominopyramid.mvp;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.lego.dominopyramid.R;
import com.lego.dominopyramid.activity.PlayActivity;
import com.lego.dominopyramid.logic.Core;

import static android.content.Context.MODE_PRIVATE;

@InjectViewState
public class PlayActivityPresenter extends MvpPresenter<PlayActivityView> {

    private SharedPreferences sPref;
    private int firstPick, secondPick;
    private Core core;

    private static final String DOMINO_STATS = "domino_stats";
    private static final String DOMINO_WINS = "domino_wins";

    public void init(PlayActivity activity) {
        core = Core.getInstance(activity);
    }

    public void dominoPress(View view) {
        if (firstPick == 0) {
            firstPick = view.getId();
            getViewState().showPick(firstPick);
        } else {
            secondPick = view.getId();
            getViewState().showPick(secondPick);
            if (firstPick != 0 && secondPick != 0) {
                if (firstPick != secondPick) {
                    core.doPick(firstPick, secondPick);
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
        core.startGame();
    }

    public void stopGame() {
        core.stopGame();
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
            wins = "" + (i+1);
        } else {
            i = Integer.parseInt(stats);
            stats = "" + (i + 1);
        }
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(DOMINO_STATS, stats);
        ed.putString(DOMINO_WINS, wins);
        ed.apply();
    }


}
