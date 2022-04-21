package org.hansib.sundries.prefs;

public interface PrimitiveBooleanPref<K extends Enum<K>> extends Pref<K, Boolean> {

	default boolean isTrue() {
		return Boolean.TRUE.equals(prefs().get(this));
	}

	default boolean isFalse() {
		return Boolean.FALSE.equals(prefs().get(this));
	}
}