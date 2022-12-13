package org.hansib.sundries.l10n.yaml.errors;

public record UnknownEnum(String enumName) implements L10nFormatError {

	@Override
	public String description() {
		return String.format("Unknown enum name '%s'", enumName);
	}
}
