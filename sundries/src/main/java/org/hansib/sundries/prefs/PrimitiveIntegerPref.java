package org.hansib.sundries.prefs;

public interface PrimitiveIntegerPref<K extends Enum<K>> extends Pref<K, Integer> {

	/**
	 * @return the auto-unboxed value of this preference
	 * @throws NullPointerException is this preference is not set (can only happen
	 *                              for optional preferences)
	 */
	default int intValue() {
		return prefs().get(this);
	}
}