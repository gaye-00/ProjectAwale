package mypaquets;

public class Plateau {

	private static final int NUM_CELLULES = 6;
	private static final int NUM_PLAYERS = 2;
	private static final int NOMBRE_GRAINES_INITIAL = 4;

	private int[][] cellules;

	public Plateau() {
		this.cellules = new int[NUM_PLAYERS][NUM_CELLULES];

		for (int i = 0; i < NUM_PLAYERS; i++) {
		  for (int j = 0; j < NUM_CELLULES; j++) {
		    this.cellules[i][j] = NOMBRE_GRAINES_INITIAL;
		  }
		}
	}

	public int getCellule(String nomCote, int cellule) {
    	int cote = convertirNom(nomCote);
   		return this.cellules[cote][cellule];
	}

	public void setCellule(String nomCote, int cellule, int nouvelle_val) {
	    int cote = convertirNom(nomCote);
	    this.cellules[cote][cellule] = nouvelle_val;
	}


	public void ajouterGraine(int numJoueur, int numTrou) {
	    cellules[numJoueur][numTrou]++;
	}

	public int nbGraines() {
        int nbGrainesTotal = 0;

        for(int[] ligne : this.cellules){
            for(int trou : ligne){
                nbGrainesTotal += trou;
            }
        }
        return nbGrainesTotal;
    }

    public int nbGraines(String nomCote) {
	    int cote = convertirNom(nomCote);
	    int nbGraines = 0;
	    for(int trou : this.cellules[cote])
	        nbGraines += trou;

	    return nbGraines;
	}
    // Le retour de 547 est non permit par cet compilateur java
	public String toString() {
		System.out.println("\n               JOUEUR 1 \n\n"+"   T0    T1    T2    T3    T4    T5"+" \n"+"-------------------------------------\n|  "+cellules[0][0]+"  |  "+cellules[0][1]+"  |  "+cellules[0][2]+"  |  "+cellules[0][3]+"  |  "+cellules[0][4]+"  |  "+cellules[0][5]+"  |\n"+"------------------------------------\n|  "+cellules[1][0]+"  |  "+cellules[1][1]+"  |  "+cellules[1][2]+"  |  "+cellules[1][3]+"  |  "+cellules[1][4]+"  |  "+cellules[1][5]+"  |\n"+"-------------------------------------\n"+"  T0    T1    T2    T3    T4    T5\n\n"+"               JOUEUR 2");
		return "";
	}

	/**
	* @param cote
	* 				c'est le cote du jouer actuel
	* @return 0 si c'est le prémier cote et 1 si c'est le deuxiéme cote
	*/
	public int convertirNom(String cote) {
		if(cote == "c1")
			return 0;

		return 1;
	}

	/**
	* @param cotejoueur c'est le cote du joueur
	* @param j c'est la cellule que l'on veut jouer
	* @return 0 si c'est le prémier
	*/
    public int jouer(String coteJoueur, int j) {

	    int cote = convertirNom(coteJoueur);
	    int nombreGraines = cellules[cote][j];

	    // Si la cellule sélectionnée est vide, on renvoie -1 et on demande une autre cellule
	    if (nombreGraines == 0)
	        return -1;

	    // On vérifie que le coup est possible
	    int verification = verifierPlateau((cote + 1) % 2, j);
	    if (verification != 0)
	        return verification;

	    setCellule(coteJoueur, j, 0);

	    return redistribuerGraines(cellules, cote, j, nombreGraines);
	}

