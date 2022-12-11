package org.hansib.sundries.l10n.yaml;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import org.hansib.sundries.l10n.FormatKey;
import org.hansib.sundries.l10n.L10n;
import org.hansib.sundries.l10n.yaml.errors.DuplicateEnum;
import org.hansib.sundries.l10n.yaml.errors.DuplicateEnumValue;
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError;
import org.hansib.sundries.l10n.yaml.errors.UnexpectedEnumNode;
import org.hansib.sundries.l10n.yaml.errors.UnexpectedRootNode;
import org.hansib.sundries.l10n.yaml.errors.UnexpectedTextValueNode;
import org.hansib.sundries.l10n.yaml.errors.UnknownEnum;
import org.hansib.sundries.l10n.yaml.errors.UnknownEnumKey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

class L10nMapReader {

	private final L10n l10n;
	private final ObjectMapper om;

	L10nMapReader(L10n l10n) {
		this.l10n = l10n;
		this.om = new ObjectMapper(new YAMLFactory());
	}

	<K extends Enum<K> & FormatKey> void read(String yaml, Consumer<L10nFormatError> errorHandler)
			throws JsonProcessingException {
		Objects.requireNonNull(errorHandler);

		final JsonNode root = om.readTree(yaml);
		if (!(root instanceof ObjectNode)) {
			errorHandler.accept(new UnexpectedRootNode(root));
			return;
		}

		Set<String> foundEnumNames = new HashSet<>();
		root.fields().forEachRemaining(e -> {
			final Class<K> enumClz = resolveEnumClz(errorHandler, foundEnumNames, e.getKey());
			if (enumClz == null)
				return;

			JsonNode valueNode = e.getValue();
			if (valueNode instanceof ObjectNode valuesNode) {
				l10n.addAll(readEnumValues(enumClz, valuesNode, errorHandler));
			} else
				errorHandler.accept(new UnexpectedEnumNode(valueNode));
		});
	}

	private <K extends Enum<K> & FormatKey> Class<K> resolveEnumClz(Consumer<L10nFormatError> errorHandler,
			Set<String> foundEnumNames, final String enumName) {
		final Class<K> enumClz = l10n.domain().get(enumName);
		if (enumClz == null) {
			errorHandler.accept(new UnknownEnum(enumName));
			return null;
		} else if (foundEnumNames.contains(enumName)) {
			errorHandler.accept(new DuplicateEnum(enumName));
			return null;
		}

		foundEnumNames.add(enumName);
		return enumClz;
	}

	static <K extends Enum<K> & FormatKey> EnumMap<K, String> readEnumValues(Class<K> enumClz, ObjectNode valuesNode,
			Consumer<L10nFormatError> errorHandler) {
		final EnumMap<K, String> result = new EnumMap<>(enumClz);

		final Iterator<Entry<String, JsonNode>> fields = valuesNode.fields();
		while (fields.hasNext()) {
			final Entry<String, JsonNode> next = fields.next();
			final String keyStr = next.getKey();
			final JsonNode valNode = next.getValue();
			if (!(valNode instanceof TextNode textNode)) {
				errorHandler.accept(new UnexpectedTextValueNode(enumClz, valNode));
				continue;
			}
			try {
				K l10nKey = Enum.valueOf(enumClz, keyStr);
				String l10nVal = textNode.textValue();
				if (result.containsKey(l10nKey))
					errorHandler.accept(new DuplicateEnumValue<K>(l10nKey, result.get(l10nKey), l10nVal));
				else
					result.put(l10nKey, l10nVal);
			} catch (IllegalArgumentException ex) {
				errorHandler.accept(new UnknownEnumKey<>(enumClz, keyStr));
			}
		}
		return result;
	}
}