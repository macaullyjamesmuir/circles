package perfectpi.circles;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;

import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    // Used when changing the language.
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Code for animation in the main view. The solution to animation was found on
        // https://stackoverflow.com/questions/15022152/how-to-animate-gif-images-in-an-android
        final ImageView animImageView = (ImageView) findViewById(R.id.hand_animation);
        animImageView.setBackgroundResource(R.drawable.hand_animation);
        animImageView.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable frameAnimation =
                        (AnimationDrawable) animImageView.getBackground();
                frameAnimation.start();
            }
        });

        setTitle(getString(R.string.app_name));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Start the pi info activity.
     */
    public void showPiInfo(View view) {
        Intent intent = new Intent(this, PiInfo.class);
        startActivity(intent);
    }

    /**
     * Start the app info activity.
     */
    public void showAppInfo(MenuItem item) {
        Intent intent = new Intent(this, AppInfo.class);
        startActivity(intent);
    }

    /**
     * Start the app info activity.
     */
    public void showAppInfo(View view) {
        Intent intent = new Intent(this, AppInfo.class);
        startActivity(intent);
    }

    /**
     * Start the calculating pi activity.
     */
    public void showCalculatingPiInfo(View view) {
        Intent intent = new Intent(this, CalculatingPi.class);
        startActivity(intent);
    }

    /**
     * Activates the language popup.
     */
    public void changeLanguage(View view) {

        new AlertDialog.Builder(this)
                .setTitle(R.string.language_popup_title)
                .setPositiveButton(R.string.language_popup_swe, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setLocale("sv");
                    }
                })
                .setNegativeButton(R.string.language_popup_eng, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setLocale("en");
                    }
                })
                .show();
    }


    /**
     * Code for changing to a specified language.
     * Taken from https://stackoverflow.com/questions/12908289/how-change-language-of-app-on-user-select-language
     * @param lang The language code for the language which should be used
     */

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }

}
