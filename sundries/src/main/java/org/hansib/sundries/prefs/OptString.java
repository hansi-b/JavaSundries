package org.hansib.sundries.prefs;

public class OptString<K extends Enum<K>> extends OptPrefClz<K, String> implements StringConverter {
	OptString(K key, Prefs<K> store) {
		super(key, store);
	}
}
