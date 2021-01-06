

public enum ShAdSuMo {
	ADDR("000","1100"),
	SUBR("000","1101"),
	ADDI("000","1110"),
	SUBI("000","1111"),
	MOVE("001","00");
	
	String mnemonique;
	String opcode;
	
	ShAdSuMo(String mnemonique, String opcode){
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
