package Assembleur;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AssembleurV2 {
	
	public ArrayList<Integer> fromStringToArray(String valeur) {
		String[] spliter = valeur.split(""); //pour passer de 111 � 1,1,1
		ArrayList<Integer> nombreBinaire = new ArrayList<Integer>();
		for (String str : spliter) {nombreBinaire.add(Integer.parseInt(str));} //on ajoute les nouveaux int � l'arraylist
		return nombreBinaire;
	}
	
	public void fromIntToBinary (ArrayList<Integer> ab, int lines, int bit) {
		int binary;
		if (lines < 0) { //dans le cas d'un nb n�gatif, on ajoute au bit le + a gauche
			lines = -lines;
			binary = Integer.parseInt(Integer.toBinaryString(lines));	//convertit la ligne de l'etiquette en binaire
			ajoutZero(ab,lines,bit);										//ajoute les x 0 devant pour donner la valeur sur x bits, en fonction du nb de lignes en base 10
			for (int i = 0; i<ab.size();i++) {
				//System.out.println("valeur ab avant set : "+ab.get(i));
			}
			ab.set(0, 1); //on met le bit de signe � 1
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
				binarySpltArr.remove(0);			//on va donc retir� tous les bits a droite jusqu'a avoir une valeur sur 8 bits
			}
		}
		for (int i = 0; i < binarySpltArr.size(); i++) {					//on split pour avoir une valeur de type [1,1,1] plutot que [111]
			ab.add(Integer.parseInt(binarySpltArr.get(i)));					//ajoute la valeur binaire des lignes dans la liste
		}
	}
	
	public void ajoutZero(ArrayList<Integer> ab, int num, int bit) { //ajoute le bon nombre de zero en fonction du int entr� pour donner une valeur binaire en x bits
		//System.out.println("convBinaire");
		//System.out.println("valeur entr�e = "+ num);
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

	public static void main(String[] args) throws IOException{
		AssembleurV2 assem = new AssembleurV2();
		ConverterBinHex converterBinHex = new ConverterBinHex();
		
		//parcour le fichier pour r�cup�rer les conditions (if/else)
		BufferedReader in = new BufferedReader(new FileReader("res/requete.txt"));
		String line;
		ArrayList<Integer> ifs = new ArrayList<Integer>();
		ArrayList<Integer> elses = new ArrayList<Integer>();
		int lignes = 0;
		//ArrayList<String> registres = new ArrayList<String>();
		//ArrayList<String> valeurs = new ArrayList<String>();
		
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
		
		//parcour une seconde fois le fichier pour r�cup�rer les requ�tes
		BufferedReader in2 = new BufferedReader(new FileReader("res/requete.txt"));
		String line2;
		int j = 0; 					//cette valeur repr�sente l'indice unique de chaque etiquettes, on va l'incrementer a chaque fois qu'on en traite une (sembable a un .remove dans la liste)
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
				switch(spltEs[i]) { //chaque mots, s�par� d'un espace, d'une ligne
				
					case "LDR" : 	System.out.println("LDR");
									spltHas = spltEs[spltEs.length-1].split("#");
									spltVir = spltHas[0].split(","); //On r�cup�re RT et la valeur du registre de base
									shift = Integer.parseInt(spltHas[1]); //On r�cup�re la valeur de shift [valeur suivant le #]
									
									mnemoniqueBinaire = assem.fromStringToArray(LoadStore.LDR.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(LoadStore.LDR.getOpcode());
									registre1 = Integer.parseInt(spltVir[1]); //le numero du registre
									assem.fromIntToBinary(registreBinaire, registre1, 3); //la valeur binaire du numero du registre
									imm8 = shift + registre1; //l'adresse imm8 est compos� de la valeur du registre + du shift effectu� [valeur apres le #]
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
									spltVir = spltHas[0].split(","); //On r�cup�re RT et la valeur du registre de base
									shift = Integer.parseInt(spltHas[1]); //On r�cup�re la valeur de shift [valeur suivant le #]
									//registres.add(spltVir[0]); 	//stocke le registre
									//valeurs.add(spltVir[1]); 	//stocke le num�ro du registre [ici son num�ro = sa valeur]
									
									mnemoniqueBinaire = assem.fromStringToArray(LoadStore.STR.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(LoadStore.STR.getOpcode());
									registre1 = Integer.parseInt(spltVir[1]); //le numero du registre
									assem.fromIntToBinary(registreBinaire, registre1, 3); //la valeur binaire du numero du registre
									imm8 = shift + registre1; //l'adresse imm8 est compos� de la valeur du registre + du shift effectu� [valeur apres le #]
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
										if (str.equals("#")) {  //permet de diff�rencier le type d'ADDS
											breaker = true;
							
											//cas LSLS Immediate
											spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
											registreCourSplit = spltHas[0].split(","); //les deux registres sollicit�s
											registre1 = Integer.parseInt(registreCourSplit[0]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
											registre2 = Integer.parseInt(registreCourSplit[1]);
					
											mnemoniqueBinaire = assem.fromStringToArray(Description.LSLS.getMnemonique());
											opcodeBinaire = assem.fromStringToArray(Description.LSLS.getOpcode());
											imm8 = Integer.parseInt(spltHas[1]); //la valeur du d�calage [imm5]
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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
									registre1 = Integer.parseInt(spltHas[0]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
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
										if (str.equals("#")) {  //permet de diff�rencier le type d'ADDS
											breaker = true;
			
											//cas LSRS Immediate
											spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
											registreCourSplit = spltHas[0].split(","); //les deux registres sollicit�s
											registre1 = Integer.parseInt(registreCourSplit[0]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
											registre2 = Integer.parseInt(registreCourSplit[1]);
	
											mnemoniqueBinaire = assem.fromStringToArray(Description.LSRS.getMnemonique());
											opcodeBinaire = assem.fromStringToArray(Description.LSRS.getOpcode());
											imm8 = Integer.parseInt(spltHas[1]); //la valeur du d�calage [imm5]
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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
									registre1 = Integer.parseInt(spltHas[0]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
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
										if (str.equals("#")) {  //permet de diff�rencier le type d'ADDS
											breaker = true;

											//cas ASRS Immediate
											spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
											registreCourSplit = spltHas[0].split(","); //les deux registres sollicit�s
											registre1 = Integer.parseInt(registreCourSplit[0]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
											registre2 = Integer.parseInt(registreCourSplit[1]);

											mnemoniqueBinaire = assem.fromStringToArray(Description.ASRS.getMnemonique());
											opcodeBinaire = assem.fromStringToArray(Description.ASRS.getOpcode());
											imm8 = Integer.parseInt(spltHas[1]); //la valeur du d�calage [imm5]
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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
									registre1 = Integer.parseInt(spltHas[0]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
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
										if (str.equals("#")) {  //permet de diff�rencier le type d'ADDS
											breaker = true;
											
											//cas ADDS Immediate
											spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
											registreCourSplit = spltHas[0].split(","); //les deux registres sollicit�s
											registre1 = Integer.parseInt(registreCourSplit[0]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
											registre2 = Integer.parseInt(registreCourSplit[1]);
											
											mnemoniqueBinaire = assem.fromStringToArray(ShAdSuMo.ADDI.getMnemonique());
											opcodeBinaire = assem.fromStringToArray(ShAdSuMo.ADDI.getOpcode());
											imm8 = Integer.parseInt(spltHas[1]); //la valeur du d�calage [imm3]
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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','
									
									mnemoniqueBinaire = assem.fromStringToArray(ShAdSuMo.ADDR.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ShAdSuMo.ADDR.getOpcode());
									registre1 = Integer.parseInt(spltHas[2]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
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
										if (str.equals("#")) {  //permet de diff�rencier le type d'ADDS
											breaker = true;
											
											//cas SUBS Immediate
											spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
											registreCourSplit = spltHas[0].split(","); //les deux registres sollicit�s
											registre1 = Integer.parseInt(registreCourSplit[0]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
											registre2 = Integer.parseInt(registreCourSplit[1]);
											
											mnemoniqueBinaire = assem.fromStringToArray(ShAdSuMo.SUBI.getMnemonique());
											opcodeBinaire = assem.fromStringToArray(ShAdSuMo.SUBI.getOpcode());
											imm8 = Integer.parseInt(spltHas[1]); //la valeur du d�calage [imm3]
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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','
									
									mnemoniqueBinaire = assem.fromStringToArray(ShAdSuMo.SUBR.getMnemonique());
									opcodeBinaire = assem.fromStringToArray(ShAdSuMo.SUBR.getOpcode());
									registre1 = Integer.parseInt(spltHas[2]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','
									
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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','

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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','

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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','

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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','

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
									spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
									registreCourSplit = spltHas[0].split(","); //les deux registres sollicit�s
									registre1 = Integer.parseInt(registreCourSplit[0]); //r�cup�re l'indice du registre x, de cet indice en d�duis sa valeur
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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','

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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','

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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','

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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','

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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','

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
									spltHas = spltEs[spltEs.length-1].split(","); //on sait que les registres sont s�par�s par des ','

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
									imm8 = Integer.parseInt(spltHas[1]); //On r�cup�re la valeur de shift [valeur suivant le #]
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
									imm8 = Integer.parseInt(spltHas[1]); //On r�cup�re la valeur de shift [valeur suivant le #]
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
				}
			}
																				
		}
		in2.close();
	}
	
}

