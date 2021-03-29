package no.onlevel.micro16F690.units.memory;

import java.util.List;

import no.onlevel.micro16F690.subroutines.Subroutine;

public class MemoryBank1 extends Memory {
	public static final String MEMORY_BANK = "Memorybank_1";
	public static final String MEM_START = "A0";
	public static final String MEM_END = "EF"; 
	public static final int MEM_LOCATIONS = 80;

	public MemoryBank1(List<String> code, Subroutine subroutines) {
		this.code = code;
		this.subroutines = subroutines;
	}

}
