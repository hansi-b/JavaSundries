package org.hansib.sundries.prefs;

public class OptInteger<K extends Enum<K>> extends OptPrefClz<K, Integer> implements IntegerConverter {
	OptInteger(K key, Prefs<K> store) {
		super(key, store);
	}
}
