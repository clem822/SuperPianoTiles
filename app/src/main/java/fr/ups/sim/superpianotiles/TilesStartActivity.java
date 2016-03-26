package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class TilesStartActivity extends Activity {

    public static TilesStartActivity tilesStartActivity;

    public static final int NB_TILES_LARGEUR = 5;
    public static final int NB_TILES_HAUTEUR = 4;

    public static final int NIVEAU_FACILE = 0;
    public static final int NIVEAU_NORMAL = 1;
    public static final int NIVEAU_DIFFICILE = 2;

    private static final int PAS_ACCELERATION_FACILE = 50;
    private static final int PAS_ACCELERATION_NORMAL = 25;
    private static final int PAS_ACCELERATION_DIFFICILE = 10;

    private TilesView tilesView;
    private Timer timer = new Timer();
    private TilesQueue tilesQueue;
    private int niveau;
    private int score = 0;

    private double frequenceDeDefilement; //(en Hz)
    private double periodeDeDefilement; //(en milli-secondes)

    private double frequenceDeRafraichissement = 200; //(en Hz)
    private double periodeDeRafraichissement = 1000/frequenceDeRafraichissement; //(en milli-secondes)

    private long tempsDebut;
    private long tempsCourant;

    private boolean aCommence = false;
    private boolean perdu = false;

    private SharedPreferences preferences;

    private boolean acceleration=false;

    private boolean soundOn;
    MediaPlayer mp_SoundTile = new MediaPlayer();
    private MediaPlayer mp_SoundTileFail;
    private List<MediaPlayer> soundUtilise = new ArrayList<MediaPlayer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiles_start);

        //TODO C'est vraiment utile de stocker sa propre référence ?
        //oui car c'est un bon moyen de la fermer a distance (dans le popup de fin de partie par exemple)
        tilesStartActivity = this;

        //Recupere les preferences de l'utilisateur
        preferences = getDefaultSharedPreferences(getApplicationContext());

        //ICI - Commentez le code
        tilesView = (TilesView) findViewById(R.id.view);
        tilesView.setTileColor(preferences.getInt("couleur", Color.BLUE));

        //récupération du niveau de la partie
        Intent intent = getIntent();
        niveau = intent.getIntExtra("niveau", 0);

        // Creation  de la liste des sons utilisés pour les touches
        initSons();

        //initialisation des vitesses de depart par niveau
        initVitesse();

        //initialisation de la file de tuile
        initTilesQueue();

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

    private void initSons() {

        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_a);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_b);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_bb);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_c);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_c_b);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_d);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_e);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_eb);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_f);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_f_b);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_g);
        soundUtilise.add(mp_SoundTile);
        mp_SoundTile = MediaPlayer.create(this, R.raw.piano_g_b);
        soundUtilise.add(mp_SoundTile);

        mp_SoundTileFail = MediaPlayer.create(this, R.raw.sound_tile_fail);

        soundOn = preferences.getBoolean("volume", true);
    }

    private void initVitesse() {
        switch (niveau)
        {
            case NIVEAU_FACILE :
                frequenceDeDefilement = 1.5;
                break;

            case NIVEAU_NORMAL :
                frequenceDeDefilement = 2.0;
                break;

            case NIVEAU_DIFFICILE :
                frequenceDeDefilement = 2.5;
                break;
            default:
                frequenceDeDefilement = 1.0;
        }
    }

    private void initTilesQueue() {
        tilesQueue = new TilesQueue(NB_TILES_HAUTEUR + 1, NB_TILES_LARGEUR);
        tilesView.setTilesQueue(tilesQueue);

        ajouterLigne(0, NIVEAU_NORMAL);
        for (int i = 1 ; i<NB_TILES_HAUTEUR+1 ; ++i)
        {
            ajouterLigne(i, niveau);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
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
            //System.out.println((int)periodeDeRafraichissement);
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
                    if (change != t.isClicked()) {
                        score++;
                        playSoundTile(true);
                    }
                }

            } else {
                t.setClicked(true);
                playSoundTile(false);
                gestionPerte();
            }
        }
        return true;
    }


    /**
     * Joue un son de touche de piano aleatoire
     * @param bonne
     */
    private void playSoundTile(boolean bonne){

        if (soundOn) {
            if (bonne) {

                List<MediaPlayer> sonJouable = new ArrayList<MediaPlayer>();
                int possibilite = 0;
                for (MediaPlayer mp : soundUtilise) {
                    if (!mp.isPlaying()) {
                        sonJouable.add(mp);
                        possibilite++;
                    }
                }

                if (possibilite != 0) {
                    sonJouable.get((int) (Math.random() * possibilite)).start();
                }

            } else
                mp_SoundTileFail.start();
        }
    }

    /**
    * Handler du timer. C'est ici qu'est géré le jeu.
    */
    private void timerHandler() {
        tempsCourant = new Date().getTime();
        double deltaT = (double) (tempsCourant - tempsDebut);
        gestionAcceleration();
        if (deltaT >= periodeDeDefilement)
        {
            tempsDebut += periodeDeDefilement;
            deltaT -= periodeDeDefilement;

            // Verifier que toutes les touches soient pressees
            if (!verificationIsClicked()) {
                gestionPerte();
                deltaT = 0;

            }
            else {
                tilesQueue.supprimerLigneBasse();
                ajouterLigne(NB_TILES_HAUTEUR, niveau);
            }

        }

        tilesView.setScore(score);

        tilesView.setDecalage(deltaT, periodeDeDefilement);
    }

    /**
    * Génère une position horizontale pour une Tile.
    * @return La position
    */
    private int generateRandomPosition() {
        return (int) Math.round(Math.random() * NB_TILES_LARGEUR - 0.5);
    }

    /**
    * Génère une position horizontale pour une Tile en excluant la position passée en paramètre.
    * @param positionInterdite La position à exclure
    * @return La position
    */
    private int generateRandomPosition(int positionInterdite) {
        int pos;
        do
        {
            pos = (int) Math.round(Math.random() * NB_TILES_LARGEUR - 0.5);
        } while (pos == positionInterdite);
        return pos;
    }

    /**
    * Génère un nombre de Tile aléatoire pour une ligne selon le niveau du jeu.
    * @param niveau Niveau du jeu
    */
    private int nbTileRandom(int niveau) {
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

    /**
    * Ajoute une ligne de tuile à la hauteur demandée, pour un niveau demandé.
    * @param hauteur hauteur à laquelle la ligne sera ajoutée
    * @param niveau niveau de difficulté
    */
    private void ajouterLigne(int hauteur, int niveau) {
        int pos = 0;
        int nbTile = nbTileRandom(niveau);
        if (nbTile == NIVEAU_NORMAL || nbTile == NIVEAU_DIFFICILE)
        {
            pos = generateRandomPosition();
            tilesQueue.addTile(hauteur, pos);
        }
        if (nbTile == NIVEAU_DIFFICILE)
        {
            pos = generateRandomPosition(pos);
            tilesQueue.addTile(hauteur, pos);
        }
    }

    /**
     * Verifier qu'il n'y ai aucune tile sur les lignes en dessous (c'est pas plutôt en dessus ?)
     * @param Y ordonnee
     * @return true si aucune tile n'est en dessous
     */
    private boolean premiereTile(float Y) {
        for(int hauteur = 0; hauteur <  tilesView.getHauteurClicked(Y); hauteur++) {
            Tile[] tiles = tilesQueue.getTiles(hauteur);
            for (Tile tile : tiles) {
                if(tile.isTrueTile() && !tile.isClicked())
                    return false;
            }
        }
        return true;
    }

    /**
     * Verifier si les tiles de la hauteur 0 sont toutes a l'etat clicked
     * @return true si elles sont a l'etat clicked
     * sinon false
     */
    private boolean verificationIsClicked() {
        Tile[] tiles = tilesQueue.getTiles(0);
        
        if(tiles != null) {
            for(Tile tile : tiles){
                if (tile.isTrueTile() && !tile.isClicked())
                {
                    //juste pour l'affichage de la tuile en rouge (peut etre pas le meilleure solution mais ça rend plutôt bien)
                    tile.setTrueTile(false);
                    tile.setClicked();
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Gere le jeu en cas de perte
     */
    private void gestionPerte() {

        // interruption du timer
        timer.cancel();
        timer.purge();
        Intent intent = new Intent(TilesStartActivity.this,PopUpPerdu.class);
        intent.putExtra("score", score);
        intent.putExtra("niveau", niveau);
        startActivity(intent);

    }

    /**
     *
     */
    private void gestionAcceleration() {
        // nombre de tuiles entre chaque acceleration
        int pas;
        switch (niveau)
        {
            case NIVEAU_FACILE :
                pas=PAS_ACCELERATION_FACILE;
                break;

            case NIVEAU_NORMAL :
                pas=PAS_ACCELERATION_NORMAL;
                break;

            case NIVEAU_DIFFICILE :
                pas = PAS_ACCELERATION_DIFFICILE;
                break;
            default:
                pas=0;
        }
        if(score%pas==0 && !acceleration) {
            frequenceDeDefilement+=0.05;
            acceleration=true;
        }
        if(score%pas>0){
            acceleration=false;
        }
        periodeDeDefilement = 1000/frequenceDeDefilement;
    }

    @Override
    public void onBackPressed() {
        // interruption du timer
        timer.cancel();
        timer.purge();
        this.finish();
    }

}
