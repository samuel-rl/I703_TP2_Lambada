# TP Compilation : Génération de code pour un sous ensemble du langage λ-ada.

## Utilisation

Vous pouvez clone le projet et l'utilisez directement dans Eclipse comme vu en TP.
Le ``Main` s'occupe de : 
- creer l'arbre
- afficher l'arbre
- generer le code
- afficher le code
- sauvegarder le code dans un fichier

🛑❗ Vous devez modifier le dossier de sauvegarde du fichier dans le `CodeGenerator.java` dans la fonction `saveToAsmFile` et vous pouvez modifier le nom du fichier qui sera sauvegarder dans l'appel de la fonction `saveToAsmFile` dans le `Main.java`.

## Exercice 1 :

Avec l'expression :

```
let prixHt = 200;
let prixTtc =  prixHt * 119 / 100 .
```

l'arbre construit est:
```
(; (let prixHt 200 )(let prixTtc (/ (* prixHt 119 )100 )))
``` 

Et le code généré est :

```
DATA SEGEMENT
	prixTtc DD
	prixHt DD
DATA ENDS
CODE SEGMENT
	mov eax, 200
	mov prixHt, eax
	mov eax, prixHt
	push eax
	mov eax, 119
	pop ebx
	mul eax, ebx
	push eax
	mov eax, 100
	pop ebx
	div ebx, eax
	mov eax, ebx
	mov prixTtc, eax
CODE ENDS
```


## Exercice 2 :

Avec l'expression :

```
let a = input;
let b = input;
while (0 < b)
do (let aux=(a mod b); let a=b; let b=aux );
output a
.
```

l'arbre construit est:
```
(; (let a input )(; (let b input )(; (while (> 0 b )(do (; (let aux (% a b ))(; (let a b )(let b aux )))))(output a ))))
``` 

Et le code généré est :

```
DATA SEGEMENT
	a DD
	b DD
	aux DD
DATA ENDS
CODE SEGMENT
	in eax
	mov a, eax
	in eax
	mov b, eax
debut_while_1:
	mov eax, 0
	push eax
	mov eax, b
	pop ebx
	sub eax, ebx
	jle faux_gt_2
	mov eax, 1
	jmp sortie_gt_2
faux_gt_2:
	mov eax, 0
sortie_gt_2:
	jz sortie_while_1
	mov eax, b
	push eax
	mov eax, a
	pop ebx
	mov ecx, eax
	div ecx, ebx
	mul ecx, ebx
	sub eax, ecx
	mov aux, eax
	mov eax, b
	mov a, eax
	mov eax, aux
	mov b, eax
	jmp debut_while_1
sortie_while_1:
	mov eax, a
	out eax
CODE ENDS
```