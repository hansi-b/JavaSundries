package org.hansib.sundries.l10n.yaml.errors;

import com.fasterxml.jackson.databind.JsonNode;

public record UnexpectedTextValueNode(Class<? extends Enum<?>> enumClz, JsonNode textNode) implements L10nFormatError {

	@Override
	public String description() {
		return String.format("Expected text node for value of enum %s, found %s: '%s'", enumClz.getSimpleName(),
				textNode.getNodeType(), textNode);
	}
}
