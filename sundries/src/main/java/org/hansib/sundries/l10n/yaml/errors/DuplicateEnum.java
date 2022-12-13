package org.hansib.sundries.l10n.yaml.errors;

public record DuplicateEnum(String enumName) implements L10nFormatError {

	@Override
	public String description() {
		return String.format("Duplicate enum name '%s'", enumName);
	}
}
