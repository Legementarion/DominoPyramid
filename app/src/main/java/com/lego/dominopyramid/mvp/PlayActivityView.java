package com.lego.dominopyramid.mvp;

import com.arellomobile.mvp.MvpView;

public interface PlayActivityView extends MvpView {

    void showPick(int firstPick);

    void cancelPick();
}
