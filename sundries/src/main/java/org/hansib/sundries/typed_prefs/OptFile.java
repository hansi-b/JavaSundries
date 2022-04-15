package org.hansib.sundries.typed_prefs;

import java.io.File;

public class OptFile<K extends Enum<K>> extends OptPrefClz<K, File> implements FileConverter {
	OptFile(K key, TypedEnumPrefs<K> store) {
		super(key, store);
	}
}