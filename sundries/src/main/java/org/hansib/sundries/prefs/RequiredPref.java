package org.hansib.sundries.prefs;

public interface RequiredPref<K extends Enum<K>, V> extends Pref<K, V> {
	default V get() {
		return prefs().get(this);
	}
}