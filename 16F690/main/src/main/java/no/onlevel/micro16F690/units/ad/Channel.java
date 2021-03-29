package no.onlevel.micro16F690.units.ad;

public enum Channel {
	Ch_0_pin19(0, 19), 
	Ch1_pin18(1, 18), 
	Ch2_pin17(2, 17), 
	Ch3_pin3(3, 3), 
	Ch4_pin16(4, 16), 
	Ch5_pin15(5, 15), 
	Ch6_pin14(6, 16), 
	Ch7_pin7(7, 7), 
	Ch8_pin8(8, 8), 
	Ch9_pin9(9, 9), 
	Ch10_pin13(10, 13), 
	Ch11_pin12(11, 12) ;
	
	private Integer chNr;
	private Integer pinNr;
	
	private Channel(Integer chNr, Integer pinNr) {
		this.chNr = chNr;
		this.pinNr = pinNr;
	}
	
	public Integer getChannel(){
		return chNr;
	}
	public Integer getPin(){
		return pinNr;
	}
}
