package fr.usmb.m1isc.compilation.tp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import fr.usmb.m1isc.compilation.tp.Arbre.NodeType;

public class CodeGenerator {
	public Arbre arbre;
	HashSet<String> variables = new HashSet<>();
	public int id;
	 
	public CodeGenerator(Arbre arbre) {
		this.arbre = arbre;
		this.getAllVariables(arbre);
		this.id = 0;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int newId) {
		this.id = newId;
	}
	
	public int getNewId() {
		int newId = this.getId() +1;
		this.setId(newId);
		return this.getId();
	}
	
	//fonction pour recuperer toutes les variables et les mettre dans le hashseet
	public void getAllVariables(Arbre arbre) {
		if(arbre != null) {
			if(arbre.getNodeType().equals(NodeType.LET)) {
				//pour supprimer les doublons
				if (!variables.contains(arbre.getArbreGauche().getValeur())) {
                    variables.add(arbre.getArbreGauche().getValeur());
                }
			}
			this.getAllVariables(arbre.arbreDroit);
			this.getAllVariables(arbre.arbreGauche);
		}
	}
	
	//partie data
	public String variablesToString() {
		StringBuilder res = new StringBuilder("");
		for (String variable : this.variables) { 		      
	           res.append("\t"+variable + " DD\n");	
	      }
		return res.toString();
	}
	
	//partie entière de DATA
	public String dataPartToString() {
		return "DATA SEGEMENT\n" + this.variablesToString() + "DATA ENDS\n";
	}
	
