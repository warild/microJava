package no.onlevel.micro16F690.units.pins;

import java.util.ArrayList;
import java.util.List;

import no.onlevel.micro16F690.units.ad.AD.Setup;

public class Pins {
	private final String TAB = "\t";
	
	private int trisA = 0;
	private int trisB = 0;
	private int trisC = 0;
	private int anChL = 0;
	private int anChH = 0;
	private int irA = 0;
	private int irB = 0;
	private int pullUp;
	
	List<String> code;
	
	public Pins(List<String> code) {
		this.code = code;
	}
	
	public Setup setupWith() {
		return new Setup(code);
	}
	
	public class Setup {
		private List<String> code;
		
		private int trisA = 0;
		private int trisB = 0;
		private int trisC = 0;
		private int anChL = 0;
		private int anChH = 0;
		private int irA = 0;
		private int irB = 0;
		private int pullUp; // use??
		
		public Setup(List<String> code) {
			this.code = code;
		}
		
		// 0-1-2-3-4--5--6--7--
		// 1-2-4-8-16-32-64-128
		public Setup pin_2(Pin_2 pin2) {
			switch (pin2) {
			case in_digital_A5:
				trisA += 32;
				break;
			case in_digital_IrOnChange_A5:
				trisA += 32;
				irA +=32;
				break;			
			case out_digital_A5:
				trisA += 0;
				break;	
			default:
				break;
			}
			return this;
		}
	
		public Setup pin_3(Pin_3 pin3) {
			switch (pin3) {
			case in_digital_A4:
				trisA += 16;
				break;
			case in_digital_IrOnChange_A4:
				trisA += 16;
				irA +=16;
				break;						
			case out_digital_A4:
				trisA += 0;
				break;
			case in_analog_Ch_3:
				trisA += 16;
				anChL += 8;
				break;
			default:
				;
			}
			return this;
		}
	
		public Setup pin_4(Pin_4 pin4) {
			switch (pin4) {
			case in_digital_A3:
				trisA += 8;
				break;
			case in_digital_A3_IrOnChange:
				trisA += 8;
				irA +=8;
				break;						
			case in_digital_MasterClear:
				trisA += 8; // ???
				break;
			case in_ICSP_Programming_voltage:
				trisA += 8; // ???
				break;
			}
			return this;
		}
	
