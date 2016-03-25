package fr.ups.sim.superpianotiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;


/**
 * Created by Guillaume on 24/03/2016.
 */
public class PopUpPerdu extends Activity {

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popperdu);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        TextView score = (TextView) findViewById(R.id.score);
        score.setText("score : " + score);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // afficher sur une
        getWindow().setLayout((int) (width * .8), (int) (height * .8));

    }



}
