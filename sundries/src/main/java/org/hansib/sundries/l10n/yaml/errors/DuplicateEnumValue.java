package org.hansib.sundries.l10n.yaml.errors;

public record DuplicateEnumValue<C extends Enum<C>>(C key, String activeValue, String duplicateValue)
		implements L10nFormatError {

	@Override
	public String description() {
		return String.format("Ignoring duplicate value '%s' for key %s of enum %s (keeping '%s')", duplicateValue, key,
				key.getDeclaringClass().getSimpleName(), activeValue);
	}
}
