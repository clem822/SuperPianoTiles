package fr.ups.sim.superpianotiles;

/**
 * Created by clem3 on 20/03/2016.
 */
public class Tile implements Comparable<Tile>{
    private int position;
    private boolean clicked = false;
    private int numero;
    //private int duree;(4 cases)


    public Tile(int position, int numero) {
        this.position = position;
        this.numero = numero;
    }

    public int getPosition() {
        return position;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public void setClicked() {
        setClicked(true);
    }

    public boolean isClicked() {
        return clicked;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public int compareTo(Tile another) {
        return ((Integer)position).compareTo((Integer)another.position);
    }

    @Override
    public String toString() {
        return ((Integer)position).toString();
    }
}
