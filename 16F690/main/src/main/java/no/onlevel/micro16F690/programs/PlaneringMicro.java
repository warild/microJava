package no.onlevel.micro16F690.programs;

import static no.onlevel.micro16F690.units.ad.Channel.Ch10_pin13;
import static no.onlevel.micro16F690.units.ad.ConvertTime._2uS_8Mhz;
import static no.onlevel.micro16F690.units.clock.Frequency._8MHz;
import static no.onlevel.micro16F690.units.common.enums.Power.On;
import static no.onlevel.micro16F690.units.interrupt.Timer2.enable;
import static no.onlevel.micro16F690.units.pins.Pin10.out_digital_B7;
import static no.onlevel.micro16F690.units.pins.Pin11.out_digital_B6;
import static no.onlevel.micro16F690.units.pins.Pin12.in_analog_Ch11;
import static no.onlevel.micro16F690.units.pins.Pin13.in_analog_Ch10;
import static no.onlevel.micro16F690.units.pins.Pin14.out_digital_C2;
import static no.onlevel.micro16F690.units.pins.Pin15.out_digital_C1;
import static no.onlevel.micro16F690.units.pins.Pin16.out_digital_C0;
import static no.onlevel.micro16F690.units.pins.Pin17.in_analog_Ch_2;
import static no.onlevel.micro16F690.units.pins.Pin18.in_ICSP_clock;
import static no.onlevel.micro16F690.units.pins.Pin19.in_ICSP_data;
import static no.onlevel.micro16F690.units.pins.Pin_2.out_digital_A5;
import static no.onlevel.micro16F690.units.pins.Pin_3.in_analog_Ch_3;
import static no.onlevel.micro16F690.units.pins.Pin_4.in_ICSP_Programming_voltage;
import static no.onlevel.micro16F690.units.pins.Pin_5.out_digital_C5;
import static no.onlevel.micro16F690.units.pins.Pin_6.out_digital_C4;
import static no.onlevel.micro16F690.units.pins.Pin_7.out_digital_C3;
import static no.onlevel.micro16F690.units.pins.Pin_8.out_digital_C6;
import static no.onlevel.micro16F690.units.pins.Pin_9.out_digital_C7;

import no.onlevel.micro16F690.Micro16f690;
import no.onlevel.micro16F690.code.logic.BlockIf;
import no.onlevel.micro16F690.code.logic.BlockIfElse;
import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.code.variable.Variable16;
import no.onlevel.micro16F690.units.ad.ResultFormat;
import no.onlevel.micro16F690.units.ad.Vref;
import no.onlevel.micro16F690.units.timer2.PostCounter;
import no.onlevel.micro16F690.units.timer2.PreCounter;

public class PlaneringMicro extends Micro16f690 {

	private Variable v0 = memory0.variable("V0");
	private Variable v1 = memory0.variable("V1");
	private Variable v2 = memory0.variable("V2");
	private Variable v3 = memory0.variable("V3");
	private Variable v4 = memory0.variable("V4");
	private Variable v5 = memory0.variable("V5");
	private Variable v6 = memory0.variable("V6");
	private Variable v7 = memory0.variable("V7");
	private Variable VFmax = memory0.variable("VFmax");
	private Variable VMmax = memory0.variable("VMmax");
	private Variable VEmax = memory0.variable("VEmax");
	private Variable VmaxNr = memory0.variable("VmaxNr");
	private Variable16 sumAlle16 = memory0.variable16("sumAlle16");
	private Variable16 sum3max16 = memory0.variable16("sum3max16");
	private Variable16 sumLavtSnitt16 = memory0.variable16("sumLavtSnitt16");
	private Variable antILavtSnitt = memory0.variable("antILavtSnitt");
	private Variable lavtSnitt = memory0.variable("lavtSnitt");
	private Variable16 multRes16 = memory0.variable16("multRes16");
	private Variable16 multSum16 = memory0.variable16("multSum16");
	private Variable posisjon = memory0.variable("posisjon");

	// variables for subroutines
	private Variable tempA = memory0.variable("tempA");
	private Variable tempB = memory0.variable("tempB");
	private Variable tempRes = memory0.variable("tempRes");
	private Variable tempCounter = memory0.variable("tempCounter");
	private Variable16 tempA16 = memory0.variable16("tempA16");
	private Variable16 tempRes16 = memory0.variable16("tempRes16");

	
	private Variable16 result16 = memory0.variable16("result16");
	private Variable result8 = memory0.variable("result8");
	private Variable16 a16 = memory0.variable16("a16");
	private Variable a8 = memory0.variable("a8");
	private Variable16 b16 = memory0.variable16("b16");
	private Variable b8 = memory0.variable("b8");
	
	// mellomlagring av W og Status ved interrupt
	private Variable tempWorkIR = memory.variable("tempWorkIR");
	private Variable tempStatusIR = memory.variable("tempStatusIR");

	// test
	private Variable16 tempB16 = memory0.variable16("tempB16");
	// LABELS
	private final String MAIN_START = "MAIN";

