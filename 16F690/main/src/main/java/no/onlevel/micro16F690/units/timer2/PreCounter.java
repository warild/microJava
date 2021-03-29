package no.onlevel.micro16F690.units.timer2;

public enum PreCounter {
	x1(0), x4(1), x16(2);

	private final int value;

	PreCounter(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
