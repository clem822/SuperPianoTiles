package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


/**
 * Created by Guillaume on 24/03/2016.
 */
public class PopUpPerdu extends Activity {

    //public static PopUpPerdu PopUpPerdu;

    private int score;
    private int niveau;
    private int meilleurScore;

    private Intent intent;

    private SharedPreferences preferences;

    private TextView scorecourant;
    private TextView popupscoremax;

    private Button boutonMenu;
    private Button boutonRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popperdu);

        preferences = getDefaultSharedPreferences(getApplicationContext());

        // gestion affichage sur une partie de l'ecran
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        affichageScore();

        initButton();

    }

    private void affichageScore() {

        // afficher le score obtenu
        intent = getIntent();
        score = intent.getIntExtra("score", 0);
        niveau = intent.getIntExtra("niveau", 0);

        scorecourant = (TextView) findViewById(R.id.scorecourant);
        scorecourant.setText("Score : " + score);


        // affichage meilleur score
        popupscoremax = (TextView) findViewById(R.id.popupscoremax);

        meilleurScore = 0;

        switch(niveau) {
            case TilesStartActivity.NIVEAU_FACILE : {
                meilleurScore = preferences.getInt("facile", 0);
                break;
            }
            case TilesStartActivity.NIVEAU_NORMAL : {
                meilleurScore = preferences.getInt("normal", 0);
                break;
            }
            case TilesStartActivity.NIVEAU_DIFFICILE : {
                meilleurScore = preferences.getInt("difficile", 0);
                break;
            }
        }

        if(score > meilleurScore) {
            TextView popUpNewRecord = (TextView) findViewById(R.id.popupnewrecord);
            popUpNewRecord.setText("Nouveau Record");
            popupscoremax.setText("Ancien Meilleur Score : " + meilleurScore);
            popUpNewRecord.setTextSize(40);
            traitementScore();
        } else {
            popupscoremax.setText("Meilleur Score : " + meilleurScore);
        }

    }

    private void initButton() {

        // retour au menu
        boutonMenu = (Button) findViewById(R.id.popupretourmenu);
        boutonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUpPerdu.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                fermer();
            }
        });


        // rejouer
        boutonRestart = (Button) findViewById(R.id.popuprestart);
        boutonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TilesStartActivity.tilesStartActivity.finish();
                Intent intent = new Intent(PopUpPerdu.this, TilesStartActivity.class);
                intent.putExtra("niveau", niveau);
                startActivity(intent);
                fermer();
            }
        });
    }

    public void fermer() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PopUpPerdu.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        fermer();
    }

    /**
     * Met a jour le meilleur score du niveau correspondant
     */
    private void traitementScore()
    {
        SharedPreferences.Editor edit = preferences.edit();
        switch (niveau){
            case TilesStartActivity.NIVEAU_FACILE :
                edit.putInt("facile",score);
                break;
            case TilesStartActivity.NIVEAU_NORMAL :
                edit.putInt("normal",score);
                break;
            case TilesStartActivity.NIVEAU_DIFFICILE :
                edit.putInt("difficile",score);
                break;

        }
        edit.apply();
    }

}
