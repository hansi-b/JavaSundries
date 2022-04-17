package org.hansib.sundries.prefs;

public class OptEnum<K extends Enum<K>, L extends Enum<L>> extends OptPrefClz<K, L> implements EnumConverter<L> {
	private final Class<L> clazz;

	OptEnum(K key, Class<L> clazz, Prefs<K> store) {
		super(key, store);
		this.clazz = clazz;
	}

	@Override
	public Class<L> valueClass() {
		return clazz;
	}
}
