package no.onlevel.micro16F690.code.logic;

import java.util.List;

import no.onlevel.micro16F690.Micro16f690;
import no.onlevel.micro16F690.code.variable.Variable;

public class BlockWhile {

	private List<String> code;
	private Variable variable;
	private String blockName;

	public BlockWhile(String blockName, List<String> code) {
		this.blockName = blockName;
		this.code = code;
	}

	public BlockWhile If(Variable variable) {
		this.variable = variable;
		return this;
	}

	public void isBiggerThan(Variable b) {
		// When variable is bigger than b do loop. 
		// When v-b is positive do skip into loop.
		// When v-b -> borrow: C=1 do skip.  
		code.add("	" + blockName + "_START");
		code.add("	movfw " + variable.getRegister().getName());
		code.add("	subwf " + b.getRegister().getName() + ",w"); 
		code.add("	btfss STATUS,C   ; Skip into loop when C=1 (no borrow). " + variable.getRegister().getName() + " is bigger");
		code.add("	goto " + blockName + "_END");
	}

	public void equals(int i){
		code.add("	movfw " + variable.getRegister().getName());
		code.add("	xorlw " + Micro16f690.toHex(i) +",w");
		code.add("	btfss STATUS,Z   ; Skip when unequals");
		code.add("	goto " + blockName + "_END");
	}
	
	public void equals(Variable r) {
		code.add("	movfw " + variable.getRegister().getName());
		code.add("	xorwf " + r.getRegister().getName() +",w");
		code.add("	btfss STATUS,Z   ; Skip when unequals"); 
		code.add("	goto " + blockName + "_END");
	}
	
	public void END() {
		code.add("	goto " + blockName + "_START");
		code.add(blockName + "_END");
	}

}
