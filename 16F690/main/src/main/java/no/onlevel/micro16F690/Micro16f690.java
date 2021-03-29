package no.onlevel.micro16F690;

import java.util.ArrayList;
import java.util.List;

import no.onlevel.micro16F690.code.logic.BlockIf;
import no.onlevel.micro16F690.code.logic.BlockIfElse;
import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.subroutines.Div_16_16_16;
import no.onlevel.micro16F690.subroutines.Div_16_16_8;
import no.onlevel.micro16F690.subroutines.Div_16_8_8;
import no.onlevel.micro16F690.subroutines.Mult_8_4_16;
import no.onlevel.micro16F690.subroutines.Subroutine;
import no.onlevel.micro16F690.units.ad.AD;
import no.onlevel.micro16F690.units.clock.Clock;
import no.onlevel.micro16F690.units.interrupt.Interrupt;
import no.onlevel.micro16F690.units.memory.MemoryBankShared;
import no.onlevel.micro16F690.units.memory.MemoryBank0;
import no.onlevel.micro16F690.units.memory.MemoryBank1;
import no.onlevel.micro16F690.units.memory.MemoryBank2;
import no.onlevel.micro16F690.units.memory.MemoryBankFixed;
import no.onlevel.micro16F690.units.pins.Pins;
import no.onlevel.micro16F690.units.timer2.Timer2;

abstract public class Micro16f690 {
	private final String TAB = "\t";

	// this list is used by all methods to write code-lines
	private List<String> code = new ArrayList<>();
	// only for setup:
	protected Subroutine subroutine = new Subroutine(code);
	
	/// Memorybanks
	protected MemoryBank0 memory0 = new MemoryBank0(code, subroutine);
	protected MemoryBank1 memory1 = new MemoryBank1(code, subroutine);
	protected MemoryBank2 memory2 = new MemoryBank2(code, subroutine);
	protected MemoryBankShared memory = new MemoryBankShared(code, subroutine);
	
	// Fixed registers
	private MemoryBankFixed fixedMemory = new MemoryBankFixed(code);
	protected Variable PORTA = fixedMemory.variable("PORTA");  //memory2 og 0 (105h og 05h)
	protected Variable PORTB = fixedMemory.variable("PORTB");  //memory2 og 0 (106h og 06)
	protected Variable PORTC = fixedMemory.variable("PORTC");  //memory2 og 0 (107h og 07)
	
	// units
	protected Clock clock = new Clock(code);
	protected Pins pins = new Pins(code);
	protected AD ad = new AD(code);
	protected Timer2 timer2 = new Timer2(code);

	// holds methods to wrap code when handling an IR
	protected Interrupt interruptRoutine = new Interrupt(code); 

	// control structure - blocks
	protected BlockIf blockIf(String blockName) {
		return new BlockIf(blockName, code);		
	}
	protected BlockIfElse blockIfElse(String blockName) {
		return new BlockIfElse(blockName, code);
		
	}
	
	// Predefined subroutines
//	protected Mult_8_4_16 mult_A8_B4_R16 = new Mult_8_4_16(code);
//	protected Div_16_16_16 div_16_16_16 = new Div_16_16_16(code);
//	protected Div_16_16_8 div_16_16_8 = new Div_16_16_8(code);
//	protected Div_16_8_8 div_16_8_8 = new Div_16_8_8(code);
	
	
	// generate assemblyCode by running Java-methods
	abstract protected void generateInterruptHandlingCode();
	abstract protected void generateSetupUnitsCode();
	abstract protected void generateMainProgram();
	abstract protected void generateSubroutinesCode();


	protected Variable preDefinedVariable(String name) {
		Variable variable = new Variable(name, code, subroutine);
		return variable;
	}

	// ---- start print ---
	public List<String> printProgram() {
		code.add("#include <p16F690.inc>");
		code.add(TAB
				+ "__config (_INTRC_OSC_NOCLKOUT & _WDT_OFF & _PWRTE_OFF & _MCLRE_OFF & _CP_OFF & _BOD_OFF & _IESO_OFF & _FCMEN_OFF)");
		code.add(TAB + "errorlevel-302");
		code.add(TAB + "errorlevel-305");
		
		code.add("\n;=============================================");
		code.add(";     Variables ");
		code.add(";=============================================");
		// Bank 0: (00) 0x20-0x7F (0x20-0x7F) 80 bytes
		// Bank 1: (01) 0x20-0x7F (0x80-0x11F) 80 bytes
		// Bank 2: (10) 0x20-0x7F (0x120-0x16F9 80 bytes
		// Bank any:(xx) 0xF0-0xFF (0xF0-0xFF) 16 bytes
		code.addAll(memory0.getAssemblyCode());
		code.addAll(memory1.getAssemblyCode());
		code.addAll(memory2.getAssemblyCode());
		code.addAll(memory.getAssemblyCode());
		
		code.add(";=============================================");
		code.add("; 	Start vector ");
		code.add(";=============================================");
		code.add(TAB + "; At (re)start instructions are read from location 0");
		code.add(TAB + "; Only 4 instructions available. Interruptvector follows at location 4");
		code.add(TAB + "org 0");
		code.add(TAB + "call SETUP_UNITS");
		code.add(TAB + "goto MAIN");
		
		code.add("\n;=============================================");
		code.add("; 	Interrupt routine");
		code.add(";=============================================");
		code.add("; Interrupts start to execute instructions at location 4.  Program space after that is free to use");
		code.add(TAB + "org 4");
		generateInterruptHandlingCode();
		code.add("	retfie\n");
		
		code.add(";=============================================");
		code.add("; 	Units");
		code.add(";=============================================");
		code.add("SETUP_UNITS");
		generateSetupUnitsCode();
		code.add("	return ; INITIALIZE_UNITS done");
		
		code.add(";=============================================");
		code.add("; 	Subroutines");
		code.add(";=============================================");		
		generateSubroutinesCode();
		code.add(";=============================================");
		code.add("; 	Main program");
		code.add(";=============================================");
		code.add("MAIN");
		generateMainProgram();
		code.add("	end");
		
		return code;
	}

