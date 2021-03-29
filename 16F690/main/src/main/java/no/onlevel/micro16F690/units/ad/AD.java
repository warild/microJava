package no.onlevel.micro16F690.units.ad;

import java.util.ArrayList;
import java.util.List;

import no.onlevel.micro16F690.code.variable.Register;
import no.onlevel.micro16F690.units.clock.Clock;
import no.onlevel.micro16F690.units.clock.Clock.Setup;
import no.onlevel.micro16F690.units.common.enums.Power;

public class AD {
	// Values that will be used when the AD is to be setup
	private Channel channel;
	private ResultFormat resultFormat;
	private Vref voltageRef;
	private ConvertTime adFrequency; // 4 el 2 for 8Mhz
	private Power module;
	
	List<String> code;
	public AD(List<String> code) {
		this.code = code;
	}

	final String TAB = "\t";
	
	public AD convertTime(ConvertTime adFrequency) {
		this.adFrequency = adFrequency;
		return this;
	}
	public AD resultFormat(ResultFormat resultFormat) {
		this.resultFormat = resultFormat;
		return this;
	}
	public AD vref(Vref vref) {
		this.voltageRef = vref;
		return this;
	}
	public AD power(Power power) {
		this.module = power;
		return this;
	}
	public AD channel(Channel channel) {
		this.channel = channel;
		return this;
	}
	
	
	public Setup setupWith() {
		return new Setup(code);
	}
	
	public class Setup {
		List<String> code;
		
		private Channel channel = Channel.Ch1_pin18;
		private ResultFormat resultFormat = ResultFormat.H8_L2;
		private Vref voltageRef = Vref.Vdd;
		private ConvertTime adFrequency = ConvertTime._2uS_8Mhz; // 4 el 2 for 8Mhz
		private Power power = Power.Off;
		
		public Setup(List<String> code) {
			this.code = code;
		}
		
		public Setup channel(Channel channel) {
			this.channel = channel;
			return this;
		}
		public Setup convertTime(ConvertTime adFrequency) {
			this.adFrequency = adFrequency;
			return this;
		}
		public Setup resultFormat(ResultFormat resultFormat) {
			this.resultFormat = resultFormat;
			return this;
		}
		public Setup vref(Vref vref) {
			this.voltageRef = vref;
			return this;
		}
		public Setup power(Power power) {
			this.power = power;
			return this;
		}
		
		public final AD buildCode() {
			code.add(";A/D converter unit");
			code.add(TAB + "; Frequencydivision to set Tad between 2-4 uS: _fff ____ ");
			code.add(TAB + "bsf STATUS,RP0 ; Bank: 01");
			code.add(TAB + "movlw 0x8f ; 1000 1111 Clear bits");
			code.add(TAB + "andwf ADCON1 ");
			if (adFrequency == ConvertTime._2uS_8Mhz) {
				code.add(TAB + "movlw 0x50 ; Tad: 2uS, 8MHz");
			}
			if (adFrequency == ConvertTime._4uS_8MHz) {
				code.add(TAB + "movlw 0x20 ; Tad: 4uS, 8MHz");
			}
			code.add(TAB + "iorwf ADCON1");
			code.add(TAB + "bcf STATUS,RP0 ; Bank: 00");
			// Vref
			if (voltageRef == Vref.Vdd) {
				code.add(TAB + "bcf ADCON0,VCFG ; Vref = Vdd");
			} else {
				code.add(TAB + "bsf ADCON0,VCFG ; Vref = external, pin 2");
			}
			// Format
			if (resultFormat == ResultFormat.H8_L2) {
				code.add(TAB + "bcf ADCON0,ADFM ; res 8+2 ");
			} else {
				code.add(TAB + "bsf ADCON0,ADFM ; res 2+8 ");
			}
			// Power
			if (power == Power.On) {
				code.add(TAB + "bsf ADCON0,ADON ; module on");
			} else {
				code.add(TAB + "bcf ADCON0,ADON ; module off");
			}
			
			return new AD(code);
		}
		
		public List<String> getAssemblyCode() {
			List<String> code = new ArrayList<>();
			// Frequency
			code.add(";A/D converter unit");
			code.add(TAB + "; Frequencydivision to set Tad between 2-4 uS: _fff ____ ");
			code.add(TAB + "bsf STATUS,RP0 ; Bank: 01");
			code.add(TAB + "movlw 0x8f ; 1000 1111 Clear bits");
			code.add(TAB + "andwf ADCON1 ");
			if (adFrequency == ConvertTime._2uS_8Mhz) {
				code.add(TAB + "movlw 0x50 ; Tad: 2uS, 8MHz");
			}
			if (adFrequency == ConvertTime._4uS_8MHz) {
				code.add(TAB + "movlw 0x20 ; Tad: 4uS, 8MHz");
			}
			code.add(TAB + "iorwf ADCON1");
			code.add(TAB + "bcf STATUS,RP0 ; Bank: 00");
			// Vref
			if (voltageRef == Vref.Vdd) {
				code.add(TAB + "bcf ADCON0,VCFG ; Vref = Vdd");
			} else {
				code.add(TAB + "bsf ADCON0,VCFG ; Vref = external, pin 2");
			}
			// Format
			if (resultFormat == ResultFormat.H8_L2) {
				code.add(TAB + "bcf ADCON0,ADFM ; res 8+2 ");
			} else {
				code.add(TAB + "bsf ADCON0,ADFM ; res 2+8 ");
			}
			// Power
			if (power == Power.On) {
				code.add(TAB + "bsf ADCON0,ADON ; module on");
			} else {
				code.add(TAB + "bcf ADCON0,ADON ; module off");
			}
			return code;
		}
	}
	
	public AD startConversion() {
		code.add("; AD converting channel " + channel.getChannel() + " (pin " + channel.getPin() +")");
		code.add("	bsf ADCON0,GO");
		return this;
	}

	public AD awaitConversion() {
		code.add(TAB + "btfsc ADCON0,GO ; Skip if H (conversion finished)");
		code.add(TAB + "goto $-1");
		// " .ADIF ");
		return this;
	}

	public AD moveResultH(Register rH) {
		code.add(TAB + "movfw ADRESH    ; Move AD.resultH to register " + rH.getName());
		code.add(TAB + "movwf " + rH.getName());
		return this;
	}

	public AD moveResultL(Register rL) {		
		code.add(TAB + "bsf STATUS,RP0 ; Bank: 01");
		code.add(TAB + "movfw ADRESL   ; Move AD.resultL to register " + rL.getName());
		code.add(TAB + "bcf STATUS,RP0 ; Bank: 00");
		code.add(TAB + "movwf " + rL.getName());
		return this;
	}

	public AD setNewChannel(Channel adChannel) {
		channel = adChannel;
		// bruke xor til å bytte kanal???
		code.add(TAB + "movlw 0xc3      ; Set analogue channel to: " + adChannel.getChannel());
		code.add(TAB + "andwf ADCON0 \t;  Clear xx00 00xx");
		code.add(TAB + "movlw " + to0x(adChannel.getChannel() * 4) + " \t; *4 --cc cc--");
		code.add(TAB + "iorwf ADCON0    ; New channel");
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
