package fr.usmb.m1isc.compilation.tp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import fr.usmb.m1isc.compilation.tp.Arbre.NodeType;

public class CodeGenerator {
	public Arbre arbre;
	public ArrayList<String> variables = new ArrayList<String>();

	 
	public CodeGenerator(Arbre arbre) {
		this.arbre = arbre;
		this.getAllVariables(arbre);
	}
	
	public void getAllVariables(Arbre arbre) {
		if(arbre != null) {
			if(arbre.getNodeType().equals(NodeType.LET)) {
				this.variables.add(arbre.getArbreGauche().getValeur());
			}
			this.getAllVariables(arbre.arbreDroit);
			this.getAllVariables(arbre.arbreGauche);
		}
	}
	
	public String variablesToString() {
		StringBuilder res = new StringBuilder("");
		for (String variable : this.variables) { 		      
	           res.append("    "+variable + " DD\n");	
	      }
		return res.toString();
	}
	
	public String dataPartToString() {
		return "DATA SEGEMENT\n" + this.variablesToString() + "DATA ENDS\n";
	}
	
	public String codeToString(Arbre arbre) {
		StringBuilder res = new StringBuilder("");
		if(arbre != null) {
			switch (arbre.getNodeType()) {
			case ENTIER :
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
			default:
				break;
			}
		}
		return res.toString();
	}
	

	public String codePartToString() {
		return "CODE SEGMENT\n" + codeToString(this.arbre) + "CODE ENDS\n";
	}

	public String toString() {
		return this.dataPartToString() + this.codePartToString();
	}
	
	public void saveToAsmFile(String fileName) throws IOException {
		FileWriter file = new FileWriter("C:/Users/samue/Downloads/" + fileName + ".asm", false);
		file.write(this.toString());
		file.close();
	}
}
