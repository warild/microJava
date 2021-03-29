package no.onlevel.micro16F690.units.timer2;

import java.util.ArrayList;
import java.util.List;

import no.onlevel.micro16F690.units.common.enums.Power;

public class Timer2 {
	private final String TAB = "\t";
	private List<String> code;
	private Setup setup ;

	public Timer2(List<String> code) {
		this.code = code;
	}

	public void handleIfIr() {
		code.add("IR_TIMER2_START");
		code.add("	btfss	PIR1,TMR2IF ;  If H, skip next, and IR is handeled ");
		code.add("	goto IR_TIMER2_END");
	}

	public void handleIrEnd() {
		code.add("IR_TIMER2_END");
	}

	public Timer2 set(PreCounter prescaler) {
		code.add("tbd");
		return this;
	}

	public Timer2 set(PostCounter postscaler) {
		code.add("tbd");
		return this;
	}

	public Timer2 setCounter(int counter) { // enum(255)
		code.add("; Timer2 - setCounter tbd");
		return this;
	}

	public Timer2 set(Power power) {
		code.add("tbd");
		return this;
	}

	// ----- init ----------------
	public Setup setupWith() {
		setup = new Setup();
		return setup; 
	}

	/**
	 * Counting period: preCounter * counter * postCounter * 4 / osc [seconds]
	 */
	public class Setup {
		private PreCounter preCounter = PreCounter.x1; // x1, x4, x16
		private int counter = 0; // x0-x255
		private PostCounter postCounter = PostCounter.x1; // x1, ... , x16
		private Power power = Power.Off;

		public Setup preCounter(PreCounter preCounter) {
			this.preCounter = preCounter;
			return this;
		}

		public Setup counter(int counter) {
			if (counter > 255) {
				// error
			}
			this.counter = counter;
			return this;
		}

		public Setup postCounter(PostCounter postCounter) {
			this.postCounter = postCounter;
			return this;
		}

		public Setup power(Power power) {
			this.power = power;
			return this;
		}

		public Timer2 buildCode() {
			// T2CON ; ---- -X-- on/off
			// T2CON ; ---- --XX prescaler 00/01/1x
			// T2CON ; -XXX X--- postscaler
			// PR2 xxxx xxxx TMR2 is increased and reset to zero after PR2 = TMR2
			// ; (Oscillator -> Fosc/4): Freq = 8MHz/4/<pre>/<count>/<post> = 8/4/1/100/2 ->
			// 100uS interval
			// Interrupt: PIR1,TMR2IF
		
			int timer2Setup = preCounter.getValue() + (postCounter.getValue() * 8) + power.getValue() * 4;
			code.add("\n;Timer 2:");
			code.add(TAB + "; Count = 4 x preCounter * counter * postcounter. Time = Count/osc");
			code.add(TAB + "; E.g: time=4*1*100*2/8Mhz -> 800/8 000 000 -> 100uS");
			code.add(TAB + "; Setup -ppp pOrr  p=postscaler O=On, r=prescaler");
			code.add(TAB + "movlw " + to0x(timer2Setup) + "; ");
			code.add(TAB + "movwf T2CON");
			code.add(TAB + "bsf   STATUS,RP0  ; Set bank: 01");
			code.add(TAB + "movlw " + to0x(counter) + "; = " + counter);
			code.add(TAB + "movwf PR2");
			code.add(TAB + "bcf   STATUS,RP0  ; set bank: 00");
			code.add(TAB + "bcf   PIR1,TMR2IF ; Clear IR-flag");
			return new Timer2(code);
		}
		
//		public List<String> getAssemblyCode() {
//			// T2CON ; ---- -X-- on/off
//			// T2CON ; ---- --XX prescaler 00/01/1x
//			// T2CON ; -XXX X--- postscaler
//			// PR2 xxxx xxxx TMR2 is increased and reset to zero after PR2 = TMR2
//			// ; (Oscillator -> Fosc/4): Freq = 8MHz/4/<pre>/<count>/<post> = 8/4/1/100/2 ->
//			// 100uS interval
//			// Interrupt: PIR1,TMR2IF
//			List<String> initCode = new ArrayList<>();
//			int timer2Setup = preCounter.getValue() + (postCounter.getValue() * 8) + power.getValue() * 4;
//			initCode.add("\n;Timer 2:");
//			initCode.add(TAB + "; Count = 4 x preCounter * counter * postcounter. Time = Count/osc");
//			initCode.add(TAB + "; E.g: time=4*1*100*2/8Mhz -> 800/8 000 000 -> 100uS");
//			initCode.add(TAB + "; Setup -ppp pOrr  p=postscaler O=On, r=prescaler");
//			initCode.add(TAB + "movlw " + to0x(timer2Setup) + "; ");
//			initCode.add(TAB + "movwf T2CON");
//			initCode.add(TAB + "bsf   STATUS,RP0  ; Set bank: 01");
//			initCode.add(TAB + "movlw " + to0x(counter) + "; = " + counter);
//			initCode.add(TAB + "movwf PR2");
//			initCode.add(TAB + "bcf   STATUS,RP0  ; set bank: 00");
//			initCode.add(TAB + "bcf   PIR1,TMR2IF ; Clear IR-flag");
//			return initCode;
//		}
	}

	public void start() {
		code.add("\n; Timer2 - start");
		code.add(TAB + "bsf T2CON,TMR2ON");
	}

//	public void enableInterrupt() {
//		// Bit PEIE of the INTCON register must be set to enable any peripheral
//		// interrupt.
//		// factory.instruction("bsf INTCON,PEIE ; Enable IR from units");
//		// clear interrup before????
//		code.add(TAB + "bsf     STATUS,RP0  ; Set bank: 01");
//		code.add(TAB + "bsf   PIE1,TMR2IE ; Enable IR from Timer2");
//		code.add(TAB + "bcf     STATUS,RP0  ; Set bank: 00");
//	}

	public void waitForIRFlag(String label) {
		code.add(label);
		code.add(TAB + "btfss PIR1,TMR2IF ; Skip when IR");
		code.add(TAB + "goto " + label);
	}

	public void clearIrFlag() {
		code.add(TAB + "bcf  PIR1,TMR2IF  ; Clear Timer2 IR-flag");
	}

	private String to0x(int value) {
		if (Integer.toHexString(value).length() == 1) {
			return "0x0" + Integer.toHexString(value);
		} else {
			return "0x" + Integer.toHexString(value).toString();
		}
	}

}
