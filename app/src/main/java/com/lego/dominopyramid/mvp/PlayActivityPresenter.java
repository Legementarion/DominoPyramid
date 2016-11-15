package com.lego.dominopyramid.mvp;

import android.view.View;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.lego.dominopyramid.activity.PlayActivity;
import com.lego.dominopyramid.logic.Core;

@InjectViewState
public class PlayActivityPresenter extends MvpPresenter<PlayActivityView> {

    private int firstPick, secondPick;
    private Core core;

    public void init(PlayActivity activity){
        core = Core.getInstance(activity);
    }

    public void dominoPress(View view) {
        if (firstPick == 0) {
            firstPick = view.getId();
        } else {
            secondPick = view.getId();
            if (firstPick != 0 && secondPick != 0) {
                if (firstPick != secondPick) {
                    core.doPick(firstPick, secondPick);
                }
                firstPick = 0;
                secondPick = 0;
            }
        }
    }

    public void startGame(){
        core.startGame();
    }

    public void stopGame(){
        core.stopGame();
    }
}
