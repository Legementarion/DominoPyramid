package com.lego.dominopyramid.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.lego.dominopyramid.R;
import com.lego.dominopyramid.mvp.PlayActivityPresenter;
import com.lego.dominopyramid.mvp.PlayActivityView;

public class PlayActivity extends MvpAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PlayActivityView {

    @InjectPresenter
    PlayActivityPresenter mPlayActivityPresenter;

    public ImageButton[] mDominoArray = new ImageButton[28];

    @Override
    protected void onStop() {
        super.onStop();
        mPlayActivityPresenter.stopGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayActivityPresenter.startGame();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mPlayActivityPresenter.init(this);

        for (int i = 1; i <= 28; i++) {
            if ((mDominoArray[i - 1] = (ImageButton) findViewById(getResources().getIdentifier("imageButton" + i, "id", getPackageName()))) == null) {
                return;
            }
            mDominoArray[i - 1].setEnabled(false);
            mDominoArray[i - 1].setOnClickListener(myOnClickListener);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPlayActivityPresenter.dominoPress(view);
        }
    };

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
                mPlayActivityPresenter.startGame();
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

    public void playerWin(){
        mPlayActivityPresenter.win(this);
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
