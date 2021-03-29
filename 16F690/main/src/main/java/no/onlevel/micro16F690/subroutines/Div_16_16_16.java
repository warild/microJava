package no.onlevel.micro16F690.subroutines;

import java.util.List;

import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.code.variable.Variable16;

/**
 * Divisjon der forholdet mellom dividend og divisor er mellom 0 og 255
 *
 */
public class Div_16_16_16 {
	List<String> code;
	private final String SUBROUTINE_NAME = "DIV_16_16_16_SUB";

	private Variable16 inputA16;
	private Variable16 inputB16;
	private Variable16 outputR16;

	public Div_16_16_16(List<String> code) {
		this.code = code;
	}
	
	public void createCallCode(Variable16 a16, Variable16 b16, Variable16 result) {
		code.add("; Division: 16bit/16bit = 16bit");
		code.add("	movfw " + a16.registerH().getName());
		code.add("	movwf " + inputA16.registerH().getName());
		code.add("	movfw " + a16.registerL().getName());
		code.add("	movwf " + inputA16.registerL().getName());
		code.add("	movfw " + b16.registerH().getName());
		code.add("	movwf " + inputB16.registerH().getName());
		code.add("	movfw " + b16.registerL().getName());
		code.add("	movwf " + inputB16.registerL().getName());
		code.add("	call " + SUBROUTINE_NAME);
		code.add("	movfw " + outputR16.registerH().getName());
		code.add("	movwf " + result.registerH().getName());
		code.add("	movfw " + outputR16.registerL().getName());
		code.add("	movwf " + result.registerL().getName());
	}

	public Setup with() {
		return new Setup(this, code);
	}

	public class Setup {
		private List<String> code;
		private Variable16 inputA16;
		private Variable16 inputB16;
		private Variable bitCounter;
		private Variable16 outputR16;
		private Div_16_16_16 call;

		public Setup(Div_16_16_16 call, List<String> code) {
			this.code = code;
			this.call = call;
		}

		public Setup A16_in(Variable16 inputA16) {
			this.call.inputA16 = inputA16; 
			this.inputA16 = inputA16;
			return this;
		}

		public Setup B16_in(Variable16 inputB16) {
			this.call.inputB16 = inputB16;
			this.inputB16 = inputB16;
			return this;
		}

		public Setup bitCounter_in(Variable bitCounter) {
			this.bitCounter = bitCounter;
			return this;
		}

		public Setup R16_in(Variable16 outputR16) {
			this.call.outputR16 = outputR16;
			this.outputR16 = outputR16;
			return this;
		}

		public Div_16_16_16 buildCode() {
			String label = "DIV_16_16_16";
			code.add(SUBROUTINE_NAME);
			code.add(";  Divider: 16bit / 16bit =16bit");
			code.add("	clrf  " + outputR16.registerH().getName());
			code.add("	clrf  " + outputR16.registerL().getName());
			code.add("	movlw 0x08  ; 8 bits");                       // 8 bits????
			code.add("	movwf " + bitCounter.getRegister().getName());
			code.add(label + "_LOOP");
			code.add("	movfw " + bitCounter.getRegister().getName());
			code.add("	btfsc STATUS,Z 	; Skip when Z=0 (counter > 0) and continue");
			code.add("	goto " + label + "_END 	; division finished");
			code.add(label + "_DIVIDE    		; if a >= b -> subtract ");
			code.add("	movfw " + inputB16.registerH().getName() + "		; Subtract if (aH>bH) or (aH=bH & aL>=bL)");
			code.add("	subwf " + inputA16.registerH().getName() + ",w");
			code.add("	btfss STATUS,C		; Skip if C=1 (no borrow) (aH >= bH)");
			code.add("	goto " + label + "_SUB_FALSE	; no subtraction (aH<bH)");
			code.add("	btfss STATUS,Z  	; Skip if Z=1 (aH=bH)");
			code.add("	goto " + label + "_SUB_TRUE	; (aH>bH)");
			code.add("	movfw " + inputB16.registerL().getName() + "	; Subtract if (aL>=bL)");
			code.add("	subwf " + inputA16.registerL().getName() + ",w");
			code.add("	btfss STATUS,C  	; Skip if C=1 (no borrow) (aL>=bL)");
			code.add("	goto " + label + "_SUB_FALSE	; no subtraction (aL<bL) & (aH=bH)");
			code.add(label + "_SUB_TRUE	");
			code.add("	rlf " + outputR16.registerL().getName() + "	; C=1");
			code.add("	rlf " + outputR16.registerH().getName() + "	; C=1");
			code.add("	movfw " + inputB16.registerL().getName());
			code.add("	subwf " + inputA16.registerL().getName());
			code.add("	btfss STATUS,C	; Skip if C=1 (no borrow)");
			code.add("	decf " + inputA16.registerH().getName() + "	; minus 1 for minne");
			code.add("	movfw " + inputB16.registerH().getName());
			code.add("	subwf " + inputA16.registerH().getName());
			code.add("	goto " + label + "_SUB_END");
			code.add(label + "_SUB_FALSE");
			code.add("	rlf " + outputR16.registerL().getName() + "	; C=0");
			code.add("	rlf " + outputR16.registerH().getName() + "	; C=0");
			code.add(label + "_SUB_END	");
			code.add("	bcf STATUS,C 	; Clear borrow");
			code.add("	rlf " + inputA16.registerL().getName());
			code.add("	rlf " + inputA16.registerH().getName());
			code.add("	decf " + bitCounter.getRegister().getName());
			code.add("	goto " + label + "_LOOP");
			code.add(label + "_END");
			code.add("	return");
			code.add("");
			return new Div_16_16_16(code);
		}
	}


//	// Subroutine
//	public Div_16_16_16(List<String> code){
//		this.code = code;
//	}
//		
//	public Div_16_16_16 internallyForA16(Variable16 tempA16) {
//		this.usedForA16 = tempA16;
//		return this;
//	}	
//	public Div_16_16_16 internallyForB16(Variable16 tempB16) {
//		this.usedForB16 = tempB16;
//		return this;
//	}
//	public Div_16_16_16 internallyForBitCount(Variable tempBitCount) {
//		this.usedForBitCount = tempBitCount;
//		return this;
//	}
//	public Div_16_16_16 internallyForResult(Variable16 tempRes16) {
//		this.usedForResult16 = tempRes16;
//		return this;
//	}

//	public void createSubroutine() {
//		code.add("\n; ------------------------------------------");
//		code.add("; Subroutine DIV_16_16_8 ");
//		code.add("; ------------------------------------------");
//		code.add(SUBROUTINE_NAME);
//		String preLabel = "_";
////		div(usedForA16, usedForB16, usedForBitCount, usedForResult16, preLabel);
//		code.add("	return");
//	}

