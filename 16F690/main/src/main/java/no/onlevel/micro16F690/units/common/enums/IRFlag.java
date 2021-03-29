package no.onlevel.micro16F690.units.common.enums;

public enum IRFlag {
	On(1), Off(0);

	private int value;

	private IRFlag(int value) {
		this.value = value;
	}

	public int getvalue() {
		return value;
	}
}
