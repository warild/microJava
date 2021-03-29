package no.onlevel.micro16F690.units.interrupt;

import java.util.List;

import no.onlevel.micro16F690.code.variable.Variable;

public class Interrupt {
	final String TAB = "\t";
	List<String> code;
	Variable workTemp;
	Variable statusTemp;
	
	public Interrupt(List<String> code) {
		this.code = code;		
	}
		
	public void startWithPushToStack(Variable workTemp, Variable statusTemp) {
		this.workTemp = workTemp;
		this.statusTemp = statusTemp;
		code.add(TAB + "movwf " + workTemp.getRegister().getName());
		code.add(TAB + "movfw STATUS");
		code.add(TAB + "movwf " + statusTemp.getRegister().getName());
		code.add(TAB + "bcf	STATUS,RP0 ; Page: x0");
		code.add(TAB + "bcf	STATUS,RP1 ; Page: 0x");
	}
	public void start() {
	
	}
		
	public void endWithPopFromStack() {
		code.add(TAB + "movfw "  + statusTemp.getRegister().getName());
		code.add(TAB + "movwf STATUS");
		code.add(TAB + "movfw " + workTemp.getRegister().getName());			
	}
	
	public void call(String label) {
		code.add(TAB + "call " + label);		
	}

	public List<String> getAssemblyCode() {
		return code;		
	}
	
	public Setup setupWith() {
		return new Setup(code);
	}
	
	
	 // Only some interrupts are handled so far
	public class Setup {
		// IrFlags will be set anyway, but IrRoutine will only be called for enabled IRs
		// To enable IrRoutine:
		// INTCON.Gie - must be set
		// INTCON.PEie - must be set for peripherals
		List<String> code;
		
		private Timer0 timer0 = Timer0.disable;
		private Timer1 timer1 = Timer1.disable;
		private Timer2 timer2 = Timer2.disable;
		private AdDone adDone = AdDone.disable;
		private AbEdge abEdge = AbEdge.disable;
		private A2Edge a2edge = A2Edge.disable;
		private EusartRxDone eusartRxDone = EusartRxDone.disable;
		private EusartTxDone eusartTxDone = EusartTxDone.disable;
		private SSPDone sspDone = SSPDone.disable;
		private EEWriteDone eeWriteDone = EEWriteDone.disable;
		private boolean isIntcon = false;
		private boolean isPie1 = false;
		private boolean isPie2 = false;

		public Setup(List<String> code) {
			this.code = code;
		}
		
		// Timers
		// INTCON.T0ie
		public Setup timer0(Timer0 timer0) {
			this.timer0 = timer0;
			isIntcon = true;
			return this;
		}

		// PIE1.TMR1ie
		public Setup timer1(Timer1 timer1) {
			this.timer1 = timer1;
			isPie1 = true;
			return this;
		}

		// PIE1.TMR2ie
		public Setup timer2(Timer2 timer2) {
			this.timer2 = timer2;
			isPie1 = true;
			return this;
		}

		// --- Finished
		// PIE1.ADie
		public Setup adDone(AdDone adDone) {
			this.adDone = adDone;
			isPie1 = true;
			return this;
		}

		// PIE1.RCie - EUSART: Has received
		public Setup eusartRxDone(EusartRxDone eusartRxDone) {
			this.eusartRxDone = eusartRxDone;
			isPie1 = true;
			return this;
		}

		// PIE1.TXie - EUSART: Transmission done
		public Setup eusartTxDone(EusartTxDone eusartTxDone) {
			this.eusartTxDone = eusartTxDone;
			isPie1 = true;
			return this;
		}

		// PIE2.EEie EE: write done
		public Setup eeWriteDone(EEWriteDone eeWriteDone) {
			this.eeWriteDone = eeWriteDone;
			isPie2 = true;
			return this;
		}

		// PIE1.SSPie - Syncronous Serial port (done)
		public Setup sspDone(SSPDone sspDone) {
			this.sspDone = sspDone;
			isPie1 = true;
			return this;
		}

		// --- Edge
		// INTCON.INTie
		public Setup A2Edge(A2Edge a2Edge) {
			this.a2edge = a2Edge;
			isIntcon = true;
			return this;
		}

		// INTCON.RABie
		public Setup abEdge(AbEdge abEdge) {
			this.abEdge = abEdge;
			isIntcon = true;
			return this;
		}

		// unhandeled - so far
		// PIE1.CCPie - CCP?
		// PIE2.OSFie - oscillator fail (done)
		// PIE2.C2ie - comp2 (done)
		// PIE2.C1ie - comp1 (done)

		// FLAGS
		// INTCON.T0if
		// INTCON.INTf
		// INTCON.RABif

		// PIR1.x bit 7 -unused
		// PIR1.ADif
		// PIR1.RCif EUSART Receive
		// PIR1.TXif EUASRT transmit
		// PIR1.SSPif Sync Serial port
		// PIR1.CCPif Capture/compare/PWM
		// PIR1.TMR2if
		// PIR1.TMR1if
		//
		// IF-flag: PIR2.
		// PIR2.OSFif
		// PIR2.C2if
		// PIR2.C1if
		// PIR2.EEif
		public Interrupt buildCode() {
			// By default all interrupts are disabled
			if (isIntcon || isPie1 || isPie2) {
				code.add("\n; Config - Events that trigger the interruptroutine");
				// INTCON (any bank)
				if (a2edge.equals(A2Edge.enable)) {
					code.add("	bsf INTCON,INTE");
				}
				if (abEdge.equals(abEdge.enable)) {
					code.add("	movfw PORTA"); // clear condition
					code.add("	bsf INTCON,RABIF"); // clear flag
					code.add("	bsf INTCON,RABIE"); // enable IR
				}
				if (timer0.equals(Timer0.enable)) {
					code.add("	bsf INTCON,T0IF"); // clear flag
					code.add("	bsf INTCON,T0IE"); // counts from a value to FF.
				}
				if (isPie1 || isPie2) {
					code.add("	bsf INTCON,PEIE"); // Peripherals (Pie1 & Pie2)
					code.add("	bsf STATUS,RP0 ; Bank_01");
					if (timer1.equals(Timer1.enable)) {
						code.add("	bsf PIE1,TMR1IE");
					}
					if (timer2.equals(Timer2.enable)) {
						code.add("	bsf PIE1,TMR2IE	; Timer2 enabled");
					}
					if (adDone.equals(AdDone.enable)) {
						code.add("	bsf PIE1,ADIE");
					}
					if (eusartRxDone.equals(EusartRxDone.enable)) {
						code.add("	bsf PIE1,RCIE");
					}
					if (eusartTxDone.equals(EusartTxDone.enable)) {
						code.add("	bsf PIE1,TXIE");
					}
					if (sspDone.equals(SSPDone.enable)) {
						code.add("	bsf PIE1,SSPIE");
					}
					if (eeWriteDone.equals(EEWriteDone.enable)) {
						code.add("	bsf PIE2,EEIE");
					}
					code.add("	bcf STATUS,RP0 ; Bank_00");
				}
				code.add("	bsf INTCON,GIE");
			}
			
			return new Interrupt(code);
		}
	}
	
}