		public Setup pin_5(Pin_5 pin5) {
			switch (pin5) {
			case in_digital_C5:
				trisC += 32;
				break;
			case out_digital_C5:
				trisC += 0;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin_6(Pin_6 pin6) {
			switch (pin6) {
			case in_digital_C4:
				trisC += 16;
				break;
			case out_digital_C4:
				trisC += 0;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin_7(Pin_7 pin7) {
			switch (pin7) {
			case in_digital_C3:
				trisC += 8;
				break;
			case out_digital_C3:
				trisC += 0;
				break;
			case in_analog_Ch7:
				trisC += 8;
				anChL += 128;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin_8(Pin_8 pin8) {
			switch (pin8) {
			case in_digital_C6:
				trisC += 64;
				break;
			case out_digital_C6:
				trisC += 0;
				break;
			case in_analog_Ch6:
				trisC += 64;
				anChH += 1;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin_9(Pin_9 pin9) {
			switch (pin9) {
			case in_digital_C7:
				trisC += 128;
				break;
			case out_digital_C7:
				trisC += 0;
				break;
			case in_analog_Ch_9:
				trisC += 128;
				anChH += 2;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin10(Pin10 pin10) {
			switch (pin10) {
			case in_digital_B7:
				trisB += 128;
				break;
			case in_digital_B7_IrOnChange:
				trisB += 128;
				irB += 128;
				break;			
			case out_digital_B7:
				trisB += 0;
				break;
			case in_EUSART_ST_sync_clock:
				trisB += 128;
				break;
			case out_EUSART_async_Tx:
				trisB += 0;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin11(Pin11 pin11) {
			switch (pin11) {
			case in_digital_B6:
				trisB += 64;
				break;
			case in_digital_IrOnChange_B6:
				trisB += 64;
				irB += 64;
				break;			
			case out_digital_B6:
				trisB += 0;
				break;
			case IIC_clock:
				trisB += 64;
				break;
			case SPI_clock:
				trisB += 64;
				break;
			case out_digital_unused_orNot:
				// mulig å sette ubrukt pinne til 0?
			}
			return this;
		}
	
		public Setup pin12(Pin12 pin12) {
			switch (pin12) {
			case in_digital_B5:
				trisB += 32;
				break;
			case in_digital_B5_IrOnChange:
				trisB += 32;
				irB += 32;
				break;			
			case out_digital_B5:
				trisB += 0;
				break;
			case in_analog_Ch11:
				trisB += 8;
				anChH += 8;
				break;
			case in_EUSART_async_st_RX:
				trisB += 8;
				break;
			case in_EUSART_sync_st_Data:
				trisB += 8;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin13(Pin13 pin13) {
			switch (pin13) {
			case in_digital_B4:
				trisB += 16;
				break;
			case in_digital_B4_IrOnChange:
				trisB += 16;
				irB += 16;
				break;			
			case out_digital_B4:
				trisB += 0;
				break;
			case in_analog_Ch10:
				trisB += 16;
				anChH += 4;
				break;
			case in_out_IIC:
				trisB += 16;
				break;
			case in_SPI_data:
				trisB += 16;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin14(Pin14 pin14) {
			switch (pin14) {
			case in_digital_C2:
				trisC += 4;
				break;
			case out_digital_C2:
				trisC += 0;
				break;
			case in_analog_Ch6:
				trisC += 4;
				anChL += 64;
				break;
			case in_comparator12:
				trisC += 4;
				anChL += 64;
				break;
			case out_PWM:
				trisC += 4;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin15(Pin15 pin15) {
			switch (pin15) {
			case in_digital_C1:
				trisC += 2;
				break;
			case out_digital_C1:
				trisC += 0;
				break;
			case in_analog_Ch5:
				trisC += 2;
				anChL += 32;
				break;
			case in_comparator12:
				trisC += 2;
				break;
			}
			return this;
		}
	
		public Setup pin16(Pin16 pin16) {
			switch (pin16) {
			case in_digital_C0:
				trisC += 1;
				break;
			case out_digital_C0:
				trisC += 0;
				break;
			case in_analog_Ch4:
				trisC += 1;
				anChL += 16;
				break;
			case in_comparator2:
				trisC += 1;
				anChL += 16;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin17(Pin17 pin17) {
			switch (pin17) {
			case in_digital_A2:
				trisA += 4;
				break;
			case in_digital_A2_IrOnChange:
				trisA += 4;
				irA +=4;
				break;			
			case out_digital_A2:
				trisA += 0;
				break;
			case in_analog_Ch_2:
				trisA += 4;
				anChL += 4;
				break;
			case out_Comparator1:
				trisA += 0;
				break;
			case in_TIMER0_clock:
				trisA += 4;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin18(Pin18 pin18) {
			switch (pin18) {
			case in_digital_A1:
				trisA += 2;
				break;
			case in_digital_IrOnChange_A1:
				trisA += 2;
				irA +=2;
				break;						
			case out_digital_A1:
				trisA += 0;
				break;
			case in_analog_Ch_1:
				trisA += 2;
				anChL += 2;
				break;
			case in_analog_Vref:
				trisA += 2;
				anChL += 2;
				break;
			case in_ICSP_clock:
				trisA += 2;
				break;
			default:
				break;
			}
			return this;
		}
	
		public Setup pin19(Pin19 pin19) {
			switch (pin19) {
			case in_digital_A0:
				trisA += 1;
				break;
			case in_digital_A0_IrOnChange:
				trisA += 1;
				irA +=1;
				break;						
			case out_digital_A0:
				trisA += 0;
				break;
			case in_analog_Ch0:
				trisA += 1;
				anChL += 1;
				break;
	
			default:
				break;
			}
			return this;
		}
		
		public Pins buildCode() {
			// pin - channel: i/p - analog
			code.add(";Port direction - in(1) or out(0)");
			code.add(TAB + "clrf  PORTA");
			code.add(TAB + "clrf  PORTB");
			code.add(TAB + "clrf  PORTC");		
			code.add(TAB + "bsf   STATUS,RP0 ; Set bank: 01");
			code.add(TAB + "movlw " + to0x(trisA));
			code.add(TAB + "movwf TRISA");
			code.add(TAB + "movlw " + to0x(trisB));
			code.add(TAB + "movwf TRISB");
			code.add(TAB + "movlw " + to0x(trisC));
			code.add(TAB + "movwf TRISC");
			if (irA != 0) {			
				code.add(";Interrupt onChange portA (pin 0-5)");
				code.add(TAB + "movlw " + to0x(irA) );
				code.add(TAB + "movwf IOCA");
			}
			code.add(TAB + "bcf   STATUS,RP0 ; Set bank: 00");
			
			code.add(";Porttype - digital(0) or analog(1)");
			code.add(TAB + "bsf   STATUS,RP1 ; Set bank: 10");
			code.add(TAB + "movlw " + to0x(anChL));
			code.add(TAB + "movwf ANSEL");
			code.add(TAB + "movlw " + to0x(anChH));
			code.add(TAB + "movwf ANSELH");
			if (irB != 0) {			
				code.add("\n;Interrupt onChange portB (pin 4-7)");
				code.add(TAB + "movlw " + to0x(irB));
				code.add(TAB + "movwf IOCB");
			}		
			code.add(TAB + "bcf   STATUS,RP1 ; Set bank: 00");
			
			return new Pins(code);
		}

	}
	private String to0x(int value) {
		if (Integer.toHexString(value).length() == 1) {
			return "0x0" + Integer.toHexString(value);
		} else {
			return "0x" + Integer.toHexString(value).toString();
		}
	}
}
