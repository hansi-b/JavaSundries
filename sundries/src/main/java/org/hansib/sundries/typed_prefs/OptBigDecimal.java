package org.hansib.sundries.typed_prefs;

import java.math.BigDecimal;

public class OptBigDecimal<K extends Enum<K>> extends OptPrefClz<K, BigDecimal> implements BigDecimalConverter {
	OptBigDecimal(K key, TypedEnumPrefs<K> store) {
		super(key, store);
	}
}
