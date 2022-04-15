package org.hansib.sundries.typed_prefs;

public class ReqInteger<K extends Enum<K>> extends ReqPrefClz<K, Integer> implements IntegerConverter {
	ReqInteger(K key, TypedEnumPrefs<K> store) {
		super(key, store);
	}
}
