package chrisjluc.onesearch.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.base.BaseGooglePlayServicesActivity;
import chrisjluc.onesearch.framework.WordSearchManager;
import chrisjluc.onesearch.models.GameDifficulty;
import chrisjluc.onesearch.models.GameMode;
import chrisjluc.onesearch.models.GameType;
import chrisjluc.onesearch.ui.gameplay.WordSearchActivity;

public class MenuActivity extends BaseGooglePlayServicesActivity implements View.OnClickListener {

    private final static String MENU_PREF_NAME = "menu_prefs";
    private final static String FIRST_TIME = "first_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if first time opening app, show splash screen
        SharedPreferences prefs = getSharedPreferences(MENU_PREF_NAME, MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean(FIRST_TIME, true);
        if (isFirstTime) {
            SharedPreferences.Editor editor = getSharedPreferences(MENU_PREF_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(FIRST_TIME, false);
            editor.commit();

            Intent i = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(i);
        }

        setContentView(R.layout.activity_menu);

        findViewById(R.id.bMenuEasy).setOnClickListener(this);
        findViewById(R.id.bMenuMedium).setOnClickListener(this);
        findViewById(R.id.bMenuHard).setOnClickListener(this);
        //TODO: Reimplement advanced after more efficient way of drawing out the grid
//        findViewById(R.id.bMenuAdvanced).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bMenuSignIn) {
            mInSignInFlow = true;
            mSignInClicked = true;
            mGoogleApiClient.connect();
            return;
        }
        String gd = null;
        switch (view.getId()) {
            case R.id.bMenuEasy:
                gd = GameDifficulty.Easy;
                break;
            case R.id.bMenuMedium:
                gd = GameDifficulty.Medium;
                break;
            case R.id.bMenuHard:
                gd = GameDifficulty.Hard;
                break;
//            case R.id.bMenuAdvanced:
//                gd = GameDifficulty.Advanced;
//                break;
        }
        WordSearchManager wsm = WordSearchManager.getInstance();
        wsm.Initialize(new GameMode(GameType.Timed, gd, 60000), getApplicationContext());
        wsm.buildWordSearches();
        Intent i = new Intent(getApplicationContext(), WordSearchActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WordSearchManager.nullify();
    }

    @Override
    public void onConnected(Bundle bundle) {
        findViewById(R.id.bMenuSignIn).setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);
        findViewById(R.id.bMenuSignIn).setVisibility(View.VISIBLE);
        findViewById(R.id.bMenuSignIn).setOnClickListener(this);
    }
}
