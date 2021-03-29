package no.onlevel.micro16F690.units.memory;

import java.util.ArrayList;
import java.util.List;

import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.code.variable.Variable16;

public class MemoryBankFixed extends Memory {
	public static final String TAB = "\t";

	public static final String MEMORY_BANK = "Memorybank_fixedRegisters";
	public static final int MEM_LOCATIONS = 80; // not important
	public static final String MEM_START = "0x00"; // not important
	public static final String MEM_END = "0xFF"; // not important
	
	private List<String> variables = new ArrayList<>();
	private List<String> variables16 = new ArrayList<>();
	List<String> code;
	
	public MemoryBankFixed(List<String> code) {
		this.code = code;
	}
	
	public Variable variable(String name) {
		variables.add(name);
		return new Variable(name, code, subroutines);
	}
	
	public Variable16 variable16(String name) {
		variables16.add(name);
		return new Variable16(name, code, subroutines);
	}
}
