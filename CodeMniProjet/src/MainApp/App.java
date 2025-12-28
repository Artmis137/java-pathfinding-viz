package MainApp;

import MainApp.WeightedGraph.Graph;
import MainApp.WeightedGraph.Vertex;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.JComponent;

/**
 * Classe gérant l'affichage graphique du labyrinthe et des algorithmes. Elle
 * permet de visualiser dynamiquement l'exploration de l'algorithme A*.
 *
 */
class Board extends JComponent {
	private static final long serialVersionUID = 1L;
	private Graph graph;
	private int pixelSize, ncols, nlines, current;
	private LinkedList<Integer> path;

	/**
	 * Constructeur de l'interface graphique.
	 * 
	 * @param pixelSize Taille en pixels d'une case (détermine la résolution de la
	 *                  fenêtre).
	 */
	public Board(int pixelSize) {
		this.pixelSize = pixelSize;
		this.current = -1;
	}

	/**
	 * Configure le graphe et les dimensions pour une instance de labyrinthe donnée.
	 * 
	 * @param graph  Le graphe représentant la carte.
	 * @param ncols  Nombre de colonnes de la grille.
	 * @param nlines Nombre de lignes de la grille.
	 */
	public void setMap(Graph graph, int ncols, int nlines) {
		this.graph = graph;
		this.ncols = ncols;
		this.nlines = nlines;
		this.path = null;
		this.current = -1;
		repaint();
	}

	/**
	 * Met à jour l'affichage pour souligner le nœud actuellement exploré.
	 * 
	 * @param current Index du sommet en cours de traitement par A*.
	 */
	public void update(int current) {
		this.current = current;
		repaint();
	}

	/**
	 * Enregistre le chemin final trouvé pour l'afficher graphiquement.
	 * 
	 * @param path Liste ordonnée des sommets formant le chemin de survie.
	 */
	public void addPath(LinkedList<Integer> path) {
		this.path = path;
		this.current = -1;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (graph == null)
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int num_case = 0;
		for (Vertex v : graph.vertexlist) {
			int i = num_case / ncols, j = num_case % ncols;
			if (v.info == '#')
				g2.setPaint(Color.BLACK);
			else if (v.info == 'F')
				g2.setPaint(Color.ORANGE);
			else if (v.info == 'D')
				g2.setPaint(Color.GREEN);
			else if (v.info == 'S')
				g2.setPaint(Color.BLUE);
			else {
				g2.setPaint(Color.LIGHT_GRAY);
				g2.draw(new Rectangle2D.Double(j * pixelSize, i * pixelSize, pixelSize, pixelSize));
				num_case++;
				continue;
			}
			g2.fill(new Rectangle2D.Double(j * pixelSize, i * pixelSize, pixelSize, pixelSize));
			if (num_case == current) {
				g2.setPaint(Color.RED);
				g2.fill(new Ellipse2D.Double(j * pixelSize + pixelSize / 2 - 4, i * pixelSize + pixelSize / 2 - 4, 8,
						8));
			}
			num_case++;
		}

		if (path != null) {
			g2.setStroke(new BasicStroke(3.0f));
			g2.setPaint(Color.RED);
			for (int k = 0; k < path.size() - 1; k++) {
				int p1 = path.get(k), p2 = path.get(k + 1);
				g2.draw(new Line2D.Double((p1 % ncols) * pixelSize + pixelSize / 2.0,
						(p1 / ncols) * pixelSize + pixelSize / 2.0, (p2 % ncols) * pixelSize + pixelSize / 2.0,
						(p2 / ncols) * pixelSize + pixelSize / 2.0));
			}
		}
	}
}

/**
 * Classe principale pilotant la résolution du problème d'Ayutthaya. Gère le
 * chargement des fichiers, la construction du graphe et les algorithmes.
 */
public class App {
	private static JFrame window;
	private static Board board;

