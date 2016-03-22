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

import java.util.NavigableSet;

/**
 * Custom view that displays tiles
 */
public class TilesView extends View {

    private int tileColor = Color.BLUE;
    private int clickedTileColor = Color.RED;
    private int textColor = Color.WHITE;
    private Drawable mExampleDrawable;
    private float textSize = 40;
    Paint pText = new Paint();
    Paint pTile = new Paint();

    private int largeurTile;
    private int hauteurTile;

    TilesQueue tilesQueue;
    private int decalage = 0;


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

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        largeurTile = contentWidth/TilesStartActivity.NB_TILES_LARGEUR;
        hauteurTile = contentHeight/TilesStartActivity.NB_TILES_HAUTEUR;

        if (tilesQueue != null)
        {
            for (int hauteur = 0 ; hauteur<TilesStartActivity.NB_TILES_HAUTEUR+1 ; ++hauteur)
            {
                NavigableSet<Tile> tiles = tilesQueue.getTiles(hauteur);
                if (tiles != null)
                {
                    for (Tile tile : tiles) {
                        addTile(tile, hauteur, canvas);
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

    public void addTile(Tile tile, int hauteur, Canvas canvas) {
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

        Rect r = new Rect();
        String numero = Integer.toString(tile.getNumero());
        pText.getTextBounds(numero, 0, numero.length(), r);
        canvas.drawText(numero, rect.centerX() - (r.width() / 2), rect.centerY() + (r.height() / 2), pText);
    }

    public void setTilesQueue(TilesQueue tilesQueue)
    {
        this.tilesQueue = tilesQueue;
    }

    public void setDecalage(double deltaT, double periodeDeDefilement) {
        decalage = (int) (deltaT * hauteurTile / periodeDeDefilement);
        postInvalidate();
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

}
