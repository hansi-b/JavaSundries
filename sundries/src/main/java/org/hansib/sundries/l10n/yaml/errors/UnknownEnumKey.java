package org.hansib.sundries.l10n.yaml.errors;

public record UnknownEnumKey<C extends Enum<C>>(Class<C> enumClz, String keyStr) implements L10nFormatError {

	@Override
	public String description() {
		return String.format("Unknown key '%s' for enum %s", keyStr, enumClz.getName());
	}
}
