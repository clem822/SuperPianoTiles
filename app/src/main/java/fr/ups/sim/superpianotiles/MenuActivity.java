package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by clem3 on 21/03/2016.
 */
public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button boutonFacile = (Button) findViewById(R.id.buttonFacile);
        boutonFacile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TilesStartActivity.class);
                intent.putExtra("niveau", TilesView.NIVEAU_FACILE);
                startActivity(intent);
            }
        });

        Button boutonNormal = (Button) findViewById(R.id.buttonNormal);
        boutonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TilesStartActivity.class);
                intent.putExtra("niveau", TilesView.NIVEAU_NORMAL);
                startActivity(intent);
            }
        });

        Button boutonDifficile = (Button) findViewById(R.id.buttonDifficile);
        boutonDifficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TilesStartActivity.class);
                intent.putExtra("niveau", TilesView.NIVEAU_DIFFICILE);
                startActivity(intent);
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
}
