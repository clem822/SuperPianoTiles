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
import java.util.NavigableSet;
import java.util.Timer;
import java.util.TimerTask;

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

        //tilesQueue = new TilesQueue();
        tilesQueue = new TilesQueue(NB_TILES_HAUTEUR + 1);
        tilesView.setTilesQueue(tilesQueue);

        Tile t = generateRandomTile();
        tilesQueue.addTile(0, t);
        for (int i = 1 ; i<NB_TILES_HAUTEUR+1 ; ++i)
        {
            int nbTile = nbTileRandom(niveau);
            if (nbTile == 1 || nbTile == 2)
            {
                t = generateRandomTile();
                tilesQueue.addTile(i, t);
            }
            if (nbTile == 2)
            {
                t = generateRandomTile(t.getPosition());
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
        if (aCommence) {
            Tile t = tilesView.getClickedTile(evt.getX(), evt.getY());
            if(t != null) {
                boolean change = t.isClicked();
                t.setClicked(true);
                if (change != t.isClicked())
                    score++;
            } else
                ;
        } else {
            aCommence = true;
            tempsCourant = new Date().getTime();
            tempsDebut = tempsCourant;
            System.out.println((int)periodeDeRafraichissement);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    fonction();
                }
            }, new Date(), (int) periodeDeRafraichissement);
        }
        return true;
    }

    public static Tile generateRandomTile() {
        return new Tile((int) Math.round(Math.random() * NB_TILES_LARGEUR - 0.5));
    }

    public static Tile generateRandomTile(int positionInterdite) {
        int pos;
        do
        {
            pos = (int) Math.round(Math.random() * NB_TILES_LARGEUR - 0.5);
        } while (pos == positionInterdite);
        return new Tile(pos);
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

    //nom de fonction à changer
    public void fonction() {
        tempsCourant = new Date().getTime();
        double deltaT = (double) (tempsCourant - tempsDebut);
        //System.out.println(deltaT);
        if (deltaT >= periodeDeDefilement)
        {
            tempsDebut += periodeDeDefilement;
            deltaT -= periodeDeDefilement;

            // Verifier que toutes les touches soient pressees
            if (!verificationIsClicked())
                gestionPerte();

            tilesQueue.supprimerLigneBasse();

            int nbTile = nbTileRandom(niveau);
            Tile t = null;
            if (nbTile == 1 || nbTile == 2)
            {
                t = generateRandomTile();
                tilesQueue.addTile(NB_TILES_HAUTEUR, t);
            }
            if (nbTile == 2)
            {
                t = generateRandomTile(t.getPosition());
                tilesQueue.addTile(NB_TILES_HAUTEUR, t);
            }
        }

        tilesView.setScore(score);

        tilesView.setDecalage(deltaT, periodeDeDefilement);
    }

    public boolean verificationIsClicked() {
        boolean isClicked = true;
        NavigableSet<Tile> tiles = tilesQueue.getTiles(0);
        
        if(tiles != null) {
            for(Tile tile : tiles){
                isClicked &= tile.isClicked();
            }
            return isClicked;
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
    }

}
