package org.hansib.sundries.l10n.yaml.errors;

public final class DuplicateEnumValue<C extends Enum<C>> implements L10nFormatError {

	private final C key;
	private final String activeValue;
	private final String duplicateValue;

	public DuplicateEnumValue(C key, String oldValue, String newValue) {
		this.key = key;
		this.activeValue = oldValue;
		this.duplicateValue = newValue;
	}

	public C key() {
		return key;
	}

	public String activeValue() {
		return activeValue;
	}

	public String duplicateValue() {
		return duplicateValue;
	}

	@Override
	public String description() {
		return String.format("Ignoring duplicate value '%s' for key %s of enum %s (keeping '%s')", duplicateValue, key,
				key.getDeclaringClass().getSimpleName(), activeValue);
	}
}
