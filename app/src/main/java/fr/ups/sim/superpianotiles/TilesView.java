package fr.ups.sim.superpianotiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom view that displays tiles
 */
public class TilesView extends View {

    private int tileColor = Color.BLUE;
    private int clickedTileColor = Color.GRAY;
    private int textColor = Color.WHITE;
    private Drawable mExampleDrawable;
    private float textSize = 40;
    private Paint pText = new Paint();
    private Paint pTile = new Paint();

    private int largeurTile;
    private int hauteurTile;

    private int contentWidth;
    private int contentHeight;

    private TilesQueue tilesQueue;
    private int decalage = 0;
    private int score = 0;


    public TilesView(Context context) {
        super(context);
        init(null, 0);
    }

    public TilesView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs, 0);
    }

    public TilesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TilesView, defStyle, 0);


        if (a.hasValue(R.styleable.TilesView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.TilesView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }
        a.recycle();

        pText.setTextSize(textSize);
        pText.setColor(textColor);
        pTile.setColor(tileColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;

        largeurTile = contentWidth/TilesStartActivity.NB_TILES_LARGEUR;
        hauteurTile = contentHeight/TilesStartActivity.NB_TILES_HAUTEUR;

        if (tilesQueue != null)
        {
            for (int hauteur = 0 ; hauteur<TilesStartActivity.NB_TILES_HAUTEUR+1 ; ++hauteur)
            {
                Tile[] tiles = tilesQueue.getTiles(hauteur);
                if (tiles != null)
                {
                    for (Tile tile : tiles) {
                        addTile(tile, hauteur, canvas);
                    }
                }
            }
        }

        afficherScore(canvas);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }

        dessinerQuadrillage(canvas);
    }

    /**
    * Affiche le score sur le canvas
    * @param canvas Canvas sur lequel afficher le score
    */
    private void afficherScore(Canvas canvas) {
        Rect r = new Rect();
        String scoreString = Integer.toString(score);
        pText.setColor(Color.RED);
        pText.setTextSize(contentHeight / 16);
        pText.getTextBounds(scoreString, 0, scoreString.length(), r);
        canvas.drawText(scoreString, contentWidth / 2 - (r.width() / 2), contentHeight / 24 + r.height(), pText);
    }

    /**
     * Dessine le quadrillage
     * @param canvas Canvas sur lequel afficher le quadrillage
     */
    private void dessinerQuadrillage(Canvas canvas){
        Paint ligne = new Paint();
        ligne.setColor(Color.BLACK);

        // Dessiner les lignes verticales
        for(int i = 1; i < TilesStartActivity.NB_TILES_LARGEUR; i++)
            canvas.drawLine(i * largeurTile, getBottom(), i * largeurTile, getTop(), ligne);

        // Dessiner les lignes horizontales
        for(int i = 0; i < TilesStartActivity.NB_TILES_HAUTEUR; i++)
            canvas.drawLine(getLeft(),
                    decalage + (TilesStartActivity.NB_TILES_HAUTEUR-1-i) * hauteurTile,
                    getRight(),
                    decalage + (TilesStartActivity.NB_TILES_HAUTEUR-1-i) * hauteurTile,
                    ligne);

    }

    /**
    * Affiche une tuile sur le canvas à la hauteur (décalée selon la valeur de decalage) demandée
    * @param tile Tuile à afficher
    * @param hauteur Hauteur de la tuile
    * @param canvas Canvas sur lequel afficher la tuile
    */
    private void addTile(Tile tile, int hauteur, Canvas canvas) {
        if (tile.isTrueTile())
        {
            if (tile.isClicked())
                pTile.setColor(clickedTileColor);
            else
                pTile.setColor(tileColor);

            int left = tile.getPosition() * largeurTile;
            int top = decalage + (TilesStartActivity.NB_TILES_HAUTEUR-1-hauteur) * hauteurTile;
            int right = left + largeurTile;
            int bottom = top + hauteurTile;

            RectF rect = new RectF(left, top, right, bottom);
            canvas.drawRoundRect(rect, 2, 2, pTile);
        }
        else if (tile.isClicked())
        {
            pTile.setColor(Color.RED);
            int left = tile.getPosition() * largeurTile;
            int top = decalage + (TilesStartActivity.NB_TILES_HAUTEUR-1-hauteur) * hauteurTile;
            int right = left + largeurTile;
            int bottom = top + hauteurTile;

            RectF rect = new RectF(left, top, right, bottom);
            canvas.drawRoundRect(rect, 2, 2, pTile);
        }
    }

    public void update() {
        postInvalidate();
    }

    /**
     * Renvoie la Tile sur laquelle l'utilisateur a clique
     * @param X abscisse du point touche
     * @param Y ordonnee du point touche
     * @return Renvoie la Tile sur laquelle l'utilisateur a clique
     * si il n'a clique sur aucune Tile renvoie null
     */
    public Tile getClickedTile (float X, float Y) {

        if (tilesQueue != null) {

            int largeurTile = contentWidth / TilesStartActivity.NB_TILES_LARGEUR;
            int hauteurTile = contentHeight / TilesStartActivity.NB_TILES_HAUTEUR;

            for (int hauteur = 0; hauteur < TilesStartActivity.NB_TILES_HAUTEUR + 1; hauteur++) {

                Tile[] tiles = tilesQueue.getTiles(hauteur);

                if (tiles != null) {

                    for (Tile tile : tiles) {
                        int left = tile.getPosition() * largeurTile;
                        int top = decalage + (TilesStartActivity.NB_TILES_HAUTEUR - 1 - hauteur) * hauteurTile;
                        int right = left + largeurTile;
                        int bottom = top + hauteurTile;
                        if (X > left && X < right
                                && Y > top && Y < bottom)
                            return tile;
                    }

                }

            }

        }

        return null;

    }

    /**
     * Obtenir la hauteur sur laquelle Y se trouve
     * @param Y ordonnee
     * @return hauteur sur laquelle Y se trouve
     * -1 en cas d'erreur
     */
    public int getHauteurClicked (float Y) {

        if (tilesQueue != null) {

            int hauteurTile = contentHeight / TilesStartActivity.NB_TILES_HAUTEUR;

            for (int hauteur = 0; hauteur < TilesStartActivity.NB_TILES_HAUTEUR + 1; hauteur++) {

                int top = decalage + (TilesStartActivity.NB_TILES_HAUTEUR - 1 - hauteur) * hauteurTile;
                int bottom = top + hauteurTile;
                if (Y > top && Y < bottom)
                    return hauteur;

            }

        }

        return -1;
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }

    /**
    * Setter sur la file de tuiles
    * @param tilesQueue
    */
    public void setTilesQueue(TilesQueue tilesQueue)
    {
        this.tilesQueue = tilesQueue;
    }

    /**
    * Calcule le décalage des tuiles par rapport à la position de départ en fonction de la période
    * de défilement et du temps passé depuis la position de départ.
    * @param deltaT Temps depuis la position de départ
    * @param periodeDeDefilement Temps que met une tuile pour avancer d'une distance égale à sa hauteur
    */
    public void setDecalage(double deltaT, double periodeDeDefilement) {
        decalage = (int) (deltaT * hauteurTile / periodeDeDefilement);
        update();
    }

    /**
    * Setter sur le score courant
    * @param score
    */
    public void setScore(int score) {
        this.score = score;
        update();
    }

    /**
    * Change la couleur des tuiles
    */
    public void setTileColor(int color){
        this.tileColor=color;
    }
}
