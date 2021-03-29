package no.onlevel.micro16F690.subroutines;

import java.util.List;

import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.code.variable.Variable16;

public class Mult_8_4_16 {
	private final String MULT_8_4_16_SUB = "MULT_8_4_16_SUB";
	private List<String> code;
	private Variable inputA8;
	private Variable inputB4;
	private Variable16 outputR16;
	
	public Mult_8_4_16(List<String> code) {
		this.code = code;
	}

	public void createCallCode(Variable a8, Variable b4, Variable16 result16) {
		code.add("; Multiplication: 8bit*4bit = 16bit");
		code.add("	movfw " + a8.getRegister().getName());
		code.add("	movwf " + inputA8.getRegister().getName());
		code.add("	movfw " + b4.getRegister().getName());
		code.add("	movwf " + inputB4.getRegister().getName());
		code.add("	call " + MULT_8_4_16_SUB);
		code.add("	movfw " + outputR16.registerH().getName());
		code.add("	movwf " + result16.registerH().getName());
		code.add("	movfw " + outputR16.registerL().getName());
		code.add("	movwf " + result16.registerL().getName());
	}

	public Setup with() {
		return new Setup(this, code);
	}

	public class Setup {
		private List<String> code;
		private Variable inputA8;
		private Variable inputB4;
		private Variable privateBitCounter;
		private Variable16 outputR16;		
		private Mult_8_4_16 mult;
		
		public Setup(Mult_8_4_16 mult, List<String> code) {
			this.mult = mult;
			this.code = code;
		}

		public Setup A8_in(Variable inputA8) {
			this.inputA8 = inputA8;
			this.mult.inputA8 = inputA8;			
			return this;
		}

		public Setup B4_in(Variable inputB4) {
			this.inputB4 = inputB4;
			this.mult.inputB4 = inputB4;		
			return this;
		}

		public Setup internalBitCounter_in(Variable privateBitCounter) {
			this.privateBitCounter = privateBitCounter;
			return this;
		}

		public Setup Result16_in(Variable16 outputR16) {
			this.outputR16 = outputR16;
			this.mult.outputR16 = outputR16;	
			return this;
		}

		public final Mult_8_4_16 buildCode() {
			String subName = "MULT_8_4_16";
			code.add(MULT_8_4_16_SUB);
			code.add("	clrf " + outputR16.registerH().getName());
			code.add("	clrf " + outputR16.registerL().getName());
			code.add("	movlw 0x04 ; 4 bits");
			code.add("	movwf " + privateBitCounter.getRegister().getName());
			code.add(subName + "_LOOP");
			code.add("	movfw " + privateBitCounter.getRegister().getName());
			code.add("	btfsc STATUS,Z    ; 0 = not zero: Skip to continue adding");
			code.add("	goto " + subName + "_LOOP_END ; multiplication is finished");
			code.add(subName + "_ADD");
			code.add("	bcf STATUS,C");
			code.add("	rrf  " + inputB4.getRegister().getName());
			code.add("	btfss STATUS,C    ; Skip when C=1");
			code.add("	goto " + subName + "_SHIFT");
			code.add(subName + "_ADD_TRUE");
			code.add("	movfw " + inputA8.getRegister().getName());
			code.add("	addwf " + outputR16.registerH().getName());
			code.add(subName + "_SHIFT");
			code.add("	rrf  " + outputR16.registerH().getName());
			code.add("	rrf  " + outputR16.registerL().getName());
			code.add("	decf " + privateBitCounter.getRegister().getName());
			code.add("	goto " + subName + "_LOOP");
			code.add(subName + "_LOOP_END");
			code.add("	bcf STATUS,C");
			code.add("	rrf  " + outputR16.registerH().getName());
			code.add("	rrf  " + outputR16.registerL().getName());
			code.add("	rrf  " + outputR16.registerH().getName());
			code.add("	rrf  " + outputR16.registerL().getName());
			code.add("	rrf  " + outputR16.registerH().getName());
			code.add("	rrf  " + outputR16.registerL().getName());
			code.add("	rrf  " + outputR16.registerH().getName());
			code.add("	rrf  " + outputR16.registerL().getName());
			code.add("	return");
			code.add("");

			return new Mult_8_4_16(code);
		}


	}

	protected String toHex(double value) {
		if (Double.toHexString(value).length() == 1) {
			return "0x0" + Double.toHexString(value);
		} else {
			return "0x" + Double.toHexString(value).toString();
		}
	}
}
