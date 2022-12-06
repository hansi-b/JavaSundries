package org.hansib.sundries.l10n;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.hansib.sundries.Errors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

class L10nMapReader {

	private final ObjectMapper om;
	private final Domain domain;

	L10nMapReader(Domain domain) {
		this.domain = domain;
		om = new ObjectMapper(new YAMLFactory());
		om.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
	}

	<K extends Enum<K>> JsonNode read(String yaml) throws JsonProcessingException {
		JsonNode root = om.readTree(yaml);

		Iterator<Entry<String, JsonNode>> iter = root.fields();

		iter.forEachRemaining(e -> {
			String enumName = e.getKey();
			Class<K> enumClz = domain.get(enumName);
			System.out.println(enumClz);
			JsonNode value = e.getValue();
			if (value instanceof ObjectNode valuesNode)
				readEnumValues(enumClz, valuesNode);
			else
				throw Errors.illegalArg("Expected object node for '%s', but got %s: '%s'", enumName,
						value.getClass().getSimpleName(), value);
		});
		return null;
	}

	<K extends Enum<K>> EnumMap<K, String> readEnumValues(Class<K> enumClz, ObjectNode valuesNode) {
		EnumMap<K, String> result = new EnumMap<>(enumClz);
		Iterator<Entry<String, JsonNode>> fields = valuesNode.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> next = fields.next();
			String k = next.getKey();
			JsonNode v = next.getValue();
			K l10nKey = Enum.valueOf(enumClz, k); // catch error

			System.out.println("\t" + k + " > " + v + " - " + v.isTextual());
		}

		return result;
	}
}