	public void generateInterruptHandlingCode() {
		interruptRoutine.startWithPushToStack(tempWorkIR, tempStatusIR);
		timer2.handleIfIr();
			PORTA.set(v0);
			timer2.setCounter(0);
			multSum16.set(2, 1);
			v2.add(VEmax, tempB);
			timer2.clearIrFlag();
		timer2.handleIrEnd();
		interruptRoutine.endWithPopFromStack();
	}

	public void generateSetupUnitsCode(){
		clock.setupWith()
			.frequency(_8MHz)
		.buildCode();
		 
		pins.setupWith()
			.pin17(in_analog_Ch_2) 
			.pin_3(in_analog_Ch_3)
			.pin13(in_analog_Ch10)
			.pin12(in_analog_Ch11)
			.pin16(out_digital_C0)
			.pin15(out_digital_C1)
			.pin14(out_digital_C2)
			.pin_7(out_digital_C3)
			.pin_6(out_digital_C4)		
			.pin_5(out_digital_C5)
			.pin_8(out_digital_C6)
			.pin_9(out_digital_C7)
			// unused pins (set output low):		
			.pin_2(out_digital_A5) 
			.pin11(out_digital_B6)
			.pin10(out_digital_B7)
			// programming
			.pin_4(in_ICSP_Programming_voltage) 
			.pin18(in_ICSP_clock) 
			.pin19(in_ICSP_data)
		.buildCode();
		
		ad.setupWith()  // input on pin13, 2uS convert time
			.channel(Ch10_pin13)
			.vref(Vref.Vpin_18_A1)
			.convertTime(_2uS_8Mhz)
			.resultFormat(ResultFormat.H8_L2)
			.power(On)
		.buildCode();
				
		timer2.setupWith() // 10mS = (16*250*5)(*4)/8MHz = 80 000cp/8 000 000Hz
			.preCounter(PreCounter.x16)
			.counter(250)
			.postCounter(PostCounter.x5)
			.power(On)
		.buildCode();

//		eusart.with().asyTx.asyRx
		// comparator
		// PWM
		// SSP
		// IIC
		// EUSART
		
		interruptRoutine.setupWith()
		.timer2(enable)
		.buildCode();	
		
//		INTCON
//		.GIE 
//		.PEIE -> portA?
//		.RABIE -> enable ir på PORTA og PORTB
//		.RABIF -> ir på pin på PORTA eller PORTB
//		-> les port og clear flag
		
	}

	protected void generateSubroutinesCode() {
		// Variables used by subroutines.
		subroutine.multiply_A8_B4_R16().with()
			.A8_in(tempA)
			.B4_in(tempB)
			.internalBitCounter_in(tempCounter)
			.Result16_in(tempRes16)		
		.buildCode(); 
		
		subroutine.div_A16_B16_R16().with()
			.A16_in(tempA16)
			.B16_in(tempB16)
			.bitCounter_in(tempCounter)
			.R16_in(tempRes16)
		.buildCode();
		
		subroutine.div_A16_B16_8().with()
			.A16_in(tempA16)
			.B16_in(tempB16)
			.bitCounter_in(tempCounter)
			.Result8_in(result8)
		.buildCode();
		
		subroutine.div_a16_b8_result8().with()
			.a16_in(tempA16)
			.b8_in(tempB)
			.bitCounter_in(tempCounter)
			.result8_in(result8)
		.buildCode();
	}

