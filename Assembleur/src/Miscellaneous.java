

public enum Miscellaneous {
	ADD("1011","00000"),
	SUB("1011","00001");
	
	String mnemonique;
	String opcode;
	
	Miscellaneous(String mnemonique, String opcode){
		this.mnemonique = mnemonique;
		this.opcode = opcode;
	}
	
	public String getMnemonique() {
		return this.mnemonique;
	}
	
	public String getOpcode() {
		return this.opcode;
	}

}
