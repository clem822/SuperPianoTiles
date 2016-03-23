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
    private NavigableSet<Tile> tiles = new TreeSet<>();

    public Noeud() {
        suivant = null;
        precedent = null;
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

    public NavigableSet<Tile> getTiles() {
        return tiles;
    }

    public void supprimerTiles() {
        tiles = new TreeSet<>();
    }

    @Override
    public String toString() {
        String str = new String();
        NavigableSet<Tile> tiles = getTiles();
        for (Tile tile : tiles) {
            str += " " + tile.toString();
        }
        return str;
    }
}
