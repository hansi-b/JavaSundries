package org.hansib.sundries.typed_prefs;

public class OptBoolean<K extends Enum<K>> extends OptPrefClz<K, Boolean> implements BooleanConverter {
	OptBoolean(K key, TypedEnumPrefs<K> store) {
		super(key, store);
	}
}