	/**
	* Répartie les graine dans le tableau 
	* @param plateau tableau à deux dimentions
	* @param ligne nombre de ligne
	* @param colonne nombre de colonne
	* @param graines nombre de graines de la cellule
	* @return nombre de graines capturés
	*/
	public int redistribuerGraines(int[][] plateau, int ligne, int colonne, int graines) {
	    int prochaineLigne = ligne;
	    int prochaineColonne = colonne;
	    String adversaire;
	    
	    while (graines > 0) {

		    if(prochaineLigne == 1) {
		        prochaineColonne++;
		        if (prochaineColonne > 5) {
		            prochaineColonne = 5;
		            prochaineLigne = (prochaineLigne + 1) % 2;
		        }
		    }
		    else {
		    	prochaineColonne--;
		    	if (prochaineColonne < 0) {
		            prochaineColonne = 0;
		            prochaineLigne = (prochaineLigne + 1) % 2;
		        }
		    }

		    if((ligne != prochaineLigne) || (colonne != prochaineColonne)) {
		        plateau[prochaineLigne][prochaineColonne]++;
		        graines--;
		    }
	    }

	    if(ligne == 0)
	    	adversaire = "c2";
	    else
	    	adversaire = "c1";

	    if(interdireAffamer(adversaire, prochaineColonne) == true) {
	        System.out.println("Vous n'avez pas le droit d'affamer votre adversaire\n");
	        return 0;
	    }

	    if(ligne == 0)
	    	return capturer("c1", "c2", prochaineColonne);
	    else
	    	return capturer("c2", "c1", prochaineColonne);
	}


	/**
	* Mange les graines du camp adverse tant que c'est possible
	* 
	* @param enCours
	*            		camp qui a jouer le coup
	* @param adversaire
	*            		camp sur lequel on mange les graines
	* @param celluleFinale
	*           		 cellule sur laquelle s'est arreté le joueur
	*/
	public int capturer(String enCours, String adversaire, int celluleFinale) {
	    int score;

	    if((adversaire == "c1"  && celluleFinale < 6) && (cellules[0][celluleFinale] == 2 || cellules[0][celluleFinale] == 3)) {
	        score = getCellule("c1", celluleFinale);
	        setCellule("c1", celluleFinale, 0);

	        return score + capturer(enCours, adversaire, celluleFinale + 1);
	    }
	    else if((adversaire == "c2"  && celluleFinale >= 0) && (cellules[1][celluleFinale] == 2 || cellules[1][celluleFinale] == 3)) {
	        score = getCellule("c2", celluleFinale);
	        setCellule("c2", celluleFinale, 0);
	        return score + capturer(enCours, adversaire, celluleFinale - 1);
	    }
	    else
	        return 0;
	}

	/**
	* Vérifie si le plateau adverse est vide et si un coup est possible
	* 
	* @param cote
	*            			cote à vérifier
	* 
	* @param j
	*           cellule que l'on souhaite jouer
	* 
	* @return false si l'affamtion est non possible et true si elle est possible
	*/
	public boolean interdireAffamer(String cote, int j) {

		int coteConverti = convertirNom(cote);
		boolean affamer = true;

		// Verifier si dans un premier temps est ce que l'affamation est possible
		for(int i = 0; i < NUM_CELLULES; ++i)
			if((cellules[coteConverti][i] != 0) && (cellules[coteConverti][i] != 2) && (cellules[coteConverti][i] != 3)) {
				affamer = false;
				break;
			}

		if(affamer == true) {
			int nbElementsRecuperable = 0;
			int nbElementsARecuperer = 0;

			for(int i = 0; i < NUM_CELLULES; ++i)
				if(cellules[coteConverti][i] != 0)
					nbElementsRecuperable++;

			if(coteConverti == 0) {
				while((cellules[coteConverti][j] == 2) || (cellules[coteConverti][j] == 3) && j > 0) {
					nbElementsARecuperer++;
					j--;
				}
			}
			else {
				while((cellules[coteConverti][j] == 2) || (cellules[coteConverti][j] == 3) && j < 6) {
					nbElementsARecuperer++;
					j++;
				}
			}

			if(nbElementsRecuperable != nbElementsARecuperer)
				affamer = false;
		}

		return affamer;
	}