	private void div(Variable16 a16, Variable16 b16, Variable bitCounter, Variable16 result, String preLabel) {
		String label = preLabel + "DIV_16_16_8";
		String counter = bitCounter.getRegister().getName();
		String aL = a16.registerL().getName();
		String aH = a16.registerH().getName();
		String bL = b16.registerL().getName();
		String bH = b16.registerH().getName();
		String resH = result.registerH().getName();
		String resL = result.registerL().getName();

		code.add(";  Divider: 16bit / 16bit =16bit");
		code.add("	clrf  " + resH);
		code.add("	clrf  " + resL);
		code.add("	movlw 0x08  ; 8 bits");
		code.add("	movwf " + counter);
		code.add(label + "_LOOP");
		code.add("	movfw " + counter);
		code.add("	btfsc STATUS,Z 	; Skip when Z=0 (counter > 0) and continue");
		code.add("	goto " + label + "_END 	; division finished");
		code.add(label + "_DIVIDE    		; if a >= b -> subtract ");
		code.add("	movfw " + bH + "		; Subtract if (aH>bH) or (aH=bH & aL>=bL)");
		code.add("	subwf " + aH + ",w");
		code.add("	btfss STATUS,C		; Skip if C=1 (no borrow) (aH >= bH)");
		code.add("	goto " + label + "_SUB_FALSE	; no subtraction (aH<bH)");
		code.add("	btfss STATUS,Z  	; Skip if Z=1 (aH=bH)");
		code.add("	goto " + label + "_SUB_TRUE	; (aH>bH)");
		code.add("	movfw " + bL + "	; Subtract if (aL>=bL)");
		code.add("	subwf " + aL + ",w");
		code.add("	btfss STATUS,C  	; Skip if C=1 (no borrow) (aL>=bL)");
		code.add("	goto " + label + "_SUB_FALSE	; no subtraction (aL<bL) & (aH=bH)");
		code.add(label + "_SUB_TRUE	");
		code.add("	rlf " + resL + "	; C=1");
		code.add("	rlf " + resH + "	; C=1");
		code.add("	movfw " + bL);
		code.add("	subwf " + aL);
		code.add("	btfss STATUS,C	; Skip if C=1 (no borrow)");
		code.add("	decf " + aH + "	; minus 1 for minne");
		code.add("	movfw " + bH);
		code.add("	subwf " + aH);
		code.add("	goto " + label + "_SUB_END");
		code.add(label + "_SUB_FALSE");
		code.add("	rlf " + resL + "	; C=0");
		code.add("	rlf " + resH + "	; C=0");
		code.add(label + "_SUB_END	");
		code.add("	bcf STATUS,C 	; Clear borrow");
		code.add("	rlf " + aL);
		code.add("	rlf " + aH);
		code.add("	decf " + counter);
		code.add("	goto " + label + "_LOOP");
		code.add(label + "_END");
	}

	protected String toHex(double value) {
		if (Double.toHexString(value).length() == 1) {
			return "0x0" + Double.toHexString(value);
		} else {
			return "0x" + Double.toHexString(value).toString();
		}
	}
}
