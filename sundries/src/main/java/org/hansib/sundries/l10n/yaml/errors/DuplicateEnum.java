package org.hansib.sundries.l10n.yaml.errors;

public final class DuplicateEnum implements L10nFormatError {

	private final String enumName;

	public DuplicateEnum(String enumName) {
		this.enumName = enumName;
	}
	
	public String enumName() {
		return enumName;
	}

	@Override
	public String description() {
		return String.format("Duplicate enum name '%s'", enumName);
	}
}
