package mypaquets;

public class Joueur {

	private String nom;
	private int score;
	private String cote;

	public Joueur() {
	    this.nom = "NR";
	    this.cote = "NR";
	    this.score = 0;
	}

	public Joueur(String nom, String cote) {
	    this.nom = nom;
	    this.cote = cote;
	    this.score = 0;
	}

	public String getNom() {
	    return this.nom;
	}

	public void setNom(String nom) {
	    this.nom = nom;
	}

	public int getScore() {
	    return this.score;
	}

	public void setScore(int score) {
	    this.score = score;
	}

	public String getCote() {
	    return this.cote;
	}

	public void setCote(String cote) {
	    this.cote = cote;
	}

	// Pour mettre à jour le score
	public void ajouteScore(int score) {
	    setScore(this.score + score);
	}

	public String toString() {
	    return "[Nom : " + this.nom + ", Score : " + this.score + ", Cote : " + this.cote + "]";
	}

}