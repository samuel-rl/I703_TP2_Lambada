/* --------------------------Section de Code Utilisateur---------------------*/
package fr.usmb.m1isc.compilation.tp;
import java_cup.runtime.Symbol;

%%

/* -----------------Section des Declarations et Options----------------------*/
// nom de la class a generer
%class LexicalAnalyzer
%unicode
%line   // numerotation des lignes
%column // numerotation caracteres par ligne

// utilisation avec CUP 
%cup

// code a ajouter dans la classe produite
%{

%}

/* definitions regulieres */

chiffre 	= [0-9]
espace 		= \s
mod 		= "%" | "mod"|"MOD"
let			= "let"|"LET"
while 		= "while" | "WHILE"
do 			= "do" | "DO"
if 			= "if" | "IF"
then 		= "then" | "THEN"
else 		= "else" | "ELSE"
input 		= "input" | "INPUT"
output 		= "output" | "OUTPUT"
nil 		= "nil" | "NIL"
not 		= "not" | "NOT"
and		 	= "and" | "AND"
or 			= "or" | "OR"


// un identifiant commence par une lettre suivit d'un charactere alphanumerique (lettre/chiffre/underscore)
ident		= [:letter:]\w*
comment1	= "//".*							// commentaire uniligne
comment2	= "/*"([^*]|("*"+[^/*]))*"*"+"/"	// commentaire multiligne
comment	= {comment1}|{comment2}	

%% 
/* ------------------------Section des Regles Lexicales----------------------*/

/* regles */

{let}		{ return new Symbol(sym.LET, yyline, yycolumn) ;}
{while}		{ return new Symbol(sym.WHILE, yyline, yycolumn) ;}
{do}		{ return new Symbol(sym.DO, yyline, yycolumn) ;}
{if}		{ return new Symbol(sym.IF, yyline, yycolumn) ;}
{then}		{ return new Symbol(sym.THEN, yyline, yycolumn) ;}
{else}		{ return new Symbol(sym.ELSE, yyline, yycolumn) ;}
{nil}		{ return new Symbol(sym.NIL, yyline, yycolumn) ;}
{input}		{ return new Symbol(sym.INPUT, yyline, yycolumn) ;}
{output}	{ return new Symbol(sym.OUTPUT, yyline, yycolumn) ;}
{and}		{ return new Symbol(sym.AND, yyline, yycolumn) ;}
{or}		{ return new Symbol(sym.OR, yyline, yycolumn) ;}
{not}		{ return new Symbol(sym.NOT, yyline, yycolumn) ;}
"="			{ return new Symbol(sym.EGAL, yyline, yycolumn) ;}
"<"			{ return new Symbol(sym.GT, yyline, yycolumn) ;}
"<="		{ return new Symbol(sym.GTE, yyline, yycolumn) ;}
"("			{ return new Symbol(sym.PAR_G, yyline, yycolumn) ;}
")"			{ return new Symbol(sym.PAR_D, yyline, yycolumn) ;}
"+"			{ return new Symbol(sym.PLUS, yyline, yycolumn) ;}
"-"			{ return new Symbol(sym.MOINS, yyline, yycolumn) ;}
"/"			{ return new Symbol(sym.DIV, yyline, yycolumn) ;}
{mod}		{ return new Symbol(sym.MOD, yyline, yycolumn) ;}
"*"			{ return new Symbol(sym.MUL, yyline, yycolumn) ;}
";"			{ return new Symbol(sym.SEMI, yyline, yycolumn) ;}
"."			{ return new Symbol(sym.POINT, yyline, yycolumn) ;}
{chiffre}+	{ return new Symbol(sym.ENTIER, yyline, yycolumn, new Integer(yytext())) ;}
{ident}		{ return new Symbol(sym.IDENT, yyline, yycolumn, yytext()) ;}
{comment}	{ /*commentaire : pas d'action*/ }
{espace} 	{ /*espace : pas d'action*/ }
.			{ return new Symbol(sym.ERROR, yyline, yycolumn) ;}