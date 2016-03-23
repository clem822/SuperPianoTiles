package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by clem3 on 21/03/2016.
 */
public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //SharedPreference permet de stocker les donnees comme les meilleurs scores
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Button boutonFacile = (Button) findViewById(R.id.buttonFacile);
        boutonFacile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TilesStartActivity.class);
                intent.putExtra("niveau", TilesStartActivity.NIVEAU_FACILE);
                startActivity(intent);
            }
        });

        //score du niveau facile (sera à 0 si pas encore de meilleur score)
        TextView scoreFacile = (TextView) findViewById(R.id.scoreFacile);
        scoreFacile.setText("meilleur score : " + Integer.toString(preferences.getInt("facile", 0)));

        Button boutonNormal = (Button) findViewById(R.id.buttonNormal);
        boutonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TilesStartActivity.class);
                intent.putExtra("niveau", TilesStartActivity.NIVEAU_NORMAL);
                startActivity(intent);
            }
        });

        //score du niveau normal (sera à 0 si pas encore de meilleur score)
        TextView scoreNormal = (TextView) findViewById(R.id.scoreNormal);
        scoreNormal.setText("meilleur score : " + Integer.toString(preferences.getInt("normal", 0)));

        Button boutonDifficile = (Button) findViewById(R.id.buttonDifficile);
        boutonDifficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TilesStartActivity.class);
                intent.putExtra("niveau", TilesStartActivity.NIVEAU_DIFFICILE);
                startActivity(intent);
            }
        });

        //score du difficile facile (sera à 0 si pas encore de meilleur score)
        TextView scoreDifficile = (TextView) findViewById(R.id.scoreDifficile);
        scoreDifficile.setText("meilleur score : " + Integer.toString(preferences.getInt("Difficile", 0)));
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
}
