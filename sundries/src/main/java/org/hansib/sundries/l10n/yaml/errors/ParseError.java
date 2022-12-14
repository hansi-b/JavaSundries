package org.hansib.sundries.l10n.yaml.errors;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Some general parse error
 */
public record ParseError(String input, JsonProcessingException error) implements L10nFormatError {

	@Override
	public String description() {
		return String.format("Exception while parsing input: %s", error);
	}
}
