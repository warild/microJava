package no.onlevel.micro16F690.units.memory;

import java.util.ArrayList;
import java.util.List;

import no.onlevel.micro16F690.code.variable.Variable;
import no.onlevel.micro16F690.code.variable.Variable16;
import no.onlevel.micro16F690.subroutines.Subroutine;

public abstract class Memory {
	public static final String MEMORY_BANK = "Memorybank_0";
	public static final String MEM_START = "20"; // hex
	public static final String MEM_END = "7F"; // hex
	public static final int MEM_LOCATIONS = 80;
	protected List<String> variables = new ArrayList<>();
	protected List<String> variables16 = new ArrayList<>();
	protected List<String> code = new ArrayList<>();
	protected Subroutine subroutines;

	public Variable variable(String name) {
		variables.add(name);
		return new Variable(name, code, subroutines);
	}

	public Variable16 variable16(String name) {
		variables16.add(name);
		// sette inn subrutine som kan kalles
		return new Variable16(name, code, subroutines);
	}

	public List<String> getAssemblyCode() {
		List<String> codeVariables = new ArrayList<>();
		if (variables.size() > 0 || variables16.size() > 0) {
			int registersUsed = variables.size() + (2 * variables16.size());
			codeVariables.add("; " + MEMORY_BANK + ": " + MEM_START + "-" + MEM_END + " (" + registersUsed + " out of "
					+ MEM_LOCATIONS + " registers in use)");
			codeVariables.add("	cblock	0x" + MEM_START);
			if (variables.size() > 0) {
				variables.forEach(v -> codeVariables.add(v));
			}
			if (variables16.size() > 0) {
				variables16.forEach(v -> {
					codeVariables.add(v + "_L");
					codeVariables.add(v + "_H");
				});
			}
			codeVariables.add("	endc\n");
		}
		return codeVariables;
	}

}
