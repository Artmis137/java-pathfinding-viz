// Par Sylvain Lobry, pour le cours "IF05X040 Algorithmique avancée"
// de l'Université de Paris, 11/2020

package MainApp;

import java.util.Queue;

import MainApp.WeightedGraph.Edge;
import MainApp.WeightedGraph.Graph;
import MainApp.WeightedGraph.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JFrame;


/**
 * Classe gérant l'affichage graphique du labyrinthe et des algorithmes.
 */
class Board extends JComponent 
{
	private static final long serialVersionUID = 1L;
	Graph graph;
	int pixelSize;
	int ncols;
	int nlines;
	int start;
	int end;
	double max_distance;
	int current;
	LinkedList<Integer> path;
	
	/**
	 * Constructeur de l'interface graphique.
	 * @param graph Le graphe à afficher.
	 * @param pixelSize Taille en pixels d'une cass.
	 * @param ncols Nombre de colonnes.
	 * @param nlines Nombre de lignes.
	 * @param colors Mapping des types de terrain vers couleurs.
	 * @param start Index du point de départ.
	 * @param end Index du point d'arrivée.
	 */
    public Board(Graph graph, int pixelSize, int ncols, int nlines, int start, int end)
    {
        super();
        this.graph = graph;
        this.pixelSize = pixelSize;
        this.ncols = ncols;
        this.nlines = nlines;
        this.start = start;
        this.end = end;
        this.max_distance = ncols * nlines;
        this.current = -1;
        this.path = null;
    }
    
    /**
     * Gère le rendu graphique des composants (cases, exploration, chemin rouge).
     */
	public void paint(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				        	RenderingHints.VALUE_ANTIALIAS_ON);
		//Fond blanc
		g2.setColor(Color.WHITE);
		g2.fill(new Rectangle2D.Double(0,0,this.ncols*this.pixelSize, this.nlines*this.pixelSize));
		
		
		int num_case = 0;
		for (WeightedGraph.Vertex v : this.graph.vertexlist)
		{
			
			int i = num_case / this.ncols;
			int j = num_case % this.ncols;

			// Logique d'affichage
			if(v.info == '#') {
				g2.setPaint(Color.BLACK); // Mur en noir
				g2.fill(new Rectangle2D.Double(j * this.pixelSize, i * this.pixelSize, this.pixelSize, this.pixelSize));
			}else if (v.info == 'F') {
                g2.setPaint(Color.ORANGE); // Feu en Orange
                g2.fill(new Rectangle2D.Double(j * this.pixelSize, i * this.pixelSize, this.pixelSize, this.pixelSize));
            } else if (v.info == 'D') {
                g2.setPaint(Color.GREEN); // Départ en Vert
                g2.fill(new Rectangle2D.Double(j * this.pixelSize, i * this.pixelSize, this.pixelSize, this.pixelSize));
            } else if (v.info == 'S') {
                g2.setPaint(Color.BLUE); // Sortie en Bleu
                g2.fill(new Rectangle2D.Double(j * this.pixelSize, i * this.pixelSize, this.pixelSize, this.pixelSize));
            } else {
                // Case libre (.) : on dessine juste une bordure légère
                g2.setPaint(Color.LIGHT_GRAY);
                g2.draw(new Rectangle2D.Double(j * this.pixelSize, i * this.pixelSize, this.pixelSize, this.pixelSize));
            }
			
			// Point rouge pour le noeud courant exploré par A*
			
			if (num_case == this.current)
			{
				g2.setPaint(Color.RED);
				g2.draw(new Ellipse2D.Double(j*this.pixelSize + this.pixelSize/2 - 3, i * this.pixelSize + this.pixelSize/2 - 3, 6, 6));
			}
			
			num_case += 1;
		}
		
		// Dessin du chemin final (si trouvé)
		
