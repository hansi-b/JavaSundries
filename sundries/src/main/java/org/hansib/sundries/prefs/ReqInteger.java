package org.hansib.sundries.prefs;

public class ReqInteger<K extends Enum<K>> extends ReqPrefClz<K, Integer> implements IntegerConverter {
	ReqInteger(K key, Prefs<K> store) {
		super(key, store);
	}
}