	/**
	 * Point d'entrée du programme. Gère les arguments de ligne de commande pour
	 * l'automatisation des tests.
	 *
	 * @param args Arguments de la console. args[0] peut contenir le chemin d'un
	 *             fichier.
	 */
	public static void main(String[] args) {
		File fileToRead = null;
		if (args.length > 0) {
			fileToRead = new File(args[0]);
			System.out.println("[INFO] Lecture du fichier argument : " + args[0]);
		} else {
			fileToRead = new File("data/ayutthaya.txt");
			System.out.println("[INFO] Utilisation du fichier par défaut (dans le dossier CodeMiniProjet) : data/ayutthaya.txt");
		}
		
		// On vérifie l'existance avant d'ouvrir le fichier
		if(!fileToRead.exists()) {
			System.out.println("[ERREUR] Le fichier est introuvable : " + fileToRead.getAbsolutePath());
		    System.out.println("[AIDE] Assurez-vous d'être dans le dossier du projet ou de donner un chemin valide en argument.");
		    return; // On quitte proprement
		}

		
		try (Scanner reader = new Scanner(fileToRead)) {
			setupGUI();
			while (reader.hasNext() && !reader.hasNextInt())
				reader.next();
			if (!reader.hasNextInt())
				return;
			int T = reader.nextInt();

			for (int t = 0; t < T; t++) {
				System.out.println("\n--- Traitement de l'Instance " + (t + 1) + " ---");
				while (reader.hasNext() && !reader.hasNextInt())
					reader.next();
				int nlines = reader.nextInt(), ncols = reader.nextInt();

				Graph graph = new Graph();
				int startV = -1, endV = -1;
				for (int i = 0; i < nlines; i++) {
					String line = reader.next();
					for (int j = 0; j < ncols; j++) {
						char s = line.charAt(j);
						graph.addVertex(s);
						if (s == 'D')
							startV = i * ncols + j;
						if (s == 'S')
							endV = i * ncols + j;
					}
				}

				buildEdges(graph, nlines, ncols);
				double[] fireTimes = computeFireTimes(graph, nlines, ncols);

				updateGUIWindow(ncols, nlines);
				board.setMap(graph, ncols, nlines);

				if (startV != -1 && endV != -1) {
					LinkedList<Integer> path = AStar(graph, startV, endV, ncols, fireTimes);
					if (graph.vertexlist.get(endV).timeFromSource != Double.POSITIVE_INFINITY) {
						System.out.println("Résultat : [Y]"); //
						board.addPath(path);
					} else {
						System.out.println("Résultat : [N]"); //
					}
				}
				Thread.sleep(1500);
			}
			System.out.println("\n[FIN] Traitement terminé.");
		} catch (FileNotFoundException e) {
			System.err.println("[ERREUR] Le fichier est introuvable.");
		} catch (Exception e) {
			System.err.println("[ERREUR] " + e.getMessage());
		} finally {
			System.exit(0); // Assure le retour au prompt
		}
	}

