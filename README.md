# Projet Algorithmie Avancée - Recherche de chemin

**Étudiant :** Mamadou NIMAGA

**Date :** 24 Décembre 2025

**Algorithmes :** Dijkstra et A\*

## 📂 Structure du Projet

L'arborescence du projet est organisée de la façon suivante :

``` text
java-pathfinding-viz/
├── data/
│   └── graph.txt          # Fichier contenant la carte et les temps de parcours
├── src/
│   └── MainApp/           # Package principal
│       ├── App.java       # Point d'entrée et logique des algorithmes
│       └── WeightedGraph.java # Structure de données (Graphe, Sommets, Arêtes)
├── .gitignore             # Fichiers exclus du suivi de version
└── README.md              # Documentation du projet
```

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

## 🧠 Détails Techniques

-   **Graphe** : Construction avec une connectivité à **8 voisins**.
-   **Poids** : Moyenne des temps de parcours entre deux sommets, multipliée par pour les déplacements diagonaux.
-   \*\*Heuristique (A\*)\*\* : Distance euclidienne entre le nœud courant et la cible.

## 📊 Résultats obtenus (graph.txt)

Le programme permet de comparer l'efficacité de Dijkstra et A\* pour un même chemin :

| Algorithme   | Nœuds explorés | Temps total (Poids) |
|--------------|----------------|---------------------|
| **Dijkstra** | 4156           | 302.61              |
| **A**\*      | 4117           | 302.61              |
