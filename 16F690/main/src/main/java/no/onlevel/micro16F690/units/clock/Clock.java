package no.onlevel.micro16F690.units.clock;

import java.util.List;

public class Clock {
	final String TAB = "\t";
	
	private List<String> code;

	public Clock(List<String> code) {
		this.code = code;
	}

	public Setup setupWith() {
		return new Setup(code);
	}
	
	public class Setup {
		private List<String> code;
		private Frequency frequency = Frequency._1MHz;

		public Setup(List<String> code) {
			this.code = code;
		}

		public Setup frequency(Frequency frequency) {
			this.frequency = frequency;
			return this;
		}

		public final Clock buildCode() {
			code.add(";Clockfrequency: " + frequency);
			code.add(TAB + "bsf   STATUS,RP0 ; Set bank: 01");
			code.add(TAB + "bsf   OSCCON,0   ; bit0: 1->internal");
			switch (frequency) {
			case _8MHz:
				code.add(TAB + "movlw 0x70       ; 7->8 MHz, 1->internal");
				break;
			case _4MHz:
				code.add(TAB + "movlw 0x60       ; 6-> 4 MHz");
				break;
			case _2MHz:
				code.add(TAB + "movlw 0x50       ; 5-> 2 MHz");
				break;
			case _1MHz:
				code.add(TAB + "movlw 0x40       ; 4-> 1 MHz");
				break;
			case _500kHz:
				code.add(TAB + "movlw 0x30       ; 3-> 500 kHz");
				break;
			case _250kHz:
				code.add(TAB + "movlw 0x20       ; 2-> 250 kHz");
				break;
			case _125kHz:
				code.add(TAB + "movlw 0x10       ; 1-> 125 kHz");
				break;
			case eksternal_pin_2:
				code.add(TAB + "movlw 0x0?       ; 0-> eksternal pin2");
				break;
			}
			code.add(TAB + "xorwf OSCCON,w   ; all bits that differs ");
			code.add(TAB + "andlw 0x70       ; -xxx ---- selecting the 3 frequency-bits ");
			code.add(TAB + "xorwf OSCCON     ; altering frequency-bits");
			code.add(TAB + "bcf   STATUS,RP0 ; Set bank: 00");	
			return new Clock(code);
		}
	}

//	public List<String> getAssemblyCode() {
//		final String TAB = "\t";
//		List<String> code = new ArrayList<>();
//		code.add(";Clockfrequency: " + frequency);
//		code.add(TAB + "bsf   STATUS,RP0 ; Set bank: 01");
//		code.add(TAB + "bsf   OSCCON,0   ; bit0: 1->internal");
//		switch (frequency) {
//		case _8MHz:
//			code.add(TAB + "movlw 0x70       ; 7->8 MHz, 1->internal");
//			break;
//		case _4MHz:
//			code.add(TAB + "movlw 0x60       ; 6-> 4 MHz");
//			break;
//		case _2MHz:
//			code.add(TAB + "movlw 0x50       ; 5-> 2 MHz");
//			break;
//		case _1MHz:
//			code.add(TAB + "movlw 0x40       ; 4-> 1 MHz");
//			break;
//		case _500kHz:
//			code.add(TAB + "movlw 0x30       ; 3-> 500 kHz");
//			break;
//		case _250kHz:
//			code.add(TAB + "movlw 0x20       ; 2-> 250 kHz");
//			break;
//		case _125kHz:
//			code.add(TAB + "movlw 0x10       ; 1-> 125 kHz");
//			break;
//		case eksternal_pin_2:
//			code.add(TAB + "movlw 0x0?       ; 0-> eksternal pin2");
//			break;
//		}
//		code.add(TAB + "xorwf OSCCON,w   ; all bits that differs ");
//		code.add(TAB + "andlw 0x70       ; -xxx ---- selecting the 3 frequency-bits ");
//		code.add(TAB + "xorwf OSCCON     ; altering frequency-bits");
//		code.add(TAB + "bcf   STATUS,RP0 ; Set bank: 00");
//		return code;
//
//	}
}
