package Assembleur;

public enum ConditionalBranch {
	BEQ("1101","0000"),
	BNE("1101","0001"),
	BCS("1101","0010"),
	BCC("1101","0011"),
	BMI("1101","0100"),
	BPL("1101","0101"),
	BVS("1101","0110"),
	BVC("1101","0111"),
	BHI("1101","1000"),
	BLS("1101","1001"),
	BGE("1101","1010"),
	BLT("1101","1011"),
	BGT("1101","1100"),
	BLE("1101","1101"),
	B("1101","1110");
	
	String mnemonique;
	String opcode;
	
	ConditionalBranch(String mnemonique, String opcode){
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
