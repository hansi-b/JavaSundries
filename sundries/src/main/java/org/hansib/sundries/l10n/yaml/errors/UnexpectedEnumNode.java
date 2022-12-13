package org.hansib.sundries.l10n.yaml.errors;

import com.fasterxml.jackson.databind.JsonNode;

public record UnexpectedEnumNode(JsonNode jsonNode) implements L10nFormatError {

	@Override
	public String description() {
		return String.format("Expected object node for enum, found %s: '%s'", jsonNode.getNodeType(), jsonNode);
	}
}
