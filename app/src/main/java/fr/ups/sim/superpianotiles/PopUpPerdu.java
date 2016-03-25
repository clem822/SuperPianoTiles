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

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popperdu);

        preferences = getDefaultSharedPreferences(getApplicationContext());

        // afficher les scores
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        int niveau = intent.getIntExtra("niveau", 0);

        TextView scorecourant = (TextView) findViewById(R.id.scorecourant);
        scorecourant.setText("Score : " + score);

        TextView popupscoremax = (TextView) findViewById(R.id.popupscoremax);

        switch(niveau) {
            case TilesStartActivity.NIVEAU_FACILE : {
                popupscoremax.setText("Score Max : " + Integer.toString(preferences.getInt("facile", 0)));
                break;
            }
            case TilesStartActivity.NIVEAU_NORMAL : {
                popupscoremax.setText("Score Max : " + Integer.toString(preferences.getInt("normal", 0)));
                break;
            }
            case TilesStartActivity.NIVEAU_DIFFICILE : {
                popupscoremax.setText("Score Max : " + Integer.toString(preferences.getInt("difficile", 0)));
                break;
            }
        }


        // gestion affichage sur une partie de l'ecran
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        // retour au menu
        Button boutonMenu = (Button) findViewById(R.id.popupretourmenu);
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
        Button boutonRestart = (Button) findViewById(R.id.popuprestart);
        boutonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TilesStartActivity.tilesStartActivity.finish();
                Intent intent = new Intent(PopUpPerdu.this, TilesStartActivity.class);
                intent.putExtra("niveau", TilesStartActivity.NIVEAU_NORMAL);
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

}
