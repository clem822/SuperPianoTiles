package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TilesStartActivity extends Activity {

    public static int NB_TILES_LARGEUR = 5;
    public static int NB_TILES_HAUTEUR = 4;

    public static int NIVEAU_FACILE = 0;
    public static int NIVEAU_NORMAL = 1;
    public static int NIVEAU_DIFFICILE = 2;

    private TilesView tilesView;
    private Timer timer = new Timer();
    private TilesQueue tilesQueue;
    private int niveau;
    private int numeroTileCourant = 1;

    private double frequenceDeDefilement = 1.0; //(en Hz)
    private double periodeDeDefilement = 1000/frequenceDeDefilement; //(en milli-secondes)

    private double frequenceDeRafraichissement = 50; //(en Hz)
    private double periodeDeRafraichissement = 1000/frequenceDeRafraichissement; //(en milli-secondes)

    private long tempsDebut;
    private long tempsCourant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiles_start);

        //System.out.println(periodeDeRafraichissement);
        //System.out.println(periodeDeDefilement);

        //ICI - Commentez le code
        tilesView = (TilesView) findViewById(R.id.view);

        Intent intent = getIntent();
        niveau = intent.getIntExtra("niveau", 0);

        tilesQueue = new TilesQueue();
        tilesView.setTilesQueue(tilesQueue);

        Tile t = generateRandomTile(numeroTileCourant++);
        tilesQueue.addTile(0, t);
        for (int i = 1 ; i<NB_TILES_HAUTEUR+2 ; ++i)
        {
            int nbTile = nbTileRandom(niveau);
            if (nbTile == 1 || nbTile == 2)
            {
                t = generateRandomTile(numeroTileCourant++);
                tilesQueue.addTile(i, t);
            }
            if (nbTile == 2)
            {
                t = generateRandomTile(numeroTileCourant++, t.getPosition());
                tilesQueue.addTile(i, t);
            }
        }

                //ICI - Commentez le code
        tilesView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEventHandler(event);
            }
        });
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
        //Log.i("TilesView", "Touch event handled");
        tempsCourant = new Date().getTime();
        tempsDebut = tempsCourant;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fonction();
            }
        }, new Date(), (int)periodeDeRafraichissement);
        return true;
    }

    public static Tile generateRandomTile(int numeroTile) {
        return new Tile((int) Math.round(Math.random() * NB_TILES_LARGEUR - 0.5), numeroTile);
    }

    public static Tile generateRandomTile(int numeroTile, int positionInterdite) {
        int pos;
        do
        {
            pos = (int) Math.round(Math.random() * NB_TILES_LARGEUR - 0.5);
        } while (pos == positionInterdite);
        return new Tile(pos, numeroTile);
    }

    public static int nbTileRandom(int niveau) {
        if (niveau == NIVEAU_FACILE)
        {
            return (int)Math.round(Math.random());
        }
        else if (niveau == NIVEAU_NORMAL)
        {
            return 1;
        }
        else if (niveau == NIVEAU_DIFFICILE)
        {
            return (int)Math.round(Math.random() * 1.5 + 0.5);
        }
        return 0;
    }

    //nom de fonction à changer
    public void fonction() {
        tempsCourant = new Date().getTime();
        double deltaT = (double) (tempsCourant - tempsDebut);
        //System.out.println(deltaT);
        if (deltaT >= periodeDeDefilement)
        {
            tempsDebut += periodeDeDefilement;
            deltaT -= periodeDeDefilement;
            tilesQueue.supprimerLigneBasse();

            int nbTile = nbTileRandom(niveau);
            Tile t = null;
            if (nbTile == 1 || nbTile == 2)
            {
                t = generateRandomTile(numeroTileCourant++);
                tilesQueue.addTile(NB_TILES_HAUTEUR + 1, t);
            }
            if (nbTile == 2)
            {
                t = generateRandomTile(numeroTileCourant++, t.getPosition());
                tilesQueue.addTile(NB_TILES_HAUTEUR + 1, t);
            }
        }

        tilesView.setDecalage(deltaT, periodeDeDefilement);
    }

}
