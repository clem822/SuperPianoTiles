package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


/**
 * Created by thibault on 24/03/16.
 */
public class SettingsActivity extends Activity {

    private CheckBox volume;

    private RadioButton couleurBleu;
    private RadioButton couleurJaune;
    private RadioButton couleurVert;

    private Button valider;
    private Button defaut;
    private Button raz;

    private SharedPreferences.Editor edit;
    private SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Recupere les preferences de l'utilisateur
        preferences = getDefaultSharedPreferences(getApplicationContext());

        volume = (CheckBox) findViewById(R.id.volume);

        couleurBleu = (RadioButton) findViewById(R.id.couleurBleu);
        couleurJaune = (RadioButton) findViewById(R.id.couleurJaune);
        couleurVert = (RadioButton) findViewById(R.id.couleurVert);

        defaut = (Button) findViewById(R.id.defaut);
        valider = (Button) findViewById(R.id.valider);
        raz = (Button) findViewById(R.id.raz);

        volume.setChecked(preferences.getBoolean("volume", true));

        switch ((preferences.getInt("couleur", Color.BLUE)))
        {
            case Color.BLUE : couleurBleu.setChecked(true);
                break;
            case Color.YELLOW : couleurJaune.setChecked(true);
                break;
            case Color.GREEN : couleurVert.setChecked(true);
                break;
        }

        valider.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit = preferences.edit();
                edit.putBoolean("volume", volume.isChecked());
                if(couleurBleu.isChecked()) {
                    edit.putInt("couleur", Color.BLUE);
                }
                if(couleurJaune.isChecked()){
                    edit.putInt("couleur", Color.YELLOW);
                }
                if(couleurVert.isChecked()) {
                    edit.putInt("couleur", Color.GREEN);
                }

                edit.apply();
                finish();
            }
        });

        defaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit = preferences.edit();
                edit.remove("couleur");
                edit.remove("volume");
                edit.apply();
                finish();
            }
        });

        raz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit = preferences.edit();
                edit.remove("facile");
                edit.remove("normal");
                edit.remove("difficile");
                edit.apply();
                finish();
            }
        });

    }

}
