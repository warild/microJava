package no.onlevel.micro16F690.units.memory;

import java.util.ArrayList;
import java.util.List;

import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.code.variable.Variable16;
import no.onlevel.micro16F690.subroutines.Subroutine;

public class MemoryBankShared extends Memory {
	public static final String TAB = "\t";

	public static final String MEMORY_BANK = "Memorybank_shared";
	public static final int MEM_LOCATIONS = 16;
	public static final String MEM_START = "0xF0";
	public static final String MEM_END = "0xFF";
	


	public MemoryBankShared(List<String> code, Subroutine subroutines) {
		this.code = code;
		this.subroutines = subroutines;
	}
	
	public List<String> getAssemblyCode() {
		List<String> codeLines = new ArrayList<>();
		if (variables.size() > 0 || variables16.size() > 0) {
			int registersUsed = variables.size() + (2 * variables16.size());
			codeLines.add("; Registers F0-FF holds the same value from any bank ("+ registersUsed +" out of 16 registers in use)" );
			codeLines.add(TAB + "cblock	0xF0 ");
			if (variables.size() > 0) {						
				variables.forEach(v -> codeLines.add(v));
			}	
			if (variables16.size() > 0) {						
				variables16.forEach(v -> {
					codeLines.add(v + "_L");
					codeLines.add(v + "_H");
				}); 	
			}
			codeLines.add(TAB + "endc\n");
		}
		return codeLines;
	}
}
