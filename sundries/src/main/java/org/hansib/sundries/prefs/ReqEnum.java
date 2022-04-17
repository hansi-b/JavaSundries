package org.hansib.sundries.prefs;

public class ReqEnum<K extends Enum<K>, L extends Enum<L>> extends ReqPrefClz<K, L> implements EnumConverter<L> {
	private final Class<L> clazz;

	ReqEnum(K key, Class<L> clazz, Prefs<K> store) {
		super(key, store);
		this.clazz = clazz;
	}

	@Override
	public Class<L> valueClass() {
		return clazz;
	}
}
