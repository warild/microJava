package no.onlevel.micro16F690.code.variable;

import java.util.ArrayList;
import java.util.List;

public class RegisterBit  {
	private List<String> codelines = new ArrayList<>();
	private int bitNr;

	private String toHex(double value) {
		if (Double.toHexString(value).length() == 1) {
			return "0x0" + Double.toHexString(value);
		} else {
			return "0x" + Double.toHexString(value).toString();
		}
	}
	
	public RegisterBit(int bitNr) {
		this.bitNr = bitNr;
	}

	public void set() {
		codelines.add(	"bsf  " + toHex(Math.pow(2, bitNr)));
	}
	
	public void clear() {
		codelines.add(	"bcf  " + toHex(Math.pow(2, bitNr)));
	}

	public void alter() {
		codelines.add(	"xorlw  " + toHex(Math.pow(2, bitNr)));
	}
	
	public List<String> getCodelines() {
		return codelines;
	}
}
