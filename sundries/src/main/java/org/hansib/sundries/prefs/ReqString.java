package org.hansib.sundries.prefs;

public class ReqString<K extends Enum<K>> extends ReqPrefClz<K, String> implements StringConverter {
	ReqString(K key, TypedEnumPrefs<K> store) {
		super(key, store);
	}
}
