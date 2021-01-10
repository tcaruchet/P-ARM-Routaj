
public class Testing {
	
	 public String twosCompliment(String bin) {
	        String twos = "", ones = "";
	        
	        if (String.valueOf(bin.charAt(0)).equals("0")) { //binaire positif, son complément à deux est le même
	        	return bin;
	        }

	        for (int i = 1; i < bin.length(); i++) {
	            ones += flip(bin.charAt(i));
	        }
	        int number0 = Integer.parseInt(ones, 2);
	        StringBuilder builder = new StringBuilder(ones);
	        boolean b = false;
	        for (int i = ones.length() - 1; i > 0; i--) {
	            if (ones.charAt(i) == '1') {
	                builder.setCharAt(i, '0');
	            } else {
	                builder.setCharAt(i, '1');
	                b = true;
	                break;
	            }
	        }
	        if (!b)
	            builder.append("1", 0, 7);

	        twos = builder.toString();

	        return twos;
	    }

	// Returns '0' for '1' and '1' for '0'
	    public char flip(char c) {
	        return (c == '0') ? '1' : '0';
	    }

	public static void main(String[] args) {
		String bin = "10000010";
		Testing main = new Testing();
		
		System.out.println(main.twosCompliment(bin));
		

	}

}
