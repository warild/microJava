package no.onlevel.micro16F690.subroutines;

import java.util.List;

import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.code.variable.Variable16;

/**
 * Divisjon der forholdet mellom dividend og divisor er mellom 0 og 255
 *
 */
public class Div_16_16_8 {
	List<String> code;
	private final String SUBROUTINE_NAME = "DIV_16_16_8_SUB";

	private Variable16 inputA16;
	private Variable16 inputB16;
	private Variable outputR8;

	public Div_16_16_8(List<String> code) {
		this.code = code;
	}

	public void createCallCode(Variable16 a16, Variable16 b16, Variable result) {
		code.add("; Division: 16bit/16bit = 8bit");
		code.add("	movfw " + a16.registerH().getName());
		code.add("	movwf " + inputA16.registerH().getName());
		code.add("	movfw " + a16.registerL().getName());
		code.add("	movwf " + inputA16.registerL().getName());
		code.add("	movfw " + b16.registerH().getName());
		code.add("	movwf " + inputB16.registerH().getName());
		code.add("	movfw " + b16.registerL().getName());
		code.add("	movwf " + inputB16.registerL().getName());
		code.add("	call " + SUBROUTINE_NAME);
		code.add("	movfw " + outputR8.getRegister().getName());
		code.add("	movwf " + result.getRegister().getName());
	}

	public Setup with() {
		return new Setup(this, code);
	}

	public class Setup {
		private List<String> code;
		private Variable16 inputA16;
		private Variable16 inputB16;
		private Variable bitCounter;
		private Variable outputR8;
		private Div_16_16_8 call;

		public Setup(Div_16_16_8 call, List<String> code) {
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

		public Setup Result8_in(Variable outputR8) {
			this.call.outputR8 = outputR8;
			this.outputR8 = outputR8;
			return this;
		}

		public Div_16_16_8 buildCode() {
			String label = "DIV_16_16_8";
			String counter = bitCounter.getRegister().getName();
			String aL = inputA16.registerL().getName();
			String aH = inputA16.registerH().getName();
			String bL = inputB16.registerL().getName();
			String bH = inputB16.registerH().getName();
			String res = outputR8.getRegister().getName();
			code.add(SUBROUTINE_NAME);
			code.add("	clrf  " + res);
			code.add("	movlw 0x10  ; 16 bits");
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
			// res
			code.add("	rlf " + res + "	; C=1");
			code.add("	movfw " + bL);
			code.add("	subwf " + aL);
			code.add("	btfss STATUS,C	; Skip if C=1 (no borrow)");
			code.add("	decf " + aH + "	; minus 1 for minne");
			code.add("	movfw " + bH);
			code.add("	subwf " + aH);
			code.add("	goto " + label + "_SUB_END");
			code.add(label + "_SUB_FALSE");
			// res
			code.add("	rlf " + res + "	; C=0");
			code.add(label + "_SUB_END	");
			code.add("	bcf STATUS,C 	; Clear borrow");
			code.add("	rlf " + aL);
			code.add("	rlf " + aH);
			code.add("	decf " + counter);
			code.add("	goto " + label + "_LOOP");
			code.add(label + "_END");
			code.add("	return");
			code.add("");
			return new Div_16_16_8(code);
		}
	}
//		
//	private void div(Variable16 a16, Variable16 b16, Variable bitCounter, Variable result, String preLabel) {
//		String label = preLabel + "DIV_16_16_8";
//		String counter = bitCounter.getRegister().getName();
//		String aL = a16.registerL().getName();
//		String aH = a16.registerH().getName();
//		String bL = b16.registerL().getName();
//		String bH = b16.registerH().getName();
//		String res = result.getRegister().getName();
//		
//		code.add(";  Divider: 16bit / 16bit = 8bit");
//		// res -> 16 bit
//		code.add("	clrf  " + res);  			
//		code.add("	movlw 0x10  ; 16 bits"); 		
//		code.add("	movwf " + counter);
//		code.add(label + "_LOOP");
//		code.add("	movfw " + counter);
//		code.add("	btfsc STATUS,Z 	; Skip when Z=0 (counter > 0) and continue");
//		code.add("	goto " + label + "_END 	; division finished");
//		code.add(label + "_DIVIDE    		; if a >= b -> subtract "); 
//		code.add("	movfw "+ bH + "		; Subtract if (aH>bH) or (aH=bH & aL>=bL)");
//		code.add("	subwf "+ aH +",w");		
//		code.add("	btfss STATUS,C		; Skip if C=1 (no borrow) (aH >= bH)");
//		code.add("	goto " + label + "_SUB_FALSE	; no subtraction (aH<bH)");		
//		code.add("	btfss STATUS,Z  	; Skip if Z=1 (aH=bH)");
//		code.add("	goto " + label + "_SUB_TRUE	; (aH>bH)");
//		code.add("	movfw " + bL + "	; Subtract if (aL>=bL)");
//		code.add("	subwf " + aL + ",w");
//		code.add("	btfss STATUS,C  	; Skip if C=1 (no borrow) (aL>=bL)");
//		code.add("	goto " + label + "_SUB_FALSE	; no subtraction (aL<bL) & (aH=bH)");
//		code.add(label + "_SUB_TRUE	");	
//		// res
//		code.add("	rlf " + res + "	; C=1");
//		code.add("	movfw " + bL);
//		code.add("	subwf " + aL);
//		code.add("	btfss STATUS,C	; Skip if C=1 (no borrow)");
//		code.add("	decf " + aH + "	; minus 1 for minne");
//		code.add("	movfw " + bH);
//		code.add("	subwf " + aH);	
//		code.add("	goto " + label + "_SUB_END");
//		code.add(label + "_SUB_FALSE");
//		// res
//		code.add("	rlf " + res + "	; C=0");
//		code.add(label + "_SUB_END	");
//		code.add("	bcf STATUS,C 	; Clear borrow");
//		code.add("	rlf " + aL); 
//		code.add("	rlf " + aH);
//		code.add("	decf " + counter);
//		code.add("	goto " + label + "_LOOP");
//		code.add(label + "_END");
//	}

	protected String toHex(double value) {
		if (Double.toHexString(value).length() == 1) {
			return "0x0" + Double.toHexString(value);
		} else {
			return "0x" + Double.toHexString(value).toString();
		}
	}
}
