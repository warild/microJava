package no.onlevel.micro16F690.code.logic;

import java.util.List;

import no.onlevel.micro16F690.Micro16f690;
import no.onlevel.micro16F690.code.variable.Variable;

public class BlockIf {

	private List<String> code;
	private Variable variable;
	private String blockName;
	
	public BlockIf(String blockName, List<String> code) {
		this.blockName = blockName;
		this.code = code;
	}
	
	public BlockIf IF(Variable variable) {	
		this.variable = variable;
		return this;
	}
	
	public void isBiggerThan(Variable r){ 
		// When variable is bigger than b do. 
		// When v-b is positive do skip into.
		// When v-b -> borrow: C=1 do skip.  
		code.add("	movfw " + variable.getRegister().getName());		
		code.add("	subwf " + r.getRegister().getName() + ",w"); // B-A: When A is bigger the result is negative (borrowed: C=0) 
		code.add("	btfss STATUS,C    ; Skip into block. No borrow (C=1). " + variable.getRegister().getName() + " is bigger");
		code.add("	goto " + blockName + "_END");
	}
	
	public void equals(int i){
		code.add("	movfw " + variable.getRegister().getName());
		code.add("	xorlw " + Micro16f690.toHex(i) +",w");
		code.add("	btfss STATUS,Z   ; Skip when equals");
		code.add("	goto " + blockName + "_END");
	}
	
	public void equals(Variable r) {
		code.add("	movfw " + variable.getRegister().getName());
		code.add("	xorwf " + r.getRegister().getName() +",w");
		code.add("	btfss STATUS,Z   ; Skip when equals"); 
		code.add("	goto " + blockName + "_END");
	}
	
	public void END() {
		code.add(blockName + "_END");
	}
	
}
