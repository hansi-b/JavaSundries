package org.hansib.sundries.l10n.yaml.errors;

import com.fasterxml.jackson.databind.JsonNode;

public final class UnexpectedEnumNode implements L10nFormatError {

	private final JsonNode jsonNode;

	public UnexpectedEnumNode(JsonNode jsonNode) {
		this.jsonNode = jsonNode;
	}

	public JsonNode jsonNode() {
		return jsonNode;
	}

	@Override
	public String description() {
		return String.format("Expected object node for enum, found %s: '%s'", jsonNode.getNodeType(), jsonNode);
	}
}
