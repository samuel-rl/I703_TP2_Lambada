package fr.usmb.m1isc.compilation.tp;


public class Arbre {
    NodeType nodeType;
    Arbre arbreGauche;
    Arbre arbreDroit;
    String valeur;
    
    
    public enum NodeType {
    	PLUS, MOINS, MOINS_UNAIRE, MUL, DIV, MOD, NOT, OR, AND, PAR_G, PAR_D, SEMI, POINT, LET, INPUT, OUTPUT, IF, THEN, ELSE, WHILE, DO, EGAL, GT, GTE, NIL, ERROR, ENTIER, IDENT;
    }

    public Arbre(NodeType n, String valeur, Arbre ag, Arbre ad) {
        this.nodeType = n;
        this.arbreDroit = ad;
        this.arbreGauche = ag;
        this.valeur = valeur;
    }
    
    public Arbre(NodeType n, Arbre ag, Arbre ad) {
        this.nodeType = n;
        this.arbreDroit = ad;
        this.arbreGauche = ag;
        this.valeur = null;
    }
    
    public Arbre(NodeType n, String valeur, Arbre ag) {
        this.nodeType = n;
        this.arbreGauche = ag;
        this.arbreDroit = null;
        this.valeur = valeur;
    }
    
    public Arbre(NodeType n, String valeur) {
        this.nodeType = n;
        this.arbreGauche = null;
        this.arbreDroit = null;
        this.valeur = valeur;
    }
    
    public Arbre(NodeType n) {
        this.nodeType = n;
    }

    public NodeType getNodeType() {
        return this.nodeType;
    }
    
    public void setNodeType(NodeType n) {
        this.nodeType = n;
    }

    public Arbre getArbreGauche() {
    	return this.arbreGauche;
    }
    
    public void setArbreGauche(Arbre a) {
        this.arbreGauche = a;
    }

    public Arbre getArbreDroit() {
    	return this.arbreDroit;
    }
    
    public void setArbreDroit(Arbre a) {
        this.arbreDroit = a;
    }
    
    public String getValeur() {
    	return this.valeur;
    }
    
    public void setValeur(String v) {
    	this.valeur = v;;
    }
    

    @Override
	public String toString() {
    	//méthode toString prise de la correction de l'examen2017-2018
    	//https://github.com/Info703/I703_Examen_2017-2018/blob/master/src/main/java/fr/usmb/m1isc/compilation/simple/Arbre.java
    	//mais problèmes de parenthèses?
    	/*
    	 StringBuilder res = new StringBuilder("");
    	 
    	res.append(this.valeur.toString());
    	if (this.getArbreGauche() != null || this.getArbreDroit() != null) {
    		res.append('(');
			if (this.getArbreGauche() != null) res.append(this.getArbreGauche()); else res.append("null");
			res.append(" ");
			if (this.getArbreDroit() != null) res.append(this.getArbreDroit()); else res.append("null");
			res.append(')');
    	}
    	return res.toString();
	  }
	  */
    	
    	StringBuilder res = new StringBuilder("");
        if (this.getArbreGauche() != null) {
            if (this.getArbreDroit() != null) {
            	res.append("(" + this.getValeur() + " " + this.getArbreGauche().toString() + this.getArbreDroit().toString() + ")");
            } else {
            	res.append("(" + this.getValeur() + " " + this.getArbreGauche().toString() + ")");
            }
        } else {
        	if (this.getArbreDroit() != null) {
        		res.append("(" + this.getValeur() + " . " + this.getArbreDroit().toString() + ")");
             }else {
             	res.append(this.getValeur() + " ");
             }
        }
        return res.toString();
    }
}