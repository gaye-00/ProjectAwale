import mypaquets.*;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Jeu {

	public static void main(String[] args) {
		
		String nomJoueur1;
		String nomJoueur2;
		Scanner sc = new Scanner(System.in);

		System.out.print("Entrez le nom du joueur c1 : ");
		nomJoueur1 = sc.nextLine();
		Joueur joueur1 = new Joueur(nomJoueur1, "c1");

		System.out.print("Entrez le nom du joueur c2 : ");
		nomJoueur2 = sc.nextLine();
		Joueur joueur2 = new Joueur(nomJoueur2, "c2");
	

		System.out.println("Commencement du jeu");
		Plateau jeu = new Plateau();
		System.out.println();
		jeu.toString();

		int nbTotalGraines = jeu.nbGraines();
		System.out.println("\nLes graines du plateau : " + nbTotalGraines + "\n");
		System.out.println(joueur1.toString());
		System.out.println(joueur2.toString());

		System.out.print("Le jeu est terminer !!!");

		// Boucle qui permet de jouer au tour par tour
		while (nbTotalGraines > 0) {

			// Tour joueur1
			System.out.print("\nJOUEUR 1: ");
			int cellule = obtenirNombre();

			int value = jeu.jouer("c1", cellule);

			while (value < 0) {
				if (value == -1)
					System.out.println("JOUEUR 1: choisissez une case non vide\n");
				if (value == -2)
					System.out.println("JOUEUR 1: vous devez nourrir le plateau adverse");
				if(value == -3){
					// si la partie est terminer on ajoute le restant dans le score
					joueur1.ajouteScore(jeu.nbGraines("c1"));
					joueur2.ajouteScore(jeu.nbGraines("c2"));
					nbTotalGraines = 0;
					break;
				}

				cellule = obtenirNombre();
				value = jeu.jouer("c1", cellule);
			}

			joueur1.ajouteScore(value);
			jeu.toString();

			// Vérifie nombre de graines sur le plateau
			nbTotalGraines = jeu.nbGraines();
			System.out.println("\nValeur du plateau : " + nbTotalGraines + "\n");

			// Affiche joueurs
			System.out.println(joueur1.toString());
			System.out.println(joueur2.toString());
			
			// Affiche coups possibles 
			System.out.println(jeu.toStringPossible(jeu.coupPossibles()));

			
			
			
			// Tour joueur2
			System.out.print("JOUEUR 2: ");
			cellule = obtenirNombre();
			value = jeu.jouer("c2", cellule);


			while (value < 0) {
				if (value == -1)
					System.out.println("JOUEUR 2: choisissez une case non vide\n");
				if (value == -2)
					System.out
							.println("JOUEUR 2: vous devez nourrir le plateau adverse");
				if(value == -3){
					joueur1.ajouteScore(jeu.nbGraines("c1"));
					joueur2.ajouteScore(jeu.nbGraines("c2"));
					nbTotalGraines = 0;
					break;
				}
				cellule = obtenirNombre();
				value = jeu.jouer("c2", cellule);
			}

			joueur2.ajouteScore(value);
			jeu.toString();

			// Vérifie nombre de graines sur le plateau
			nbTotalGraines = jeu.nbGraines();
			System.out.println("\nValeur du plateau : " + nbTotalGraines + "\n");

			// Affiche joueurs
			System.out.println(joueur1.toString());
			System.out.println(joueur2.toString());

			// Affiche coups possibles
			System.out.println(jeu.toStringPossible(jeu.coupPossibles()));

			// Si il reste plus assez de graines ou que l'un des joueurs vient de faire un
			// coup gagnant on sort de la boucle
			int une = 0;
			if ((joueur1.getScore() > 24 || joueur2.getScore() > 24) && une == 0) {

				une = 1;
				if (joueur1.getScore() > 24)
					System.out.println("\n" + nomJoueur1 + " à déja gagner.");	
				else
					System.out.println("\n" + nomJoueur2 + " à déja gagner.");

				int quit = zeroOuUn();

				if(quit == 0)
					break;
			}
		}

		if (joueur1.getScore() > joueur2.getScore())
			System.out.println("JOUEUR 1 emporte la partie. Félicitation");
		else if (joueur1.getScore() < joueur2.getScore())
			System.out.println("JOUEUR 2 emporte la partie. Félicitation");
		else
			System.out.println("Egalité entre les deux joueurs");

		jeu.toString();
	}

	// Méthode qui force l'utilisateur à entrer un entier entre 0 et 5
	public static int obtenirNombre() {

	    Scanner scanner = new Scanner(System.in);
	    while (true) {
	        System.out.print("Entrez un nombre entier compris entre 0 et 5 : ");
	        try {
	            int num = scanner.nextInt();
	            if (num >= 0 && num <= 5) {
	                return num;
	            } else {
	                System.out.println("Le nombre doit être compris entre 0 et 5. Veuillez réessayer.");
	            }
	        } catch (InputMismatchException e) {
	            System.out.println("Vous devez entrer un nombre entier. Veuillez réessayer.");
	            scanner.next();
	        }
	    }
	}

	// Méthode qui force l'utilisateur à entrer 0 ou 1 
	public static int zeroOuUn() {

	    Scanner scanner = new Scanner(System.in);
	    while (true) {
	        System.out.print("\nTapez 0 pour quitter ou 1 pour continuer ? ");
	        try {
	            int num = scanner.nextInt();
	            if (num == 0 || num == 1) {
	                return num;
	            } else {
	                System.out.println("Le nombre doit être 0 ou 1. Veuillez réessayer.");
	            }
	        } catch (InputMismatchException e) {
	            System.out.println("Vous devez entrer un nombre entier. Veuillez réessayer.");
	            scanner.next();
	        }
	    }
	}
}