package fr.ups.sim.superpianotiles;

/**
 * Created by clem3 on 20/03/2016.
 */
public class TilesQueue {
    private Noeud bas;
    private int nbNoeuds;
    private int nbTileParNoeud;

    /**
     * Initialise un nouvel objet TilesQueue qui contiendra "nbNoeuds" noeuds et "nbTileParNoeud"
     * tuiles par noeud.
     * @param nbNoeuds Nombre de noeds
     * @param nbTileParNoeud Nombre de tuiles par noeud
     */
    public TilesQueue(int nbNoeuds, int nbTileParNoeud) {
        if (nbNoeuds <= 0) {
            System.out.println("Le nombre de noeuds doit être supérieur à 0. Il sera initialisé à " + (TilesStartActivity.NB_TILES_HAUTEUR + 1) + ".");
            this.nbNoeuds = TilesStartActivity.NB_TILES_HAUTEUR + 1;
        } else {
            this.nbNoeuds = nbNoeuds;
        }
        if (nbTileParNoeud < 0) {
            System.out.println("Le nombre de tuile par noeud doit être positif. Il sera initialisé à " + TilesStartActivity.NB_TILES_LARGEUR + ".");
            this.nbTileParNoeud = TilesStartActivity.NB_TILES_LARGEUR;
        } else {
            this.nbTileParNoeud = nbTileParNoeud;
        }
        for (int i = 0 ; i < nbNoeuds ; ++i)
        {
            ajouterNoeud();
        }
    }

    /**
     *Ajoute une tuile à la hauteur et la position demandée
     * @param hauteur
     * @param position
     */
    public void addTile(int hauteur, int position) {
        if (hauteur >= 0 && hauteur < nbNoeuds)
        {
            getNoeud(hauteur).addTile(position);
        }
    }

    /**
     * Renvoie le contenu de la ligne qui est à la hauteur passée en paramètres
     * @param hauteur
     * @return Le contenu de la ligne qui est à la hauteur passée en paramètres
     */
    public Tile[] getTiles(int hauteur) {
        if (hauteur >= 0 && hauteur < nbNoeuds)
        {
            return getNoeud(hauteur).getTiles();
        }

        return null;
    }

    /**
     * Supprime la ligne la plus basse de la file
     */
    public void supprimerLigneBasse() {
        bas.supprimerTiles();
        bas = bas.getSuivant();
    }

    /**
     * Ajoute un noeud à le file
     */
    private void ajouterNoeud() {
        if (bas == null)
        {
            bas = new Noeud(nbTileParNoeud);
            bas.setPrecedent(bas);
            bas.setSuivant(bas);
        }
        else
        {
            Noeud n = new Noeud(nbTileParNoeud);
            bas.getPrecedent().setSuivant(n);
            n.setPrecedent(bas.getPrecedent());
            bas.setPrecedent(n);
            n.setSuivant(bas);
        }
    }

    /**
     * Retourne le noeud d'undice "index"
     * @param index Indice du noeud
     * @return
     */
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

    @Override
    public String toString() {
        String str = "";
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