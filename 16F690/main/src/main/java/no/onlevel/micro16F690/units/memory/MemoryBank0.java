package no.onlevel.micro16F690.units.memory;

import java.util.List;

import no.onlevel.micro16F690.subroutines.Subroutine;

public class MemoryBank0 extends Memory {
	public static final String MEMORY_BANK = "Memorybank_0";
	public static final String MEM_START = "20";
	public static final String MEM_END = "7F";
	public static final int MEM_LOCATIONS = 80;

	public MemoryBank0(List<String> code, Subroutine subroutines) {
		this.code = code;
		this.subroutines = subroutines;
	}

}
