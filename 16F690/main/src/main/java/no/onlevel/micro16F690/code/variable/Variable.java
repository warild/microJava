package no.onlevel.micro16F690.code.variable;

import java.util.List;

import no.onlevel.micro16F690.subroutines.Subroutine;

public class Variable {
	List<String> code;
	private Register register;
	protected Subroutine subroutine;
	
	public Variable(String name, List<String> code, Subroutine subroutine){
		this.code = code;
		this.register = new Register(name); 
		this.subroutine = subroutine;
	}
		
	public Register getRegister(){
		return register;
	}
	
	// Set
	public Variable set(int k) {
		if (k == 0){
			code.add("	clrf  " + register.getName()); 			
		} else {
			code.add("	movlw " + toHex(k));
			code.add("	movwf " + register.getName());
		}
		return this;
	}
	
	public void set(Register otherRegister) {
		code.add("	movfw " + otherRegister.getName()); 
		code.add("	movwf " + register.getName());		
	}
	public void set(Variable v) {
		code.add("	movfw " + v.getRegister().getName()); 
		code.add("	movwf " + register.getName());		
	}
	
	// Add
	public void add(int k) {
		if (k == 1) {
			code.add("	incf " + register.getName());
		} else {
			code.add("	movlw " + toHex(k));
			code.add("	addwf " + register.getName());
		}
	}
	public Variable add(Variable var) {	
		code.add("	movfw " + var.getRegister().getName()); 
		code.add("	addwf " + register.getName()); 	
		return this;
	}	
	public void add(Variable a, Variable b) {	
		code.add("	movfw " + a.getRegister().getName());
		code.add("	addwf " + b.getRegister().getName() +",w");
		code.add("	movwf " + register.getName());
	}
	
	// Subtract
	public void sub(int k) {
		code.add("	movlw " + toHex(k));
		code.add("	subwf " + register.getName());
	}
	public void sub(Variable var) {	
		code.add("	movfw " + var.getRegister().getName()); 
		code.add("	subwf " + register.getName()); 		
	}
	public void sub(Variable a, Variable b) {	
		code.add("	movfw " + b.getRegister().getName());
		code.add("	subwf " + a.getRegister().getName() +",w");
		code.add("	movwf " + register.getName());
	}
	public void subIsNegThen(int k) {
		if (k == 0) {			
			code.add("	btfss STATUS,C   ; 1=no borrow. Skip to not_negative");
			code.add("	clrf  "+ register.getName() +" ; Replace neg value with " +k);
		} else { 
			code.add("	movlw " + toHex(k));
			code.add("	btfss STATUS,C   ; 1=no borow. Skip to not_negative");
			code.add("	movwf "+ register +" ; Replace neg value with " + k);
		}
	}
	// AND
	public void and(int k) {
		code.add("	movlw " + toHex(k));
		code.add("	andwf " + register.getName());		
	}
	public void and(Variable r) {	
		code.add("	movfw " + r.getRegister().getName()); 
		code.add("	andwf " + register.getName()); 		
	}	
	// OR
	public void or(int k) {
		code.add("	movlw " + toHex(k));
		code.add("	iorwf " + register.getName());		
	}
	public void or(Variable r) {	
		code.add("	movfw " + r.getRegister().getName()); 
		code.add("	iorwf " + register.getName()); 		
	}	
	// XOR
	public void xor(int k) {
		code.add("	movlw " + toHex(k));
		code.add("	xorwf " + register.getName());		
	}
	public void xor(Variable r) {	
		code.add("	movfw " + r.getRegister().getName()); 
		code.add("	xorwf " + register.getName()); 		
	}
	
	/*
	 * Subroutines - call
	 */
	public Call call = new Call(code, this);
	
	public class Call {
		private List<String> code;
		private Variable variable;
		public Call(List<String> code, Variable variable) {
			this.code = code;
			this.variable = variable;
		}

		public void divide_a16_b16(Variable16 a16, Variable16 b16) {
			subroutine.call.div_A16_B16(a16, b16, variable);	
		}
		public void divide_a16_b8(Variable16 a16, Variable b8) {
			subroutine.call.div_A16_B8(a16, b8, variable);	
		}
	}
	
	
	
	// 	------------
	public RegisterBit bit0 = new RegisterBit(0);
	public RegisterBit bit1 = new RegisterBit(1);
	public RegisterBit bit2 = new RegisterBit(2);
	public RegisterBit bit3 = new RegisterBit(3);
	public RegisterBit bit4 = new RegisterBit(4);
	public RegisterBit bit5 = new RegisterBit(5);
	public RegisterBit bit6 = new RegisterBit(6);
	public RegisterBit bit7 = new RegisterBit(7);
	
	protected String toHex(int value) {
		if (Integer.toHexString(value).length() == 1) {
			return "0x0" + Integer.toHexString(value).toString();
		} else {
			return "0x" + Integer.toHexString(value).toString();
		}
	}
}
