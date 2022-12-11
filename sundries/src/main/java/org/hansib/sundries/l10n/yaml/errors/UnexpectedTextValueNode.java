package org.hansib.sundries.l10n.yaml.errors;

import com.fasterxml.jackson.databind.JsonNode;

public final class UnexpectedTextValueNode implements L10nFormatError {

	private final Class<? extends Enum<?>> enumClz;
	private final JsonNode node;

	public UnexpectedTextValueNode(Class<? extends Enum<?>> enumClz, JsonNode jsonNode) {
		this.enumClz = enumClz;
		this.node = jsonNode;
	}

	public JsonNode jsonNode() {
		return node;
	}

	@Override
	public String description() {
		return String.format("Expected text node for value of enum %s, found %s: '%s'", enumClz.getSimpleName(),
				node.getNodeType(), node);
	}
}