	protected void generateMainProgram() {
		// subroutines work always. How many extra cycles when calling sub? 8 cps in addition ? . (1uS)  
		result16.call.multiply_a8_b4(a8, b8);
		result16.call.divide_a16_b16(a16, b16);
		result8.call.divide_a16_b16(a16, b16);
		result8.call.divide_a16_b8(a16, b8);
		
		
		// wait for IR or wait for A5 going low (or high)
		// 1: Read AD and change timer value
		// 2: Handle timer interrupts (Length of pulse - min pulse length - integrate
		// diff - adjust pr time/pr delta)

//		comment("AD conversion");
//		ad  // Ch 3
//		.startConversion()
//		.awaitConversion()		
//		.setNewChannel(Channel.Ch3_pin3)		
//		.moveResultH(v1);
//		ad  // Ch 11
//		.startConversion()
//		.awaitConversion()
//		.setNewChannel(Channel.Ch11_pin12)	
//		.moveResultH(v2);
//		ad	// Ch 10
//		.startConversion()
//		.awaitConversion()
//		.setNewChannel(Channel.Ch10_pin13)
//		.moveResultH(v3);		
//		ad	// Ch 2
//		.startConversion()		
//		.awaitConversion()		
//		.setNewChannel(Channel.Ch2_pin17)		
//		.moveResultH(v4);
//		
		v0.set(1);
		v1.set(0);
		v2.set(0);
		v3.set(0);
		v4.set(0);
		v5.set(0);
		v6.set(0);
		v7.set(0);

		comment("Summer opp alle AD verdier");
		sumAlle16.set(0, v0);
		sumAlle16.add(v1);
		sumAlle16.add(v2);
		sumAlle16.add(v3);
		sumAlle16.add(v4);
		sumAlle16.add(v5);
		sumAlle16.add(v6);
		sumAlle16.add(v7);

//		sumAlle16.add(v1).add(v2).add(v3);

		PORTC.set(sumAlle16.registerL());
		LABEL("STOP");
		LABEL("	goto STOP");

		comment("Finn den høyeste verdien og ta vare på info om den og de to på sidene.");
		// 1
		VFmax.set(0);
		VMmax.set(v0);
		VEmax.set(v1);
		VmaxNr.set(0);
		antILavtSnitt.set(6);
		// 2		
		BlockIf v1Bigger = blockIf("V1_IS_BIGGER");
		BlockIf v2Bigger = blockIf("V2_IS_BIGGER");
		BlockIf v3Bigger = blockIf("V3_IS_BIGGER");
		BlockIf v4Bigger = blockIf("V4_IS_BIGGER");
		BlockIf v5Bigger = blockIf("V5_IS_BIGGER");
		BlockIf v6Bigger = blockIf("V6_IS_BIGGER");
		BlockIf v7Bigger = blockIf("V7_IS_BIGGER");
		v1Bigger.IF(v1).isBiggerThan(VMmax);
		VFmax.set(v0);
		VMmax.set(v1);
		VEmax.set(v2);
		VmaxNr.set(1);
		antILavtSnitt.set(5);
		v1Bigger.END();
		v2Bigger.IF(v2).isBiggerThan(VMmax);
		VFmax.set(v1);
		VMmax.set(v2);
		VEmax.set(v3);
		VmaxNr.set(2);
		antILavtSnitt.set(5);
		v2Bigger.END();
		v3Bigger.IF(v3).isBiggerThan(VMmax);
		VFmax.set(v2);
		VMmax.set(v3);
		VEmax.set(v4);
		VmaxNr.set(3);
		antILavtSnitt.set(5);
		v3Bigger.END();
		v4Bigger.IF(v4).isBiggerThan(VMmax);
		VFmax.set(v3);
		VMmax.set(v4);
		VEmax.set(v5);
		VmaxNr.set(4);
		antILavtSnitt.set(5);
		v4Bigger.END();
		v5Bigger.IF(v5).isBiggerThan(VMmax);
		VFmax.set(v4);
		VMmax.set(v5);
		VEmax.set(v6);
		VmaxNr.set(5);
		antILavtSnitt.set(5);
		v5Bigger.END();
		v6Bigger.IF(v6).isBiggerThan(VMmax);
		VFmax.set(v5);
		VMmax.set(v6);
		VEmax.set(v7);
		VmaxNr.set(6);
		antILavtSnitt.set(5);
		v6Bigger.END();
		v7Bigger.IF(v7).isBiggerThan(VMmax);
		VFmax.set(v6);
		VMmax.set(v7);
		VEmax.set(0);
		VmaxNr.set(7);
		antILavtSnitt.set(6);
		v7Bigger.END();

		comment("Trekk de tre høyeste verdiene fra resten.");
		sumLavtSnitt16.set(sumAlle16);
		sumLavtSnitt16.sub(VFmax); // VFmax eller VEmax kan være 0
		sumLavtSnitt16.sub(VMmax);
		sumLavtSnitt16.sub(VEmax);

		comment("Beregn lavt snitt ved å dele summen av lavt snitt på de resterende - 2 eller 1 når 4 avlesninger");
		lavtSnitt.call.divide_a16_b8( sumLavtSnitt16, antILavtSnitt);

		comment("Lag ekte max-verdier - uten bakgrunnsstøy");
		// if lavtSnitt > VFmax -> VFmax = 0
		VFmax.sub(lavtSnitt);
		VFmax.subIsNegThen(0);
		VMmax.sub(lavtSnitt);
		VEmax.sub(lavtSnitt);
		VEmax.subIsNegThen(0);

		comment("Summer opp de tre (ekte) max-verdier. Skal dele med denne seinere.");
		sum3max16.set(0, VFmax);
		sum3max16.add(VMmax);
		sum3max16.add(VEmax);

		comment("Multipliser de tre (ekte) max-verdier med nr og summer opp");
		multSum16.call.multiply_a8_b4(VFmax, VmaxNr);
		VmaxNr.add(1);
		multRes16.call.multiply_a8_b4(VMmax, VmaxNr);
		multSum16.add(multRes16);
		VmaxNr.add(1);
		multRes16.call.multiply_a8_b4(VEmax, VmaxNr);
		multSum16.add(multRes16);

		comment("Gang multSum16 med to og trekk fra en halv sum3Max");
		multSum16.shiftLeft(1);
		tempA16.set(sum3max16);
		tempA16.shiftRight(1);
		multSum16.sub(tempA16);

		comment("Divider og finn posisjonen. Skalaen er fra 0,5 til 7,5 (000 10000 - 111 10000)");
		posisjon.call.divide_a16_b16(multSum16, sum3max16);

		comment("Skriv posisjonen til PORTC");
		PORTC.set(posisjon);

		LABEL("	goto " + MAIN_START);
	}

}
