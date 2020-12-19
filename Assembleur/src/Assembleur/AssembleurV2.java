package Assembleur;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AssembleurV2 {
	
	static int ifLines = 0;
	static int elseLines = 0;
	static int thenLines = 0;
	static int endLines = 0;
	static int loopLines = 0;
	
	public ArrayList<Integer> fromStringToArray(String valeur) {
		String[] spliter = valeur.split(""); //pour passer de 111 à 1,1,1
		ArrayList<Integer> nombreBinaire = new ArrayList<Integer>();
		for (String str : spliter) {nombreBinaire.add(Integer.parseInt(str));} //on ajoute les nouveaux int à l'arraylist
		return nombreBinaire;
	}
	
	public void fromIntToBinary (ArrayList<Integer> ab, int lines, int bit) {
		int binary;
		if (lines < 0) { //dans le cas d'un nb négatif, on ajoute au bit le + a gauche
			lines = -lines;
			binary = Integer.parseInt(Integer.toBinaryString(lines));	//convertit la ligne de l'etiquette en binaire
			ajoutZero(ab,lines,bit);										//ajoute les x 0 devant pour donner la valeur sur x bits, en fonction du nb de lignes en base 10
			for (int i = 0; i<ab.size();i++) {
				//System.out.println("valeur ab avant set : "+ab.get(i));
			}
			ab.set(0, 1); //on met le bit de signe à 1
		}else {
			binary = Integer.parseInt(Integer.toBinaryString(lines));	//convertit la ligne de l'etiquette en binaire
			ajoutZero(ab,lines,bit);										//ajoute les x 0 devant pour donner la valeur sur x bits, en fonction du nb de lignes en base 10
		}
		String binaryS = String.valueOf(binary);
		String[] binarySplt = binaryS.split("");
		ArrayList<String> binarySpltArr = new ArrayList<String>();
		for (int i = 0; i < binarySplt.length; i++) {binarySpltArr.add(binarySplt[i]);} //on passe d'un tableau a une ArrayList
		if (binarySpltArr.size() > 8 ) { 									//nb > 255 (2^8 - 1)												
			while (binarySpltArr.size() > 8 ) {
				binarySpltArr.remove(0);			//on va donc retiré tous les bits a droite jusqu'a avoir une valeur sur 8 bits
			}
		}
		for (int i = 0; i < binarySpltArr.size(); i++) {					//on split pour avoir une valeur de type [1,1,1] plutot que [111]
			ab.add(Integer.parseInt(binarySpltArr.get(i)));					//ajoute la valeur binaire des lignes dans la liste
		}
	}
	
	public void ajoutZero(ArrayList<Integer> ab, int num, int bit) { //ajoute le bon nombre de zero en fonction du int entré pour donner une valeur binaire en x bits
		//System.out.println("convBinaire");
		//System.out.println("valeur entrée = "+ num);
		if (num < 2) {for (int i = 0; i < bit-1; i++) {ab.add(0);}return;} 
		if (num < 4 & num >= 2) {for (int i = 0; i < bit-2; i++) {ab.add(0);}return;}
		if (num < 8 & num >= 4) {for (int i = 0; i < bit-3; i++) {ab.add(0);}return;}
		if (num < 16 & num >= 8) {for (int i = 0; i < bit-4; i++) {ab.add(0);}return;}
		if (num < 32 & num >= 16) {for (int i = 0; i < bit-5; i++) {ab.add(0);}return;}
		if (num < 64 & num >= 32) {for (int i = 0; i < bit-6; i++) {ab.add(0);}return;}
		if (num <128 & num >= 64) {for (int i = 0; i < bit-7; i++) {ab.add(0);}return;}
		if (num > 128) {return;}
	}

	public void affichBin(ArrayList<Integer> valeurBinaire) {
		for (int p : valeurBinaire) {System.out.print(p);}
		System.out.println();
	}

	public void affichXex(ArrayList<String> valeurBinaire) {
		for (String p : valeurBinaire) {System.out.print(p);}
	}

	public static int conditionalSwitch(ArrayList<Integer> ifArray, ArrayList<Integer> elseArray,ArrayList<Integer> thenArray, ArrayList<Integer> endArray, ArrayList<Integer> loopArray,String[] spltHas) {
		int imm8 = 0;
		switch(spltHas[0]) {
		case "if" :		imm8 = ifArray.get(ifLines);
						ifLines++;
						//elseLines++; //on incrémente les deux car si on choisit un if, cad qu'on ne choisit pas son else
						break;
					
		case "else"	:	imm8 = elseArray.get(elseLines);
						elseLines++;
						//ifLines++;
						break;
						
		case "then" : 	imm8 = thenArray.get(thenLines);
						thenLines++;
						break;
		 				
		case "end" : 	imm8 = endArray.get(endLines);
						//endLines++; on n'a qu'un seul end
						break;
		  				
		case "loop" : 	imm8 = loopArray.get(loopLines);
						loopLines++;
						break;
		}
		return imm8;
	}
	
	public static void main(String[] args) throws IOException{
		AssembleurV2 assem = new AssembleurV2();
		ConverterBinHex converterBinHex = new ConverterBinHex();
		
		//parcour le fichier pour récupérer les conditions (if/else)
		BufferedReader in = new BufferedReader(new FileReader("res/requeteBranch"));
		String line;
		ArrayList<Integer> ifArray = new ArrayList<Integer>();
		ArrayList<Integer> elseArray = new ArrayList<Integer>();
		ArrayList<Integer> thenArray = new ArrayList<Integer>();
		ArrayList<Integer> endArray = new ArrayList<Integer>();
		ArrayList<Integer> loopArray = new ArrayList<Integer>();
		int lignes = 0;
		
		while ((line = in.readLine()) != null){ //on parcours une premier fois le fichier pour repérer les étiquettes
			lignes++;
			String[] splt = line.split(":");
			switch(splt[0]) {
				case "if" :		System.out.println("if");
								ifArray.add(lignes);
								break;
							
				case "else"	:	System.out.println("else");
								elseArray.add(lignes);
								break;
								
				case "then" : 	System.out.println("then");
				 				thenArray.add(lignes);
				 				break;
				 				
				case "end" : 	System.out.println("end");
				  				endArray.add(lignes);
				  				break;
				  				
				case "loop" : 	System.out.println("loop");
								loopArray.add(lignes);
								break;
			}
		}
		in.close();
		
		//parcours une seconde fois le fichier pour récupérer les requêtes
		BufferedReader in2 = new BufferedReader(new FileReader("res/requeteBranch"));
		String line2;
		while ((line2 = in2.readLine()) != null){
			String[] spltEs = line2.split(" ");
			String[] registreCourSplit;
			String[] spltHas;
			String[] spltVir;
			ArrayList<Integer> motBinaire = new ArrayList<Integer>();
			ArrayList<Integer> mnemoniqueBinaire = new ArrayList<Integer>();
			ArrayList<Integer> opcodeBinaire = new ArrayList<Integer>();
			ArrayList<Integer> immBinaire = new ArrayList<Integer>();
			ArrayList<Integer> registreBinaire = new ArrayList<Integer>();
			int shift;
			int imm8;
			int registre1;
			int registre2;
			boolean breaker = false;
		
			for (int i = 0; i < spltEs.length; i++) {
				//System.out.println("valeur j = "+j);
				switch(spltEs[i]) { //chaque mots, séparé d'un espace, d'une ligne
				
					case "LDR" : 	System.out.println("LDR");
									spltHas = spltEs[spltEs.length-1].split("#");
									spltVir = spltHas[0].split(","); //On récupère RT et la valeur du registre de base
									shift = Integer.parseInt(spltHas[1]); //On récupère la valeur de shift [valeur suivant le #]
									
									mnemoniqueBinaire = assem.fromStringToArray(LoadStore.LDR.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(LoadStore.LDR.getOpcode());
									registre1 = Integer.parseInt(spltVir[1]); //le numero du registre
									assem.fromIntToBinary(registreBinaire, registre1, 3); //la valeur binaire du numero du registre
									imm8 = shift + registre1; //l'adresse imm8 est composé de la valeur du registre + du shift effectué [valeur apres le #]
									assem.fromIntToBinary(immBinaire, imm8, 8); //la valeur binaire de l'adresse imm8
									
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(registreBinaire);
									motBinaire.addAll(immBinaire);
									
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre1+") ");assem.affichBin(registreBinaire);
									System.out.println("shift [base 10] = "+shift);
									System.out.print("imm8 = ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
									
									motBinaire.clear();
									break;
									
					case "STR" : 	System.out.println("STR");
									spltHas = spltEs[spltEs.length-1].split("#");
									spltVir = spltHas[0].split(","); //On récupère RT et la valeur du registre de base
									shift = Integer.parseInt(spltHas[1]); //On récupère la valeur de shift [valeur suivant le #]
									
									mnemoniqueBinaire = assem.fromStringToArray(LoadStore.STR.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(LoadStore.STR.getOpcode());
									registre1 = Integer.parseInt(spltVir[1]); //le numero du registre
									assem.fromIntToBinary(registreBinaire, registre1, 3); //la valeur binaire du numero du registre
									imm8 = shift + registre1; //l'adresse imm8 est composé de la valeur du registre + du shift effectué [valeur apres le #]
									assem.fromIntToBinary(immBinaire, imm8, 8); //la valeur binaire de l'adresse imm8
									
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(registreBinaire);
									motBinaire.addAll(immBinaire);
									
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre1+") ");assem.affichBin(registreBinaire);
									System.out.println("shift [base 10] = "+shift);
									System.out.print("imm8 = ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
									
									motBinaire.clear();
									break;
									
					case "LSLS" :	System.out.println("LSLS");
									for (String str : spltEs[1].split("")) {
										if (str.equals("#")) {  //permet de différencier le type d'ADDS
											breaker = true;
							
											//cas LSLS Immediate
											spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont séparés des valeurs par un #
											registreCourSplit = spltHas[0].split(","); //les deux registres sollicités
											registre1 = Integer.parseInt(registreCourSplit[0]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
											registre2 = Integer.parseInt(registreCourSplit[1]);
					
											mnemoniqueBinaire = assem.fromStringToArray(Description.LSLS.getMnemonique());
											opcodeBinaire = assem.fromStringToArray(Description.LSLS.getOpcode());
											imm8 = Integer.parseInt(spltHas[1]); //la valeur du décalage [imm5]
											assem.fromIntToBinary(immBinaire, imm8, 5); //valeur du decalage en binaire
											assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
											assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RD
											
											//on ajoute tout pour constituer notre mot
											motBinaire.addAll(mnemoniqueBinaire);
											motBinaire.addAll(opcodeBinaire);
											motBinaire.addAll(immBinaire);
											motBinaire.addAll(registreBinaire);
											
											//affichage
											System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
											System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
											System.out.print("RM et RD = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
											System.out.print("imm8 = ");assem.affichBin(immBinaire);
											System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
											System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
											System.out.println("\n");
											
											motBinaire.clear();
											break;
										}
									}
									if (breaker == true){break;}
									
									//cas LSLS Register
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres et imm5 sont séparés des valeurs par un #
									registre1 = Integer.parseInt(spltHas[0]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
									registre2 = Integer.parseInt(spltHas[1]);
									
									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.LSLS.getMnemonique()); //RD
									opcodeBinaire = assem.fromStringToArray(DataProcessing.LSLS.getOpcode()); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RD
									
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);
									motBinaire.addAll(registreBinaire);
									
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("RM et RD = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
									
									motBinaire.clear();
									breaker = false;
									break;

									
									
					case "LSRS" : 	System.out.println("LSRS");
									for (String str : spltEs[1].split("")) {
										if (str.equals("#")) {  //permet de différencier le type d'ADDS
											breaker = true;
			
											//cas LSRS Immediate
											spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont séparés des valeurs par un #
											registreCourSplit = spltHas[0].split(","); //les deux registres sollicités
											registre1 = Integer.parseInt(registreCourSplit[0]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
											registre2 = Integer.parseInt(registreCourSplit[1]);
	
											mnemoniqueBinaire = assem.fromStringToArray(Description.LSRS.getMnemonique());
											opcodeBinaire = assem.fromStringToArray(Description.LSRS.getOpcode());
											imm8 = Integer.parseInt(spltHas[1]); //la valeur du décalage [imm5]
											assem.fromIntToBinary(immBinaire, imm8, 5); //valeur du decalage en binaire
											assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
											assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RD
								
											//on ajoute tout pour constituer notre mot
											motBinaire.addAll(mnemoniqueBinaire);
											motBinaire.addAll(opcodeBinaire);
											motBinaire.addAll(immBinaire);
											motBinaire.addAll(registreBinaire);
							
											//affichage
											System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
											System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
											System.out.print("RM et RD = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
											System.out.print("imm8 = ");assem.affichBin(immBinaire);
											System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
											System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
											System.out.println("\n");
							
											motBinaire.clear();
											break;
										}
									}
									if (breaker == true){break;}
					
									//cas LSRS Register
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres et imm5 sont séparés des valeurs par un #
									registre1 = Integer.parseInt(spltHas[0]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
									registre2 = Integer.parseInt(spltHas[1]);
					
									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.LSRS.getMnemonique()); //RD
									opcodeBinaire = assem.fromStringToArray(DataProcessing.LSRS.getOpcode()); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RD
				
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);
									motBinaire.addAll(registreBinaire);
					
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("RM et RD = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
					
									motBinaire.clear();
									breaker = false;
									break;
									
					case "ASRS" :	System.out.println("ASRS");
									for (String str : spltEs[1].split("")) {
										if (str.equals("#")) {  //permet de différencier le type d'ADDS
											breaker = true;

											//cas ASRS Immediate
											spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont séparés des valeurs par un #
											registreCourSplit = spltHas[0].split(","); //les deux registres sollicités
											registre1 = Integer.parseInt(registreCourSplit[0]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
											registre2 = Integer.parseInt(registreCourSplit[1]);

											mnemoniqueBinaire = assem.fromStringToArray(Description.ASRS.getMnemonique());
											opcodeBinaire = assem.fromStringToArray(Description.ASRS.getOpcode());
											imm8 = Integer.parseInt(spltHas[1]); //la valeur du décalage [imm5]
											assem.fromIntToBinary(immBinaire, imm8, 5); //valeur du decalage en binaire
											assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
											assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RD
				
											//on ajoute tout pour constituer notre mot
											motBinaire.addAll(mnemoniqueBinaire);
											motBinaire.addAll(opcodeBinaire);
											motBinaire.addAll(immBinaire);
											motBinaire.addAll(registreBinaire);
			
											//affichage
											System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
											System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
											System.out.print("RM et RD = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
											System.out.print("imm8 = ");assem.affichBin(immBinaire);
											System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
											System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
											System.out.println("\n");
			
											motBinaire.clear();
											break;
										}
									}
									if (breaker == true){break;}
	
									//cas ASRS Register
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres et imm5 sont séparés des valeurs par un #
									registre1 = Integer.parseInt(spltHas[0]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
									registre2 = Integer.parseInt(spltHas[1]);
	
									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.ASRS.getMnemonique()); //RD
									opcodeBinaire = assem.fromStringToArray(DataProcessing.ASRS.getOpcode()); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RD

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);
									motBinaire.addAll(registreBinaire);
	
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("RM et RD = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
	
									motBinaire.clear();
									breaker = false;
									break;
									
					case "ADDS" :	System.out.println("ADDS"); // on ne sait pas encore si c'est add register ou add immediate
									for (String str : spltEs[1].split("")) {
										if (str.equals("#")) {  //permet de différencier le type d'ADDS
											breaker = true;
											
											//cas ADDS Immediate
											spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont séparés des valeurs par un #
											registreCourSplit = spltHas[0].split(","); //les deux registres sollicités
											registre1 = Integer.parseInt(registreCourSplit[0]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
											registre2 = Integer.parseInt(registreCourSplit[1]);
											
											mnemoniqueBinaire = assem.fromStringToArray(ShAdSuMo.ADDI.getMnemonique());
											opcodeBinaire = assem.fromStringToArray(ShAdSuMo.ADDI.getOpcode());
											imm8 = Integer.parseInt(spltHas[1]); //la valeur du décalage [imm3]
											assem.fromIntToBinary(immBinaire, imm8, 3); //valeur du decalage en binaire
											assem.fromIntToBinary(registreBinaire, registre2, 3); //RN
											assem.fromIntToBinary(registreBinaire, registre1, 3); //RN + RD
											
											//on ajoute tout pour constituer notre mot
											motBinaire.addAll(mnemoniqueBinaire);
											motBinaire.addAll(opcodeBinaire);
											motBinaire.addAll(immBinaire);
											motBinaire.addAll(registreBinaire);
											
											//affichage
											System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
											System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
											System.out.print("RM et RD = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
											System.out.print("imm8 = ");assem.affichBin(immBinaire);
											System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
											System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
											System.out.println("\n");
											
											motBinaire.clear();
											break;

										}
									}
									if (breaker == true){break;}
									
									// cas ADDS Register
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','
									
									mnemoniqueBinaire = assem.fromStringToArray(ShAdSuMo.ADDR.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ShAdSuMo.ADDR.getOpcode());
									registre1 = Integer.parseInt(spltHas[2]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RM
									registre1 = Integer.parseInt(spltHas[1]);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RN
									registre1 = Integer.parseInt(spltHas[0]);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RN + RD
									
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(registreBinaire);
									
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("RM et RN et RD = ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
									
									motBinaire.clear();
									breaker = false;
									break;
									
					case "SUBS" : 	System.out.println("SUBS");
									for (String str : spltEs[1].split("")) {
										if (str.equals("#")) {  //permet de différencier le type d'ADDS
											breaker = true;
											
											//cas SUBS Immediate
											spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont séparés des valeurs par un #
											registreCourSplit = spltHas[0].split(","); //les deux registres sollicités
											registre1 = Integer.parseInt(registreCourSplit[0]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
											registre2 = Integer.parseInt(registreCourSplit[1]);
											
											mnemoniqueBinaire = assem.fromStringToArray(ShAdSuMo.SUBI.getMnemonique());
											opcodeBinaire = assem.fromStringToArray(ShAdSuMo.SUBI.getOpcode());
											imm8 = Integer.parseInt(spltHas[1]); //la valeur du décalage [imm3]
											assem.fromIntToBinary(immBinaire, imm8, 3); //valeur du decalage en binaire
											assem.fromIntToBinary(registreBinaire, registre2, 3); //RN
											assem.fromIntToBinary(registreBinaire, registre1, 3); //RN + RD
											
											//on ajoute tout pour constituer notre mot
											motBinaire.addAll(mnemoniqueBinaire);
											motBinaire.addAll(opcodeBinaire);
											motBinaire.addAll(immBinaire);
											motBinaire.addAll(registreBinaire);
											
											//affichage
											System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
											System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
											System.out.print("RM et RD = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
											System.out.print("imm8 = ");assem.affichBin(immBinaire);
											System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
											System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
											System.out.println("\n");
											
											motBinaire.clear();
											break;
										}
									}
									if (breaker == true){break;}
									
									// cas SUBS Register
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','
									
									mnemoniqueBinaire = assem.fromStringToArray(ShAdSuMo.SUBR.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ShAdSuMo.SUBR.getOpcode());
									registre1 = Integer.parseInt(spltHas[2]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RM
									registre1 = Integer.parseInt(spltHas[1]);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RN
									registre1 = Integer.parseInt(spltHas[0]);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RN + RD
									
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(registreBinaire);
									
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("RM et RN et RD = ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
									
									motBinaire.clear();
									breaker = false;
									break;
									
					case "MOVS" : 	System.out.println("MOV");
									spltHas = spltEs[spltEs.length-1].split("#");
									
									mnemoniqueBinaire = assem.fromStringToArray(ShAdSuMo.MOVE.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ShAdSuMo.MOVE.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									imm8 = Integer.parseInt(spltHas[1]);
									assem.fromIntToBinary(immBinaire, imm8, 8); //valeur du decalage en binaire
									
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(registreBinaire);
									motBinaire.addAll(immBinaire);
									
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("imm8 ("+imm8+") = ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
									
									motBinaire.clear();
									break;
									
					case "ANDS" : 	System.out.println("ANDS");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','
									
									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.ANDS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.ANDS.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
									
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);
									
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
									
									motBinaire.clear();
									break;
									
					case "EORS" : 	System.out.println("EORS");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.EORS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.EORS.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
									
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);
									
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
									
									motBinaire.clear();
									break;
									
					case "ADCS" : 	System.out.println("ADCS");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.ADCS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.ADCS.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
					
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);
					
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
					
									motBinaire.clear();
									break;
					
					case "RORS" : 	System.out.println("RORS");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.RORS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.RORS.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
	
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);
	
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
	
									motBinaire.clear();
									break;
									
					case "TST" : 	System.out.println("TST");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.TST.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.TST.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "RSBS" :	System.out.println("RSBS");
									//cas LSRS Immediate
									spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont séparés des valeurs par un #
									registreCourSplit = spltHas[0].split(","); //les deux registres sollicités
									registre1 = Integer.parseInt(registreCourSplit[0]); //récupère l'indice du registre x, de cet indice en déduis sa valeur
									registre2 = Integer.parseInt(registreCourSplit[1]);

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.RSBS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.RSBS.getOpcode());
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RM + RD
		
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);
									motBinaire.addAll(registreBinaire);
	
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("RM et RD = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
	
									motBinaire.clear();
									break;
									
					case "CMP" : 	System.out.println("CMP");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.CMP.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.CMP.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "CMN" : 	System.out.println("CMN");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.CMN.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.CMN.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "ORRS" : 	System.out.println("ORRS");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.ORRS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.ORRS.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "MULS" : 	System.out.println("MULS");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.MULS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.MULS.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "BICS" : 	System.out.println("BICS");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.BICS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.BICS.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
					
					case "MVNS" : 	System.out.println("MVNS");
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont séparés par des ','

									mnemoniqueBinaire = assem.fromStringToArray(DataProcessing.MVNS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(DataProcessing.MVNS.getOpcode());
									registre1 = Integer.parseInt(spltHas[0]); //RD
									registre2 = Integer.parseInt(spltHas[1]); //RM
									assem.fromIntToBinary(registreBinaire, registre2, 3); //RM

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									assem.fromIntToBinary(registreBinaire, registre1, 3); //RD
									motBinaire.addAll(registreBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("registre = ("+registre2+" et "+registre1+") ");assem.affichBin(registreBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
									
									motBinaire.clear();
									break;
									
					case "ADD" : 	System.out.println("ADD");
									spltHas = spltEs[spltEs.length-1].split("#");
					
									mnemoniqueBinaire = assem.fromStringToArray(Miscellaneous.ADD.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(Miscellaneous.ADD.getOpcode());
									imm8 = Integer.parseInt(spltHas[1]); //On récupère la valeur de shift [valeur suivant le #]
									assem.fromIntToBinary(immBinaire, imm8, 7); //la valeur binaire de l'adresse imm8
					
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);
					
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("imm8 = ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
					
									motBinaire.clear();
									break;
									
					case "SUB"  : 	System.out.println("SUB");
									spltHas = spltEs[spltEs.length-1].split("#");
					
									mnemoniqueBinaire = assem.fromStringToArray(Miscellaneous.SUB.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(Miscellaneous.SUB.getOpcode());
									imm8 = Integer.parseInt(spltHas[1]); //On récupère la valeur de shift [valeur suivant le #]
									assem.fromIntToBinary(immBinaire, imm8, 7); //la valeur binaire de l'adresse imm8
	
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);
	
									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.print("imm8 = ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
	
									motBinaire.clear();
									break;
									
					case "B" : 		System.out.println("B"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.B.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.B.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);
								
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
								
					case "BEQ" : 	System.out.println("BEQ"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BEQ.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BEQ.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);
					
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
								
					case "BNE" : 	System.out.println("BNE"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BNE.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BNE.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);
		
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "BCS" : 	System.out.println("BCS"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BCS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BCS.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "BCC" : 	System.out.println("BCC"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BCC.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BCC.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "BMI" : 	System.out.println("BMI"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BMI.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BMI.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "BPL" : 	System.out.println("BPL"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BPL.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BPL.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "BVS" : 	System.out.println("BVS"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BVS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BVS.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "BHI" : 	System.out.println("BHI"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BHI.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BHI.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
									
									motBinaire.clear();
									break;
									
					case "BLS" : 	System.out.println("BLS"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BLS.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BLS.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);
									
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
					
									motBinaire.clear();
									break;
									
					case "BGE" : 	System.out.println("BGE"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BGE.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BGE.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);
					
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");
	
									motBinaire.clear();
									break;
									
					case "BLT" : 	System.out.println("BLT"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BLT.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BLT.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);
	
									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "BGT" : 	System.out.println("BGT"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BGT.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BGT.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;
									
					case "BLE" : 	System.out.println("BLE"); //toujours vrai, utilisé pour relancer une boucle
									spltHas = spltEs[spltEs.length-1].split(" "); //spltHas[0] = cond
									imm8 = conditionalSwitch(ifArray, elseArray, thenArray, endArray, loopArray,spltHas);									
									mnemoniqueBinaire = assem.fromStringToArray(ConditionalBranch.BLE.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ConditionalBranch.BLE.getOpcode());
									assem.fromIntToBinary(immBinaire, imm8, 8);

									//on ajoute tout pour constituer notre mot
									motBinaire.addAll(mnemoniqueBinaire);
									motBinaire.addAll(opcodeBinaire);
									motBinaire.addAll(immBinaire);

									//affichage
									System.out.print("mnemonique = ");assem.affichBin(mnemoniqueBinaire);
									System.out.print("opcode = ");assem.affichBin(opcodeBinaire);
									System.out.println("mot exécuté = "+spltHas[0]);
									System.out.print("imm8 = ("+imm8+") ");assem.affichBin(immBinaire);
									System.out.print("mot entier [binaire] = ");assem.affichBin(motBinaire);
									System.out.print("mot entier [hexa] = ");assem.affichXex(converterBinHex.hexaconverteur(motBinaire));
									System.out.println("\n");

									motBinaire.clear();
									break;


				}
			}
																				
		}
		in2.close();
	}

	
	
}


