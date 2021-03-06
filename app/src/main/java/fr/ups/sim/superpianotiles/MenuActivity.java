package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by clem3 on 21/03/2016
 */
public class MenuActivity extends Activity {

    private SharedPreferences preferences;
    private Button boutonFacile;
    private Button boutonNormal;
    private Button boutonDifficile;

    private TextView scoreFacile;
    private TextView scoreNormal;
    private TextView scoreDifficile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //SharedPreference permet de stocker les donnees comme les meilleurs scores
        preferences = getDefaultSharedPreferences(getApplicationContext());

        boutonFacile = (Button) findViewById(R.id.buttonFacile);
        boutonFacile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TilesStartActivity.class);
                intent.putExtra("niveau", TilesStartActivity.NIVEAU_FACILE);
                startActivity(intent);
            }
        });


        boutonNormal = (Button) findViewById(R.id.buttonNormal);
        boutonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TilesStartActivity.class);
                intent.putExtra("niveau", TilesStartActivity.NIVEAU_NORMAL);
                startActivity(intent);
            }
        });


        boutonDifficile = (Button) findViewById(R.id.buttonDifficile);
        boutonDifficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TilesStartActivity.class);
                intent.putExtra("niveau", TilesStartActivity.NIVEAU_DIFFICILE);
                System.out.println(preferences.getInt("couleur", 0));
                startActivity(intent);
            }
        });

        recupererScores();

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
            Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recupererScores();
    }

    public void recupererScores() {

        //score du niveau facile (sera à 0 si pas encore de meilleur score)
        scoreFacile = (TextView) findViewById(R.id.scoreFacile);
        scoreFacile.setText(Integer.toString(preferences.getInt("facile", 0)));

        //score du niveau normal (sera à 0 si pas encore de meilleur score)
        scoreNormal = (TextView) findViewById(R.id.scoreNormal);
        scoreNormal.setText(Integer.toString(preferences.getInt("normal", 0)));

        //score du difficile facile (sera à 0 si pas encore de meilleur score)
        scoreDifficile = (TextView) findViewById(R.id.scoreDifficile);
        scoreDifficile.setText(Integer.toString(preferences.getInt("difficile", 0)));

    }
}
