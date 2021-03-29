package no.onlevel.micro16F690;

public class Microinstructions {
	static final String ADD_F = "addwf ";
	static final String ADD_L = "addlw ";
	static final String SUB_FminusW = "subwf ";
	static final String SUB_L = "sublw ";
	
	static final String AND_F = "andwf ";
	static final String AND_L = "andlw ";
	static final String COM_F = "comf "; // invert
	static final String OR_F = "iorwf ";
	static final String OR_L = "iorlw ";
	static final String XOR_F = "xorwf ";
	static final String XOR_L = "xorlw ";

	static final String MOV_F = "movf ";
	static final String MOV_WF = "movwf ";
	static final String MOV_L = "movlw ";

	static final String CLR_F = "clrf ";
	static final String CLR_W = "clrw ";
	static final String DEC_F = "decf ";
	static final String INC_F = "incf ";	
	static final String RLR = "rlf ";
	static final String RRF = "rrf ";
	static final String BCF = "bcf ";
	static final String BSF = "bsf ";
	static final String SWAPF = "swapf ";
	static final String CLR_WDT = "clrwdt";
	
	static final String NOP = "nop ";
	static final String INC_SkipIf0 = "incfsz ";
	static final String DEC_SkipIf0 = "decfsz ";
	static final String BTF_SkipIfC = "btfsc "; // Bit Test f, Skip if Clear
	static final String BTF_SkipIfS = "btfss "; // Bit Test f, Skip if Set
		
	static final String GOTO = "goto ";
	static final String CALL = "call ";
	static final String RETURN = "return";
	static final String RETURN_LW = "retlw";
	static final String RETURN_FIE = "retfie";
	static final String SLEEP = "sleep";
	
	private int C = 0;

		
	
}
