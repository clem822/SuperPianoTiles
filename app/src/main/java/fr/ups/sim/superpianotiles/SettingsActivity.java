package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


/**
 * Created by thibault on 24/03/16.
 */
public class SettingsActivity extends Activity {

    private CheckBox volume;
    private RadioButton orange;
    private RadioButton jaune;
    private RadioButton vert;
    private RadioButton bleu;
    private RadioButton violet;
    private RadioButton rose;

    private RadioGroup couleur;

    private Button retourMenu;
    private Button reset;

    private SharedPreferences.Editor edit;
    private SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Recupere les preferences de l'utilisateur
        preferences = getDefaultSharedPreferences(getApplicationContext());

        volume = (CheckBox) findViewById(R.id.volume);
        orange = (RadioButton) findViewById(R.id.orange);
        jaune = (RadioButton) findViewById(R.id.jaune);
        vert = (RadioButton) findViewById(R.id.vert);
        bleu = (RadioButton) findViewById(R.id.bleu);
        violet = (RadioButton) findViewById(R.id.violet);
        rose = (RadioButton) findViewById(R.id.rose);

        couleur = (RadioGroup) findViewById(R.id.couleur);

        reset = (Button) findViewById(R.id.reset);
        retourMenu = (Button) findViewById(R.id.retourMenu);

        if (preferences.getInt("couleur", Color.rgb(48, 79, 254))==Color.rgb(255, 109, 0)){
            orange.setChecked(true);
        }
        if (preferences.getInt("couleur", Color.rgb(48, 79, 254))==Color.rgb(255, 234, 0)){
            jaune.setChecked(true);
        }
        if (preferences.getInt("couleur", Color.rgb(48, 79, 254))==Color.rgb(100, 1221, 23)){
            vert.setChecked(true);
        }
        if (preferences.getInt("couleur", Color.rgb(48, 79, 254))==Color.rgb(48, 79, 254)){
            bleu.setChecked(true);
        }
        if (preferences.getInt("couleur", Color.rgb(48, 79, 254))==Color.rgb(170, 0, 255)){
            violet.setChecked(true);
        }
        if (preferences.getInt("couleur", Color.rgb(48, 79, 254))==Color.rgb(245, 0, 87)){
            rose.setChecked(true);
        }

        volume.setChecked(preferences.getBoolean("volume",true));

        couleur.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                edit = preferences.edit();
                if (orange.isChecked()) {
                    edit.putInt("couleur", Color.rgb(255, 109, 0));
                }
                if (jaune.isChecked()) {
                    edit.putInt("couleur", Color.rgb(255, 234, 0));
                }
                if (vert.isChecked()) {
                    edit.putInt("couleur", Color.rgb(100, 1221, 23));
                }
                if (bleu.isChecked()) {
                    edit.putInt("couleur", Color.rgb(48, 79, 254));
                }
                if (violet.isChecked()) {
                    edit.putInt("couleur", Color.rgb(170, 0, 255));
                }
                if (rose.isChecked()) {
                    edit.putInt("couleur", Color.rgb(245, 0, 87));
                }
                edit.apply();
            }
        });

        volume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit = preferences.edit();
                edit.putBoolean("volume",volume.isChecked());
                edit.apply();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
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

        retourMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
