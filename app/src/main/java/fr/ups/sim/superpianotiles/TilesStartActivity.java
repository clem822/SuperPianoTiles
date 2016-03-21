package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.logging.Logger;

public class TilesStartActivity extends Activity {

    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiles_start);

        //ICI - Commentez le code
        TilesView tilesView = (TilesView) findViewById(R.id.view);

        Intent intent = getIntent();
        System.out.println("niveau = " + intent.getIntExtra("niveau", 0));
        tilesView.setNiveau(intent.getIntExtra("niveau", 0));

                //ICI - Commentez le code
        tilesView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEventHandler(event);
            }
        });

//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("timer");
//                TilesView tilesView = (TilesView) findViewById(R.id.view);
//                tilesView.incrementeDecalage();
//                tilesView.postInvalidate();
//            }
//        }, new Date(), 50);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tiles_start, menu);
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
            // ICI - A compléter pour déclencher l'ouverture de l'écran de paramétrage
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * ICI - Commentez le code
     */
    private boolean onTouchEventHandler (MotionEvent evt){
        Log.i("TilesView", "Touch event handled");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TilesView tilesView = (TilesView) findViewById(R.id.view);
                tilesView.incrementeDecalage();
                tilesView.postInvalidate();
            }
        }, new Date(), 5);
        return true;
    }
}