		int prev = -1;
		if (this.path != null)
		{
			g2.setStroke(new BasicStroke(3.0f));
			for (int cur : this.path)
			{
				if (prev != -1)
				{
					g2.setPaint(Color.RED);
					int i = prev / this.ncols;
					int j = prev % this.ncols;
					int i2 = cur / this.ncols;
					int j2 = cur % this.ncols;
					g2.draw(new Line2D.Double(j * this.pixelSize + this.pixelSize/2.0, i * this.pixelSize + this.pixelSize/2.0, j2 * this.pixelSize + this.pixelSize/2.0, i2 * this.pixelSize + this.pixelSize/2.0));
				}
				prev = cur;
			}
		}
	}
	
	/**
	 * Met à jour le noeud courant en cours d'exploration et rafraîchit l'affichage.
	 * @param graph Le graphe mis à jour.
	 * @param current L'index du noeud actuellement traité.
	 */
	public void update(Graph graph, int current)
	{
		this.graph = graph;
		this.current = current;
		repaint();
	}
	
	/**
	 * Affiche le chemin final calculé par l'algorithme.
	 * @param graph Le graphe
	 * @param path Liste ordonnée des index des sommets du chemin.
	 */
	public void addPath(Graph graph, LinkedList<Integer> path)
	{
		this.graph = graph;
		this.path = path;
		this.current = -1;
		repaint();
	}
}

/**
 * Classe principale contenant les implémentations des algorithmes de pathfinding.
 */
public class App {
	
	/**
	 * Initialise la fenêtre principale de l'application.
	 * @param board
	 * @param nlines
	 * @param ncols
	 * @param pixelSize
	 */
	private static void drawBoard(Board board, int nlines, int ncols, int pixelSize)
	{
	    JFrame window = new JFrame("Labyrinthe d'Ayutthaya");
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(0, 0, ncols*pixelSize+20, nlines*pixelSize+40);
	    window.getContentPane().add(board);
	    window.setVisible(true);
	}
	

	/**
	 * Calcule le temps d'arrivée du feu sur chaque case du labyrinthe.
	 * Utilise un algorithme de parcours en largeur (BFS) multi-sources.
	 * Le feu ne peut pas traverser les murs ('#').
	 * @param graph Le graphe représentant la pièce.
	 * @param nlines Nombre de lignes de la grille.
	 * @param ncols Nombre de colonnes de la grille.
	 * @return Un tableau contenant le temps minimum d'arrivée du feu pour chaque sommet.
	 */
	private static double[] computeFireTimes(Graph graph, int nlines, int ncols) {
		int totalNodes = nlines * ncols;
		double[] fireTimes = new double[totalNodes];
		Arrays.fill(fireTimes, Double.POSITIVE_INFINITY);
		Queue<Integer> queue = new LinkedList<>();
		
		// On identifie toutes les sources de feu initiales
		for(Vertex v : graph.vertexlist) {
			if(v.info == 'F') {
				fireTimes[v.num] = 0;
				queue.add(v.num);
			}
		}
		
		int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // N, S, E, 0
		while(!queue.isEmpty()) {
			int u = queue.poll();
			int r = u / ncols;
			int c = u % ncols;
			
			for(int[] d : dirs) {
				int nr = r + d[0], nc = c + d[1];
				if(nr >= 0 && nr < nlines && nc >= 0 && nc < ncols) {
					int vIdx = nr * ncols + nc;
					// Le feu ne traverse pas les murs.
					if(graph.vertexlist.get(vIdx).info != '#' && fireTimes[vIdx] == Double.POSITIVE_INFINITY) {
						fireTimes[vIdx] = fireTimes[u] + 1;
						queue.add(vIdx);
					}
				}
			}
		}
		
		return fireTimes;
	}
	
	/**
	 * Estime la distance restante entre deux sommets.
	 * Utilise la distance de Manhattan (|x1-x2| + |y1-y2|), optimale pour 
	 * les déplacements orthogonaux (4-connexité).
	 */
	private static double estimationManhattan(int n1, int n2, int ncols) {
		return (Math.abs( n1 % ncols- n2 % ncols) + Math.abs(n1 / ncols - n2 / ncols));
	}

	/**
	 * Recherche le plus court chemin pour le prisonnier en évitant le feu.
	 * Implémentation de l'algorithme A* avec une contrainte de survie dynamique :
	 * le prisonnier doit atteindre une case strictement avant le feu.
	 * @param graph Le graphe du labyrinthe.
	 * @param start Index du sommet de départ ('D').
	 * @param end Index du sommet de sortie ('S').
	 * @param ncols Nombre de colonnes (pour le calcul de l'heuristique).
	 * @param board Composant graphique pour l'animation.
	 * @param fireTimes Temps d'arrivée du feu pré-calculés.
	 * @return La liste des sommets formant le chemin de survie, ou une liste vide.
	 */
	private static LinkedList<Integer> AStar(Graph graph, int start, int end, int ncols, Board board, double[] fireTimes)
	{
		// Initialisation g(start) = 0
		graph.vertexlist.get(start).timeFromSource=0;
		
		// PriorityQueue utilisant l'heuristique de Manhattan
		PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingDouble(v -> graph.vertexlist.get(v).timeFromSource + estimationManhattan(v, end, ncols)));
		
