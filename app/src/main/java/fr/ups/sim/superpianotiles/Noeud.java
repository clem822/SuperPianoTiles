package fr.ups.sim.superpianotiles;

/**
 * Created by clem3 on 21/03/2016.
 */
public class Noeud {
    private Noeud suivant;
    private Noeud precedent;
    private Tile[] tabTiles;

    /**
     * Initialise un nouvel objet Noeud qui contiendra "nbTuiles" tuiles
     * @param nbTuiles Nombre de tuiles du noeud
     */
    public Noeud(int nbTuiles) {
        suivant = null;
        precedent = null;
        tabTiles = new Tile[nbTuiles];
        for (int i = 0 ; i < tabTiles.length ; ++i) {
            tabTiles[i] = new Tile(i, false);
        }
    }

    /**
     * Ajoute une tuile à la position passée en paramètre.
     * @param position
     */
    public void addTile(int position) {
        if (position >=0 && position < tabTiles.length)
        {
            tabTiles[position].setTrueTile();
            tabTiles[position].setClicked(false);
        }
    }

    /**
     * Supprime toutes les tuiles du noeud.
     */
    public void supprimerTiles() {
        for (Tile tile : tabTiles) {
            tile.setTrueTile(false);
            tile.setClicked(false);
        }
    }

    /**
     * Getter sur le noeud suivant
     * @return Le noeud suivant
     */
    public Noeud getSuivant() {
        return suivant;
    }

    /**
     * Getter sur le noeud precedent
     * @return Le noeud precedent
     */
    public Noeud getPrecedent() {
        return precedent;
    }

    /**
     * Getter sur le tableau de tuiles
     * @return Le tableau de tuiles
     */
    public Tile[] getTiles() {
        return tabTiles;
    }

    /**
     * Setter sur le noeud suivant
     * @param suivant
     */
    public void setSuivant(Noeud suivant) {
        this.suivant = suivant;
    }

    /**
     * Setter sur le noeud precedent
     * @param precedent
     */
    public void setPrecedent(Noeud precedent) {
        this.precedent = precedent;
    }

    @Override
    public String toString() {
        String str = "";
        for (Tile tile : tabTiles) {
            str += " " + tile.toString();
        }
        return str;
    }
}