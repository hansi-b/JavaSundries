package org.hansib.sundries.prefs;

public class ReqBoolean<K extends Enum<K>> extends ReqPrefClz<K, Boolean> implements BooleanConverter {
	ReqBoolean(K key, Prefs<K> store) {
		super(key, store);
	}
}
