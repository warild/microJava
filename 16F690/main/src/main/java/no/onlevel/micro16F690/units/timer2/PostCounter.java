package no.onlevel.micro16F690.units.timer2;

public enum PostCounter {
	x1(1), x2(2), x3(3), x4(4), x5(5), x6(6), x7(7), x8(8), x9(9), x10(10), x11(11), x12(12), x13(13), x14(14), x15(15), 
	x16(16);

	int value;
	
	private PostCounter(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
