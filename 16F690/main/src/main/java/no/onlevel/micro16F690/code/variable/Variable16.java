package no.onlevel.micro16F690.code.variable;

import java.util.List;

import no.onlevel.micro16F690.subroutines.Subroutine;

public class Variable16 {

	protected List<String> code;
	protected Subroutine subroutine;

	private Register registerL;
	private Register registerH;

	public Variable16(String name, List<String> code, Subroutine subroutines) {
		this.code = code;
		this.registerL = new Register(name + "_L");
		this.registerH = new Register(name + "_H");
		this.subroutine = subroutines;
	}

	public Variable16(String nameH, String nameL, List<String> code) {
		this.code = code;
		this.registerL = new Register(nameL);
		this.registerH = new Register(nameH);
	}

	public Register registerL() {
		return registerL;
	}

	public Register registerH() {
		return registerH;
	}

	public void clear() {
		code.add("	clrf " + registerL.getName());
		code.add("	clrf " + registerH.getName());
	}

	public void set(int rH, int rL) {
		code.add("	movlw " + toHex(rH));
		code.add("	movwf " + registerH.getName());
		code.add("	movlw " + toHex(rL));
		code.add("	movwf " + registerL.getName());
	}

	public void set(int rH, Variable r) {
		if (rH == 0) {
			code.add("	clrf " + registerH.getName());
		} else {
			code.add("	movlw " + toHex(rH));
			code.add("	movwf " + registerH.getName());
		}
		code.add("	movfw " + r.getRegister().getName());
		code.add("	movwf " + registerL.getName());
	}

	public void set(Variable rH, Variable rL) {
		code.add("	movfw " + rH.getRegister().getName());
		code.add("	movwf " + registerH.getName());
		code.add("	movlw " + rL.getRegister().getName());
		code.add("	movwf " + registerL.getName());
	}

	public void set(Variable16 r16) {
		code.add("	movfw " + r16.registerH().getName());
		code.add("	movwf " + registerH.getName());
		code.add("	movfw " + r16.registerL.getName());
		code.add("	movwf " + registerL.getName());
	}

	public Variable16 add(Variable r) {
		code.add("	movfw " + r.getRegister().getName());
		code.add("	addwf " + registerL.getName());
		code.add("	btfss STATUS,C  ; skip if C=0");
		code.add("	incf  " + registerH.getName());
		return this;
	}

	public void add(Variable16 a) {
		code.add("	movfw " + a.registerL.getName());
		code.add("	addwf " + registerL.getName());
		code.add("	btfss STATUS,C  ; skip if C=0");
		code.add("	incf  " + registerH.getName());
		code.add("	movfw " + a.registerH.getName());
		code.add("	addwf " + registerH.getName());
	}

	public void sub(Variable rL) {
		code.add("	movfw " + rL.getRegister().getName());
		code.add("	subwf " + registerL.getName());
		code.add("	btfss STATUS,C  ; skip if C=1");
		code.add("	decf  " + registerH.getName());
	}

	public void sub(Variable16 r) {
		// Må bruke 2s komplement(?) Subtract er dss add med 2s komplement, men begge
		// bytene vil få lagt til 1. må trekke fra 1 for byte to: Borow: trekk fra 1 -
		// Ikke borrow trekk fra 0
		code.add("	movfw " + r.registerL.getName());
		code.add("	subwf " + registerL.getName());
		code.add("	btfsc STATUS,C  ; If no borrow, subtract 1 (due to 2s complement for H-byte)");
		code.add("	decf  " + registerH.getName()); // 0 -> -1 if nameH==0 -> sub/dec else: dec/sub
		code.add("	movfw " + r.registerH.getName());
		code.add("	subwf " + registerH.getName()); // Problemet er at borrow kan bli satt av dec og ikke her. Da blir
													// borrow false, men skulle ha vært true.
	}

	public void shiftLeft(int positions) {
		for (int posNr = 0; posNr < positions; posNr++) {
			code.add("	bcf   STATUS,C");
			code.add("	rlf " + registerL.getName());
			code.add("	rlf " + registerH.getName());
		}
	}

	public void shiftRight(int positions) {
		for (int posNr = 0; posNr < positions; posNr++) {
			code.add("	bcf   STATUS,C");
			code.add("	rrf " + registerL.getName());
			code.add("	rrf " + registerH.getName());
		}
	}

	/*
	 * Subroutines
	 */
	public Call call = new Call(code, this);
	
	public class Call {
		private List<String> code;
		private Variable16 variable16;
		public Call(List<String> code, Variable16 variable16) {
			this.code = code;
			this.variable16 = variable16;
		}

		public void multiply_a8_b4(Variable a8, Variable b4) {
			subroutine.call.multiply_A8_B4(a8, b4, variable16);
		}

		public void divide_a16_b16(Variable16 a16, Variable16 b16) {
			subroutine.call.div_A16_B16(a16, b16, variable16);
		}
	}

	// ------------
	protected String toHex(int value) {
		if (Integer.toHexString(value).length() == 1) {
			return "0x0" + Integer.toHexString(value).toString();
		} else {
			return "0x" + Integer.toHexString(value).toString();
		}
	}

	public RegisterBit bit0 = new RegisterBit(0);
	public RegisterBit bit1 = new RegisterBit(1);
	public RegisterBit bit2 = new RegisterBit(2);
	public RegisterBit bit3 = new RegisterBit(3);
	public RegisterBit bit4 = new RegisterBit(4);
	public RegisterBit bit5 = new RegisterBit(5);
	public RegisterBit bit6 = new RegisterBit(6);
	public RegisterBit bit7 = new RegisterBit(7);

}
