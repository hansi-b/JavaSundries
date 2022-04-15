package org.hansib.sundries.typed_prefs;

import java.math.BigDecimal;

public class ReqBigDecimal<K extends Enum<K>> extends ReqPrefClz<K, BigDecimal> implements BigDecimalConverter {
	ReqBigDecimal(K key, TypedEnumPrefs<K> store) {
		super(key, store);
	}
}
