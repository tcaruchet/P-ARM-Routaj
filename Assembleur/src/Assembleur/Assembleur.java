package Assembleur;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Assembleur { //4 bits pour etiquette, 4 bits pour mnemonique, 8 bits pour code imm
	
	ArrayList<Integer> mnemonique;
	
	ArrayList<Integer> b(){ //mnemonique dédié aux 15 requetes B
		mnemonique = new ArrayList<>();
		System.out.println();
		mnemonique.add(1);
		mnemonique.add(1);
		mnemonique.add(0);
		mnemonique.add(1);
		return mnemonique;
	}
	
	void convBinaire(ArrayList<Integer> ab, int num, int bit) { //ajoute le bon nombre de zero en fonction du int entré pour donner une valeur binaire en 8 bits
		//System.out.println("convBinaire");
		//System.out.println("valeur entrée = "+ num);
		if (num < 2) {for (int i = 0; i < bit-1; i++) {ab.add(0);}return;} 
		if (num < 4 & num >= 2) {for (int i = 0; i < bit-2; i++) {ab.add(0);}return;}
		if (num < 8 & num >= 4) {for (int i = 0; i < bit-3; i++) {ab.add(0);}return;}
		if (num < 16 & num >= 8) {for (int i = 0; i < bit-4; i++) {ab.add(0);}return;}
		if (num < 32 & num >= 16) {for (int i = 0; i < bit-5; i++) {ab.add(0);}return;}
		if (num < 64 & num >= 32) {for (int i = 0; i < bit-6; i++) {ab.add(0);}return;}
		if (num < 128 & num >= 64) {for (int i = 0; i < bit-7; i++) {ab.add(0);}return;}
	}
	
	void fromLinesToBinary (ArrayList<Integer> ab, int lines) {			//Depuis le nb de lignes de l'étiquette, renvoie la liste avec les 8 bit de imm8 remplis
		int binary = Integer.parseInt(Integer.toBinaryString(lines));	//convertit la ligne de l'etiquette en binaire
		convBinaire(ab,lines,8);										//ajoute les x 0 devant pour donner la valeur sur 8 bits, en fonction du nb de lignes en base 10
		ab.add(binary);													//ajoute la valeur binaire des lignes dans la liste
	}
	
	int isOverflow(int a, int b) {
		int o = 1; 
		if (a+b > -128 || a+b < 127) {
			o = 0;
		}
		return o;
	}
	
	int isCarry(int a, int b) {
		int c = 0; //retenue
		if (a+b > 255) {
			c = 1;
		}
		return c;
	}

	public static void main(String[] args) throws IOException{
		Assembleur assem = new Assembleur();
		BufferedReader in = new BufferedReader(new FileReader("D:\\enzod\\Polytech\\Archi\\P-ARM-Routaj\\logisim_project"));
		String line;
		ArrayList<Integer> ifs = new ArrayList<>();
		ArrayList<Integer> elses = new ArrayList<>();
		int lignes = 0;
		ArrayList<String> registres = new ArrayList<>();
		ArrayList<String> registresCourant = new ArrayList<>();
		ArrayList<String> valeurs = new ArrayList<>();
		ArrayList<Integer> valeurBinaire;
		int oui;
		
		while ((line = in.readLine()) != null){
			lignes++;
			String[] splt = line.split(":");
			
			if (splt[0].equals("if")) {
				System.out.println("if");
				ifs.add(lignes);
			}
			if (splt[0].equals("else")) {
				System.out.println("else");
				elses.add(lignes);
			}
		}
		in.close();
		
		BufferedReader in2 = new BufferedReader(new FileReader("D:\\enzod\\Polytech\\Archi\\P-ARM-Routaj\\logisim_project"));
		String line2;
		int j = 0; 					//cette valeur représente l'indice unique de chaque etiquettes, on va l'incrementer a chaque fois qu'on en traite une (sembable a un .remove dans la liste)
		int overflow;
		int carry;
		//System.out.println("avant while");
		while ((line2 = in2.readLine()) != null){
			//System.out.println("while");
			String[] spltEs = line2.split(" ");
			String[] registreCourSplit;
			int registre1;
			int registre2;
			for (int i = 0; i < spltEs.length; i++) {
				switch(spltEs[i]) {
					 
					case "ldr" : System.out.println("ldr"); 		  //récupère les différentes affectation dans les registres, et les met dans les lists appropriées
								 String[] spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres sont séparés des valeurs par un #
								 registres.add(spltHas[0]); 
								 valeurs.add(spltHas[1]);
								 break;
								 
					case "BEQ" : System.out.println("BEQ");									//BEQ = egal
								 valeurBinaire = init(assem,0); 							//ajoute la valeur 0 apres les 4 zeros
								 registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								 registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								 registre2 = registres.indexOf(registreCourSplit[1]);
								 
								 if (valeurs.get(registre1).equals(valeurs.get(registre2))) { //On utilise l'indice récupéré précédemment pour vérifier l'égalité des 2 valeurs des registres
									 conditionIf(assem, ifs, valeurBinaire, j);				  //on récupere la valeur de lignes pour le if, qu'on va convertir en binaire. A la fin de cet appel de fonction, nous avons nos 8 bits de imm
									 j++;													  //on incrémente j pour ne pas retomber sur les meme etiquettes
									 
								 }else {
									 conditionElse(assem, elses, valeurBinaire, j);
									 j++;
								 }
								 affich(valeurBinaire);
								 System.out.println();
								 break;
								 		
					case "BNE" : System.out.println("BNE");							//BNE = different, meme model que BEQ
								valeurBinaire = init(assem,1); //ajoute la valeur 1 apres les 3 zeros
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								overflow = assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2)));

								
					 			if (overflow ==1 || !valeurs.get(registre1).equals(valeurs.get(registre2))) { 		
									 conditionIf(assem, ifs, valeurBinaire, j);	
									 j++;
									 
								 }else {
									 conditionElse(assem, elses, valeurBinaire, j);
									 j++;
								 }
					 			affich(valeurBinaire);
								 System.out.println();
								 break;
					 			 
					case "BVS" : System.out.println("BVS");							//BCS = retenue
								valeurBinaire = init(assem,6); //ajoute la valeur 6 apres le zero
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								carry = assem.isCarry(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2)));
					 			 
					 			 if (carry == 1) { //il y a une retenue
					 				 conditionIf(assem, ifs, valeurBinaire,j);
					 				 j++;
					 			 }else {
					 				conditionElse(assem, elses, valeurBinaire, j);
									j++;
					 			 }
					 			 	affich(valeurBinaire);
					 			System.out.println();
					 			break;
					
					case "BVC" : System.out.println("BVC"); 						//BCC = sans retenue
								valeurBinaire = init(assem,7); //ajoute la valeur 7 apres le zero
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								carry = assem.isCarry(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2)));
		 			 			
		 			 			if (carry == 0) { //il y a une retenue
		 			 				conditionIf(assem, ifs, valeurBinaire, j);
		 			 				j++;
		 			 			}else {
		 			 				conditionElse(assem, elses, valeurBinaire, j);
		 			 				j++;
		 			 			}
		 			 			affich(valeurBinaire);
		 			 			System.out.println();
		 			 			break;
		 			
					case "BMI" : System.out.println("BMI"); 						//BCC = négatif
								valeurBinaire = init(assem,4); //ajoute la valeur 3 apres les 3 zeros
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								//System.out.println("operation = "+(Integer.parseInt(valeurs.get(registre1)) +"+"+ (Integer.parseInt(valeurs.get(registre2)))));
	 			 			
								if ((Integer.parseInt(valeurs.get(registre1)) < (Integer.parseInt(valeurs.get(registre2))))) { //l'addition des deux valeurs est négative
									conditionIf(assem, ifs, valeurBinaire, j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire);
								System.out.println();
								break;
								
					case "BPL" : System.out.println("BPL"); 						//BCC = négatif
								valeurBinaire = init(assem,5); //ajoute la valeur 3 apres les 3 zeros
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								overflow = assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2)));
		 			
								if (overflow == 1 || (Integer.parseInt(valeurs.get(registre1)) >= (Integer.parseInt(valeurs.get(registre2))))) { //l'addition des deux valeurs est négative
									conditionIf(assem, ifs, valeurBinaire, j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire);
								System.out.println();
								break;
								
								
					case "BCS" : System.out.println("BCS");							//BCS = retenue
								valeurBinaire = init(assem,2); //ajoute la valeur 2 apres les 3 zeros
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								overflow = assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2)));
		 			 
								if (overflow == 1 ||(Integer.parseInt(valeurs.get(registre1))) >= (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire);
								System.out.println();
								break;
						
					case "BCC" :System.out.println("BCC");							//BCS = retenue
								valeurBinaire = init(assem,3); //ajoute la valeur 3 apres les 3 zeros
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
			 
								if ((Integer.parseInt(valeurs.get(registre1))) < (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire);
								System.out.println();
								break;
								
					case "BHI" :System.out.println("BHI");							//BCS = retenue
								valeurBinaire = init(assem,8); //ajoute la valeur 8 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								carry = assem.isCarry(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2)));
			 
								if (carry == 1 ||(Integer.parseInt(valeurs.get(registre1))) > (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire);
								System.out.println();
								break;
						
					case "BLS" :System.out.println("BLS");							//BCS = retenue
								valeurBinaire = init(assem,9); //ajoute la valeur 9 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								carry = assem.isCarry(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2)));
 
								if (carry == 0 || (Integer.parseInt(valeurs.get(registre1))) <= (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire);
								System.out.println();
								break;
						
					case "BGE" :System.out.println("BGE");							//BCS = retenue
								valeurBinaire = init(assem,10); //ajoute la valeur 9 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);

								if ((Integer.parseInt(valeurs.get(registre1))) >= (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire);
								System.out.println();
								break;
								
					case "BLT" :System.out.println("BLT");							//BCS = retenue
								valeurBinaire = init(assem,11); //ajoute la valeur 9 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								overflow = assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2)));

								if (overflow == 1 || (Integer.parseInt(valeurs.get(registre1))) < (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire);
								System.out.println();
								break;
						
					case "BGT" :System.out.println("BGT");							//BCS = retenue
								valeurBinaire = init(assem,12); //ajoute la valeur 9 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								
								if ((Integer.parseInt(valeurs.get(registre1))) > (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire);
								System.out.println();
								break;
								
					case "BLE" :System.out.println("BLE");							//BCS = retenue
								valeurBinaire = init(assem,13); //ajoute la valeur 9 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on récupère ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va récupérer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								overflow = assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2)));

								if (overflow == 1 || (Integer.parseInt(valeurs.get(registre1))) <= (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire);
								System.out.println();
								break;
						
					default : break;
				}
			}
			
		}
	      // Afficher le contenu du fichier
			  //System.out.println (line);
		
		in2.close();
	}

	private static void affich(ArrayList<Integer> valeurBinaire) {
		for (int p : valeurBinaire) {
			System.out.print(p);
		}
	}
	
	private static void aff(ArrayList<String> valeurBinaire) {
		for (String p : valeurBinaire) {
			System.out.print(p);
		}
	}
	
	private static void conditionElse(Assembleur assem, ArrayList<Integer> elses, ArrayList<Integer> valeurBinaire, int j) {
		int elsess = elses.get(j);	
		System.out.println("else");
		assem.fromLinesToBinary(valeurBinaire, elsess);
	}

	private static void conditionIf(Assembleur assem, ArrayList<Integer> ifs, ArrayList<Integer> valeurBinaire, int j) {
		int ifss = ifs.get(j);	
		System.out.println("if");
		assem.fromLinesToBinary(valeurBinaire, ifss);
	}

	private static ArrayList<Integer> init(Assembleur assem, int mnemonique) {
		ArrayList<Integer> valeurBinaire;
		valeurBinaire = new ArrayList<>();
		//System.out.println("valeur du new arraylist = "+valeurBinaire);
		valeurBinaire.addAll(assem.b()); 											//on ajoute les 1101 au debut de l'arraylist
		//System.out.println("valeur B = "+valeurBinaire);
		assem.convBinaire(valeurBinaire, mnemonique, 4);						    //on ajoute les 4 bits qui identifie le mnemonique
		valeurBinaire.add(Integer.parseInt(Integer.toBinaryString(mnemonique)));	//on ajoute ca a l'arraylist
		return valeurBinaire;
	}
}
