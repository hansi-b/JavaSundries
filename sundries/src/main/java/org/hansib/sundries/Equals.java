package org.hansib.sundries;

public class Equals {

	public static <T> boolean nullSafeEquals(final T one, final T other) {
		return one == null ? other == null : one.equals(other);
	}

}
