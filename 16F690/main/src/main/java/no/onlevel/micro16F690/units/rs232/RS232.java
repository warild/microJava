package no.onlevel.micro16F690.units.rs232;

import java.util.ArrayList;
import java.util.List;

import no.onlevel.micro16F690.code.variable.Register;
import no.onlevel.micro16F690.units.common.enums.Power;

public class RS232 {
	
	// set unit as either:
	// 1: async 8 bit
	// 2: async 8bit with addressing (RS-485)
	// 3: sync 8 bit SPI
	// IR or not

	// UNIT:
	// async communication
	// TXSTA,TXEN - 1 = enable TX-unit
	// TXSTA,SYNC - 0 = async
	// RCSTA,SPEN - 1 = TX-pin is set to output (you must disable ANSEL bit)
	// RCSTA,SPEN - 1 = RX-pin is set to input (you must disable ANSEL bit)
	
//	Initialize the SPBRGH, SPBRG register pair and
//	the BRGH and BRG16 bits to achieve the desired
//	baud rate
	// If 9-bit transmission is desired, set the TX9 control bit.
//	If 9-bit transmission is selected, the ninth bit
//	should be loaded into the TX9D data bit.
	
	// Run
//	Load 8-bit data into the TXREG register.
	
//	The EUSART supports 9-bit character reception. When
//	the RX9 bit of the RCSTA register is set the EUSART
//	will shift 9 bits into the RSR for each character
//	received. The RX9D bit of the RCSTA register is the
//	ninth and Most Significant data bit of the top unread
//	character in the receive FIFO.
	// kan adressere ved at når bit 9 er 1 så er innholdet en adresse, ellers er det data.
//	
//	Enable Receive
//	RCSTA,CREN = 1 Enable Rx
//	RCSTA,SYNC = 0 Async
//	RCSTA,SPEN = 1 Rx pin auto config (ANSEL must be disabled)
	
	// IR:
	// INTCON,GIE bit = h -> enabled global IR
	// INTCON,PEIE bit = h -> enabled peripheral IR
	// PIE1, RCIE - enable Rx
	// PIE1, TXIE - enable Tx
	// set TXIE before sending -check - clear after writing to TXREG.
	// Handle IR:
	// PIR1, RCIF - Rx flag, 1=buffer full. Cleared by reading (if 1 -> read)
	// PIR1, TXIF - Tx flag, 1=buffer is ready for writing.  (if 1 -> write)
	// TXREG bank0 - Data to transmit (19)
	// RCREG bank0 - Data to transmit (1A)
	
	// Status:
	// TXSTA,TRMT status: 0-while sending goes on 
	
//	The operation of the EUSART module is controlled
//	through three registers:
//	• Transmit Status and Control (TXSTA)
//	• Receive Status and Control (RCSTA)
//	• Baud Rate Control (BAUDCTL)
	
	private Power power;
	
	List<String> code;
	public RS232(List<String> code) {
		this.code = code;
	}

	final String TAB = "\t";
	

	public RS232 power(Power power) {
		this.power = power;
		return this;
	}

	public Init setup = new Init();	
	public class Init {
		private Power power = Power.Off;
		
//		public Init channel(Channel channel) {
//			this.channel = channel;
//			return this;
//		}
//		public Init convertTime(ConvertTime adFrequency) {
//			this.adFrequency = adFrequency;
//			return this;
//		}
//		public Init resultFormat(ResultFormat resultFormat) {
//			this.resultFormat = resultFormat;
//			return this;
//		}
//		public Init vref(Vref vref) {
//			this.voltageRef = vref;
//			return this;
//		}
//		public Init power(Power power) {
//			this.power = power;
//			return this;
//		}
		
		public List<String> getAssemblyCode() {
			List<String> code = new ArrayList<>();
			// Frequency
			code.add(";A/D converter unit");
			code.add(TAB + "; Frequencydivision to set Tad between 2-4 uS: _fff ____ ");
			code.add(TAB + "bsf STATUS,RP0 ; Bank: 01");
			code.add(TAB + "movlw 0x8f ; 1000 1111 Clear bits");
			code.add(TAB + "andwf ADCON1 ");
//			if (adFrequency == ConvertTime._2uS_8Mhz) {
//				code.add(TAB + "movlw 0x50 ; Tad: 2uS, 8MHz");
//			}
//			if (adFrequency == ConvertTime._4uS_8MHz) {
//				code.add(TAB + "movlw 0x20 ; Tad: 4uS, 8MHz");
//			}
//			code.add(TAB + "iorwf ADCON1");
//			code.add(TAB + "bcf STATUS,RP0 ; Bank: 00");
//			// Vref
//			if (voltageRef == Vref.Vdd) {
//				code.add(TAB + "bcf ADCON0,VCFG ; Vref = Vdd");
//			} else {
//				code.add(TAB + "bsf ADCON0,VCFG ; Vref = external, pin 2");
//			}
//			// Format
//			if (resultFormat == ResultFormat.H8_L2) {
//				code.add(TAB + "bcf ADCON0,ADFM ; res 8+2 ");
//			} else {
//				code.add(TAB + "bsf ADCON0,ADFM ; res 2+8 ");
//			}
//			// Power
			if (power == Power.On) {
				code.add(TAB + "bsf ADCON0,ADON ; module on");
			} else {
				code.add(TAB + "bcf ADCON0,ADON ; module off");
			}
			return code;
		}
	}
	
//	public RS232 startConversion() {
//		code.add("; AD converting channel " + channel.getChannel() + " (pin " + channel.getPin() +")");
//		code.add("	bsf ADCON0,GO");
//		return this;
//	}

	public RS232 awaitConversion() {
		code.add(TAB + "btfsc ADCON0,GO ; Skip if H (conversion finished)");
		code.add(TAB + "goto $-1");
		// " .ADIF ");
		return this;
	}


	private String to0x(int value) {
		if (Integer.toHexString(value).length() == 1) {
			return "0x0" + Integer.toHexString(value);
		} else {
			return "0x" + Integer.toHexString(value).toString();
		}
	}
}
