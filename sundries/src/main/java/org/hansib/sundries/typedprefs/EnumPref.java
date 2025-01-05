package org.hansib.sundries.typedprefs;

public final class EnumPref<K extends Enum<K>> extends TypedPref<K> {
	private Class<K> enumClazz;

	EnumPref(Class<K> enumClazz) {
		this.enumClazz = enumClazz;
	}

	public Class<K> enumClazz() {
		return enumClazz;
	}
}