	//partie du code
	public String codeToString(Arbre arbre) {
		StringBuilder res = new StringBuilder("");
		int tempId;
		if(arbre != null) {
			switch (arbre.getNodeType()) {
			case ENTIER:
            case IDENT:
            	res.append("\tmov eax, " + arbre.getValeur() + "\n");
                break;
            case SEMI:
            	res.append(codeToString(arbre.getArbreGauche()));
            	res.append(codeToString(arbre.getArbreDroit()));
                break;
            case LET :
            	res.append(codeToString(arbre.getArbreDroit()));
                res.append("\tmov " + arbre.getArbreGauche().getValeur() + ", eax\n");
                break;
            case PLUS:
            	res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tpush eax\n");
                res.append(codeToString(arbre.getArbreDroit()));
                res.append("\tpop ebx\n");
                res.append("\tadd eax, ebx\n");
                break;
            case MOINS:
            	res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tpush eax\n");
                res.append(codeToString(arbre.getArbreDroit()));
                res.append("\tpop ebx\n");
                res.append("\tsub ebx, eax\n");
                res.append("\tmov eax, ebx\n");
                break;          
            case MUL:
            	res.append(codeToString(arbre.getArbreGauche()));
            	res.append("\tpush eax\n");
            	res.append(codeToString(arbre.getArbreDroit()));
                res.append("\tpop ebx\n");
                res.append("\tmul eax, ebx\n");
                break;
            case DIV:
            	res.append(codeToString(arbre.getArbreGauche()));
            	res.append("\tpush eax\n");
            	res.append(codeToString(arbre.getArbreDroit()));
                res.append("\tpop ebx\n");
                res.append("\tdiv ebx, eax\n");
                res.append("\tmov eax, ebx\n");
                break;
            case MOD:  
                res.append(codeToString(arbre.getArbreDroit()));
                res.append("\tpush eax\n");
                res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tpop ebx\n");
                res.append("\tmov ecx, eax\n");
                res.append("\tdiv ecx, ebx\n");
                res.append("\tmul ecx, ebx\n");
                res.append("\tsub eax, ecx\n");
                break;
            case WHILE:
    			tempId = getNewId();
    			res.append("debut_while_" + tempId + ":\n");
    			res.append(codeToString(arbre.getArbreGauche()));
    			res.append("\tjz sortie_while_" + tempId + "\n");
    			res.append(codeToString(arbre.getArbreDroit()));
    			res.append("\tjmp debut_while_" + tempId + "\n");
    			res.append("sortie_while_" + tempId + ":\n");
    			break;
            case IF:
            	tempId = getNewId();
            	res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tjz else_" + tempId + "\n");
                res.append(codeToString(arbre.getArbreDroit().getArbreGauche()));
                res.append("\tjmp sortie_if_" + tempId + "\n");
                res.append("else_" + tempId + ":\n");
                res.append(codeToString(arbre.getArbreDroit().getArbreDroit()));
                res.append("sortie_if_" + tempId + ":\n");
                break;
            case AND:
                tempId = getNewId();
                res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tjz sortie_and_" + tempId + "\n");
                res.append(codeToString(arbre.getArbreDroit()));
                res.append("\tsortie_and_" + tempId + ":\n");
                break;
            case OR:
                tempId = getNewId();
                res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tjnz sortie_or_" + tempId + "\n");
                res.append(codeToString(arbre.getArbreDroit()));
                res.append("sortie_or_" + tempId + ":\n");
                break;
            case EGAL:
                tempId = getNewId();
                res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tpush eax\n");
                res.append(codeToString(arbre.getArbreDroit()));
                res.append("\tpop ebx\n");
                res.append("\tsub eax, ebx\n");
                res.append("\tjnz faux_eq_" + tempId + "\n");
                res.append("\tmov eax, 1\n");
                res.append("\tjmp sortie_eq" + tempId + "\n");
                res.append("\tfaux_eq_" + tempId + ":\n");
                res.append("\tmov eax, 0\n");
                res.append("sortie_eq" + tempId + ":\n");
                break;
            case NOT:
                tempId = getNewId();
               	res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tjz true_not_" + tempId + "\n");
                res.append("\tmov eax, 0\n");
                res.append("\tjmp sortie_not_" + tempId + "\n");
                res.append("\ttrue_not_" + tempId + ":\n");
                res.append("\tmov eax, 1\n");
                res.append("\tsortie_not_" + tempId + ":\n");
                break;
            case GT:
                tempId = getNewId();
                res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tpush eax\n");
                res.append(codeToString(arbre.getArbreDroit()));
                res.append("\tpop ebx\n");
                res.append("\tsub eax, ebx\n");
                res.append("\tjle false_gt_" + tempId + "\n"); 
                res.append("\tmov eax, 1\n");
                res.append("\tjmp sortie_gt_" + tempId + "\n");
                res.append("\tfalse_gt_" + tempId + ":\n");
                res.append("\tmov eax, 0\n");
                res.append("sortie_gt_" + tempId + ":\n");
                break;
            case GTE:
                tempId = getNewId();
               	res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tpush eax\n");
                res.append(codeToString(arbre.getArbreDroit()));
                res.append("\tpop ebx\n");
                res.append("\tsub eax, ebx\n");
                res.append("\tjl false_gteq_" + tempId + "\n");
                res.append("\tmov eax, 1\n");
                res.append("\tjmp sortie_gteq" + tempId + "\n");
                res.append("\tfalse_gteq_" + tempId + ":\n");
                res.append("\tmov eax, 0\n");
                res.append("\tsortie_gteq" + tempId + ":\n");
                break;
            case INPUT:
            	res.append("\tin eax\n");
                break;
            case OUTPUT:
            	res.append(codeToString(arbre.getArbreGauche()));
                res.append("\tout eax\n");
                break;
			default:
				break;
			}
		}
		return res.toString();
	}
	
	//Partie entière de code
	public String codePartToString() {
		return "CODE SEGMENT\n" + codeToString(this.arbre) + "CODE ENDS\n";
	}

	public String toString() {
		return this.dataPartToString() + this.codePartToString();
	}
	
	//Fonction pour save le fichier 
	//Attention changer le lien 
	public void saveToAsmFile(String fileName) throws IOException {
		FileWriter file = new FileWriter("C:/Users/samue/Downloads/" + fileName + ".asm", false);
		file.write(this.toString());
		file.close();
	}
}
