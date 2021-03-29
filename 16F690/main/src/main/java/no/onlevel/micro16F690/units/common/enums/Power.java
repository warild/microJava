package no.onlevel.micro16F690.units.common.enums;

public enum Power {
	On(1), Off(0);

	private int value;

	private Power(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
