package org.hansib.sundries.l10n.yaml.errors;

public final class UnknownEnumKey<C extends Enum<C>> implements L10nFormatError {

	private final Class<C> enumClz;
	private final String keyStr;

	public UnknownEnumKey(Class<C> enumClz, String keyStr) {
		this.enumClz = enumClz;
		this.keyStr = keyStr;
	}

	public Class<C> enumClz() {
		return enumClz;
	}

	public String keyString() {
		return keyStr;
	}

	@Override
	public String description() {
		return String.format("Unknown key '%s' for enum %s", keyStr, enumClz.getName());
	}
}
