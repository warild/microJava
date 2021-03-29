package no.onlevel.micro16F690.code.logic;

import java.util.List;

import no.onlevel.micro16F690.Micro16f690;
import no.onlevel.micro16F690.code.variable.Variable;

public class BlockRepeat {

	private List<String> code;
	private Variable variable;
	private String blockName;

	public BlockRepeat(String blockName, List<String> code) {
		this.blockName = blockName;
		this.code = code;
	}

	public BlockRepeat If(Variable variable) {
		this.variable = variable;
		return this;
	}

	public void isBiggerThan(Variable b) {
		// When variable is bigger than b do repeat. 
		// When v-b is positive do not skip.
		// When v-b is negative do skip.
		// When v-b -> borrow: C=0 do skip.  
		code.add("	movfw " + variable.getRegister().getName());
		code.add("	subwf " + b.getRegister().getName() + ",w"); 
		code.add("	btfsc STATUS,C   ; Skip if C=0. Leave repeat block. " + variable.getRegister().getName() + " is bigger");
		code.add("	goto " + blockName + "_START");
	}

	public void equals(int i){
		code.add("	movfw " + variable.getRegister().getName());
		code.add("	xorlw " + Micro16f690.toHex(i) +",w");
		code.add("	btfsc STATUS,Z   ; Skip when unequals");
		code.add("	goto " + blockName + "_START");
	}
	
	public void equals(Variable r) {
		code.add("	movfw " + variable.getRegister().getName());
		code.add("	xorwf " + r.getRegister().getName() +",w");
		code.add("	btfsc STATUS,Z   ; Skip when unequals"); 
		code.add("	goto " + blockName + "_START");
	}
	
	public void START() {
		code.add("	" + blockName + "_START");
	}
	
	public void END() {
		code.add(blockName + "_END");
	}

}
