package no.onlevel.micro16F690.programs;

import no.onlevel.micro16F690.Micro16f690;
import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.code.variable.Variable16;
import no.onlevel.micro16F690.units.ad.Channel;
import no.onlevel.micro16F690.units.ad.ConvertTime;
import no.onlevel.micro16F690.units.ad.ResultFormat;
import no.onlevel.micro16F690.units.ad.Vref;
import no.onlevel.micro16F690.units.clock.Frequency;
import no.onlevel.micro16F690.units.common.enums.Power;
import no.onlevel.micro16F690.units.pins.Pin10;
import no.onlevel.micro16F690.units.pins.Pin11;
import no.onlevel.micro16F690.units.pins.Pin12;
import no.onlevel.micro16F690.units.pins.Pin13;
import no.onlevel.micro16F690.units.pins.Pin14;
import no.onlevel.micro16F690.units.pins.Pin15;
import no.onlevel.micro16F690.units.pins.Pin16;
import no.onlevel.micro16F690.units.pins.Pin17;
import no.onlevel.micro16F690.units.pins.Pin18;
import no.onlevel.micro16F690.units.pins.Pin19;
import no.onlevel.micro16F690.units.pins.Pin_2;
import no.onlevel.micro16F690.units.pins.Pin_3;
import no.onlevel.micro16F690.units.pins.Pin_4;
import no.onlevel.micro16F690.units.pins.Pin_5;
import no.onlevel.micro16F690.units.pins.Pin_6;
import no.onlevel.micro16F690.units.pins.Pin_7;
import no.onlevel.micro16F690.units.pins.Pin_8;
import no.onlevel.micro16F690.units.pins.Pin_9;
import no.onlevel.micro16F690.units.timer2.PostCounter;
import no.onlevel.micro16F690.units.timer2.PreCounter;


/**
 * Målepinne
 * 1: Sett opp en bølge på ca 1500 Hz
 * 2: Mål spenning på topp og bunn fra vikling A og vikling B.
 * Antar at det går greit å sette på en firkantbølge.
 * En måling ca 40 uS.
 * 2000 Hz -> 500 uS bølgelengde. 250uS høy -> kan måle begge høye og 
 * begge lave innen en bølgelengde.
 * Deretter beregnes posisjonen: p=(a0-a1)/((a0-a1)+(b0-b1))
 * 
 * Bruker timer2 til å lage klokkepulser
 *
 */
public class MaalepinneMicro extends Micro16f690 {
	
	private Variable16 a0 = memory0.variable16("a0");
	private Variable16 a1 = memory0.variable16("a1");
	private Variable16 b0 = memory0.variable16("b0");
	private Variable16 b1 = memory0.variable16("b1");
	private Variable16 a = memory0.variable16("a");
	private Variable16 b = memory0.variable16("b");
	private Variable16 ab = memory0.variable16("ab");	
	private Variable16 onePosition = memory0.variable16("onePosition");
	// filtered - sum of more positions		
	private Variable16 position = memory0.variable16("position");
	
	private Variable tempBitCounter = memory0.variable("tempBitCounter");
	private Variable16 tempA16 = memory0.variable16("tempA16");
	private Variable16 tempB16 = memory0.variable16("tempB16");
	private Variable16 tempRes16 = memory0.variable16("tempRes16");
	
	private Variable workIR = memory.variable("workIR");
	private Variable statusIR = memory.variable("statusIR");
	
	// LABELS
	private final String SETUP_UNITS = "CONFIGURE_UNITS";
	private final String LOOP_START = "MAIN_PROGRAM";
		
	protected void generateInterruptHandlingCode() {
		interruptRoutine.startWithPushToStack(workIR, statusIR);
		// code gors here
		interruptRoutine.endWithPopFromStack();
	}

	protected void generateSetupUnitsCode(){
	
		clock.setupWith()
			.frequency(Frequency._8MHz)
		.buildCode();
		
		pins.setupWith()	
			.pin10(Pin10.in_digital_B7)
			.pin11(Pin11.in_digital_B6) 
			.pin_9(Pin_9.out_digital_C7)
			.pin_8(Pin_8.out_digital_C6)
			.pin_5(Pin_5.out_digital_C5)
			.pin_6(Pin_6.out_digital_C4)
			.pin_7(Pin_7.out_digital_C3)
			.pin14(Pin14.out_digital_C2)
			.pin15(Pin15.out_digital_C1)
			.pin16(Pin16.out_digital_C0)
			
			.pin12(Pin12.in_analog_Ch11)
			.pin13(Pin13.in_analog_Ch10)
			
			.pin_2(Pin_2.in_digital_IrOnChange_A5)
			.pin_2(Pin_2.in_digital_A5)
			.pin_3(Pin_3.in_analog_Ch_3)
			.pin17(Pin17.in_analog_Ch_2) 
			
			.pin_4(Pin_4.in_ICSP_Programming_voltage)		
			.pin18(Pin18.in_ICSP_clock)
			.pin19(Pin19.in_ICSP_data)
		.buildCode();
		
		ad.setupWith()
			.convertTime(ConvertTime._2uS_8Mhz)
			.vref(Vref.Vdd)
			.resultFormat(ResultFormat.H2_L8)
			.channel(Channel.Ch11_pin12)
			.power(Power.On)
		.buildCode();
		 	
		timer2  //  (4*16*250*5)/8MHz = 80 000cp/8MHz = 100uS
		.set(PreCounter.x16)
		.setCounter(250)    
		.set(PostCounter.x5) 
		.set(Power.On);
	}
	
	protected void generateSubroutinesCode() {			 
		// Internal registers in subroutines		
		subroutine.div_A16_B16_R16().with()
			.A16_in(tempA16)
			.B16_in(tempB16)
			.bitCounter_in(tempBitCounter)
			.R16_in(tempRes16)
		.buildCode();		
	}
	
	protected void generateMainProgram() {		
		LABEL(LOOP_START);

		// wait for IR - wait for A5 going low (or high)
		// 1: Read AD and change timer value
		// 2: Handle timer interrupts (Length of pulse - min pulse length - integrate diff - adjust pr time/pr delta)
		
		comment("Sample a0 and b0 - wave top");		
		ad	// Ch 11
		.startConversion()		
		.awaitConversion()		
		.setNewChannel(Channel.Ch10_pin13)		
		.moveResultH(a0.registerH())
		.moveResultL(a0.registerL());
		ad	// Ch 10
		.startConversion()		
		.awaitConversion()		
		.setNewChannel(Channel.Ch11_pin12)		
		.moveResultH(b0.registerH())
		.moveResultL(b0.registerL());
		
		// wait
		comment("Sample a1 and b1 - wave bottom");
		ad	// Ch 11
		.startConversion()		
		.awaitConversion()		
		.setNewChannel(Channel.Ch10_pin13)		
		.moveResultH(a1.registerH())
		.moveResultL(a1.registerL());
		ad	// Ch 10
		.startConversion()		
		.awaitConversion()		
		.setNewChannel(Channel.Ch11_pin12)		
		.moveResultH(b1.registerH())
		.moveResultL(b1.registerL());
		
		a.set(a0);
		a.sub(a1);
		b.set(b0);
		b.sub(b1);
		
		//position = a /(a+b)
		ab.set(a);
		ab.add(b);		
		position.call.divide_a16_b16(a, ab);
		
		comment("Skriv posisjonen til PORTC");
		PORTC.set(position.registerH());
		
		LABEL("	goto " + LOOP_START);
	}
}
