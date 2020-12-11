package fr.usmb.m1isc.compilation.tp;

import java.io.FileReader;
import java.io.InputStreamReader;

import java_cup.runtime.Symbol;

public class Main {

	public static void main(String[] args) throws Exception  {
		 LexicalAnalyzer yy;
		 if (args.length > 0)
		        yy = new LexicalAnalyzer(new FileReader(args[0])) ;
		    else
		        yy = new LexicalAnalyzer(new InputStreamReader(System.in)) ;
		@SuppressWarnings("deprecation")
		parser p = new parser (yy);
		
		Arbre arbre = (Arbre) p.parse().value;
		
		System.out.println(arbre.toString());
		
		CodeGenerator cg = new CodeGenerator(arbre);
		System.out.println(cg.toString());
		cg.saveToAsmFile("test");
		
	}
}