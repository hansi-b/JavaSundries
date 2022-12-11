package org.hansib.sundries.l10n.yaml.errors;

public final class UnknownEnum implements L10nFormatError {

	private final String enumName;

	public UnknownEnum(String enumName) {
		this.enumName = enumName;
	}

	public String enumName() {
		return enumName;
	}

	@Override
	public String description() {
		return String.format("Unknown enum name '%s'", enumName);
	}
}
