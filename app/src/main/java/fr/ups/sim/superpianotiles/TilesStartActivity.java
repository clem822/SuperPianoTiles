package fr.ups.sim.superpianotiles;

import android.app.Activity;
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

        /*TilesLevel level = new TilesLevel();
        level.addTile(0, new Tile(2));
        level.addTile(0, new Tile(0));
        level.addTile(1, new Tile(3));
        level.addTile(2, new Tile(4));
        Tile t = new Tile(3);
        t.setClicked();
        level.addTile(3, t);
        level.addTile(4, new Tile(4));
        level.addTile(5, new Tile(2));
        level.addTile(5, new Tile(0));
        level.addTile(6, new Tile(3));
        level.addTile(6, new Tile(4));
        level.addTile(7, new Tile(3));
        level.addTile(7, new Tile(4));
        System.out.println(level);
        tilesView.setLevel(level);*/

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
