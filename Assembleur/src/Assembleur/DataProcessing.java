package Assembleur;

public enum DataProcessing {
	AND("010000","0000"),
	OR("010000","0001"),
	LSLS("010000","0010"),
	LSRS("010000","0011"),
	ASRS("010000","0100"),
	ADCS("010000","0101"),
	SBCS("010000","0110"),
	RORS("010000","0111"),
	TST("010000","1000"),
	RSBS("010000","1001"),
	CMP("010000","1010"),
	CMN("010000","1011"),
	ORRS("010000","1100"),
	MULS("010000","1101"),
	BICS("010000","1110"),
	MVNS("010000","1111");
	
	String mnemonique;
	String opcode;
	
	DataProcessing(String mnemonique, String opcode){
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
