package fr.ups.sim.superpianotiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.Random;

/**
 * Custom view that displays tiles
 */
public class TilesView extends View {

    public static int NB_TILES_LARGEUR = 5;
    public static int NB_TILES_HAUTEUR = 4;

    public static int NIVEAU_FACILE = 0;
    public static int NIVEAU_NORMAL = 1;
    public static int NIVEAU_DIFFICILE = 2;

    private int tileColor = Color.BLUE;
    private int clickedTileColor = Color.RED;
    private int textColor = Color.WHITE;
    private Drawable mExampleDrawable;
    private float textSize = 40;
    Paint pText = new Paint();
    Paint pTile = new Paint();
    //private List<Pair<Integer, Integer>> tiles;
    TilesLevel level;
    private int decalage = 0;

    private int largeurTile;
    private int hauteurTile;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int contentWidth;
    private int contentHeight;

    private int niveau;


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


        //niveau = NIVEAU_DIFFICILE;

        level = new TilesLevel();
        Tile t = generateRandomTile();
        level.addTile(0, t);
        for (int i = 1 ; i<NB_TILES_HAUTEUR+2 ; ++i)
        {
            int nbTile = nbTileRandom(niveau);
            if (nbTile == 1 || nbTile == 2)
            {
                t = generateRandomTile();
                level.addTile(i, t);
            }
            if (nbTile == 2)
            {
                t = generateRandomTile(t.getPosition());
                level.addTile(i, t);
            }
        }
    }

    private int nbTileRandom(int niveau) {
        if (niveau == NIVEAU_FACILE)
        {
            return (int)Math.round(Math.random());
        }
        else if (niveau == NIVEAU_NORMAL)
        {
            return 1;
        }
        else if (niveau == NIVEAU_DIFFICILE)
        {
            return (int)Math.round(Math.random() * 1.5 + 0.5);
        }
        return 0;
    }

    private Tile generateRandomTile() {
        return new Tile((int) Math.round(Math.random() * NB_TILES_LARGEUR - 0.5));
    }

    private Tile generateRandomTile(int positionInterdite) {
        int pos;
        do
        {
            pos = (int) Math.round(Math.random() * NB_TILES_LARGEUR - 0.5);
        } while (pos == positionInterdite);
        return new Tile(pos);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;

        largeurTile = contentWidth/NB_TILES_LARGEUR;
        hauteurTile = contentHeight/NB_TILES_HAUTEUR;

        if (level != null)
        {
            for (int hauteur = 0 ; hauteur<NB_TILES_HAUTEUR+1 ; ++hauteur)
            {
                Tile[] tiles = level.getTiles(hauteur);
                if (tiles != null)
                {
                    for (Tile tile : tiles) {
                        addTile(tile, hauteur, canvas);
                        /*int left = tiles[i].getPosition() * largeurTile;
                        int top = decalage + (NB_TILES_HAUTEUR-1-hauteur) * hauteurTile;
                        int right = left + largeurTile;
                        int bottom = top + hauteurTile;
                        addTile("1", new RectF(left, top, right, bottom), tiles[i].isClicked(), canvas);*/
                    }
                }
            }
        }

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }

    public void addTile(String order, RectF rect, Canvas canvas) {
        canvas.drawRoundRect(rect, 2, 2, pTile);
        Rect r = new Rect();
        pText.getTextBounds(order, 0, order.length(), r);
        canvas.drawText(order, rect.centerX() - (r.width() / 2), rect.centerY() + (r.height() / 2), pText);
    }

    public void addTile(String order, RectF rect, boolean isClicked, Canvas canvas) {
        if (isClicked)
            pTile.setColor(clickedTileColor);
        else
            pTile.setColor(tileColor);

        canvas.drawRoundRect(rect, 2, 2, pTile);
        Rect r = new Rect();
        pText.getTextBounds(order, 0, order.length(), r);
        canvas.drawText(order, rect.centerX() - (r.width() / 2), rect.centerY() + (r.height() / 2), pText);
    }

    public void addTile(Tile tile, int hauteur, Canvas canvas) {
        if (tile.isClicked())
            pTile.setColor(clickedTileColor);
        else
            pTile.setColor(tileColor);

        int left = tile.getPosition() * largeurTile;
        int top = decalage + (NB_TILES_HAUTEUR-1-hauteur) * hauteurTile;
        int right = left + largeurTile;
        int bottom = top + hauteurTile;

        canvas.drawRoundRect(new RectF(left, top, right, bottom), 2, 2, pTile);
    }

    public void addTile(String order, Tile tile, int hauteur, Canvas canvas) {
        if (tile.isClicked())
            pTile.setColor(clickedTileColor);
        else
            pTile.setColor(tileColor);

        int left = tile.getPosition() * largeurTile;
        int top = decalage + (NB_TILES_HAUTEUR-1-hauteur) * hauteurTile;
        int right = left + largeurTile;
        int bottom = top + hauteurTile;

        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, 2, 2, pTile);

        Rect r = new Rect();
        pText.getTextBounds(order, 0, order.length(), r);
        canvas.drawText(order, rect.centerX() - (r.width() / 2), rect.centerY() + (r.height() / 2), pText);
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

    public void setLevel(TilesLevel level)
    {
        this.level = level;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public void incrementeDecalage()
    {
        decalage += 1;
        if (decalage >= hauteurTile)
        {
            decalage -= hauteurTile;
            level.supprimerLigneBasse();

            int nbTile = nbTileRandom(niveau);
            Tile t = null;
            if (nbTile == 1 || nbTile == 2)
            {
                t = generateRandomTile();
                level.addTile(NB_TILES_HAUTEUR+1, t);
            }
            if (nbTile == 2)
            {
                t = generateRandomTile(t.getPosition());
                level.addTile(NB_TILES_HAUTEUR+1, t);
            }
        }
    }
}
