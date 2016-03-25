package fr.ups.sim.superpianotiles;

/**
 * Created by clem3 on 20/03/2016.
 */
public class Tile {
    private int position;
    private boolean clicked = false;
    private boolean trueTile;
    //private int duree;(4 cases)

    /**
     * Initialise un nouvel objet Tile à la position passée en paramètre qui sera une vraie tuile
     * @param position La position sur la largeur
     */
    public Tile(int position) {
        this(position, true);
    }

    /**
     * Initialise un nouvel objet Tile à la position passée en paramètre qui sera selon trueTile
     * une vraie tuile ou non.
     * @param position La position sur la largeur
     * @param trueTile true si c'est une vraie tuile, false sinon
     */
    public Tile(int position, boolean trueTile) {
        this.position = position;
        this.trueTile = trueTile;
    }

    /**
     * Getter sur la position
     * @return La position sur la largeur
     */
    public int getPosition() {
        return position;
    }

    /**
     * Renvoie true si c'est une vraie tuile, false sinon.
     * @return true si c'est une vraie tuile, false sinon
     */
    public boolean isTrueTile() {
        return trueTile;
    }

    /**
     * Renvoie true si la tuile a été cliquée, false sinon.
     * @return true si la tuile a été cliquée, false sinon
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Setter sur trueTile
     * @param trueTile
     */
    public void setTrueTile(boolean trueTile) {
        this.trueTile = trueTile;
    }

    /**
     * Mets le booléen trueTile à true.
     */
    public void setTrueTile() {
        setTrueTile(true);
    }

    /**
     * Setter sur clicked
     * @param clicked
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    /**
     * Mets le booléen clicked à true.
     */
    public void setClicked() {
        setClicked(true);
    }

    @Override
    public String toString() {
        if (trueTile) return Integer.toString(position);
        else return "";
    }
}
