package org.hansib.sundries.prefs;

import java.math.BigDecimal;

public class ReqBigDecimal<K extends Enum<K>> extends ReqPrefClz<K, BigDecimal> implements BigDecimalConverter {
	ReqBigDecimal(K key, Prefs<K> store) {
		super(key, store);
	}
}
