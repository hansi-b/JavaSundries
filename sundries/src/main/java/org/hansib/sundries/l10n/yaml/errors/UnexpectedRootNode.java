package org.hansib.sundries.l10n.yaml.errors;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The root node of the L10n input is not an object node; we will read anything from this.
 */
public final class UnexpectedRootNode implements L10nFormatError {

	private final JsonNode node;

	public UnexpectedRootNode(JsonNode jsonNode) {
		this.node = jsonNode;
	}

	public JsonNode foundRoot() {
		return node;
	}

	@Override
	public String description() {
		return String.format("Expected object root node, found %s: '%s'", node.getNodeType(), node);
	}
}