	/**
	 * Initialise la structure de la fenêtre graphique.
	 */
	private static void setupGUI() {
		window = new JFrame("Labyrinthe d'Ayutthaya - Visualisation");
		board = new Board(40);
		window.add(board);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	/**
	 * Adapte la taille de la fenêtre aux dimensions de la grille courante.
	 * 
	 * @param ncols  Nombre de colonnes.
	 * @param nlines Nombre de lignes.
	 */
	private static void updateGUIWindow(int ncols, int nlines) {
		window.setSize(ncols * 40 + 20, nlines * 40 + 40);
		window.setLocationRelativeTo(null);
	}

	/**
	 * Établit les connexions entre les sommets (4-connexité).
	 * 
	 * @param g  Le graphe à construire.
	 * @param nl Nombre de lignes.
	 * @param nc Nombre de colonnes.
	 */
	private static void buildEdges(Graph g, int nl, int nc) {
		int[][] steps = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		for (int i = 0; i < nl; i++) {
			for (int j = 0; j < nc; j++) {
				int src = i * nc + j;
				if (g.vertexlist.get(src).info == '#')
					continue;
				for (int[] d : steps) {
					int ni = i + d[0], nj = j + d[1];
					if (ni >= 0 && ni < nl && nj >= 0 && nj < nc) {
						int dst = ni * nc + nj;
						if (g.vertexlist.get(dst).info != '#')
							g.addEgde(src, dst, 1.0);
					}
				}
			}
		}
	}

	/**
	 * Simule l'expansion du feu sur la grille.
	 * 
	 * @param g  Le graphe du labyrinthe.
	 * @param nl Nombre de lignes.
	 * @param nc Nombre de colonnes.
	 * @return Un tableau de doubles associant chaque index de sommet au temps
	 *         d'arrivée du feu.
	 */
	private static double[] computeFireTimes(Graph g, int nl, int nc) {
		double[] fTimes = new double[nl * nc];
		Arrays.fill(fTimes, Double.POSITIVE_INFINITY);
		Queue<Integer> q = new LinkedList<>();
		for (Vertex v : g.vertexlist)
			if (v.info == 'F') {
				fTimes[v.num] = 0;
				q.add(v.num);
			}
		int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		while (!q.isEmpty()) {
			int u = q.poll();
			for (int[] d : dirs) {
				int ni = (u / nc) + d[0], nj = (u % nc) + d[1];
				if (ni >= 0 && ni < nl && nj >= 0 && nj < nc) {
					int v = ni * nc + nj;
					if (g.vertexlist.get(v).info != '#' && fTimes[v] == Double.POSITIVE_INFINITY) {
						fTimes[v] = fTimes[u] + 1;
						q.add(v);
					}
				}
			}
		}
		return fTimes;
	}

	/**
	 * Recherche le plus court chemin de survie via l'algorithme A*. Utilise la
	 * distance de Manhattan comme heuristique : $$h(n) = |x_n - x_{but}| + |y_n -
	 * y_{but}|$$
	 * 
	 * @param g  Le graphe du labyrinthe.
	 * @param s  Index du sommet de départ.
	 * @param e  Index du sommet de sortie.
	 * @param nc Nombre de colonnes (pour les coordonnées).
	 * @param ft Tableau des temps d'arrivée du feu pré-calculés.
	 * @return La liste des sommets constituant le chemin de survie.
	 */
	private static LinkedList<Integer> AStar(Graph g, int s, int e, int nc, double[] ft) {
		g.vertexlist.get(s).timeFromSource = 0;
		PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingDouble(
				v -> g.vertexlist.get(v).timeFromSource + Math.abs(v % nc - e % nc) + Math.abs(v / nc - e / nc)));
		pq.add(s);
		HashSet<Integer> visited = new HashSet<>();
		while (!pq.isEmpty()) {
			int u = pq.poll();
			if (u == e)
				break;
			if (visited.contains(u))
				continue;
			visited.add(u);
			board.update(u);
			try {
				Thread.sleep(30);
			} catch (Exception ex) {
			}

			for (WeightedGraph.Edge edge : g.vertexlist.get(u).adjacencylist) {
				int v = edge.destination;
				double arr = g.vertexlist.get(u).timeFromSource + 1;
				// Condition de survie : arriver avant le feu
				if (arr < ft[v] && arr < g.vertexlist.get(v).timeFromSource) {
					g.vertexlist.get(v).timeFromSource = arr;
					g.vertexlist.get(v).prev = g.vertexlist.get(u);
					pq.add(v);
				}
			}
		}
		LinkedList<Integer> p = new LinkedList<>();
		Vertex curr = g.vertexlist.get(e);
		while (curr != null) {
			p.addFirst(curr.num);
			curr = curr.prev;
		}
		return p;
	}
}