

public enum Description{
	LSLS("000","00"),
	LSRS("000","01"),
	ASRS("000","10");
	
	String mnemonique;
	String opcode;
	
	Description(String mnemonique, String opcode){
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
