package Assembleur;

import java.util.ArrayList;

public enum LoadStore {
	STR("100","10"),
	LDR("100","11");
	
	String mnemonique;
	String opcode;
	
	LoadStore(String mnemonique, String opcode){
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
