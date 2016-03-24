package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class TilesStartActivity extends Activity {

    public static final int NB_TILES_LARGEUR = 5;
    public static final int NB_TILES_HAUTEUR = 4;

    public static final int NIVEAU_FACILE = 0;
    public static final int NIVEAU_NORMAL = 1;
    public static final int NIVEAU_DIFFICILE = 2;

    private TilesView tilesView;
    private Timer timer = new Timer();
    private TilesQueue tilesQueue;
    private int niveau;
    private int score = 0;

    private double frequenceDeDefilement = 1.0; //(en Hz)
    private double periodeDeDefilement = 1000/frequenceDeDefilement; //(en milli-secondes)

    private double frequenceDeRafraichissement = 200; //(en Hz)
    private double periodeDeRafraichissement = 1000/frequenceDeRafraichissement; //(en milli-secondes)

    private long tempsDebut;
    private long tempsCourant;

    private boolean aCommence = false;
    private boolean perdu = false;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiles_start);

        //System.out.println(periodeDeRafraichissement);
        //System.out.println(periodeDeDefilement);

        //Recupere les preferences de l'utilisateur
        preferences = getDefaultSharedPreferences(getApplicationContext());

        //ICI - Commentez le code
        tilesView = (TilesView) findViewById(R.id.view);
        tilesView.setTileColor(preferences.getInt("couleur", Color.BLUE));

        Intent intent = getIntent();
        niveau = intent.getIntExtra("niveau", 0);

        //tilesQueue = new TilesQueue();
        tilesQueue = new TilesQueue(NB_TILES_HAUTEUR + 1, NB_TILES_LARGEUR);
        tilesView.setTilesQueue(tilesQueue);

        int pos = generateRandomPosition();
        tilesQueue.addTile(0, pos);
        for (int i = 1 ; i<NB_TILES_HAUTEUR+1 ; ++i)
        {
            int nbTile = nbTileRandom(niveau);
            if (nbTile == 1 || nbTile == 2)
            {
                pos = generateRandomPosition();
                tilesQueue.addTile(i, pos);
            }
            if (nbTile == 2)
            {
                pos = generateRandomPosition(pos);
                tilesQueue.addTile(i, pos);
            }
        }

                //ICI - Commentez le code
        tilesView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN: {
                        return onTouchEventHandler(event);
                    }
                }
                return true;
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
        int pointerIndex = evt.getActionIndex();
        Tile t = tilesView.getClickedTile(evt.getX(pointerIndex), evt.getY(pointerIndex));

        if (!aCommence && t != null && t.isTrueTile() && premiereTile(evt.getY(pointerIndex))) {
            aCommence = true;
            tempsCourant = new Date().getTime();
            tempsDebut = tempsCourant;
            System.out.println((int)periodeDeRafraichissement);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timerHandler();
                }
            }, new Date(), (int) periodeDeRafraichissement);
        }

        if (aCommence && t != null) {
            if (t.isTrueTile())
            {

                if(premiereTile(evt.getY(pointerIndex))) {
                    boolean change = t.isClicked();
                    t.setClicked(true);
                    if (change != t.isClicked())
                        score++;
                }

            } else {
                t.setClicked(true);
                gestionPerte();
            }
        }
        return true;
    }

    /*
     * Verifier qu'il n'y ai aucune tile sur les lignes en dessous
     * @param Y ordonnee
     * @return true si aucune tile n'est en dessous
     */
    public boolean premiereTile(float Y) {
        for(int hauteur = 0; hauteur <  tilesView.getHauteurClicked(Y); hauteur++) {
            Tile[] tiles = tilesQueue.getTiles(hauteur);
            for (Tile tile : tiles) {
                if(tile.isTrueTile() && !tile.isClicked())
                    return false;
            }
        }
        return true;
    }

    public static int generateRandomPosition() {
        return (int) Math.round(Math.random() * NB_TILES_LARGEUR - 0.5);
    }

    public static int generateRandomPosition(int positionInterdite) {
        int pos;
        do
        {
            pos = (int) Math.round(Math.random() * NB_TILES_LARGEUR - 0.5);
        } while (pos == positionInterdite);
        return pos;
    }

    public static int nbTileRandom(int niveau) {
        switch (niveau)
        {
            case NIVEAU_FACILE :
                return (int)Math.round(Math.random());
            case NIVEAU_NORMAL :
                return 1;
            case NIVEAU_DIFFICILE :
                return (int)Math.round(Math.random() * 1.5 + 0.5);
            default:
                return 0;
        }
    }

    public void timerHandler() {
        tempsCourant = new Date().getTime();
        double deltaT = (double) (tempsCourant - tempsDebut);
        //System.out.println(deltaT);
        if (deltaT >= periodeDeDefilement)
        {
            tempsDebut += periodeDeDefilement;
            deltaT -= periodeDeDefilement;

            // Verifier que toutes les touches soient pressees
            if (!verificationIsClicked()) {
                gestionPerte();
                deltaT = 0;
            }
            else
            {
                tilesQueue.supprimerLigneBasse();

                int nbTile = nbTileRandom(niveau);
                int pos = 0;
                if (nbTile == 1 || nbTile == 2)
                {
                    pos = generateRandomPosition();
                    tilesQueue.addTile(NB_TILES_HAUTEUR, pos);
                }
                if (nbTile == 2)
                {
                    pos = generateRandomPosition(pos);
                    tilesQueue.addTile(NB_TILES_HAUTEUR, pos);
                }
            }

        }

        tilesView.setScore(score);

        tilesView.setDecalage(deltaT, periodeDeDefilement);
    }

    public boolean verificationIsClicked() {
        //boolean isClicked = true;
        Tile[] tiles = tilesQueue.getTiles(0);
        
        if(tiles != null) {
            for(Tile tile : tiles){
                if (tile.isTrueTile() && !tile.isClicked())
                {
                    //juste pour l'affichage de la tuile en rouge (peut etre pas le meilleure solution mais ça rend plutôt bien)
                    tile.setTrueTile(false);
                    tile.setClicked();
                    return false;
                    //isClicked &= tile.isClicked();
                }
            }
            return true;
            //return isClicked;
        }
        return false;
    }

    /**
     * Gere le jeu en cas de perte
     */
    public void gestionPerte() {
        perdu = true; // a voir si utilise finalement ?
        // interruption du timer
        timer.cancel();
        timer.purge();
        //Enlever ça !
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getBaseContext(), "Dans le cul t'as perdu !", Toast.LENGTH_LONG).show();
            }
        });
    }

}
