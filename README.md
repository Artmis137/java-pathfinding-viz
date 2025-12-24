# Projet Algorithmie Avancée - Recherche de chemin

**Étudiant :** Mamadou NIMAGA

**Date :** 24 Décembre 2025

**Algorithmes :** Dijkstra et A\*

## 📂 Structure du Projet

L'arborescence du projet est organisée de la façon suivante :

``` text
java-pathfinding-viz/
├── data/
│   └── graph.txt          # Carte d'entrée définissant les types de terrain
├── doc/                   # DOCUMENTATION GÉNÉRÉE (Javadoc)
│   └── index.html         # Point d'entrée de la documentation technique
├── src/
│   └── MainApp/           # Package principal
│       ├── App.java       # Interface graphique et Algorithmes (Dijkstra/A*)
│       └── WeightedGraph.java # Structure de données (Graphe, Sommets, Arêtes)
├── out.txt                # Résultat du dernier chemin calculé
├── README.md              # Ce fichier
└── .gitignore             # Fichiers exclus du rendu (bin/, .settings/, etc.)            # Documentation du projet
```

## Documentation Technique (Javadoc)

Une documentation complète des classes et des méthodes a été rédigée. Pour la consulter :

1.  Accédez au dossier `doc/`.
2.  Ouvrez le fichier **`index.html`** dans votre navigateur web préféré.
3.  Vous y trouverez le détail des implémentations, notamment la gestion du voisinage à 8 directions et le calcul des poids.

## Compilation et Exécution

### Depuis un IDE (Eclipse / IntelliJ)

1.  Importer le projet comme "Java Project".
2.  S'assurer que le dossier `data` est bien à la racine du projet.
3.  Exécuter la classe `App.java`.

### Depuis le Terminal (Ligne de commande)

À la racine du projet :

``` bash
# Compilation
javac -d bin src/MainApp/*.java

# Exécution
java -cp bin MainApp.App
```

## Récupération du dépôt (Git)

Pour récupérer l'intégralité du projet et son historique de développement :

``` bash
git clone https://github.com/Artmis137/java-pathfinding-viz.git
```

## Détails Techniques

-   **Graphe** : Construction avec une connectivité à **8 voisins**.
-   **Poids** : Moyenne des temps de parcours entre deux sommets, multipliée par pour les déplacements diagonaux.
-   **Heuristique (A*)** : Distance euclidienne entre le nœud courant et la cible.

## Résultats obtenus (graph.txt)

Le programme permet de comparer l'efficacité de Dijkstra et A\* pour un même chemin :

| Algorithme   | Nœuds explorés | Temps total (Poids) |
|--------------|----------------|---------------------|
| **Dijkstra** | 4156           | 302.61              |
| **A**\*      | 4117           | 302.61              |
