package Assembleur;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Assembleur { //4 bits pour etiquette, 4 bits pour mnemonique, 8 bits pour code imm
	
	ArrayList<Integer> mnemonique;
	public int overflow;
	public int carry;
	public int negative;
	public int nul;
	
	ArrayList<Integer> b(){ //mnemonique d�di� aux 15 requetes B
		mnemonique = new ArrayList<>();
		System.out.println();
		mnemonique.add(1);
		mnemonique.add(1);
		mnemonique.add(0);
		mnemonique.add(1);
		return mnemonique;
	}
	
	ArrayList<Integer> descr(){ //mnemonique d�di� aux 3 requetes Description
		mnemonique = new ArrayList<>();
		System.out.println();
		mnemonique.add(0);
		mnemonique.add(0);
		//System.out.print("mnemonique = ");affich(mnemonique);
		return mnemonique;
	}
	
	ArrayList<Integer> loadStore(){ //mnemonique d�di� aux 2 requetes Load/S
		mnemonique = new ArrayList<>();
		System.out.println();
		mnemonique.add(1);
		mnemonique.add(0);
		mnemonique.add(0);
		mnemonique.add(1);
		//System.out.print("mnemonique = ");affich(mnemonique);
		return mnemonique;
	}
	
	void convBinaire(ArrayList<Integer> ab, int num, int bit) { //ajoute le bon nombre de zero en fonction du int entr� pour donner une valeur binaire en 8 bits
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
	
	void fromLinesToBinary (ArrayList<Integer> ab, int lines, int bit) {			//Depuis le nb de lignes de l'�tiquette, renvoie la liste avec les 8 bit de imm8 remplis
		int binary;
		setOverflow(isOverflow(lines, 0));
		setCarry(isCarry(lines, 0));
		if (lines < 0) { //dans le cas d'un nb n�gatif, on ajoute au bit le + a gauche
			lines = -lines;
			binary = Integer.parseInt(Integer.toBinaryString(lines));	//convertit la ligne de l'etiquette en binaire
			convBinaire(ab,lines,bit);										//ajoute les x 0 devant pour donner la valeur sur x bits, en fonction du nb de lignes en base 10
			for (int i = 0; i<ab.size();i++) {
				//System.out.println("valeur ab avant set : "+ab.get(i));
			}
			ab.set((16-bit), 1); //on met le bit de signe � la valeur 8 car c'est le bit de signe de l'imm8
		}else {
			binary = Integer.parseInt(Integer.toBinaryString(lines));	//convertit la ligne de l'etiquette en binaire
			convBinaire(ab,lines,bit);										//ajoute les x 0 devant pour donner la valeur sur x bits, en fonction du nb de lignes en base 10
		}
		String binaryS = String.valueOf(binary);
		String[] binarySplt = binaryS.split("");
		ArrayList<String> binarySpltArr = new ArrayList<String>();
		for (int i = 0; i < binarySplt.length; i++) {binarySpltArr.add(binarySplt[i]);}
		
		if (binarySpltArr.size() > 8 ) { 									//nb > 255 (2^8 - 1)												
			while (binarySpltArr.size() > 8 ) {
				binarySpltArr.remove(0);			//on va donc retir� tous les bits a droite jusqu'a avoir une valeur sur 8 bits
			}
		}
		//System.out.println("pour = "+lines);
		for (int i = 0; i < binarySpltArr.size(); i++) {					//on split pour avoir une valeur de type [1,1,1] plutot que [111]
			ab.add(Integer.parseInt(binarySpltArr.get(i)));					//ajoute la valeur binaire des lignes dans la liste
			//System.out.println("add : "+binarySpltArr.get(i));
		}
	}
	
	int isOverflow(int a, int b) {
		int o = 1; 
		if ((a+b) > -128 && (a+b) < 127) {
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
	
	public void setCarry(int valeur) {
		this.carry = valeur;
	}
	
	public int getCarry() {
		return this.carry;
	}

	public void setOverflow(int valeur) {
		this.overflow = valeur;
	}
	
	public int getOverflow() {
		return this.overflow;
	}
	
	public void setNegative(int valeur) {
		this.negative = valeur;
	}
	
	public int getNegative() {
		return this.negative;
	}
	
	public void setNul(int valeur) {
		this.nul = valeur;
	}
	
	public int getNul() {
		return this.nul;
	}
	

	public static void main(String[] args) throws IOException{
		Assembleur assem = new Assembleur();
		ConverterBinHex converterBinHex = new ConverterBinHex();
		
		BufferedReader in = new BufferedReader(new FileReader("res/requete.txt"));
		String line;
		ArrayList<Integer> ifs = new ArrayList<>();
		ArrayList<Integer> elses = new ArrayList<>();
		int lignes = 0;
		ArrayList<String> registres = new ArrayList<>();
		ArrayList<String> registresCourant = new ArrayList<>();
		ArrayList<String> valeurs = new ArrayList<>();
		ArrayList<Integer> valeurBinaire;

		
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
		
		BufferedReader in2 = new BufferedReader(new FileReader("res/requete.txt"));
		String line2;
		int j = 0; 					//cette valeur repr�sente l'indice unique de chaque etiquettes, on va l'incrementer a chaque fois qu'on en traite une (sembable a un .remove dans la liste)
		while ((line2 = in2.readLine()) != null){
			String[] spltEs = line2.split(" ");
			String[] registreCourSplit;
			int registre1;
			int registre2;
			
			String imm;
			String rNrM;
			ArrayList<Integer> constructionRegistre = new ArrayList<Integer>();;
			ArrayList<Integer> constructionImm = new ArrayList<Integer>();;
			assem.setNegative(0);
			assem.setOverflow(0);
			
			for (int i = 0; i < spltEs.length; i++) {
				switch(spltEs[i]) {
					 
					/*case "ldr" : System.out.println("ldr"); 		  //r�cup�re les diff�rentes affectation dans les registres, et les met dans les lists appropri�es
								 String[] spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres sont s�par�s des valeurs par un #
								 registres.add(spltHas[0]); 
								 valeurs.add(spltHas[1]);
								 break;
					*/		
				
					// RT = Source register avec adresse = valeur RT +/- imm8
					case "STR" : System.out.println("STR"); //store la valeur dans un registre
								 String[] spltHas = spltEs[spltEs.length-1].split("#");
								 String[] spltVir = spltHas[0].split(","); //On r�cup�re RT et la valeur du Base register
								 registres.add(spltVir[0]); //ajoute le registre
								 valeurs.add(spltVir[1]); 	//valeur du registre
								 
								 int shift = Integer.parseInt(spltHas[1]); 	//imm 8 [-32 ; 32]
								 valeurBinaire = initStoreLoad(assem, 0); //mnemonique + opcode
								 int rt = Integer.parseInt(spltVir[1]);
								 assem.fromLinesToBinary(valeurBinaire,rt , 3); //mnemonique + opcode + RT
								 int imm8 = rt + shift;
								 assem.fromLinesToBinary(valeurBinaire, imm8, 8);//mnemonique + opcode + RT + imm8
								 
								 affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								 aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								 break;
					//charge le contenu d'un registre depuis une adresse			 
					case "LDR" : System.out.println("LDR"); 
								 String[] spltHas2 = spltEs[spltEs.length-1].split("#");
								 String[] spltVir2 = spltHas2[0].split(","); //On r�cup�re RT et la valeur du Base register
								 //registres.add(spltVir2[0]); //ajoute le registre
								 //valeurs.add(spltVir2[1]); 	//valeur du registre
								 
								 int shift2 = Integer.parseInt(spltHas2[1]); 	//imm 8 [-32 ; 32]
								 valeurBinaire = initStoreLoad(assem, 1); //mnemonique + opcode
								 int rt2 = Integer.parseInt(spltVir2[1]);
								 assem.fromLinesToBinary(valeurBinaire,rt2 , 3); //mnemonique + opcode + RT
								 int imm82 = rt2 + shift2; //imm8 = numero du registre + valeur du shift
								 assem.fromLinesToBinary(valeurBinaire, imm82, 8);//mnemonique + opcode + RT + imm8
								 
								 affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								 aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								 break; 
								 
								 
					case "LSLS" : System.out.println("LSLS");
					  			  valeurBinaire = initDescr(assem, 0);
					  			  spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
					  			  rNrM = spltHas[0];
					  			  imm = spltHas[1];
					  			  registreCourSplit = spltHas[0].split(",");
					  			  registre1 = registres.indexOf(registreCourSplit[0]);
					  			  registre2 = registres.indexOf(registreCourSplit[1]);
					  
					  			  constructionImm.clear(); //on va le r�utiliser pour r�cup�rer la valeur du nouveau registre
					  			  assem.fromLinesToBinary(constructionRegistre, Integer.parseInt(valeurs.get(registre1)), 3); //  R1 binaire = Rm (R0 = R8 = Rd)
					  			  assem.fromLinesToBinary(constructionImm, Integer.parseInt(imm), 5); // imm binaire
					  			  valeurBinaire.addAll(constructionImm); // mnemonique + id + imm5
					  			  constructionImm.clear(); //on va le r�utiliser pour r�cup�rer la valeur du nouveau registre
					  			  constructionImm = shiftLeft(constructionRegistre, Integer.parseInt(imm), assem); //new registre
					  			  valeurBinaire.addAll(constructionImm); //mnemonique + id + imm5 + RM 
		  			  			  valeurBinaire.addAll(constructionRegistre); //mnemonique + id + imm5 + RM + RD
					  
					  			  affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
					  			  aff(converterBinHex.hexaconverteur(valeurBinaire));
					  			  System.out.println("\n");
					  			  assem.setNegative(0);
					  			  assem.setOverflow(0);
					  			  assem.setCarry(0);
					  			  assem.setNul(0);
					  			  break;
					  
					  
					case "LSRS" : System.out.println("LSRS");
					  			  valeurBinaire = initDescr(assem, 1);
					  			  spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
					  			  rNrM = spltHas[0];
					  			  imm = spltHas[1];
					  			  registreCourSplit = spltHas[0].split(",");
					  			  registre1 = registres.indexOf(registreCourSplit[0]);
					  			  registre2 = registres.indexOf(registreCourSplit[1]);
					  
					  			  constructionImm.clear(); //on va le r�utiliser pour r�cup�rer la valeur du nouveau registre
					  			  assem.fromLinesToBinary(constructionRegistre, Integer.parseInt(valeurs.get(registre1)), 3); //  R1 binaire = Rm (R0 = R8 = Rd)
					  			  assem.fromLinesToBinary(constructionImm, Integer.parseInt(imm), 5); // imm binaire
					  			  valeurBinaire.addAll(constructionImm); // mnemonique + id + imm5
					  			  constructionImm.clear(); //on va le r�utiliser pour r�cup�rer la valeur du nouveau registre
					  			  constructionImm = shiftRight(constructionRegistre, Integer.parseInt(imm), assem); //new registre
					  			  valeurBinaire.addAll(constructionImm); //mnemonique + id + imm5 + RM 
		  			  			  valeurBinaire.addAll(constructionRegistre); //mnemonique + id + imm5 + RM + RD
					  
					  			  affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
					  			  aff(converterBinHex.hexaconverteur(valeurBinaire));
					  			  System.out.println("\n");
					  			  assem.setNegative(0);
					  			  assem.setOverflow(0);
					  			  assem.setCarry(0);
					  			  assem.setNul(0);
					  			  break;
			
					case "ASRS" : System.out.println("ASRS");
		  			  			  valeurBinaire = initDescr(assem, 2);
		  			  			  spltHas = spltEs[spltEs.length-1].split("#"); //on sait que les registres et imm5 sont s�par�s des valeurs par un #
		  			  			  rNrM = spltHas[0];
		  			  			  imm = spltHas[1];
		  			  			  registreCourSplit = spltHas[0].split(",");			//Dans l'exemple ASRS R5,R6#2
		  			  			  registre1 = registres.indexOf(registreCourSplit[0]); //R5 = RD -> d�cale le contenu de ce registre
		  			  			  registre2 = registres.indexOf(registreCourSplit[1]); //R6 = RM -> ecrit le resultat du decalage dans ce registre
		  			  
		  			  			  constructionImm.clear(); //on va le r�utiliser pour r�cup�rer la valeur du nouveau registre
		  			  			  assem.fromLinesToBinary(constructionRegistre, Integer.parseInt(valeurs.get(registre1)), 3); //  RD
		  			  			  assem.fromLinesToBinary(constructionImm, Integer.parseInt(imm), 5); // imm binaire
		  			  			  valeurBinaire.addAll(constructionImm); // mnemonique + id + imm5
		  			  			  constructionImm.clear(); //on va le r�utiliser pour r�cup�rer la valeur du nouveau registre
		  			  			  constructionImm = shiftRightFlag(constructionRegistre, Integer.parseInt(imm), assem.getCarry(), assem); //RM
		  			  			  valeurBinaire.addAll(constructionImm); //mnemonique + id + imm5 + RM 
		  			  			  valeurBinaire.addAll(constructionRegistre); //mnemonique + id + imm5 + RM + RD
		  			  			  
		  
		  			  			  affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
		  			  			  aff(converterBinHex.hexaconverteur(valeurBinaire));
		  			  			  System.out.println("\n");
		  			  			  assem.setNegative(0);
		  			  			  assem.setOverflow(0);
		  			  			  assem.setCarry(0);
		  			  			  assem.setNul(0);
		  			  			  break;
						 
								 
					case "BEQ" : System.out.println("BEQ");									//BEQ = egal
								 valeurBinaire = init(assem,0); 							//ajoute la valeur 0 apres les 4 zeros
								 registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								 registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								 registre2 = registres.indexOf(registreCourSplit[1]);
								 
								 if (valeurs.get(registre1).equals(valeurs.get(registre2))) { //On utilise l'indice r�cup�r� pr�c�demment pour v�rifier l'�galit� des 2 valeurs des registres
									 conditionIf(assem, ifs, valeurBinaire, j);				  //on r�cupere la valeur de lignes pour le if, qu'on va convertir en binaire. A la fin de cet appel de fonction, nous avons nos 8 bits de imm
									 j++;													  //on incr�mente j pour ne pas retomber sur les meme etiquettes
									 
								 }else {
									 conditionElse(assem, elses, valeurBinaire, j);
									 j++;
								 }
								 assem.setNul(1);
								 affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								 aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								 
								 assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								 break;
								 		
					case "BNE" : System.out.println("BNE");							//BNE = different, meme model que BEQ
								valeurBinaire = init(assem,1); //ajoute la valeur 1 apres les 3 zeros
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								assem.setOverflow(assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2))));

								
					 			if (assem.getOverflow() ==1 || !valeurs.get(registre1).equals(valeurs.get(registre2))) { 		
									 conditionIf(assem, ifs, valeurBinaire, j);	
									 j++;
									 
								 }else {
									 conditionElse(assem, elses, valeurBinaire, j);
									 j++;
								 }
					 			assem.setNul(0);
					 			affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
					 			aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								 assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								 break;
					 			 
					case "BVS" : System.out.println("BVS");							//BCS = retenue
								valeurBinaire = init(assem,6); //ajoute la valeur 6 apres le zero
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								assem.setOverflow(assem.isCarry(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2))));
					 			 
					 			 if (assem.getOverflow() == 1) { //il y a une retenue
					 				 conditionIf(assem, ifs, valeurBinaire,j);
					 				 j++;
					 			 }else {
					 				conditionElse(assem, elses, valeurBinaire, j);
									j++;
					 			 }
					 			affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
					 			aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
					 			assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
					 			break;
					
					case "BVC" : System.out.println("BVC"); 						//BCC = sans retenue
								valeurBinaire = init(assem,7); //ajoute la valeur 7 apres le zero
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								assem.setOverflow(assem.isCarry(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2))));
		 			 			
		 			 			if (assem.getOverflow() == 0) { //il y a une retenue
		 			 				conditionIf(assem, ifs, valeurBinaire, j);
		 			 				j++;
		 			 			}else {
		 			 				conditionElse(assem, elses, valeurBinaire, j);
		 			 				j++;
		 			 			}
		 			 			affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
		 			 			aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
		 			 			assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
		 			 			break;
		 			
					case "BMI" : System.out.println("BMI"); 						//BCC = n�gatif
								valeurBinaire = init(assem,4); //ajoute la valeur 3 apres les 3 zeros
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								//System.out.println("operation = "+(Integer.parseInt(valeurs.get(registre1)) +"+"+ (Integer.parseInt(valeurs.get(registre2)))));
	 			 			
								if ((Integer.parseInt(valeurs.get(registre1)) < (Integer.parseInt(valeurs.get(registre2))))) { //l'addition des deux valeurs est n�gative
									conditionIf(assem, ifs, valeurBinaire, j);
									j++;
									assem.setNegative(1);
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
									assem.setNegative(0);
								}
								affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								break;
								
					case "BPL" : System.out.println("BPL"); 						//BCC = n�gatif
								valeurBinaire = init(assem,5); //ajoute la valeur 3 apres les 3 zeros
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								assem.setOverflow(assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2))));
		 			
								if (assem.getOverflow() == 1 || (Integer.parseInt(valeurs.get(registre1)) >= (Integer.parseInt(valeurs.get(registre2))))) { //l'addition des deux valeurs est n�gative
									conditionIf(assem, ifs, valeurBinaire, j);
									j++;
									assem.setNegative(0);
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
									assem.setNegative(1);
								}
								affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								break;
								
								
					case "BCS" : System.out.println("BCS");							//BCS = retenue
								valeurBinaire = init(assem,2); //ajoute la valeur 2 apres les 3 zeros
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								assem.setCarry(assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2))));
		 			 
								if (assem.getCarry() == 1 ||(Integer.parseInt(valeurs.get(registre1))) >= (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								break;
						
					case "BCC" :System.out.println("BCC");							//BCS = retenue
								valeurBinaire = init(assem,3); //ajoute la valeur 3 apres les 3 zeros
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
			 
								if ((Integer.parseInt(valeurs.get(registre1))) < (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								break;
								
					case "BHI" :System.out.println("BHI");							//BCS = retenue
								valeurBinaire = init(assem,8); //ajoute la valeur 8 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								assem.setCarry(assem.isCarry(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2))));
			 
								if (assem.getCarry() == 1 ||(Integer.parseInt(valeurs.get(registre1))) > (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									assem.setNul(0);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									assem.setNul(1);
									j++;
								}
								affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								break;
						
					case "BLS" :System.out.println("BLS");							//BCS = retenue
								valeurBinaire = init(assem,9); //ajoute la valeur 9 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								assem.setCarry(assem.isCarry(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2))));
 
								if (assem.getCarry() == 0 || (Integer.parseInt(valeurs.get(registre1))) <= (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									assem.setNul(1);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									assem.setNul(0);
									j++;
								}
								affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								break;
						
					case "BGE" :System.out.println("BGE");							//BCS = retenue
								valeurBinaire = init(assem,10); //ajoute la valeur 9 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								assem.setOverflow(assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2))));
								
								if (assem.getOverflow() == 0 ||(Integer.parseInt(valeurs.get(registre1))) >= (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									assem.setNegative(0);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
									assem.setNegative(0);
								}
								
								affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								break;
								
					case "BLT" :System.out.println("BLT");							//BCS = retenue
								valeurBinaire = init(assem,11); //ajoute la valeur 9 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								assem.setOverflow(assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2))));

								if (assem.getOverflow() == 1 || (Integer.parseInt(valeurs.get(registre1))) < (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									assem.setNegative(0);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									assem.setNegative(1);
									j++;
								}
								affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								break;
						
					case "BGT" :System.out.println("BGT");							//BCS = retenue
								valeurBinaire = init(assem,12); //ajoute la valeur 9 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								
								if ((Integer.parseInt(valeurs.get(registre1))) > (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									assem.setNegative(0);
									assem.setOverflow(0);
									assem.setNul(0);
									j++;
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									j++;
								}
								affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								break;
								
					case "BLE" :System.out.println("BLE");							//BCS = retenue
								valeurBinaire = init(assem,13); //ajoute la valeur 9 en binaire
								registreCourSplit = spltEs[spltEs.length-1].split(",");    //on r�cup�re ainsi les x registres courants
								registre1 = registres.indexOf(registreCourSplit[0]);	//Maintenant, on va r�cup�rer l'indice de ces registres courant dans l'arryList registre qui regroupe TOUS les registres
								registre2 = registres.indexOf(registreCourSplit[1]);
								assem.setOverflow(assem.isOverflow(Integer.parseInt(valeurs.get(registre1)), Integer.parseInt(valeurs.get(registre2))));

								if (assem.getOverflow() == 1 || (Integer.parseInt(valeurs.get(registre1))) <= (Integer.parseInt(valeurs.get(registre2)))  ) { //il y a une retenue
									conditionIf(assem, ifs, valeurBinaire,j);
									j++;
									assem.setNegative(0);
								}else {
									conditionElse(assem, elses, valeurBinaire, j);
									assem.setNegative(0);
									j++;
								}
								affich(valeurBinaire, assem.getCarry(), assem.getOverflow(), assem.getNegative(), assem.getNul());
								aff(converterBinHex.hexaconverteur(valeurBinaire));
								 System.out.println("\n");
								assem.setNegative(0);
								 assem.setOverflow(0);
								 assem.setCarry(0);
								 assem.setNul(0);
								break;
								
					default : break;
				}
			}
			
		}
	      // Afficher le contenu du fichier
			  //System.out.println (line);
		
		in2.close();
	}
	
	
	
	private static void affich(ArrayList<Integer> valeurBinaire, int carry, int overflow, int negative, int nul) {
		
		for (int p : valeurBinaire) {
			System.out.print(p);
		}
		System.out.println("\n(Carry) C : "+carry);
		System.out.println("(oVerflow) V : "+overflow);
		System.out.println("(Negative) N : "+negative);
		System.out.println("(Zero) Z : "+nul);
		
	}
	
	private static void aff(ArrayList<String> valeurBinaire) {
		//System.out.println("val binaire 1  = "+valeurBinaire.get(0));
		for (String p : valeurBinaire) {
			System.out.print(p);
		}
	}
	
	private static void conditionElse(Assembleur assem, ArrayList<Integer> elses, ArrayList<Integer> valeurBinaire, int j) {
		int elsess = elses.get(j);	
		System.out.println("else");
		assem.fromLinesToBinary(valeurBinaire, elsess,8);
	}

	private static void conditionIf(Assembleur assem, ArrayList<Integer> ifs, ArrayList<Integer> valeurBinaire, int j) {
		int ifss = ifs.get(j);	
		System.out.println("if");
		assem.fromLinesToBinary(valeurBinaire, ifss,8);
	}
	
	private static ArrayList<Integer> shiftRight(ArrayList<Integer> liste,int imm5, Assembleur assem){ //R1, imm //0 ajout� a gauche //liste = RD
		assem.setOverflow(0); //overflow tjr egal a 0 lors de decalage a gauche
		ArrayList<Integer> tempo = new ArrayList<Integer>();
		for (int i = 0 ; i < imm5; i++) {tempo.add(0);} //on ajoute les 0 au debut																			//RD = 	  111
		tempo.addAll(liste);																																//RM = 111000
		assem.setCarry(tempo.get(3)); //la retenue est �gale au dernier bit sortant
		for (int j = tempo.size()-1; j > 2; j--) {tempo.remove(j);} //on retire le surplus, a partir de 2 car c'est l'indice nb 3, or c'est cod� sur 3 bits	//RM =    000
		if (tempo.contains(1)){assem.setNul(0);}
		else {assem.setNul(1);}
		return tempo;																												  //tempo = RM
	}
	
	private static ArrayList<Integer> shiftLeft(ArrayList<Integer> liste,int imm5, Assembleur assem){ //0 ajout� a droite //liste = RD
		assem.setOverflow(0); //overflow tjr egal a 0 lors de decalage a droite
		ArrayList<Integer> tempo = new ArrayList<Integer>();
		ArrayList<Integer> tempo2 = new ArrayList<Integer>();
		tempo.addAll(liste);																//RD = 111
		for (int i = 0; i < imm5; i++) {tempo.add(0);} //on ajoute les 0 a la fin			//RM = 000111
		assem.setCarry(tempo.get(tempo.size() - 4)); 	//la retenue est �gale au dernier bit sortant
		for (int j = tempo.size() - 3; j < tempo.size(); j++) {tempo2.add(tempo.get(j));}	//RM = 000
		if (tempo2.contains(1)){assem.setNul(0);}
		else {assem.setNul(1);}
		return tempo2;																										//tempo = RM
	}
	
	private static ArrayList<Integer> shiftRightFlag(ArrayList<Integer> liste,int imm5, int carry, Assembleur assem){ //liste = RD
		ArrayList<Integer> tempo = new ArrayList<Integer>();
		tempo = shiftRight(liste,imm5,assem);
		//tempo.remove(2);
		//Bit de signe maintenu
		//je pars du principe que r�ins�r� = de 001 � 101
		tempo.set(0, liste.get(0));
		if (tempo.contains(1)){assem.setNul(0);}
		else {assem.setNul(1);}
		return tempo;																								   //tempo = RM
	}

	private static ArrayList<Integer> init(Assembleur assem, int opcode) {
		ArrayList<Integer> valeurBinaire;
		valeurBinaire = new ArrayList<>();
		valeurBinaire.addAll(assem.b()); 											//on ajoute les 1101 au debut de l'arraylist
		assem.convBinaire(valeurBinaire, opcode, 4);						    //on ajoute les 4 bits qui identifie l'opcode
		String code = Integer.toBinaryString(opcode);
		String[] spltCode = code.split("");
		for (String i : spltCode) {valeurBinaire.add(Integer.parseInt(i));}			//on ajoute a l'ArrayList un a un les valeurs de l'opcode
		return valeurBinaire;
	}
	
	private static ArrayList<Integer> initDescr(Assembleur assem, int opcode) {
		ArrayList<Integer> valeurBinaire;
		valeurBinaire = new ArrayList<>();
		valeurBinaire.addAll(assem.descr()); 										//on ajoute les 00 au debut de l'arraylist
		assem.convBinaire(valeurBinaire, opcode, 3);						    //on ajoute les 3 bits qui identifie l'opcode
		String code = Integer.toBinaryString(opcode);
		String[] spltCode = code.split("");
		for (String i : spltCode) {valeurBinaire.add(Integer.parseInt(i));}			//on ajoute a l'ArrayList un a un les valeurs de l'opcode
		return valeurBinaire;
	}
	
	private static ArrayList<Integer> initStoreLoad(Assembleur assem, int opcode) {
		ArrayList<Integer> valeurBinaire;
		valeurBinaire = new ArrayList<>();
		valeurBinaire.addAll(assem.loadStore()); 									//on ajoute les 00 au debut de l'arraylist
		assem.convBinaire(valeurBinaire, opcode, 1);						    	//on ajoute le 1 bits qui identifie l'opcode
		String code = Integer.toBinaryString(opcode);
		String[] spltCode = code.split("");
		for (String i : spltCode) {valeurBinaire.add(Integer.parseInt(i));}			//on ajoute a l'ArrayList un a un les valeurs de l'opcode
		return valeurBinaire;
	}
}
