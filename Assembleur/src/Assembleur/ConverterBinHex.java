package Assembleur;
import java.util.ArrayList;
import java.util.List;
public class ConverterBinHex {

	public static ArrayList<String> hexaconverteur(ArrayList<Integer> binaire){
		    int[] hex = new int[1000];
		    int i = 1, j = 0, rem, dec = 0;
		   ArrayList<String> hexa = new ArrayList<String>();

		    for (int bin: convertStringToTabInt(convertIntegersToString(binaire))
		         ) {
		    	//System.out.println("valeur bin avant = "+bin);
		    	//System.out.println("val j debut= "+j);
		    	if (bin == 0) {
		    		hexa.add(Integer.toString(0));
		    	}else {
			        while (bin > 0) {
			            rem = bin % 2;
			            dec = dec + rem * i;
			            i = i * 2;
			            bin = bin / 10;
			        }
			        //System.out.println("val j = "+j);
			        i = 0;
			        //System.out.println("val i = "+i);
			        while (dec != 0) {
			            hex[i] = dec % 16;
			            dec = dec / 16;
			            i++;
			        }
			        //System.out.println("valeur bin apres = "+bin);
			        //System.out.println("i - 1 = "+(i-1));
			        for (j = i - 1; j >= 0; j--) {
			        	//System.out.println("valeur de hex = "+hex[j]);
			            if (hex[j] > 9) {
			                char hexun = (char) (hex[j] + 55);
			                hexa.add(Character.toString(hexun));
			            } else {
			            	//if (hex[j] != 0) {
			                hexa.add(Integer.toString(hex[j]));
			            	//}
			            }
			        }
		    	}
		    }
		    return hexa;
		}


		    public static String convertIntegersToString(List<Integer> integers)
		    {
		        int j = 0;
		        StringBuilder sb = new StringBuilder();
		        for (int i = 0; i < integers.size() ; i++) {
		            int num = integers.get(i);
		            sb.append(num);
		            j++;
		            if (j == 4 || j == 8 || j == 12){
		                sb.append(",");
		            }
		        }
		        String result = sb.toString();
		        //System.out.println("resultat = "+result);
		        return result;
		    }
		    public static int[] convertStringToTabInt(String s){
		    int[] tabint = new int[4];
		    String[] tabString = s.split(",");
		    for (int i=0; i<4;i++){
		        tabint[i] = Integer.parseInt(tabString[i]);
		        //System.out.println("tab = "+tabint[i]);
		    }
		    return tabint;
		    }
}