	protected void comment(String text) {
		code.add("\n; " + text);
	}

	// protected void START(String block) {
	// code.add(TAB + block);
	// }

//	protected void ELSE(String label) {
//		code.add(TAB + "goto " + label + "_END");
//		code.add(label + "_ELSE");
//	}

//	protected void END(String label) {
//		code.add(label + "_END");
//	}

	protected void LABEL(String label) {
		code.add(label);
	}
	
	public static String toHex(int value) {
		if (Integer.toHexString(value).length() == 1) {
			return "0x0" + Integer.toHexString(value).toString();
		} else {
			return "0x" + Integer.toHexString(value).toString();
		}
	}
//	public final List<String> printVariables() {
//		// Bank 0: (00) 0x20-0x7F (0x20-0x7F) 80 bytes
//		// Bank 1: (01) 0x20-0x7F (0x80-0x11F) 80 bytes
//		// Bank 2: (10) 0x20-0x7F (0x120-0x16F9 80 bytes
//		// Bank any:(xx) 0xF0-0xFF (0xF0-0xFF) 16 bytes
//		List<String> localCode = new ArrayList<>();
//		localCode.add("\n;=============================================");
//		localCode.add("; Variables ");
//		localCode.add(";=============================================");
//		localCode.addAll(memory0.getAssemblyCode());
//		localCode.addAll(memory1.getAssemblyCode());
//		localCode.addAll(memory2.getAssemblyCode());
//		localCode.addAll(memory.getAssemblyCode());
//		return localCode;
//	}

//	public final List<String> printStartVector() {
//		List<String> localCode = new ArrayList<>();
//		localCode.add(";=============================================");
//		localCode.add("; 	Start vector ");
//		localCode.add(";=============================================");
//		localCode.add(TAB + "; At (re)start instructions are read from location 0");
//		localCode.add(TAB + "; Only 4 instructions available. Interruptvector follows at location 4");
//		localCode.add(TAB + "org 0");
//		localCode.add(TAB + "call INITIALIZE_UNITS");
//		localCode.add(TAB + "goto MAIN");
//		return localCode;
//	}

//	public final List<String> printInterruptRoutine() {
//		List<String> localCode = new ArrayList<>();
//		localCode.add("\n;=============================================");
//		localCode.add("; 	Interrupt vector ");
//		localCode.add(";=============================================");
//		localCode.add("; Interrupts start to execute instructions at location 4");
//		localCode.add("; Program space after that is free to use");
//		localCode.add(TAB + "org 4");
//		localCode.addAll(interruptRoutine.getAssemblyCode());
//		localCode.add("	retfie\n");
//		return localCode;
//	}



	// ;--------------------------------------
	// ; Work og Status lagres i området som er felles for alle blokker
	// (unbanked area) ved Interrupt.
	// ;--------------------------------------
	// cblock 0x7D
	// mainWork
	// mainStatus
	// endc

//	public List<String> generateAssemblyCode() {

//		code.add(printProgram());
//		code.addAll(printVariables());
//		code.addAll(printStartVector());
//		code.addAll(printInterruptRoutine());
//
//		
//		interruptRoutine();
//
//		code.add(";=============================================");
//		code.add("; 	Configure units");
//		code.add(";=============================================");
//		generateSetupUnitsCode();
//
//		generateSubroutinesCode();
//		code.add("\n; ------------------------------------------");
//		code.add("; Subroutine MULT_8_4_16 ");
//		code.add("; ------------------------------------------");
//
//
//		code.add("\n; ------------------------------------------");
//		code.add("; Subroutine DIV_16_8_8");
//		code.add("; ------------------------------------------");
////		div_16_8_8.makeSubroutineCode();
//
//		code.add(";=============================================");
//		code.add("; 	Main program");
//		code.add(";=============================================");
//		generateMainProgram();
//		code.add("\n	end");
//
//		return code;
//
//	}
//
//	protected String toHex(int value) {
//		if (Integer.toHexString(value).length() == 1) {
//			return "0x0" + Integer.toHexString(value);
//		} else {
//			return "0x" + Integer.toHexString(value).toString();
//		}
//	}
//

//

}
