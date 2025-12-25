// Par Sylvain Lobry, pour le cours "IF05X040 Algorithmique avancée"
// de l'Université de Paris, 11/2020

package MainApp;

import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Classe prinicipale définissant la structure d'un graphe pondéré.
 * Elle contient les définitions des arêtes, des sommets et de la gestion globale du graphe.
 */
public class WeightedGraph {
	/**
	 * Représente une arête orientée et pondérée reliant deux sommets.
	 */
    static class Edge {
        int source;
        int destination;
        double weight;

        public Edge(int source, int destination, double weight) {
            this.source = source; 			// Index du sommet de départ
            this.destination = destination; // Index du sommet d'arrivée
            this.weight = weight;			// Poids de l'arête (moyenne des indivTime * sqrt(2) si diagonale)
        }
    }
    
    /**
     * Représente un sommet (noeud) du graphe.
     * Contient les informations néceessaires pour les algorithmes de recherche de chemin.
     */
    static class Vertex {
    	double indivTime;		// Temps de traversée propre à cette case (défni par la couleur)
    	double timeFromSource;  // Valeur g(n) : coût cumulé depuis le départ (Dijkstra/A*)
    	double heuristic;		// Valeur h(n) : estimation du coût vers l'arrivéer (A*)
    	Vertex prev;			// Référence vers le sommet précédent pour reconstruire le chemin final
    	LinkedList<Edge> adjacencylist; // Liste des arêtes partant de ce sommet (8 voisins max)
    	int num;				// Index unique du sommet dans la liste du graphe
    	
    	/**
    	 * Constructeur d'un sommet avec initailisation des valeurs par défaut.
    	 * @param num L'index du sommet.
    	 */
    	public Vertex(int num) {
    		this.indivTime = Double.POSITIVE_INFINITY;
    		this.timeFromSource = Double.POSITIVE_INFINITY;
    		this.heuristic = -1;
    		this.prev = null;
    		this.adjacencylist = new LinkedList<Edge>();
    		this.num = num;
    	}
    	
    	/**
    	 * Ajoute un voisin à la liste d'adjacence du sommet.
    	 * @param e L'arête reliant ce sommet àn son voisin.
    	 */
    	public void addNeighbor(Edge e) {
    		this.adjacencylist.addFirst(e);
    	}
    }

    /**
     * Gère la colleciton des sommets et la création des connexions.
     */
    static class Graph {
        ArrayList<Vertex> vertexlist; // Liste exhaustive de tous les sommets de la carte
        int num_v = 0;				  // Compteur pour l'attribution automatique des index

        
        /**
         * Constructeur par défaut du graph qui initalise {@link vertexList} avec une liste vide.
         */
        Graph() {
            vertexlist = new ArrayList<Vertex>();
        }

        /**
         * Ajoute un nouveau sommet au grpahe.
         * @param indivTime  Le coût traversée de ce sommet.
         */
        public void addVertex(double indivTime)
        {
        	Vertex v = new Vertex(num_v);
        	v.indivTime = indivTime;
        	vertexlist.add(v);
        	num_v = num_v + 1;
        }
        
        /**
         * Crée et ajoute une arête entre deux sommets.
         * @param source Index du sommet source
         * @param destination Index du sommet destination.
         * @param weight Poids calculé de l'arête.
         */
        public void addEgde(int source, int destination, double weight) {
            Edge edge = new Edge(source, destination, weight);
            vertexlist.get(source).addNeighbor(edge);
        }

    }
    
    /**
     * Méthode de test rapide pour valider la structure de données.
     * @param args Pour passer des arguments à partir du sommet (non traité ici).
     */
      public static void main(String[] args) {
            int vertices = 6;
            Graph graph = new Graph();
            graph.addVertex(10);
            graph.addVertex(10);
            graph.addVertex(10);
            graph.addVertex(10);
            graph.addVertex(10);
            graph.addVertex(10);
            graph.addEgde(0, 1, 4);
            graph.addEgde(0, 2, 3);
            graph.addEgde(1, 3, 2);
            graph.addEgde(1, 2, 5);
            graph.addEgde(2, 3, 7);
            graph.addEgde(3, 4, 2);
            graph.addEgde(4, 0, 4);
            graph.addEgde(4, 1, 4);
            graph.addEgde(4, 5, 6);
            //graph.printGraph();
        }
}
