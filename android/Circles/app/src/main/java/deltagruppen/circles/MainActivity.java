package deltagruppen.circles;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import java.lang.Float;


public class MainActivity extends ActionBarActivity {

    Button tester; //TODO Eventually remove
    TableLayout pop;
    TextView userPi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        userPi = (TextView) findViewById(R.id.userPiString);
        setupPopup();
    }

    //TODO Change setupPopup so it activates when user pi has been calculated instead of when a button is pressed.
    private void setupPopup() {
        //Prepares the popup so it is at it's starting position and sets the final position.
        tester = (Button) findViewById(R.id.the_tester); //TODO Eventually remove
        pop = (TableLayout) findViewById(R.id.popUp);
        RelativeLayout.LayoutParams startLayout = new RelativeLayout.LayoutParams(0, 0);
        final RelativeLayout.LayoutParams endLayout = new RelativeLayout.LayoutParams(750, 750);
        pop.setLayoutParams(startLayout);
        //TODO Change all code in method below this line
        tester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sets where the popup appears
                pop.setLayoutParams(endLayout);
                endLayout.setMargins(500, 50, 200, 50);
            }
        });
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

    public void showPiInfo(MenuItem item) {
        Intent intent = new Intent(this, PiInfo.class);
        startActivity(intent);
    }

    public void showAppInfo(MenuItem item) {
        Intent intent = new Intent(this, AppInfo.class);
        startActivity(intent);
    }

    public void showCalculatingPiInfo(MenuItem item) {
        Intent intent = new Intent(this, CalculatingPi.class);
        startActivity(intent);
    }

    public void showPiInfoV(View view) {
        Intent intent = new Intent(this, PiInfo.class);
        startActivity(intent);
    }

    public void showCalculatingPiInfoV(View view) {
        Intent intent = new Intent(this, CalculatingPi.class);
        startActivity(intent);
    }

    //Assuming pi is counted as a float, this changes it to a string that can be used in the xml.
    public void piToString (float pi){
        String piString = Float.toString(pi);
        updateUserPi(piString);
    }

    public void updateUserPi(String update){
        userPi.setText(update);
    }
}
