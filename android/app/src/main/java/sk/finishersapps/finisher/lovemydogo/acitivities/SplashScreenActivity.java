package sk.finishersapps.finisher.lovemydogo.acitivities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import sk.finishersapps.finisher.lovemydogo.R;

/**
 * Shows nice picture of dog :). And its purpouse is to ask permission to location from user.
 * If he denies permission application will ask him until he allows permission. If he blocks Asking of
 * permission application will not work. SORRY
 *
 */
public class SplashScreenActivity extends Activity {

    private static final long SPLASH_DISPLAY_LENGTH = 2000;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private View mDecorView = null;

    /**
     * Inits whole activity and starts its logic.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initView();
        if(isLocationAvailable()){
            closeSplash();
        }
    }

    /**
     * Handles callback of permission. If user denies permission is asked again. If user permits
     * It will call method closeSplashScreen()
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    closeSplash();
                } else {
                   isLocationAvailable();
                }
            }
        }
    }

    private boolean isLocationAvailable() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        }
        return true;
    }

    /**
     * Inits view :O.
     */
    private void initView() {
        mDecorView = getWindow().getDecorView();
        hideSystemUI();
    }

    /**
     * Closes this activity and opens MainMenuActivity agter time of SPLASH_DISPLAY_LENGTH contant.
     */
    private void closeSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainMenuActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    /**
     * Hides system overlay - tranforms activity into full screen so you can see that puppy on whole
     * display :)).
     */
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

}
