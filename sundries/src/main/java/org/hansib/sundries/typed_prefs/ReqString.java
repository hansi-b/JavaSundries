package org.hansib.sundries.typed_prefs;

public class ReqString<K extends Enum<K>> extends ReqPrefClz<K, String> implements StringConverter {
	ReqString(K key, TypedEnumPrefs<K> store) {
		super(key, store);
	}
}
