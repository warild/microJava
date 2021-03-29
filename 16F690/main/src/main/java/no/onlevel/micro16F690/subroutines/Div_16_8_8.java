package no.onlevel.micro16F690.subroutines;

import java.util.List;

import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.code.variable.Variable16;

/**
 * Divisjon for å feks Beregne snitt.
 *
 */
public class Div_16_8_8 {
	private final String SUBROUTINE_NAME = "DIV_16_8_8_SUB";
	List<String> code;
	private Variable16 inputA16;
	private Variable inputB8;
	private Variable outputR8;

	public Div_16_8_8(List<String> code) {
		this.code = code;
	}

	public void createCallCode(Variable16 a16, Variable b8, Variable result) {
		code.add("; Division; 16bit/8bit = 8bit");
		code.add("	movfw " + a16.registerH().getName());
		code.add("	movwf " + inputA16.registerH().getName());
		code.add("	movfw " + a16.registerL().getName());
		code.add("	movwf " + inputA16.registerL().getName());
		code.add("	movfw " + b8.getRegister().getName());
		code.add("	movwf " + inputB8.getRegister().getName());
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
		private Variable inputB8;
		private Variable bitCounter;
		private Variable outputR8;
		private Div_16_8_8 call;

		public Setup(Div_16_8_8 call, List<String> code) {
			this.code = code;
			this.call = call;
		}

		public Setup a16_in(Variable16 inputA16) {
			this.call.inputA16 = inputA16;
			this.inputA16 = inputA16;
			return this;
		}

		public Setup b8_in(Variable inputB8) {
			this.call.inputB8 = inputB8;
			this.inputB8 = inputB8;
			return this;
		}

		public Setup bitCounter_in(Variable bitCounter) {
			this.bitCounter = bitCounter;
			return this;
		}

		public Setup result8_in(Variable outputR8) {
			this.call.outputR8 = outputR8;
			this.outputR8 = outputR8;
			return this;
		}

		public Div_16_8_8 buildCode() {
			String label = "DIV_16_8_8";
			code.add(SUBROUTINE_NAME);
			code.add("	movlw 0x08");
			code.add("	movwf " + bitCounter.getRegister().getName());
			code.add("	clrf  " + outputR8.getRegister().getName());
			code.add(label + "_LOOP");
			code.add("	movfw " + bitCounter.getRegister().getName());
			code.add("	btfsc STATUS,Z ; not Zero: Skip to continue");
			code.add("	goto " + label + "_LOOP_END"); // return if subroutine
			code.add("	bcf STATUS,C");
			code.add("	rlf  " + inputA16.registerL().getName());
			code.add("	rlf  " + inputA16.registerH().getName());
			code.add("	movfw " + inputB8.getRegister().getName());
			code.add("	subwf " + inputA16.registerH().getName() + ",w");
			code.add("	btfss STATUS,C ; Skip when C=1");
			code.add("	goto " + label + "_CARRY_FALSE");
			code.add(label + "_CARRY_TRUE");
			code.add("	rlf  " + outputR8.getRegister().getName() + "; <- C=1");
			code.add("	movfw " + inputB8.getRegister().getName());
			code.add("	subwf " + inputA16.registerH().getName());
			code.add("	decf " + bitCounter.getRegister().getName());
			code.add("	goto " + label + "_LOOP");
			code.add(label + "_CARRY_FALSE");
			code.add("	rlf  " + outputR8.getRegister().getName() + "; <- C=0");
			code.add("	decf " + bitCounter.getRegister().getName());
			code.add("	goto " + label + "_LOOP");
			code.add(label + "_LOOP_END"); // not needed if sub
			code.add("	return");
			code.add("");
			return new Div_16_8_8(code);
		}
	}

//	private void div(Variable16 a16, Variable b8, Variable bitCounter, Variable result, String preLabel) {
//		String subLabel = preLabel + "DIV_16_8_8";
//		code.add("	movlw 0x08");
//		code.add("	movwf " + bitCounter.getRegister().getName());
//		code.add("	clrf  " + result.getRegister().getName());
//		code.add(subLabel + "_LOOP");
//		code.add("	movfw " + bitCounter.getRegister().getName());
//		code.add("	btfsc STATUS,Z ; not Zero: Skip to continue");
//		code.add("	goto " + subLabel + "_LOOP_END"); // return if subroutine
//		code.add("	bcf STATUS,C");
//		code.add("	rlf  " + a16.registerL().getName());
//		code.add("	rlf  " + a16.registerH().getName());
//		code.add("	movfw " + b8.getRegister().getName());
//		code.add("	subwf " + a16.registerH().getName() + ",w");
//		code.add("	btfss STATUS,C ; Skip when C=1");
//		code.add("	goto " + subLabel + "_CARRY_FALSE");
//		code.add(subLabel + "_CARRY_TRUE");
//		code.add("	rlf  " + result.getRegister().getName() + "; <- C=1");
//		code.add("	movfw " + b8.getRegister().getName());
//		code.add("	subwf " + a16.registerH().getName());
//		code.add("	decf " + bitCounter.getRegister().getName());
//		code.add("	goto " + subLabel + "_LOOP");
//		code.add(subLabel + "_CARRY_FALSE");
//		code.add("	rlf  " + result.getRegister().getName() + "; <- C=0");
//		code.add("	decf " + bitCounter.getRegister().getName());
//		code.add("	goto " + subLabel + "_LOOP");
//		code.add(subLabel + "_LOOP_END"); // not needed if sub
//	}

	protected String toHex(double value) {
		if (Double.toHexString(value).length() == 1) {
			return "0x0" + Double.toHexString(value);
		} else {
			return "0x" + Double.toHexString(value).toString();
		}
	}
}