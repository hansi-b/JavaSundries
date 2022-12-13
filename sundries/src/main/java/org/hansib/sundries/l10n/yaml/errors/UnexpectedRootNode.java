package org.hansib.sundries.l10n.yaml.errors;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The root node of the L10n input is not an object node; we will read anything from this.
 */
public record UnexpectedRootNode(JsonNode rootNode) implements L10nFormatError {

	@Override
	public String description() {
		return String.format("Expected object root node, found %s: '%s'", rootNode.getNodeType(), rootNode);
	}
}
