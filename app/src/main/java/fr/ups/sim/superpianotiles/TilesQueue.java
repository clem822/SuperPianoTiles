package fr.ups.sim.superpianotiles;

import java.util.NavigableSet;

/**
 * Created by clem3 on 20/03/2016.
 */
public class TilesQueue {
    private Noeud bas;
    private final int nbNoeuds;

    public TilesQueue(int nbNoeuds) {
        if (nbNoeuds <= 0) {
            System.out.println("Le nombre de noeuds doit être supérieur à 0. Il sera initialisé à 1.");
            this.nbNoeuds = 1;
        } else {
            this.nbNoeuds = nbNoeuds;
        }
        for (int i = 0 ; i < nbNoeuds ; ++i)
        {
            ajouterNoeud();
        }
    }

    private void ajouterNoeud() {
        if (bas == null)
        {
            bas = new Noeud();
            bas.setPrecedent(bas);
            bas.setSuivant(bas);
        }
        else
        {
            Noeud n = new Noeud();
            bas.getPrecedent().setSuivant(n);
            n.setPrecedent(bas.getPrecedent());
            bas.setPrecedent(n);
            n.setSuivant(bas);
        }
    }

    private Noeud getNoeud(int index) {
        Noeud courant = bas;
        int hauteurCourante = 0;
        while (hauteurCourante < index)
        {
            courant = courant.getSuivant();
            ++hauteurCourante;
        }
        return courant;
    }

    public void addTile(int hauteur, Tile t) {
        if (hauteur >= 0 && hauteur < nbNoeuds)
        {
            getNoeud(hauteur).addTile(t);
        }
    }

    public NavigableSet<Tile> getTiles(int hauteur) {
        if (hauteur >= 0 && hauteur < nbNoeuds)
        {
            return getNoeud(hauteur).getTiles();
        }

        return null;
    }

    public void supprimerLigneBasse() {
        bas.supprimerTiles();
        bas = bas.getSuivant();
    }

    @Override
    public String toString() {
        String str = new String();
        Noeud courant = bas;
        int hauteurCourante = 0;
        while (hauteurCourante < nbNoeuds)
        {
            str += courant.toString();
            str += "\n";
            courant = courant.getSuivant();
            ++hauteurCourante;
        }
        return str;
    }

}



/*
package fr.ups.sim.superpianotiles;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.TreeSet;

*/
/**
 * Created by clem3 on 20/03/2016.
 *//*

public class TilesQueue {
    private Noeud bas;
    private Noeud haut;
    //nombre de noeuds entre haut et bas (compris)
    private int hauteurRestante;

    public void addTile(int hauteur, Tile t) {
        if (hauteur >= 0)
        {
            if (bas == null)
                ajouterNoeud();

            while (hauteurRestante <= hauteur)
            {
                ajouterNoeud();
            }

            Noeud courant = haut;
            int hauteurCourante = hauteurRestante-1;
            while (hauteurCourante > hauteur)
            {
                courant = courant.getPrecedent();
                --hauteurCourante;
            }

            courant.addTile(t);
        }

    }

    private void ajouterNoeud() {
        if (bas == null)
        {
            bas = new Noeud();
            haut = bas;
            hauteurRestante = 1;
        }
        else
        {
            Noeud n = new Noeud();
            haut.setSuivant(n);
            n.setPrecedent(haut);
            haut = n;
            ++hauteurRestante;
        }
    }

    public NavigableSet<Tile> getTiles(int hauteur) {
        //System.out.println("hauteur = " + hauteur);
        //System.out.println("Level = " + this);
        if (hauteur >= 0 && hauteur < hauteurRestante)
        {
            Noeud courant = bas;
            int hauteurCourante = 0;
            while (hauteurCourante < hauteur)
            {
                courant = courant.getSuivant();
                ++hauteurCourante;
            }

            return courant.getTiles();
        }

        return null;
    }

    public void supprimerLigneBasse() {
        if (bas != null)
        {
            //System.out.println(this);
            bas = bas.getSuivant();
            --hauteurRestante;
            bas.setPrecedent(null);
            if (hauteurRestante == 0)
                haut = null;
            //System.out.println(bas);
        }
    }

    public void ajouterLigneVide(int hauteur) {
        if (hauteur >= 0)
        {
            if (bas == null)
                ajouterNoeud();

            while (hauteurRestante <= hauteur)
            {
                ajouterNoeud();
            }
        }
    }

    @Override
    public String toString() {
        String str = new String();
        if (haut != null) {
            Noeud courant = haut;
            while (courant != bas)
            {
                //System.out.println(courant);
                str += courant.toString();
                courant = courant.getPrecedent();
                str += "\n";
            }
            str += courant.toString();
        }
        return str;
    }

}
*/
