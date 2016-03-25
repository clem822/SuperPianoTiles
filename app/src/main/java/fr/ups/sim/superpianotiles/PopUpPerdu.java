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
    private int meilleurScore;

    private Intent intent;

    private TextView scoreView;
    private  TextView messageView;
    private  TextView meilleurScoreView;

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popperdu);

        intent = getIntent();
        score = intent.getIntExtra("score", 0);
        message = intent.getStringExtra("message");
        meilleurScore = intent.getIntExtra("meilleurScore", 0);

        messageView = (TextView) findViewById(R.id.message);
        messageView.setText(message);

        scoreView = (TextView) findViewById(R.id.score);
        scoreView.setText("score : " + score);

        meilleurScoreView = (TextView) findViewById(R.id.meilleurScore);
        meilleurScoreView.setText("meilleur score : "+meilleurScore);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // afficher sur une
        getWindow().setLayout((int) (width * .8), (int) (height * .8));

    }



}
