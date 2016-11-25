package com.lego.dominopyramid.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.lego.dominopyramid.R;
import com.lego.dominopyramid.mvp.PlayActivityPresenter;
import com.lego.dominopyramid.mvp.PlayActivityView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class PlayActivity extends MvpAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PlayActivityView {

    @InjectPresenter
    PlayActivityPresenter mPlayActivityPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindViews({R.id.imageButton1, R.id.imageButton2, R.id.imageButton3, R.id.imageButton4, R.id.imageButton5,
            R.id.imageButton6, R.id.imageButton7, R.id.imageButton8, R.id.imageButton9, R.id.imageButton10,
            R.id.imageButton11, R.id.imageButton12, R.id.imageButton13, R.id.imageButton14, R.id.imageButton15,
            R.id.imageButton16, R.id.imageButton17, R.id.imageButton18, R.id.imageButton19, R.id.imageButton20,
            R.id.imageButton21, R.id.imageButton22, R.id.imageButton23, R.id.imageButton24, R.id.imageButton25,
            R.id.imageButton26, R.id.imageButton27, R.id.imageButton28})
    List<ImageButton> mDominoArray;

    @BindView(R.id.nav_view)
    NavigationView mNavView;

    @Override
    protected void onStop() {
        super.onStop();
        mPlayActivityPresenter.stopGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayActivityPresenter.startGame(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);


        for (ImageButton imageButton : mDominoArray) {
            imageButton.setEnabled(false);
            imageButton.setOnClickListener(view -> mPlayActivityPresenter.dominoPress(view));
        }
        mPlayActivityPresenter.init(this, mDominoArray);

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_game:
                mPlayActivityPresenter.stopGame();
                mPlayActivityPresenter.startGame(this);
                break;
            case R.id.nav_stats:
                mPlayActivityPresenter.showStats(this);
                break;
            case R.id.nav_help:
                mPlayActivityPresenter.showRules(this);
                break;
            case R.id.nav_exit:
                finish();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void playerWin() {
        mPlayActivityPresenter.playedWinGame(this, true);
    }

    @Override
    public void showPick(int firstPick) {
        for (ImageButton domino : mDominoArray) {
            if (domino.getId() == firstPick) {
                domino.setBackgroundResource(getResources().getIdentifier("selection_bg", "drawable", getPackageName()));
            }
        }
    }

    @Override
    public void cancelPick() {
        for (ImageButton domino : mDominoArray) {
            domino.setBackgroundResource(getResources().getIdentifier("colorTransparent", "colors", getPackageName()));
        }
    }
}
