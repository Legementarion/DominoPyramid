package com.lego.dominopyramid.mvp;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.lego.dominopyramid.R;
import com.lego.dominopyramid.activity.PlayActivity;
import com.lego.dominopyramid.logic.Core;

@InjectViewState
public class PlayActivityPresenter extends MvpPresenter<PlayActivityView> {

    private int firstPick, secondPick;
    private Core core;

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
                new Handler().postDelayed(() -> {firstPick = 0;
                    secondPick = 0;
                    getViewState().cancelPick();}, 400);
            }
        }
    }

    public void startGame() {
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
}
