package org.hansib.sundries.typed_prefs;

public class OptString<K extends Enum<K>> extends OptPrefClz<K, String> implements StringConverter {
	OptString(K key, TypedEnumPrefs<K> store) {
		super(key, store);
	}
}