		pq.add(start);
		HashSet<Integer> visited = new HashSet<>();		
		int number_tries = 0;
		
		while(!pq.isEmpty()) {
			int u = pq.poll();
			
			if(u == end) break;
			if(visited.contains(u)) continue;
			visited.add(u);
			number_tries++;
			
			// Animation graphique
			if(board != null) {
				try {
		    	    board.update(graph, u);
		    	    Thread.sleep(10);
		    	} catch(InterruptedException e) {
		    	    System.out.println("Animation interrompue");
		    	}
			}  
			
			for(WeightedGraph.Edge edge : graph.vertexlist.get(u).adjacencylist) {
				int v = edge.destination;
				double arrivalTime = graph.vertexlist.get(u).timeFromSource + edge.weight;
				
				// Condition de survie : le prisionnier (arrivalTime) doit être là avant le feu (fireTimes)
				if(arrivalTime < fireTimes[v]) {
					if(arrivalTime < graph.vertexlist.get(v).timeFromSource) {
						graph.vertexlist.get(v).timeFromSource = arrivalTime;
						graph.vertexlist.get(v).prev = graph.vertexlist.get(u);
						pq.add(v);
					}
				}
			}
		}
		
		// Reconstruction du chemin
		LinkedList<Integer> path = new LinkedList<>();
		Vertex current = graph.vertexlist.get(end);
		while(current != null) {
			path.addFirst(current.num);
			current = current.prev;
		}
		
		// Affichage du chemin final
		if(board != null && !path.isEmpty()) {
			board.addPath(graph, path); // Trace la ligne rouge finale
		}
		
		System.out.println("Calcul terminé : " + number_tries + " noeuds exporés.");		
		return path;
	}
	
	
	/**
	 * Charge la carte, construit le graphe et gère l'interaction utilisateur.
	 * @param args
	 */
	public static void main(String[] args) {		
		try {
			// Obtenir le fichier de ayutthaya
			File myObj = new File("data/ayutthaya.txt");
			Scanner myReader = new Scanner(myObj);
			
			while(myReader.hasNext() && !myReader.hasNextInt()) {
				myReader.next();
			}
			if(!myReader.hasNextInt()) return;
			int T = myReader.nextInt(); // Nombre d'instances
			
			
			for(int t = 0; t < T; t++) {
				int nlines = myReader.nextInt();
				int ncols = myReader.nextInt();
				Graph graph = new Graph();
				int startV = -1, endV = -1;
				
				// Lecture de la grille et détection D/S
				for(int i = 0; i< nlines; i++) {
					String line = myReader.next();
					for(int j = 0; j < ncols; j++) {
						char symbol = line.charAt(j);
						graph.addVertex(symbol);
						if(symbol == 'D') startV = i * ncols + j;;
						if(symbol == 'S') endV = i * ncols + j;;
					}
				}
				
				// 4-connexité sans les murs
				for(int line = 0; line < nlines; line++) {
					for(int col = 0; col < ncols; col++) {
						int source = line * ncols + col;
						if(graph.vertexlist.get(source).info == '#') continue;
						int[][] steps = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
						for (int[] d : steps) {
                            int vL = line + d[0], vC = col + d[1];
                            if (vL >= 0 && vL < nlines && vC >= 0 && vC < ncols) {
                                int dst = vL * ncols + vC;
                                if (graph.vertexlist.get(dst).info != '#') graph.addEgde(source, dst, 1.0);
                            }
                        }
					}
				}
				
				double[] fireTimes = computeFireTimes(graph, nlines, ncols);
				Board board = new Board(graph, 40, ncols, nlines, startV, endV);
				drawBoard(board, nlines, ncols, 40);
				
				// Exécution (A* pour l'exemple)
				if(startV!= -1 && endV != -1) {
					AStar(graph, startV, endV, ncols, board, fireTimes);
					if(graph.vertexlist.get(endV).timeFromSource != Double.POSITIVE_INFINITY) {
						System.out.println("Instance " + (t+1) + ": Y");
					}else {
						System.out.println("Instance " + (t+1) + ": N");
					}
				}
				// Pausse entre les instances pour voir le résultat
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}
