# Mini-Projet : Algorithmique Avancée

**Étudiant :** Mamadou NIMAGA

**Date :** Décembre 2025

**Cours :** IF05X040 - Algorithmique avancée (Université Paris Cité)

## 📂 Structure du Rendu

Le projet est organisé en deux modules logiciels distincts, accompagnés de leurs rapports d'analyse respectifs à la racine.

``` text
NIMAGA_MiniProjet/
├── README.md                <-- Ce fichier d'instructions
├── NIMAGA_MiniProjet.pdf    <-- Rapport d'analyse : Partie A (Ayutthaya)
├── NIMAGA_PartB.pdf         <-- Rapport d'analyse : Partie B (Visualiseur)
│
├── CodeMiniProjet/          <-- MODULE A : Labyrinthe d'Ayutthaya
│   ├── src/MainApp/         <-- Sources Java documentées (A*, BFS Fire)
│   ├── bin/                 <-- Fichiers compilés (.class)
│   ├── data/                <-- ayutthaya.txt (Instances de test)
│   ├── doc/                 <-- Javadoc technique (index.html)
│   └── NIMAGA_MiniProjet.jar <-- Exécutable binaire (Module A)
│
└── CodePartB/               <-- MODULE B : Visualiseur de Chemins
    ├── src/MainApp/         <-- Sources Java (Dijkstra, A* 8-voisins)
    ├── bin/                 <-- Fichiers compilés (.class)
    ├── data/                <-- graph.txt (Carte de terrain)
    ├── doc/                 <-- Javadoc technique (index.html)
    └── NIMAGA_PartB.jar     <-- Exécutable binaire (Module B)
```

## ⚠️ **IMPORTANT : Gestion des chemins de fichiers**

*(Valable pour module A et B)* Le programme utilise des chemins relatifs pour accéder aux données par défaut. - **Si vous utilisez un argument** : Vous pouvez lancer le JAR de n'importe où. `java -jar NIMAGA_MiniProjet.jar /un/chemin/absolu/vers/test.txt` - **Si vous ne mettez pas d'argument** : Vous devez impérativement lancer la commande depuis la racine du dossier du projet (là où se trouve le dossier `data/`). Sinon, le programme ne trouvera pas les instances par défaut et s'arrêtera.

## Module A : Le Labyrinthe d'Ayutthaya

Ce module résout le problème d'évasion d'un prisonnier face à un incendie dynamique.

### Spécificités techniques

-   **Algorithme de survie** : A\* avec contrainte de temps dynamique.
-   **Heuristique** : Distance de Manhattan (), optimale pour la **4-connexité**.
-   **Gestion du feu** : Pré-calcul de la propagation par un **BFS multi-sources**.

### Exécution (JAR)

Le programme accepte le chemin d'un fichier d'entrée en argument.

``` bash
java -jar CodeMiniProjet/NIMAGA_MiniProjet.jar CodeMiniProjet/data/ayutthaya.txt
```

*Si aucun argument n'est fourni, le programme charge par défaut `data/ayutthaya.txt` qui utilise l'exmple donné dans le sujet.*

### Résultats Validés

Le programme affiche les sorties attendues pour les instances types (exemple donné dans le sujet) :

-   **Instance 1** : `Y` (Le prisonnier s'échappe).
-   **Instance 2** : `N` (Le feu bloque la sortie).
-   **Instance 3** : `N` (Prisonnier emmuré).

## Module B : Visualiseur de Chemins (Dijkstra / A\*)

Ce module compare l'efficacité des algorithmes de recherche de chemin sur différents types de terrains (herbe, eau, sable, etc.).

### Spécificités Techniques

-   **Connectivité** : 8-voisins (déplacements diagonaux inclus).
-   **Poids** : Coût calculé selon le type de terrain. Les diagonales sont pondérées par sqrt(2).
-   **Heuristique** : Distance Euclidienne.

### Exécution (JAR)

``` bash
java -jar CodePartB/NIMAGA_PartB.jar CodePartB/data/graph.txt
```

## Documentation technique (Javadoc)

Chaque module contient sa propre documentation générée automatiquement à partir des commentaires Javadoc (en français).

Pour consulter les détails des classes, des méthodes, de leurs **arguments (@param)** et de leurs **retours (@return)** :

1.  Naviguez dans le dossier `doc/` du module souhaité.
2.  Ouvrez le fichier **`index.html`** dans un navigateur web.

-   **Doc Module A** : `CodeMiniProjet/doc/index.html`
-   **Doc Module B** : `CodePartB/doc/index.html`

## Compilation manuelle (sources)

Si vous souhaitez recompiler les projets manuellement :

**Module A :**

``` bash
javac -d CodeMiniProjet/bin CodeMiniProjet/src/MainApp/*.java
java -cp CodeMiniProjet/bin MainApp.App CodeMiniProjet/data/ayutthaya.txt
```

**Module B :**

``` bash
javac -d CodePartB/bin CodePartB/src/MainApp/*.java
java -cp CodePartB/bin MainApp.App CodePartB/data/graph.txt
```

## Rapports d'analyse

Les rapports détaillent les preuves de complexité, l'analyse des heuristiques et les captures d'écran des résultats obtenus :

-   **Partie A** : Analyse de la propagation du feu et de la survie.
-   **Partie B** : Comparaison des performances entre Dijkstra et A\*.
