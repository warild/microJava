package no.onlevel.micro16F690.subroutines;

import java.util.List;

import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.code.variable.Variable16;

public class Subroutine {
	private List<String> code;

	public Subroutine(List<String> code) {
		this.code = code;
	}

	private Mult_8_4_16 mult_A8_B4;
	private Div_16_16_16 div_16_16_16;
	private Div_16_16_8 div_16_16_8;
	private Div_16_8_8 div_16_8_8;

	public Call call = new Call();

	public class Call {

		// generate call-code
		public void multiply_A8_B4(Variable a8, Variable b4, Variable16 res16) {
			mult_A8_B4.createCallCode(a8, b4, res16);
		}

		public void div_A16_B16(Variable16 a16, Variable16 b16, Variable16 res16) {
			div_16_16_16.createCallCode(a16, b16, res16);
		}

		public void div_A16_B16(Variable16 a16, Variable16 b16, Variable res8) {
			div_16_16_8.createCallCode(a16, b16, res8);
		}

		public void div_A16_B8(Variable16 a16, Variable b8, Variable res8) {
			div_16_8_8.createCallCode(a16, b8, res8);
		}
	}

	// Generate subroutine code
	public Mult_8_4_16 multiply_A8_B4_R16() {
		this.mult_A8_B4 = new Mult_8_4_16(code);
		return mult_A8_B4;
	}

	public Div_16_16_16 div_A16_B16_R16() {
		this.div_16_16_16 = new Div_16_16_16(code);
		return div_16_16_16;
	}

	public Div_16_16_8 div_A16_B16_8() {
		this.div_16_16_8 = new Div_16_16_8(code);
		return div_16_16_8;
	}

	public Div_16_8_8 div_a16_b8_result8() {
		this.div_16_8_8 = new Div_16_8_8(code);
		return div_16_8_8;
	}

//	}
}
