package org.hansib.copper_wire;

public class Errors {

	private Errors() {
		// instantiation prevention
	}

	public static IllegalArgumentException illegalArg(final String fmt, final Object... args) {
		return new IllegalArgumentException(String.format(fmt, args));
	}
}