	/**
	* Vérifie si le plateau adverse est vide et si un coup est possible
	* 
	* @param i
	*            camp à vérifier
	* 
	* @param j
	*            cellule que l'on souhaite jouer
	* 
	* @return 0 si coup possible, -2 si plateau adverse vide et il existe au
	*         moins un coup possible pour le nourrir mais pas celui demandé, -3
	*         si le plateau adverse est vide et qu'on ne peut pas le nourrir
	*/
	public int verifierPlateau(int i, int j) {
		// On vérifie si le plateau du c1 est vide, signifique que c'est le tour de c2
		if (i == 0) {

			int grainesJoueur = nbGraines("c1");

			// Le plateau adverse n'est pas vide, on joue ce que l'on veut
			if (grainesJoueur != 0)
				return 0;

			// Plateau adverse vide, on vérifie que sur la case demandée on a assez de graines pour le nourrir
			if ((cellules[1][j] + j) > NUM_CELLULES)
				return 0;

			// Sinon le plateau adverse est vide et case impossible à jouer. On
			// vérifie donc si une case est possible
			for (int k = 0; k < NUM_CELLULES; k++)
				if ((cellules[1][k] + k) > NUM_CELLULES)
					return -2;

			// Si rien de tout cela ne marche, cela signifie que le plateau
			// adverse est affamé et qu'on ne peut pas le nourrir,fin de la
			// partie
			return -3;

		}

		// On vérifie si le plateau du c2 est vide, signifique que c'est le tour de c1
		// Même procédé que pour le tour de c2
		else {

			int grainesJoueur = nbGraines("c2");

			// Le plateau adverse n'est pas vide, on joue ce que l'on veut
			if (grainesJoueur != 0)
				return 0;

			// Plateau adverse vide, on vérifie que sur la case demandée on a assez de graines pour le nourrir
			if (cellules[0][j] - j < 0)
				return 0;

			// Sinon le plateau adverse est vide et case impossible à jouer. On
			// vérifie donc si une case est possible
			for (int k = 0; k < NUM_CELLULES; k++) {
				if (cellules[0][k] - k < 0)
					return -2;
			}

			// Si rien de tout cela ne marche, cela signifie que le plateau
			// adverse est affamé et qu'on ne peut pas le nourrir,fin de la partie
			return -3;
		}
	}

	/**
	* Indique les coups possibles pour chaque joueur : 0 : coup impossible pour
	* ce joueur et cette cellule, 1 : coup possible pour ce joueur et cette
	* cellule
	* 
	* @return les coups possibles pour chaque joueur sous forme d'un tableau à
	*         deux dimensions avec valeurs 0 et 1
	*/
	public int[][] coupPossibles() {

		int[][] coup = new int[NUM_PLAYERS][NUM_CELLULES];

		for (int i = 0; i < NUM_PLAYERS; i++)
			for (int j = 0; j < NUM_CELLULES; j++)
				coup[i][j] = 0;

		int NbGrainesc1 = nbGraines("c1");
		int NbGrainesc2 = nbGraines("c2");

		// Si le joueur 2 possède des graines, le joueur 1 peut jouer n'importe
		// quelle cellule avec des graines
		if (NbGrainesc2 != 0) {
			for (int j = 0; j < NUM_CELLULES; j++)
				if (cellules[0][j] != 0)
					coup[0][j] = 1;
		}
		// Sinon le joueur 1 devra nourrir l'adversaire obligatoirement
		else {
			for (int j = 0; j < NUM_CELLULES; j++)
				if ((cellules[0][j] - j) < 0)
					coup[0][j] = 1;
		}

		// Si le joueur 1 possède des graines, le joueur 2 peut jouer n'importe
		// quelle cellule avec des graines
		if (NbGrainesc1 != 0) {
			for (int j = 0; j < NUM_CELLULES; j++)
				if (cellules[1][j] != 0)
					coup[1][j] = 1;
		}
		// Sinon le joueur 2 devra nourrir l'dversaire obligatoirement
		else {
			for (int j = 0; j < NUM_CELLULES; j++)
				if ((cellules[1][j] + j) > NUM_CELLULES)
					coup[1][j] = 1;
		}

		return coup;
	}

	/**
	* Renvoie en string le tableau des coups possibles
	* @param plateau tableau des coups possibles
	* @return string du tableau des coups possibles
	*/
	
	public String toStringPossible(int[][] plateau) {

		String coups = "";

		for (int i = 0; i < NUM_PLAYERS; i++) {
			coups += "Le JOUEUR " + (i + 1) + " peut jouer les cases suivantes :";
			for (int j = 0; j < NUM_CELLULES; j++)
				if (plateau[i][j] == 1)
					coups += " " + j;
			coups += "\n";
		}

		return coups;
	}

}