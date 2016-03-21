package fr.ups.sim.superpianotiles;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Created by clem3 on 21/03/2016.
 */
public class Noeud {
    private Noeud suivant;
    private Noeud precedent;
    private NavigableSet<Tile> tiles;

    public Noeud() {
        suivant = null;
        precedent = null;
        tiles = new TreeSet<>();
    }

    public void addTile(Tile t)
    {
        tiles.add(t);
    }

    public void setSuivant(Noeud suivant) {
        this.suivant = suivant;
    }

    public void setPrecedent(Noeud precedent) {
        this.precedent = precedent;
    }

    public Noeud getSuivant() {
        return suivant;
    }

    public Noeud getPrecedent() {
        return precedent;
    }

    public Tile[] getTiles() {
        Tile[] ret = Arrays.copyOf(tiles.toArray(), tiles.toArray().length, Tile[].class);
        return ret;
    }

    @Override
    public String toString() {
        String str = new String();
        Tile[] tab = getTiles();
        for (int i = 0 ; i<tab.length ; ++i)
        {
            str += " " + tab[i].toString();
        }
        return str;
    }
}
