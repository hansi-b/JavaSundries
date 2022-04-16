package org.hansib.sundries.typed_prefs;

public class ReqEnum<K extends Enum<K>, L extends Enum<L>> extends ReqPrefClz<K, L> implements EnumConverter<L> {
	private final Class<L> clazz;

	ReqEnum(K key, Class<L> clazz, TypedEnumPrefs<K> store) {
		super(key, store);
		this.clazz = clazz;
	}

	@Override
	public Class<L> valueClass() {
		return clazz;
	}
}
